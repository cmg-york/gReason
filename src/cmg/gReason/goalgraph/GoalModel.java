package cmg.gReason.goalgraph;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

import cmg.gReason.inputs.drawio.GraphElement;
import cmg.gReason.outputs.dtGolog.InitializerProcessor;
import cmg.gReason.outputs.dtGolog.PreconditionProcessor;

/**
 * 
 * A goal model object containing a tree of {@linkplain GMNode} objects.
 * @author Sotirios Liaskos
 *
 */
public class GoalModel {
	//TODO: 0. Push contribution links down to effects (satisfaction only).
	//TODO: 1. Ensure no conflicts between effects and general predicates. Issue warnings etc.
	//TODO: 3. Add default effects to tasks that do not have explicit ones.
	
	ArrayList<GraphElement> elements = new ArrayList<GraphElement>();
	ArrayList<GMNode> goalModel = new ArrayList<GMNode>();
	HashSet<Predicate> prePredicates = new HashSet<Predicate>();
	HashSet<Predicate> initPredicates = new HashSet<Predicate>();
	HashSet<Predicate> effectPredicates = new HashSet<Predicate>();

	HashSet<Predicate> trueInitPredicates = new HashSet<Predicate>();
	HashSet<Predicate> falseInitPredicates = new HashSet<Predicate>();
	

