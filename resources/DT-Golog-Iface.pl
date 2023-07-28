:-style_check(-discontiguous).
:-style_check(-discontiguous).
:-style_check(-singleton).
:-consult("DT-Golog.pl").
:-consult("DT-Golog-Ext.pl").
:- multifile getRewardMode/1.
:- multifile getRewardModeDTG/1.
:- multifile penalizeDeadlock/1.
:- multifile deadlockPenalty/1.
:- multifile getInfeasiblePenalty/1.


/*
Default (recommended) Configuration. 
Both DTG and the QE will assume that instant rather than cummulative reward is calcualted.
*/
getRewardMode(instant).
getRewardModeDTG(instant).
/*getInfeasiblePenalty(-100).*/
getInfeasiblePenalty(0).
penalizeDeadlock(0).
deadlockPenalty(0).

/* 
possibleAt(+SituationNum,+Action)
+SituationNum: a list of indexes of Stochastic Actions from the first to the last
+Action: an index to the action in question.
*/
possibleAt(SituationNum,ANum) :- 
						constructSituation(SituationNum,S),
						agentActionList(Pool),
						nth0(ANum,Pool,A),
						poss(A,S).



/*
getActionOutcomes(+AgentActionNum,+SituationNum,-StochActionsListNum,-ProbList).
+AgentActionNum: the index of an agent action in the agent actions list
+SituationNum: a list of indexes of Stochastic Actions from the first to the last, representing the situation
-StochActionsListNum: a list of indexes of Stochastic Actions from the first to the last, representing the outcomes of AgentActionNum.
- Problist: a list of probabilities corresponding to the StochActionsListNum under situation SituationNum.
*/
getActionOutcomes(AgentActionNum, SituationNum, StochActionsListNum, ProbList):-
	agentActionList(AgentA),
	stochasticActionList(StochA),
	constructSituation(SituationNum,S),
	nth0(AgentActionNum,AgentA,AgentActionTerm),
	nondetActions(AgentActionTerm,S,StochActionsListTerm),
	getProbs(StochActionsListTerm,S,ProbList),
	fromItemsToIndex(StochActionsListTerm,StochA,StochActionsListNum).
	

/*
getRewardRL(+SNum,-R).
+SNum: a list of indexes of stochastic actions, representing the current situation.
-R: a reward value for the current situation. May be instant (the reward of the last action) 
or cummulative (the reward of all actions since s0) depending on congiguration. See top of the file.
A deadlock penaly may also be defined if penalizeDeadlock is set to 1 -- this feature is not used anymore.
*/
getRewardRL(SNum,R) :- penalizeDeadlock(1),
						constructSituation(SNum,S),
						deadlock(S), 
						deadlockPenalty(R).
getRewardRL(SNum,R) :- \+ (penalizeDeadlock(1), constructSituation(SNum,S), deadlock(S)),
						getRewardRL_(SNum,R).

/*
getState(+SNum,-Res)
From an indexed situation S returns a binary list marking the fluents that are true.
+SNum: a list of indexes of stochastic actions, representing the current situation.
-Res: a binary list representing the state of each of the fluents.
*/
getState(SNum,Res) :- constructSituation(SNum,S),
					getStateG(S,Res).


/*
getCCState(+SNum,-Res)
From an indexed situation S returns a list with the value of continuous fluents.
+SNum: a list of indexes of stochastic actions, representing the current situation.
-Res: a list representing the value of each of the continuous fluents.
*/
getCCState(SNum,Res) :- constructSituation(SNum,S),
						getStateShapeInfo(Fs,_,_),
						trueCCFluents(Fs,S,ResF),
						extractValues(ResF,Res).


/*
getTransState(+SNum,-Res)
Get the state of the trascendent fluents.
+SNum: a list of indexes of stochastic actions, representing the current situation.
-Res: a list of transcendent predicates unified with the values at the given situation.
*/
/* If inintial state has been ommited, just return an empty list */
getTransState(_,[]) :- \+ current_predicate(transStateStructure/1), 
                        \+ current_predicate(init/1),!.
/* Default (no trans-states defined) is to return the hardcoded initial state */
getTransState(_,Res) :- \+ current_predicate(transStateStructure/1), 
                            init(Res),!.
/* If both are defined work as follows */
getTransState(SNum,Res) :- current_predicate(transStateStructure/1), 
                        constructSituation(SNum,S),
						transStateStructure(Fs),
						trueCCFluents(Fs,S,Res),!.



/*
getRun(+SNum,-C)
Given a situation, calculates the current run.
+SNum: a list of indexes of stochastic actions, representing the current situation.
-C: an integer >=0 representing the current run, starting from 0.
*/
/*getRun(SNum,C) :- constructSituation(SNum,S),runCounter(C,S).*/

/*
done(+SNum)
Decides if a situation signifies the end of an episode (due to deadlock or root goal completion).
+SNum: a list of indexes of stochastic actions, representing the current situation.
*/
done(SNum) :- constructSituation(SNum,S),noActionPossible(S),!.
/* done(SNum) :- constructSituation(SNum,S),episodeDone(S).*/

/*

H E L P E R S 

*/

/*
actionSize(-L)
Returns the number of agent actions in the current domain. Used for array memory allocation.
-L: the number of agent actions in the current domain.
*/
actionSize(L) :- agentActionList(As), length(As,L).

/*
stateSize(-S)
Returns the number of possible states given the number of fluents of interest. Used for array memory allocation.
-S: the number of possible states.
*/
stateSize(S) :- fluentList(Fs), length(Fs,L), S is 2**L.
stateSizeBits(L) :- fluentList(Fs), length(Fs,L).

/*
getStateShapeInfo(-Terms,-Mins,-Maxs)
Returns a list of fluent signatures (Terms) as well as the minimum (Mins) and maximum (Maxs) values allowed for these signatures.
-Terms: a list of predicates of state-representing fluents.
-Mins: a list of minimum values for the argument of each predicate.
-Maxs: a list of maximum values for the argument of each predicate.
Application: to be directly hard-coded in the domain specification. 
*/
getStateShapeInfo(Terms,Mins,Maxs) :- 
			ccStateShapeInfo(X),unwrapStateShapeInfo(X,Terms,Mins,Maxs).

/*
achieved(+SNum)
Holds if in the situation SNum the root goal is satisfied.
+SNum: a list of indexes of stochastic actions, representing the current situation.
*/
achieved(SNum) :- constructSituation(SNum,S),goalAchieved(S).
