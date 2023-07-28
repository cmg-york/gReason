:-style_check(-discontiguous).
:-style_check(-singleton).


/******  State Shape Information **** 

State shape information is offered in a predicate in the form of a list:

ccStateShapeInfo([[state_fluent1(), minVal2, maxVal2],
				  [state_fluent2(), minVal2, maxVal2],
				  ...
				  ]).

The following routines extract elements from this list.

*/

/* 
getStateShapeInfo(-Terms,-Mins,-Maxs)
Reads the list of state fluents and retruns separate lists with the term literal (Terms) and the Minimum and Maximum values specified
*/
unwrapStateShapeInfo([],[],[],[]).
unwrapStateShapeInfo([Top|Rest],[TopTerm|RestTerm],[TopMin|RestMin],[TopMax|RestMax]) :-
							unwrapStateShapeInfo(Rest,RestTerm,RestMin,RestMax),
							nth0(0,Top,TopTerm),
							nth0(1,Top,TopMin),
							nth0(2,Top,TopMax).



/* C O N T I N U O U S   S T A T E */


trueCCFluents([],_,[]).
trueCCFluents([TopPool|Pool],S,[TopPool|Result]) :- 
			holds(TopPool,S),trueCCFluents(Pool,S,Result),!.
trueCCFluents([TopPool|Pool],S,Result) :- 
			trueCCFluents(Pool,S,Result).


extractValues([],[]).
extractValues([Top|Fs],[TopRes|Res]) :- 
		extractValues(Fs,Res),
		arg(1,Top,TopRes).




/* D I S C R E T E   S T A T E */


whatIsTrue([],_,[]).
whatIsTrue([TopPool|Pool],S,[TopResult|Result]) :- 
			((holds(TopPool,S),TopResult is 1);
			(\+ holds(TopPool,S),TopResult is 0)),
			whatIsTrue(Pool,S,Result).

/*
getStateG(+S,-Res)
From a situation S returns a binary list marking the fluents that are true.
*/
/* getStateG(S,Res) :- fluentList(Fs),
					whatIsTrue(Fs,S,Res),write(Res).
*/
getStateG(S,Res) :- fluentList(Fs),
					whatIsTrue(Fs,S,Res).


/* 
constructSituation/2
From a list of action index number 0..(numAction-1) construct a situation 
*/
constructSituation(NumActionList,S) :-  reverse(NumActionList,A),constructSit(A,S).
%constructSit([],[]).
constructSit([],s0).
constructSit([TopList|List],do(Res,S)):-  
					stochasticActionList(Actions),
					nth0(TopList,Actions,Res),
					constructSit(List,S).


getProbs([],_,[]).
getProbs([TopAction|StochActionList],S,[TopProb|Probs]) :- 
		prob(TopAction,TopProb,S),
		getProbs(StochActionList,S,Probs).


/*
getRewardRL(+SNum,-R).
+SNum: a list of indexes of stochastic actions, represneting the current situation.
*/
getRewardRL_(SNum,R) :- 
/*		getRewardMode(episodic),*/
		constructSituation(SNum,S),
		reward(R,S).
		
		/*
getRewardRL_(SNum,R) :- 
		getRewardMode(cummulative),
		constructSituation(SNum,S),
		rewardCum(R,S).
		
getRewardRL_(SNum,R) :- 
		getRewardMode(instant),
		constructSituation(SNum,S),
		rewardInst(R,S).
		
getRewardRL_(SNum,R) :- 
		\+ (getRewardMode(instant);getRewardMode(cummulative);getRewardMode(episodic)),
		write("ERROR: No reward mode declared.").
*/


%
% Episodic reward: cummulative at the end of the episode
%
rewardEpis(R,S) :- \+ goalAchieved(S),
				R is 0. 
rewardEpis(R,S) :-  goalAchieved(S),
				rewardCum(R,S). 

%
% DTGolog Reward
%
/*
reward(R,S) :- penalizeDeadlock(1), deadlock(S), deadlockPenalty(R).

reward(R,S) :- \+ (penalizeDeadlock(1), deadlock(S), deadlockPenalty(R)),
				getRewardModeDTG(instant),
				rewardInst(R,S).
reward(R,S) :- \+ (penalizeDeadlock(1), deadlock(S), deadlockPenalty(R)),
				getRewardModeDTG(episodic),
				rewardEpis(R,S).				
*/

episodeDone(S) :- noActionPossible(S);goalAchieved(S).
deadlock(S) :- noActionPossible(S),\+ goalAchieved(S).



/* 
HELPERS!
*/


/* 
From items to index [and reverse]
*/
fromItemsToIndex([],_,[]).
fromItemsToIndex([TopItem|ItemList],Pool,[TopIndex|IndexList]):-
	nth0(TopIndex,Pool,TopItem),
	fromItemsToIndex(ItemList,Pool,IndexList).


% possibleAgentActionsNum(+SNum,ActionList)
% + SNum: a list of indexes of Stochastic Actions from the first to the last
% - Res: A list of indexes of agent Actions.
possibleAgentActionsNum(SNum,Res) :- constructSituation(SNum,S),
								possibleAgentActions(S,Res).


 
% possibleAgentActions(+Situation,ActionList)
% + Situation: a Golog situation
% - ActionList: A list of indexes of Agent Actions.
possA(X,S):-agentAction(X),poss(X,S).
possibleAgentActions(S,Res) :- 
								setof(X, possA(X,S), Bag),
								agentActionList(Pool),
								fromItemsToIndex(Bag,Pool,Res).


% noActionPossibleAgentActions(+Situation)
% + Situation: a Golog situation
noActionPossible(S) :- \+ (setof(X, poss(X,S), Bag),length(Bag,X),X > 0).
%noActionPossible(S) :- (setof(X, poss(X,S), Bag),length(Bag,X),X =:= 0).

/*
FindVal(-X,+A,+T)
Given an predicate term T, find its value (X) with a list A that contains it unified with that value.
-X: The value
+A: The list of instantiated predicates
+T: The predicate name we are interested in.
*/
findVal(X,[Top|Rest],T) :- functor(Top,T,1),arg(1,Top,X),!.
findVal(1,[Top|Rest],T) :- functor(Top,T,0),!.
findVal(X,[Top|Rest],T) :- findVal(X,Rest,T).



