package cmg.gReason.outputs.dtGolog;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import cmg.gReason.goalgraph.Contribution;
import cmg.gReason.goalgraph.Formula;
import cmg.gReason.goalgraph.GMNode;
import cmg.gReason.goalgraph.GoalModel;
import cmg.gReason.goalgraph.Predicate;

public class Translator {

	private GoalModel g;
	private String[] seps = new String[3]; 
	private DTGologSpec spec;
	private Properties defaults;
	
	
	
	/**
	 * 
	 *  C O N S T R U C T O R S
	 *  
	 */
	
	
	/**
	 * Create a new empty translator.
	 */
	public Translator() {
		this.spec = new DTGologSpec();
		setOps("," , ";" , "\\+");
	}
	
	/**
	 * Create a new empty translator and add a goal model and a properties object in it.
	 * @param props A properties object with useful information about he translation.
	 * @param m The goal model to be translated
	 */
	public Translator(Properties props, GoalModel m) {
		this();
		String gologPath = "";
		this.defaults = props;
		try {
			gologPath = this.getProperty("golog.path");
		} catch (Exception e1) {
			System.err.println(e1.getMessage());
			e1.printStackTrace();
		}
		
		try {
			this.spec.setGologPath(gologPath);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		this.defaults = props;
		this.g = m;
	}
	
	
	
	/*
	 * 
	 * M A I N    C A L L 
	 * 
	 */
	
	
	/**
	 * Perform the translation of the {@linkplain GoalModel} object into a DT-Golog specification. 
	 */
	public void translate() {
	    expandPreconditions();
	    genSatAttFormlae();
	    genPreconditionFormulae();
	    buildMutualExclusionSet();
	    mainIteration();
	}
	
	
	
	/**
	 * Get the translation result.
	 * @return A string containing the final DT-Golog specification.
	 */
	public String getSpec() {
		return(spec.print());
	}
	


	/*
	 * 
	 * P R E P A R A T O R Y    S T E P S 
	 * 
	 */
	
	
	
	/**
	 * Ensures that incoming and outgoing preconditions of high-level goals are inherited by lower level goals and tasks.   
	 */
	public void expandPreconditions() {
		expandPres(this.getGoalModel().getRoot());
	}

	private void expandPres(GMNode n) {
		if (!n.getIncompingPrecedences().isEmpty()) {
			for (GMNode ch:n.getANDChildren()) {
				for (GMNode pre:n.getIncompingPrecedences()) {
					ch.addInPrecedence(pre);
					pre.addOutPrecedence(ch);
				}
				for (GMNode npre:n.getIncomingNegPrecedences()) {
					ch.addInNegPrecedence(npre);
					npre.addOutNegPrecedence(ch);
				}
			}
			for (GMNode ch:n.getORChildren()) {
				for (GMNode pre:n.getIncompingPrecedences()) {
					ch.addInPrecedence(pre);
					pre.addOutPrecedence(ch);
				}
				for (GMNode npre:n.getIncomingNegPrecedences()) {
					ch.addInPrecedence(npre);
					npre.addOutPrecedence(ch);
				}
			}
		}
		for (GMNode ch:n.getANDChildren()) {
			expandPres(ch);
		}
		for (GMNode ch:n.getORChildren()) {
			expandPres(ch);
		}
	}
  	
  	
    /**
     * For each GMNode, generate satisfaction and attainment formulae.
     * TODO: utilize predicates
     */
    public void genSatAttFormlae() {
		for (GMNode n:g.getGoalModel()) {
			Formula f_sat = new Formula();
			Formula f_att = new Formula();

			if (n.getType().equals("task")) {
				for (GMNode m:n.getEffects()) {
					f_att.addOrItem(new Formula(adaptEffectDescr(m.getLabel())));
					if (!m.getEffectStatus().equals("failure"))
						f_sat.addOrItem(new Formula(adaptEffectDescr(m.getLabel())));
				}
			}
			if (n.getType().equals("goal")) {
				for (GMNode ch:n.getANDChildren()) {
					if (!ch.getType().equals("effect")) {
						f_sat.addAndItem(new Formula(toIdentifier(ch.getId()) + "(S)"));
						f_att.addOrItem(new Formula(toIdentifier(ch.getId()) + "_Att(S)"));
					} else {
						f_sat.addAndItem(new Formula(adaptEffectDescr(ch.getLabel())));
						f_att.addOrItem(new Formula(adaptEffectDescr(ch.getLabel())));
					}
				}
				for (GMNode ch:n.getORChildren()) {
					if (!ch.getType().equals("effect")) {
						f_sat.addOrItem(new Formula(toIdentifier(ch.getId()) + "(S)"));
						f_att.addOrItem(new Formula(toIdentifier(ch.getId()) + "_Att(S)"));
					} else {
						f_sat.addOrItem(new Formula(adaptEffectDescr(ch.getLabel())));
						f_att.addOrItem(new Formula(adaptEffectDescr(ch.getLabel())));
					}
				}
			}
			n.setAttemptFormula(f_att);
			n.setSatisfactionFormula(f_sat);
		}
    }
    

    	
	/**
	 * For each GMNode, generate the precondition formulae.
	 * TODO: utilize predicates
	 */
	public void genPreconditionFormulae() {
		for (GMNode n: g.getGoalModel()) {
			if (n.getType().equals("goal") || n.getType().equals("task")) {
				Formula fPre = null;
				Formula fNegPre = null;
				if (!n.getIncompingPrecedences().isEmpty()) {
					fPre = new Formula();
					for (GMNode ins:n.getIncompingPrecedences()) {
						switch (ins.getType()){
						case "goal":
						case "task":
							fPre.addAndItem(new Formula(toIdentifier(ins.getId())+"(S)"));
							break;
						case "precondition":
							PreconditionProcessor frmProc = new PreconditionProcessor(ins.getLabel());
							fPre.addAndItem(new Formula(frmProc.getDTFormula()));
							break;
						default:
							fPre.addAndItem(new Formula(adaptEffectDescr(ins.getLabel())));
						}
					}
				}
				
				if (!n.getIncomingNegPrecedences().isEmpty()) {
					fNegPre = new Formula();
					fNegPre.negate();
					for (GMNode nins:n.getIncomingNegPrecedences()) {
						switch (nins.getType()){
						case "goal":
						case "task":
							fNegPre.addAndItem(new Formula(toIdentifier(nins.getId())+"_Att(S)"));
							break;
						case "precondition":
							PreconditionProcessor frmProc = new PreconditionProcessor(nins.getLabel());
							fPre.addAndItem(new Formula(frmProc.getDTFormula()));
							break;
						default:
							fNegPre.addOrItem(new Formula(adaptEffectDescr(nins.getLabel())));
						}
					}
				}
				
				Formula fin = new Formula();
				if (fPre != null) fin.addAndItem(fPre);
				if (fNegPre != null) {
					fin.addAndItem(fNegPre);
				}
				if (!fin.isEmpty()) {
					n.setPreconditionFormula(fin);
				}
			}
		}
	}

    
    
    
	/**
	 * 
	 * Ensures that children of OR-decomposition are mutually exclusive by adding negative "pre" type constraints
	 *  
	 */
    public void buildMutualExclusionSet() {
    	Formula excSet;
		for (GMNode n:g.getGoalModel()) {
			if (n.getType().equals("task")) {
				excSet = buildMutualExclusionSetRec(n, null); //new Formula());
				if (excSet!=null)
					n.getPreconditionFormula().addAndItem(excSet);
			}
		}
    }
    
  	public Formula buildMutualExclusionSetRec(GMNode n, Formula f) {
    	GMNode par = n.getParent();
    	if (par!=null) {
    		if (!par.getORChildren().isEmpty()) {
    			for (GMNode ch: par.getORChildren()) {
    				if (!ch.equals(n)) {
    					if (f==null) f = new Formula();
    					f.addOrItem(ch.getAttemptFormula());
    				}
    			}
    		}
    		return buildMutualExclusionSetRec(par,f);
    	} else {
    		if (f!=null) f.negate();
    		return (f);
    	}
    }

	
    
    
	
	/**
	 * The main spec writing iteration.
	 */
	public void mainIteration() {
		String agentActionList = "agentActionList([";
		String agentActions = "";
		String stochasticActionList = "stochasticActionList([";
		String nondetActions = "";
		String fluentList = "fluentList([";
		String probs = "";
		String successorState = "";
		String senseConditions = "";
		String restoreSitArgs = "";
		String translationPredicates = "";
		String procs = "";
		String satFormulae = "";
		String attFormulae = "";
		String preconditions = "";
		String rewards = "";
		String roots = "";
		String initializations = "";
		
		for (GMNode n:this.g.getGoalModel()) {
			if (n.getType().equals("task")) {
				agentActionList += toIdentifier(n.getId()) + ","; 
				agentActions += "agentAction(" + toIdentifier(n.getId()) + ").\n";
				nondetActions += "nondetActions(" + toIdentifier(n.getId()) + ",_,[";
				
				satFormulae += toIdentifier(n.getId()) + "(S)" + " :- " + n.getSatisfactionFormula().stringPrint(this.seps) + ".\n";
				attFormulae += toIdentifier(n.getId()) + "_Att(S)" + " :- " + n.getAttemptFormula().stringPrint(this.seps) + ".\n";
				preconditions += "poss(" + toIdentifier(n.getId()) + ", S) :- " + n.getPreconditionFormula().stringPrint(seps) + ".\n";
				

				translationPredicates += "map(" + toIdentifier(n.getId()) + ",'" + n.getLabel() + "').\n";
				for (GMNode eff:n.getEffects()) {
					stochasticActionList += eff.getLabel() +",";
					nondetActions += eff.getLabel() +",";
					fluentList += eff.getLabel() + "_fl,";
					probs += "prob(" + eff.getLabel() + "," + eff.getInWeight() + ",_).\n";
					successorState += eff.getLabel() + "_fl" + "(do(A,S)) :- " + eff.getLabel() + "_fl" + "(S); " + "A=" + eff.getLabel() + ".\n"; 
					senseConditions += "senseCondition(" + eff.getLabel() + "," + eff.getLabel() + "_fl).\n";
					restoreSitArgs += "restoreSitArg(" + eff.getLabel() + "_fl,S," + eff.getLabel() + "_fl(S)).\n";
					translationPredicates += "map(" + eff.getLabel() + ",'" + eff.getLabel() + "').\n";
					translationPredicates += "map(" + eff.getLabel() + "_fl,'" + eff.getLabel() + " (fl)').\n";
					preconditions += "poss(" + eff.getLabel() + ", S) :- poss(" +  toIdentifier(n.getId()) + ", S). % Added\n";
					//rewards += "reward(" + eff.getLabel() + ",0.0,S):- \\+ holds(" +  eff.getLabel() + "_fl,S).\n";
					//rewards += "reward(" + eff.getLabel() + ",1.0,S):- holds(" +  eff.getLabel() + "_fl,S).\n";
					rewards += "reward(" + eff.getLabel() + ",0.0,s0):-!.\n";
					rewards += "reward(" + eff.getLabel() + ",0.0,do(A,S)):- \\+ (A = " +  eff.getLabel() + ").\n";
					rewards += "reward(" + eff.getLabel() + ",1.0,do(A,S)):- (A = " +  eff.getLabel() + ").\n";

					
				}//effects
				nondetActions = trimLast(nondetActions,1) + "]).\n";

				rewards += "reward(" + toIdentifier(n.getId()) + ",0.0,S):- \\+" + n.getSatisfactionFormula().stringPrint(this.seps) + ".\n";
				rewards += "reward(" + toIdentifier(n.getId()) + ",1.0,S):- " +    n.getSatisfactionFormula().stringPrint(this.seps) + ".\n";
				
			} else if (n.getType().equals("goal")) {
				if (!n.getORChildren().isEmpty()) {
					procs += "proc(" + toIdentifier(n.getId()) + ",";
					for (GMNode ch:n.getORChildren()) {
						procs += toIdentifier(ch.getId()) + " # ";
					}
					procs = trimLast(procs,2) + ").\n";
				}
				
				if (!n.getANDChildren().isEmpty()) {
			        List<List<GMNode>> permutations = generatePermutations(n.getANDChildren());
			        for (List<GMNode> permutation : permutations) {
			        	procs += "proc(" + toIdentifier(n.getId()) + ",";
			            for (GMNode gmn: permutation) {
			            	procs += toIdentifier(gmn.getId()) + " : ";
			            }
			            procs = trimLast(procs,2) + ").\n";
			        }
				}
				
				satFormulae += toIdentifier(n.getId()) + "(S)" + " :- " + n.getSatisfactionFormula().stringPrint(this.seps) + ".\n";
				attFormulae += toIdentifier(n.getId()) + "_Att(S)" + " :- " + n.getAttemptFormula().stringPrint(this.seps) + ".\n";
				
				rewards += "reward(" + toIdentifier(n.getId()) + ",1.0,S):- "    + n.getSatisfactionFormula().stringPrint(this.seps) + ".\n";
				rewards += "reward(" + toIdentifier(n.getId()) + ",0.0,S):- \\+" + n.getSatisfactionFormula().stringPrint(this.seps) + ".\n";
				
			} else if (n.getType().equals("quality")) {
				String rewardPart = "";
				String numPart = " R is ";
				int origCounter = 0;
				for (GMNode inC:n.getIncompingContributions()) {
					//Work with qualities first
					Contribution cont = (Contribution) inC;
					String identifier = "";
					if (cont.getContributionOrigin().getType().equals("effect")) {
						identifier = cont.getContributionOrigin().getLabel();
					} else { // Goal or task
						identifier = toIdentifier(cont.getContributionOrigin().getId()); 
					}
					
					//rewardPart += " reward(" + toIdentifier(cont.getContributionOrigin().getId()) + ",R" + origCounter + ",S),";
					rewardPart += " reward(" + identifier + ",R" + origCounter + ",S),";
					numPart += cont.getContributionWeight() + "*R" + origCounter + " + "; 
					origCounter++;
				}
				rewards += "reward(" + toIdentifier(n.getId()) + ",0,s0):-!.\n";
				rewards += "reward(" + toIdentifier(n.getId()) + ",R,S) :-\n    ";
				rewards += rewardPart + trimLast(numPart, 3) + ".\n";
			}
			
		}//tasks, goals or qualities
		
		for(Predicate pred: g.getTrueInitPredicates()) {
			initializations += pred.getPredicateTextWithPlaceHolder(true) + ".\n";
			restoreSitArgs += "restoreSitArg(" + pred.getSignature() + "_fl,S," + pred.getPredicateTextWithSituation(true) + ").\n";
		}
		for(Predicate pred: g.getFalseInitPredicates()) {
			initializations += pred.getPredicateTextWithPlaceHolder(true) + ":-fail.\n";
			restoreSitArgs += "restoreSitArg(" + pred.getSignature() + "_fl,S," + pred.getPredicateTextWithSituation(true) + ").\n";
		}
		
		
		agentActionList = trimLast(agentActionList,1) + "]).\n";
		stochasticActionList = trimLast(stochasticActionList,1) + "]).\n";
		fluentList =  trimLast(fluentList,1) +  "]).\n";
		
		roots += "reward(R,S) :- reward(" + toIdentifier( g.getQRoot().getId()) + ",R,S).\n";
		roots += "root(" + toIdentifier( g.getRoot().getId()) + ").\n";
		

		spec.setAgentActionList(agentActionList);
		spec.setFluentList(fluentList);
		spec.setNondetActions(nondetActions);
		spec.setProbs(probs);
		spec.setRestoreSitArgs(restoreSitArgs);
		spec.setSenseConditions(senseConditions);
		spec.setStochasticActionList(stochasticActionList);
		spec.setSuccessorState(successorState);
		spec.setAgentActions(agentActions);
		spec.setTranslations(translationPredicates);
		spec.setProcs(procs);
		spec.setSatFormulae(satFormulae);
		spec.setPreconditions(preconditions);
		spec.setRewards(rewards);
		spec.setAttFormulae(attFormulae);
		spec.setRoots(roots);
		spec.setInitializations(initializations);
	}
		
	
	
	
	/*
	 * 
	 *  ROUTINES FOR AND-CHILDREN PERMUTATIONS 
	 *  
	 */
	
	
    /**
     * Generates all possible permutations of a give List of GMNode objects. To be used for AND-decompositions.
     * @param elements The list of {@linkplain GMNode} objects
     * @return The permutations.
     */
    public List<List<GMNode>> generatePermutations(List<GMNode> elements) {
        List<List<GMNode>> permutations = new ArrayList<>();
        List<List<GMNode>> cleanList = new ArrayList<>();
        generatePermutationsHelper(elements, new ArrayList<>(), permutations);
        
        //Clean up the permutations
        for (List<GMNode> perm: permutations) {
        	boolean clean = true;
        	for (GMNode elem: perm) {
        		for (GMNode prec: elem.getIncompingPrecedences()) {
        			if (perm.indexOf(prec) >= perm.indexOf(elem)) {
        				clean = false;
        			} else {
        			}
        		}
        		for (GMNode nprec: elem.getIncomingNegPrecedences()) {
        			if (perm.indexOf(nprec) <= perm.indexOf(elem)) {
        				clean = false;
        			}
        		}
        	}
        	if (clean) {
        		cleanList.add(perm);
        	}
        }
        
        return cleanList;
    }

    private <T> void generatePermutationsHelper(List<T> remainingElements, List<T> currentPermutation, List<List<T>> permutations) {
        if (remainingElements.isEmpty()) {
            permutations.add(new ArrayList<>(currentPermutation));
        } else {
            for (int i = 0; i < remainingElements.size(); i++) {
                T currentElement = remainingElements.get(i);

                currentPermutation.add(currentElement);

                List<T> remaining = new ArrayList<>(remainingElements);
                remaining.remove(i);
                generatePermutationsHelper(remaining, currentPermutation, permutations);

                currentPermutation.remove(currentPermutation.size() - 1);
            }
        }
    }
	
	
	/*
	 * 
	 * 
	 * M I S C   G E T T E R S,   S E T T E R S,   A N D   H E L P E R S 
	 *  
	 * 
	 */
	

    /**
     * Gets the value associated with the given property key from the set properties.
     * 
     * @param key The key of the property whose value is to be retrieved.
     * @return The value associated with the given property key.
     * @throws Exception If the property key is not found in the defaults or if the value associated with the key is empty.
     */
	public String getProperty(String key) throws Exception {
		if (!this.defaults.getProperty(key).isEmpty()) {
			return(this.defaults.getProperty(key));
		} else {
			throw new Exception("Invalid property key: " + key);
		}
	}
	
	
	/**
	 * Sets the operator symbols for AND, OR, and NOT
	 * @param andOp The symbol for an AND operator.
	 * @param orOp The symbol for an OR operator. 
	 * @param notOp The symbol for the NOT operator.
	 */
	public void setOps(String andOp, String orOp, String notOp) {
		seps[0] = andOp;
		seps[1] = orOp;
		seps[2] = notOp;
	}

	public String[] getOps() {
		return seps;
	}
	
	public GoalModel getGoalModel() {
		return g;
	}

	//TODO: -1. This needs to become more elegant. Utilize Predicate objects
	/** 
	 * Adds the "_fl(S)" to an effect predicate. [Subject to change]
	 * @param s The effect predicate
	 * @return The same predicate with the "_fl(S)" ending. 
	 */
	private String adaptEffectDescr(String s) {
		return (s + "_fl(S)");
	}

	/**
	 * Turns a random string and turns it into an acceptable Prolog atom identifier. It replaces all non-alphanumeric characters with 
	 * underscores '_' and adds "d_" in the front.
	 * @param s The string to be processed. 
	 * @return The clean identifier produced.
	 */
	private String toIdentifier(String s) {
		return("d_" + s.replaceAll("[^a-zA-Z0-9]", "_"));
	}
	
	//TODO: This should be moved elsewhere.
	private String trimLast(String s, int n) {
		return (s.substring(0, s.length()-n));
	}
	
	
	
	
	
	/* 
	 * 
	 * 
	 * D E G U G I N G
	 * 
	 * 
	 */
	
	
	
	public void debugPrintSpec() {
		System.out.println(spec.print());
	}

	
	public void debugPrintSatAttFormulas() {
		for (GMNode n: g.getGoalModel()) {
			System.out.println("Node (Sat): " + n.getLabel() + "-> " + n.getSatisfactionFormula().stringPrint(this.seps));
		}
		for (GMNode n: g.getGoalModel()) {
			System.out.println("Node (Att): " + n.getLabel() + "-> " + n.getAttemptFormula().stringPrint(this.seps));
		}			
	}

	
	public void debugPreconditionFormulas() {
		for (GMNode n: g.getGoalModel()) {
			if (n.getType().equals("task")) {
				System.out.print("Node (Pre): " + n.getLabel());
				System.out.println(" <- " + n.getPreconditionFormula().stringPrint(this.seps));
			}
		}			
	}
	
	
}