	private GMNode root;
	private GMNode qroot;
	

	
	/**
	 * Reads the list of {@linkplain GraphElement} objects (which are primarily relationships) and generates a goal model based on {@linkplain GMNode} objects.
	 * @throws Exception in a variety of situations in wich the goal model is invalid.
	 */
	public void createGoalGraph() throws Exception {

		for (GraphElement c:elements) {
			if (c.getCategory().equals("relationship")) {
				
				//Create the node
				GMNode gmn = new GMNode();
				GMNode gmnOrigin;
				GMNode gmnTarget;
				
				gmn.setId(c.getID());
				gmn.setLabel(c.getLabel());
				gmn.setType(c.getType());
				
				//Handle origin and destination
				String strOrigin = c.getOriginID();
				gmnOrigin = findNodeByID(strOrigin);
				if (gmnOrigin == null) {
					gmnOrigin = new GMNode();
					GraphElement elOrigin = findElementByID(strOrigin);
					gmnOrigin.setId(elOrigin.getID());
					gmnOrigin.setLabel(elOrigin.getLabel());
					gmnOrigin.setType(elOrigin.getType());
					gmnOrigin.setEffectStatus(elOrigin.getStatus());
					goalModel.add(gmnOrigin);
				}

				String strTarget = c.getTargetID();
				gmnTarget = findNodeByID(strTarget);
				if (gmnTarget == null) {
					gmnTarget = new GMNode();
					GraphElement elTarget = findElementByID(strTarget);
					gmnTarget.setId(elTarget.getID());
					gmnTarget.setLabel(elTarget.getLabel());
					gmnTarget.setType(elTarget.getType());
					gmnTarget.setEffectStatus(elTarget.getStatus());
					goalModel.add(gmnTarget);
				}
				
				//gmn, gmnOrigin, gmnTarget
				switch(gmn.getType()) {
				case "orDecomp":
					gmnTarget.addORChild(gmnOrigin);
					gmnOrigin.setParent(gmnTarget);
					break;
				case "andDecomp":
					gmnTarget.addANDChild(gmnOrigin);
					gmnOrigin.setParent(gmnTarget);
					break;
				case "precedenceLink":
					gmnOrigin.addOutPrecedence(gmnTarget);
					gmnTarget.addInPrecedence(gmnOrigin);
					break;
				case "negPrecedenceLink":
					gmnOrigin.addOutNegPrecedence(gmnTarget);
					gmnTarget.addInNegPrecedence(gmnOrigin);
					break;
				case "contributionLink":
					gmnOrigin.addOutContribution(new Contribution(gmnOrigin,gmnTarget,gmn.getLabel()));
					gmnTarget.addInContribution(new Contribution(gmnOrigin,gmnTarget,gmn.getLabel()));
					break;
				case "effectLink":
					gmnOrigin.setOutEffectLink(gmnTarget); //gmnTarget is an effect group
					gmnTarget.setInEffectLink(gmnOrigin); //gmnOrigin is a task
					break;
				case "effectGroupLink":
					gmnOrigin.addEffect(gmnTarget);
					//System.out.println("Adding effect group" + gmnTarget.getId() + " to " + gmnOrigin.getLabel());
					gmnTarget.setParent(gmnOrigin);
					gmnTarget.setInWeight(Float.parseFloat(gmn.getLabel()));
					break;
				}
			} else if (c.getType().equals("initialization")) {
				InitializerProcessor l = new InitializerProcessor(c.getLabel());
				this.initPredicates = l.getPreds();

			} else if (c.getType().equals("precondition")) {
				PreconditionProcessor fp = new PreconditionProcessor(c.getLabel());
				for (Predicate pred: fp.getPredicates()) {
					prePredicates.add(pred);
				}
				
			} else if (c.getType().equals("effect")) {
				effectPredicates.add(new Predicate(c.getLabel()));
			}
		}//loop 
		
		//Bypass effectGroups
		byPassEffectGroups();

		//Fill in predicate lists
		processPrePredicates();
		
		try {
			findRoot();
		} catch (Exception e) {
			System.err.println(e.getMessage());
			e.printStackTrace();
		}

		augmentRoot();
		
	}

	
	/**
	 * Removes the effect group element, such that, in the end, tasks are directly connected to the effects. 
	 */
	private void byPassEffectGroups() {
		Iterator<GMNode> itr = goalModel.iterator();
		
		while (itr.hasNext()) {
			GMNode n = itr.next();
			if (n.getType().equals("effectGroup")) {
				n.getInEffectLink().setEffects(n.getEffects());
				for (GMNode o:n.getEffects())
					o.setParent(n.getInEffectLink());
				itr.remove();
			}
		}
	}
	
	
	/**
	 * Organizes the list of precondition predicates, which was created by reading initialization, precondition elements. 
	 * It specifically iterates over the precondition predicates, and identifies if any of them are mentioned in the initialization element.
	 * If yes they are added in an trueInit list otherwise in a falseInit list. Effect predicates are excluded.    
	 * @throws Exception if an initialized predicate is an effect predicate.
	 */
	public void processPrePredicates() throws Exception {
		for (Predicate pred: getPrePredicates()) {
			//System.out.println("Processing precondition predicate:" + pred.getPredicate());
			boolean consider = true;
			for (Predicate effPred: getEffectPredicates()) {
				if (pred.equals(effPred)) {
					consider = false;
					//System.out.println("  [Excluded] Matches with effect predicate:" + effPred.getPredicate());
				}
			}
			
			if (consider) {
				boolean initialyTrue = false;
				for (Predicate initial: getInitPredicates()) {
					//System.out.println("Processing initialization predicate:" + initial.getPredicate());
					for (Predicate effPred: getEffectPredicates()) {
						if (effPred.equals(initial)) 
							throw new Exception ("Error in predicate " + initial.getPredicateText() + ". Initialization list should not contain effect predicates.");
					}					
					
					if (initial.equals(pred)) {
						initialyTrue = true;
						break;
					}
				}
				if (initialyTrue) {
					trueInitPredicates.add(pred);
				} else {
					falseInitPredicates.add(pred);
				}
			}//consider
		}
	}
	
	
	/**
	 * Find and set the roots of the goal decomposition and quality subgraphs.  
	 * @throws Exception if multiple roots are found.
	 */
	private void findRoot() throws Exception {
		ArrayList<String> roots = new ArrayList<String>();
		ArrayList<String> qRoots = new ArrayList<String>();
		
		for (GMNode n:goalModel) {
			if (n.getType().equals("goal") && (n.getParent()==null) ) {
				this.root = n;
				roots.add(n.getLabel());
			}
			if (n.getType().equals("quality") && n.getOutgoingContributions().isEmpty()) {
				this.qroot = n;
				qRoots.add(n.getLabel());
			}
		}
		if (roots.size() > 1)
			throw new Exception("Error: Too many root goals, 1 expected, " + roots.size() + " found: " + roots);
		if (roots.size() > 1)
			throw new Exception("Error: Too many quality root goals, 1 expected, " + roots.size() + " found: " + qRoots);
	}
	
	
	/**
	 * Creates a new root and a new terminal task. The new root is AND-decomposed into the old root and the terminal task. 
	 * The terminal task takes the old root as precondition. Fulfillment of the root goal is hence marked by performance of 
	 * terminal task. 
	 */
	public void augmentRoot() {
		GMNode newRoot = new GMNode();
		GMNode termTask = new GMNode();
		GMNode termEffect1 = new GMNode();
		GMNode termEffect2 = new GMNode();
				
		termEffect1.setId("tEffectS_id");
		termEffect1.setLabel("terminalTaskS");
		termEffect1.setType("effect");
		termEffect1.setInWeight((float) 1.0);
		termEffect1.setEffectStatus("success");
		effectPredicates.add(new Predicate(termEffect1.getLabel()));
		this.getGoalModel().add(termEffect1);
		
		termEffect2.setId("tEffectF_id");
		termEffect2.setLabel("terminalTaskF");
		termEffect2.setType("effect");
		termEffect2.setInWeight((float) 0.0);
		termEffect2.setEffectStatus("failure");
		effectPredicates.add(new Predicate(termEffect2.getLabel()));
		this.getGoalModel().add(termEffect2);
		
		termTask.setId("tTask");
		termTask.setLabel("Terminal Task");
		termTask.setType("task");
	
		termTask.addInPrecedence(this.getRoot());
		termTask.addEffect(termEffect1);
		termTask.addEffect(termEffect2);
		
		newRoot.setId("gRoot");
		newRoot.setLabel("Grand Root");
		newRoot.setType("goal");
		newRoot.addANDChild(this.getRoot());
		newRoot.addANDChild(termTask);
		termTask.setParent(newRoot);
		this.getRoot().setParent(newRoot);

		this.getGoalModel().add(termTask);
		this.getGoalModel().add(newRoot);
				
		try {
			findRoot();
		} catch (Exception e) {
			e.printStackTrace();
		}	

	}
	
	
	
	
	
	
	/**
	 * 
	 * 
	 * M I S C   G E T T E R S,   S E T T E R S,   A N D   H E L P E R S 
	 *  
	 * 
	 */
	
	
	public GMNode findNodeByID(String ID) {
		GMNode n = null;
		for (GMNode m:goalModel) {
			if (m.getId().equals(ID))
				n = m; 
		}
		return (n);
	}
	
