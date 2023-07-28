package cmg.gReason.outputs.dtGolog;

import java.io.File;

public class DTGologSpec {
	private String agentActionList,
					agentActions,
					stochasticActionList, 
					nondetActions, 
					fluentList, 
					probs,
					senseConditions,
					restoreSitArgs,
					successorState,
					translations,
					procs,
					satFormulae,
					attFormulae,
					preconditions,
					rewards,
					initializations
					;

	private String gologPath;
	private String roots;

	
	/**
	 * Relative path to the Golog interpreter (a PL file)
	 * @return The path to the Golog interpreter
	 */
	public String getGologPath() {
		return this.gologPath;
	}

	/**
	 * Set the path to the golog interpreter (a PL file)
	 * @param gologPath The path (relative or absolute)
	 * @throws Exception File does not exist or is a directory.
	 */
	public void setGologPath(String gologPath) throws Exception {
		File f = new File(gologPath);
		if(f.exists() && !f.isDirectory()) { 
			this.gologPath = gologPath;
		} else 
			throw new Exception("Invalid DT-Golog path:" + gologPath);
	}
	

	/**
	 * Return the pre-amble of the specification. It includes linking to the DT-Golog interpeter, declaring multi-file and 
	 * dynamic predicates and shortcuts. 
	 * @return The preamble of the specification.
	 */
	public String getPreamble() {
		String s = "";
		s += ":-consult('" + getGologPath() + "').\n";
		s += ":-style_check(-discontiguous).\n";
		s += ":-style_check(-singleton).\n";
		s += ":- multifile getRewardMode/1.\n";
		s += ":- multifile reward/2.\n";
		s += ":- multifile getRewardModeDTG/1.\n";
		s += ":- multifile penalizeDeadlock/1.\n";
		s += ":- multifile deadlockPenalty/1.\n";
		s += ":- multifile getInfeasiblePenalty/1.\n";
		s += ":-dynamic(init/1).\n";
		
		s += "\n%\n% Run shortcuts \n%\n";
		s += "run(Pol,Util,Prob) :- root(X),bp(X,10,Pol,Util,Prob,_).\n";
		s += "run :- root(X),bp(X,10,_,Util,Prob,_),writeln(\"\"),write(\"Util = \"),writeln(Util),write(\"Prob = \"),writeln(Prob).\n";
		s += "goalAchieved(S) :- d_gRoot(S).\n";
		
		s += "\n%\n% OPTIONS \n%\n";
		s += "getObsType(discrete).\n"; 
		s += "getNumRuns(1).\n";
		s += "init([]).\n\n";
		
		return (s);
	}

	
	/**
	 * Generates the entire specification, based on the constituent components.
	 * @return The augmented DT-Golog specification.
	 */
	public String print() {
		return(
				"\n%\n% PREAMBLE \n%\n" +
				this.getPreamble() + 
				"\n%\n% LISTS: Agent Actions, Stochastic Actions, Fluents\n%\n" +
				agentActionList + 
				agentActions + "\n" +
				stochasticActionList + "\n" + 
				nondetActions + "\n" +
				fluentList +
				"\n%\n% Procedures \n%\n" +
				procs +			
				"\n%\n% Satisfaction Formulae \n%\n" +
				satFormulae +
				"\n%\n% Attempt Formulae \n%\n" +
				attFormulae +
				"\n%\n% Preconditions \n%\n" +
				preconditions +
				"\n%\n% Stochastic Action Probabilities \n%\n" +
				probs +
				"\n%\n% Successor State Axioms \n%\n" +
				successorState +
				"\n%\n% Initializations \n%\n" +
				initializations +
				"\n%\n% Sense Conditions \n%\n" +
				senseConditions +
				"\n%\n% Restoring Situation Argument \n%\n" +
				restoreSitArgs + 
				"\n%\n% Tranlsation Predicates \n%\n" +
				translations + 
				"\n%\n% Rewards \n%\n" +
				rewards + 
				"\n%\n% Roots \n%\n" +
				roots);
	}
	
	/*
	 * 
	 * 
	 *  LIST OF GETTERS and SETTERS for constituents of the specification.
	 * 
	 * 
	 */
	
	
	public String getAgentActionList() {
		return agentActionList;
	}

	public void setAgentActionList(String agentActionList) {
		this.agentActionList = agentActionList;
	}

	public String getStochasticActionList() {
		return stochasticActionList;
	}

	public void setStochasticActionList(String stochasticActionList) {
		this.stochasticActionList = stochasticActionList;
	}

	public String getNondetActions() {
		return nondetActions;
	}

	public void setNondetActions(String nondetActions) {
		this.nondetActions = nondetActions;
	}

	public String getFluentList() {
		return fluentList;
	}

	public void setFluentList(String fluentList) {
		this.fluentList = fluentList;
	}

	public String getProbs() {
		return probs;
	}

	public void setProbs(String probs) {
		this.probs = probs;
	}

	public String getSenseConditions() {
		return senseConditions;
	}

	public void setSenseConditions(String senseConditions) {
		this.senseConditions = senseConditions;
	}

	public String getRestoreSitArgs() {
		return restoreSitArgs;
	}

	public void setRestoreSitArgs(String restoreSitArgs) {
		this.restoreSitArgs = restoreSitArgs;
	}

	public String getSuccessorState() {
		return successorState;
	}

	public void setSuccessorState(String successorState) {
		this.successorState = successorState;
	}

	public String getAgentActions() {
		return agentActions;
	}

	public void setAgentActions(String agentActions) {
		this.agentActions = agentActions;
	}

	public String getTranslations() {
		return translations;
	}

	public void setTranslations(String translations) {
		this.translations = translations;
	}

	public String getProcs() {
		return procs;
	}

	public void setProcs(String procs) {
		this.procs = procs;
	}

	public String getSatFormulae() {
		return satFormulae;
	}

	public void setSatFormulae(String satFormulae) {
		this.satFormulae = satFormulae;
	}


	public String getAttFormulae() {
		return (this.attFormulae);
	}
	
	public void setAttFormulae(String attFormulae) {
		this.attFormulae = attFormulae;
	}
	
	public String getPreconditions() {
		return preconditions;
	}

	public void setPreconditions(String preconditions) {
		this.preconditions = preconditions;
	}

	public void setRewards(String rewards) {
		this.rewards = rewards;
	}
	
	public String getRewards() {
		return(this.rewards);
	}

	public void setRoots(String roots) {
		this.roots = roots;
	}
	
	public String getRoots() {
		return(this.roots);
	}

	public void setInitializations(String initializations) {
		this.initializations = initializations;
	}
	
}