	public GraphElement findElementByID(String ID) {
		GraphElement n = null;
		for (GraphElement m:elements) {
			if (m.getID().equals(ID))
				n = m; 
		}
		return (n);
	}
	
	public void addElement(GraphElement e) {
		elements.add(e);
	}
	
	public ArrayList<GMNode> getGoalModel() {
		return goalModel;
	}

	public HashSet<Predicate> getPrePredicates() {
		return prePredicates;
	}

	public HashSet<Predicate> getInitPredicates() {
		return initPredicates;
	}

	public HashSet<Predicate> getEffectPredicates() {
		return effectPredicates;
	}

	public HashSet<Predicate> getTrueInitPredicates() {
		return trueInitPredicates;
	}

	public HashSet<Predicate> getFalseInitPredicates() {
		return falseInitPredicates;
	}
	
	
	
	/**
	 * Returns the root of the qualities graph.
	 * @return The root of the qualities graph.
	 */
	public GMNode getQRoot() {
		return qroot;
	}

	/**
	 * Returns the root of the hardgoal graph.
	 * @return The root of the hardgoal graph.
	 */
	public GMNode getRoot() {
		return root;
	}




	/**
	 * 
	 * 
	 * D E B U G I N G 
	 *  
	 * 
	 */
	
	
	public void debugPrintGraphElementList() {
		for (GraphElement c:elements) {
	        System.out.println("-----");
	        System.out.println("Object Id : " + c.getID());
	        System.out.println("Type : " + c.getType());
	        System.out.println("Label : " + c.getLabel());
	        System.out.println("Category : " + c.getCategory());
	        System.out.println("Origin : " + c.getOriginID());
	        System.out.println("Target : " + c.getTargetID());
	        System.out.println("Status : " + c.getStatus());
	        System.out.println();
		}
	}
	
	public void debugGoalModelFlat() {
		for (GMNode c:goalModel) {
			System.out.println(c.getLabel() + " (" + c.getType() + ") ID:" + c.getId());
			System.out.println(" --> AND Children: " + GMNode.debugPrintList(c.getANDChildren(),","));
			System.out.println(" --> OR Children: " + GMNode.debugPrintList(c.getORChildren(),","));
			System.out.println(" --> Effects: " + GMNode.debugPrintList(c.getEffects(),","));
			System.out.println(" --> InPrecedences: " + GMNode.debugPrintList(c.getIncompingPrecedences(),","));
			System.out.println(" --> OutPrecedences: " + GMNode.debugPrintList(c.getOutgoingPrecedences(),","));
			System.out.println(" --> InContributions: " + GMNode.debugPrintList(c.getIncompingContributions(),","));
			System.out.println(" --> OutContributions: " + GMNode.debugPrintList(c.getOutgoingContributions(),","));
			System.out.println(" --> InWeight: " + Float.toString(c.getInWeight()));
			System.out.println(" --> Status: " + c.getEffectStatus());
			if (c.getParent()!= null)
				System.out.println(" --> Parent: " + c.getParent().getLabel());
			else 
				System.out.println(" --> Parent: none");
		}
	}
	
}
