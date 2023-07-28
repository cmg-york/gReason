
%
% PREAMBLE 
%
:-consult('null').
:-style_check(-discontiguous).
:-style_check(-singleton).
:- multifile getRewardMode/1.
:- multifile reward/2.
:- multifile getRewardModeDTG/1.
:- multifile penalizeDeadlock/1.
:- multifile deadlockPenalty/1.
:- multifile getInfeasiblePenalty/1.
:-dynamic(init/1).

%
% Run shortcuts 
%
run(Pol,Util,Prob) :- root(X),bp(X,10,Pol,Util,Prob,_).
run :- root(X),bp(X,10,_,Util,Prob,_),writeln(""),write("Util = "),writeln(Util),write("Prob = "),writeln(Prob).
goalAchieved(S) :- d_gRoot(S).

%
% OPTIONS 
%
getObsType(discrete).
getNumRuns(1).
init([]).


%
% LISTS: Agent Actions, Stochastic Actions, Fluents
%
agentActionList([d_hWYSzjRRIHH0uWY2PTZe_6,d_hWYSzjRRIHH0uWY2PTZe_7,d_hWYSzjRRIHH0uWY2PTZe_23,d_hWYSzjRRIHH0uWY2PTZe_22,d_hWYSzjRRIHH0uWY2PTZe_59,d_hWYSzjRRIHH0uWY2PTZe_60,d_tTask]).
agentAction(d_hWYSzjRRIHH0uWY2PTZe_6).
agentAction(d_hWYSzjRRIHH0uWY2PTZe_7).
agentAction(d_hWYSzjRRIHH0uWY2PTZe_23).
agentAction(d_hWYSzjRRIHH0uWY2PTZe_22).
agentAction(d_hWYSzjRRIHH0uWY2PTZe_59).
agentAction(d_hWYSzjRRIHH0uWY2PTZe_60).
agentAction(d_tTask).

stochasticActionList([refTixSucc,refTixFailed,nonRefTixSucc,nonRefTixFailed,onlineSubmittedSucc,onlineSubmittedwProblems,onlineFailed,paperSubmittedSucc,paperSubmittedwProblems,paperFailed,cmtGranted,cmtDenied,headGranted,headDenied,terminalTaskS,terminalTaskF]).

nondetActions(d_hWYSzjRRIHH0uWY2PTZe_6,_,[refTixSucc,refTixFailed]).
nondetActions(d_hWYSzjRRIHH0uWY2PTZe_7,_,[nonRefTixSucc,nonRefTixFailed]).
nondetActions(d_hWYSzjRRIHH0uWY2PTZe_23,_,[onlineSubmittedSucc,onlineSubmittedwProblems,onlineFailed]).
nondetActions(d_hWYSzjRRIHH0uWY2PTZe_22,_,[paperSubmittedSucc,paperSubmittedwProblems,paperFailed]).
nondetActions(d_hWYSzjRRIHH0uWY2PTZe_59,_,[cmtGranted,cmtDenied]).
nondetActions(d_hWYSzjRRIHH0uWY2PTZe_60,_,[headGranted,headDenied]).
nondetActions(d_tTask,_,[terminalTaskS,terminalTaskF]).

fluentList([refTixSucc_fl,refTixFailed_fl,nonRefTixSucc_fl,nonRefTixFailed_fl,onlineSubmittedSucc_fl,onlineSubmittedwProblems_fl,onlineFailed_fl,paperSubmittedSucc_fl,paperSubmittedwProblems_fl,paperFailed_fl,cmtGranted_fl,cmtDenied_fl,headGranted_fl,headDenied_fl,terminalTaskS_fl,terminalTaskF_fl]).

%
% Procedures 
%
proc(d_hWYSzjRRIHH0uWY2PTZe_55,d_hWYSzjRRIHH0uWY2PTZe_3 : d_hWYSzjRRIHH0uWY2PTZe_56 ).
proc(d_hWYSzjRRIHH0uWY2PTZe_1,d_hWYSzjRRIHH0uWY2PTZe_2 : d_hWYSzjRRIHH0uWY2PTZe_55 ).
proc(d_hWYSzjRRIHH0uWY2PTZe_2,d_hWYSzjRRIHH0uWY2PTZe_6 # d_hWYSzjRRIHH0uWY2PTZe_7 ).
proc(d_hWYSzjRRIHH0uWY2PTZe_3,d_hWYSzjRRIHH0uWY2PTZe_23 # d_hWYSzjRRIHH0uWY2PTZe_22 ).
proc(d_hWYSzjRRIHH0uWY2PTZe_56,d_hWYSzjRRIHH0uWY2PTZe_59 # d_hWYSzjRRIHH0uWY2PTZe_60 ).
proc(d_gRoot,d_hWYSzjRRIHH0uWY2PTZe_1 : d_tTask ).

%
% Satisfaction Formulae 
%
d_hWYSzjRRIHH0uWY2PTZe_55(S) :- (d_hWYSzjRRIHH0uWY2PTZe_3(S),d_hWYSzjRRIHH0uWY2PTZe_56(S)).
d_hWYSzjRRIHH0uWY2PTZe_1(S) :- (d_hWYSzjRRIHH0uWY2PTZe_55(S),d_hWYSzjRRIHH0uWY2PTZe_2(S)).
d_hWYSzjRRIHH0uWY2PTZe_2(S) :- (d_hWYSzjRRIHH0uWY2PTZe_6(S);d_hWYSzjRRIHH0uWY2PTZe_7(S)).
d_hWYSzjRRIHH0uWY2PTZe_6(S) :- (refTixSucc_fl(S)).
d_hWYSzjRRIHH0uWY2PTZe_7(S) :- (nonRefTixSucc_fl(S)).
d_hWYSzjRRIHH0uWY2PTZe_23(S) :- (onlineSubmittedSucc_fl(S);onlineSubmittedwProblems_fl(S)).
d_hWYSzjRRIHH0uWY2PTZe_3(S) :- (d_hWYSzjRRIHH0uWY2PTZe_23(S);d_hWYSzjRRIHH0uWY2PTZe_22(S)).
d_hWYSzjRRIHH0uWY2PTZe_22(S) :- (paperSubmittedSucc_fl(S);paperSubmittedwProblems_fl(S)).
d_hWYSzjRRIHH0uWY2PTZe_56(S) :- (d_hWYSzjRRIHH0uWY2PTZe_59(S);d_hWYSzjRRIHH0uWY2PTZe_60(S)).
d_hWYSzjRRIHH0uWY2PTZe_59(S) :- (cmtGranted_fl(S)).
d_hWYSzjRRIHH0uWY2PTZe_60(S) :- (headGranted_fl(S)).
d_tTask(S) :- (terminalTaskS_fl(S)).
d_gRoot(S) :- (d_hWYSzjRRIHH0uWY2PTZe_1(S),d_tTask(S)).

%
% Attempt Formulae 
%
d_hWYSzjRRIHH0uWY2PTZe_55_Att(S) :- (d_hWYSzjRRIHH0uWY2PTZe_3_Att(S);d_hWYSzjRRIHH0uWY2PTZe_56_Att(S)).
d_hWYSzjRRIHH0uWY2PTZe_1_Att(S) :- (d_hWYSzjRRIHH0uWY2PTZe_55_Att(S);d_hWYSzjRRIHH0uWY2PTZe_2_Att(S)).
d_hWYSzjRRIHH0uWY2PTZe_2_Att(S) :- (d_hWYSzjRRIHH0uWY2PTZe_6_Att(S);d_hWYSzjRRIHH0uWY2PTZe_7_Att(S)).
d_hWYSzjRRIHH0uWY2PTZe_6_Att(S) :- (refTixSucc_fl(S);refTixFailed_fl(S)).
d_hWYSzjRRIHH0uWY2PTZe_7_Att(S) :- (nonRefTixSucc_fl(S);nonRefTixFailed_fl(S)).
d_hWYSzjRRIHH0uWY2PTZe_23_Att(S) :- (onlineSubmittedSucc_fl(S);onlineSubmittedwProblems_fl(S);onlineFailed_fl(S)).
d_hWYSzjRRIHH0uWY2PTZe_3_Att(S) :- (d_hWYSzjRRIHH0uWY2PTZe_23_Att(S);d_hWYSzjRRIHH0uWY2PTZe_22_Att(S)).
d_hWYSzjRRIHH0uWY2PTZe_22_Att(S) :- (paperSubmittedSucc_fl(S);paperSubmittedwProblems_fl(S);paperFailed_fl(S)).
d_hWYSzjRRIHH0uWY2PTZe_56_Att(S) :- (d_hWYSzjRRIHH0uWY2PTZe_59_Att(S);d_hWYSzjRRIHH0uWY2PTZe_60_Att(S)).
d_hWYSzjRRIHH0uWY2PTZe_59_Att(S) :- (cmtGranted_fl(S);cmtDenied_fl(S)).
d_hWYSzjRRIHH0uWY2PTZe_60_Att(S) :- (headGranted_fl(S);headDenied_fl(S)).
d_tTask_Att(S) :- (terminalTaskS_fl(S);terminalTaskF_fl(S)).
d_gRoot_Att(S) :- (d_hWYSzjRRIHH0uWY2PTZe_1_Att(S);d_tTask_Att(S)).

%
% Preconditions 
%
poss(d_hWYSzjRRIHH0uWY2PTZe_6, S) :- (\+((nonRefTixSucc_fl(S);nonRefTixFailed_fl(S)))).
poss(refTixSucc, S) :- poss(d_hWYSzjRRIHH0uWY2PTZe_6, S). % Added
poss(refTixFailed, S) :- poss(d_hWYSzjRRIHH0uWY2PTZe_6, S). % Added
poss(d_hWYSzjRRIHH0uWY2PTZe_7, S) :- (\+((refTixSucc_fl(S);refTixFailed_fl(S)))).
poss(nonRefTixSucc, S) :- poss(d_hWYSzjRRIHH0uWY2PTZe_7, S). % Added
poss(nonRefTixFailed, S) :- poss(d_hWYSzjRRIHH0uWY2PTZe_7, S). % Added
poss(d_hWYSzjRRIHH0uWY2PTZe_23, S) :- ((hasAccount_fl(researcher,S),d_hWYSzjRRIHH0uWY2PTZe_2(S)),\+((paperSubmittedSucc_fl(S);paperSubmittedwProblems_fl(S);paperFailed_fl(S)))).
poss(onlineSubmittedSucc, S) :- poss(d_hWYSzjRRIHH0uWY2PTZe_23, S). % Added
poss(onlineSubmittedwProblems, S) :- poss(d_hWYSzjRRIHH0uWY2PTZe_23, S). % Added
poss(onlineFailed, S) :- poss(d_hWYSzjRRIHH0uWY2PTZe_23, S). % Added
poss(d_hWYSzjRRIHH0uWY2PTZe_22, S) :- ((d_hWYSzjRRIHH0uWY2PTZe_2(S)),\+((onlineSubmittedSucc_fl(S);onlineSubmittedwProblems_fl(S);onlineFailed_fl(S)))).
poss(paperSubmittedSucc, S) :- poss(d_hWYSzjRRIHH0uWY2PTZe_22, S). % Added
poss(paperSubmittedwProblems, S) :- poss(d_hWYSzjRRIHH0uWY2PTZe_22, S). % Added
poss(paperFailed, S) :- poss(d_hWYSzjRRIHH0uWY2PTZe_22, S). % Added
poss(d_hWYSzjRRIHH0uWY2PTZe_59, S) :- ((d_hWYSzjRRIHH0uWY2PTZe_3(S),d_hWYSzjRRIHH0uWY2PTZe_2(S)),\+(d_hWYSzjRRIHH0uWY2PTZe_23_Att(S)),\+((headGranted_fl(S);headDenied_fl(S)))).
poss(cmtGranted, S) :- poss(d_hWYSzjRRIHH0uWY2PTZe_59, S). % Added
poss(cmtDenied, S) :- poss(d_hWYSzjRRIHH0uWY2PTZe_59, S). % Added
poss(d_hWYSzjRRIHH0uWY2PTZe_60, S) :- ((isAuthorized_fl(head,S),\+onVacation_fl(head,S),d_hWYSzjRRIHH0uWY2PTZe_3(S),d_hWYSzjRRIHH0uWY2PTZe_2(S)),\+((cmtGranted_fl(S);cmtDenied_fl(S)))).
poss(headGranted, S) :- poss(d_hWYSzjRRIHH0uWY2PTZe_60, S). % Added
poss(headDenied, S) :- poss(d_hWYSzjRRIHH0uWY2PTZe_60, S). % Added
poss(d_tTask, S) :- ((d_hWYSzjRRIHH0uWY2PTZe_1(S))).
poss(terminalTaskS, S) :- poss(d_tTask, S). % Added
poss(terminalTaskF, S) :- poss(d_tTask, S). % Added

%
% Stochastic Action Probabilities 
%
prob(refTixSucc,0.95,_).
prob(refTixFailed,0.05,_).
prob(nonRefTixSucc,0.95,_).
prob(nonRefTixFailed,0.05,_).
prob(onlineSubmittedSucc,0.89,_).
prob(onlineSubmittedwProblems,0.1,_).
prob(onlineFailed,0.01,_).
prob(paperSubmittedSucc,0.7,_).
prob(paperSubmittedwProblems,0.2,_).
prob(paperFailed,0.1,_).
prob(cmtGranted,0.5,_).
prob(cmtDenied,0.5,_).
prob(headGranted,0.7,_).
prob(headDenied,0.3,_).
prob(terminalTaskS,1.0,_).
prob(terminalTaskF,0.0,_).

%
% Successor State Axioms 
%
refTixSucc_fl(do(A,S)) :- refTixSucc_fl(S); A=refTixSucc.
refTixFailed_fl(do(A,S)) :- refTixFailed_fl(S); A=refTixFailed.
nonRefTixSucc_fl(do(A,S)) :- nonRefTixSucc_fl(S); A=nonRefTixSucc.
nonRefTixFailed_fl(do(A,S)) :- nonRefTixFailed_fl(S); A=nonRefTixFailed.
onlineSubmittedSucc_fl(do(A,S)) :- onlineSubmittedSucc_fl(S); A=onlineSubmittedSucc.
onlineSubmittedwProblems_fl(do(A,S)) :- onlineSubmittedwProblems_fl(S); A=onlineSubmittedwProblems.
onlineFailed_fl(do(A,S)) :- onlineFailed_fl(S); A=onlineFailed.
paperSubmittedSucc_fl(do(A,S)) :- paperSubmittedSucc_fl(S); A=paperSubmittedSucc.
paperSubmittedwProblems_fl(do(A,S)) :- paperSubmittedwProblems_fl(S); A=paperSubmittedwProblems.
paperFailed_fl(do(A,S)) :- paperFailed_fl(S); A=paperFailed.
cmtGranted_fl(do(A,S)) :- cmtGranted_fl(S); A=cmtGranted.
cmtDenied_fl(do(A,S)) :- cmtDenied_fl(S); A=cmtDenied.
headGranted_fl(do(A,S)) :- headGranted_fl(S); A=headGranted.
headDenied_fl(do(A,S)) :- headDenied_fl(S); A=headDenied.
terminalTaskS_fl(do(A,S)) :- terminalTaskS_fl(S); A=terminalTaskS.
terminalTaskF_fl(do(A,S)) :- terminalTaskF_fl(S); A=terminalTaskF.

%
% Initializations 
%
hasAccount_fl(researcher,_).
isAuthorized_fl(head,_).
onVacation_fl(head,_):-fail.

%
% Sense Conditions 
%
senseCondition(refTixSucc,refTixSucc_fl).
senseCondition(refTixFailed,refTixFailed_fl).
senseCondition(nonRefTixSucc,nonRefTixSucc_fl).
senseCondition(nonRefTixFailed,nonRefTixFailed_fl).
senseCondition(onlineSubmittedSucc,onlineSubmittedSucc_fl).
senseCondition(onlineSubmittedwProblems,onlineSubmittedwProblems_fl).
senseCondition(onlineFailed,onlineFailed_fl).
senseCondition(paperSubmittedSucc,paperSubmittedSucc_fl).
senseCondition(paperSubmittedwProblems,paperSubmittedwProblems_fl).
senseCondition(paperFailed,paperFailed_fl).
senseCondition(cmtGranted,cmtGranted_fl).
senseCondition(cmtDenied,cmtDenied_fl).
senseCondition(headGranted,headGranted_fl).
senseCondition(headDenied,headDenied_fl).
senseCondition(terminalTaskS,terminalTaskS_fl).
senseCondition(terminalTaskF,terminalTaskF_fl).

%
% Restoring Situation Argument 
%
restoreSitArg(refTixSucc_fl,S,refTixSucc_fl(S)).
restoreSitArg(refTixFailed_fl,S,refTixFailed_fl(S)).
restoreSitArg(nonRefTixSucc_fl,S,nonRefTixSucc_fl(S)).
restoreSitArg(nonRefTixFailed_fl,S,nonRefTixFailed_fl(S)).
restoreSitArg(onlineSubmittedSucc_fl,S,onlineSubmittedSucc_fl(S)).
restoreSitArg(onlineSubmittedwProblems_fl,S,onlineSubmittedwProblems_fl(S)).
restoreSitArg(onlineFailed_fl,S,onlineFailed_fl(S)).
restoreSitArg(paperSubmittedSucc_fl,S,paperSubmittedSucc_fl(S)).
restoreSitArg(paperSubmittedwProblems_fl,S,paperSubmittedwProblems_fl(S)).
restoreSitArg(paperFailed_fl,S,paperFailed_fl(S)).
restoreSitArg(cmtGranted_fl,S,cmtGranted_fl(S)).
restoreSitArg(cmtDenied_fl,S,cmtDenied_fl(S)).
restoreSitArg(headGranted_fl,S,headGranted_fl(S)).
restoreSitArg(headDenied_fl,S,headDenied_fl(S)).
restoreSitArg(terminalTaskS_fl,S,terminalTaskS_fl(S)).
restoreSitArg(terminalTaskF_fl,S,terminalTaskF_fl(S)).
restoreSitArg(hasAccount_fl,S,hasAccount_fl(researcher,S)).
restoreSitArg(isAuthorized_fl,S,isAuthorized_fl(head,S)).
restoreSitArg(onVacation_fl,S,onVacation_fl(head,S)).

%
% Tranlsation Predicates 
%
map(d_hWYSzjRRIHH0uWY2PTZe_6,'Book Refundable Tickets').
map(refTixSucc,'refTixSucc').
map(refTixSucc_fl,'refTixSucc (fl)').
map(refTixFailed,'refTixFailed').
map(refTixFailed_fl,'refTixFailed (fl)').
map(d_hWYSzjRRIHH0uWY2PTZe_7,'Book Non-Refundable Tickets').
map(nonRefTixSucc,'nonRefTixSucc').
map(nonRefTixSucc_fl,'nonRefTixSucc (fl)').
map(nonRefTixFailed,'nonRefTixFailed').
map(nonRefTixFailed_fl,'nonRefTixFailed (fl)').
map(d_hWYSzjRRIHH0uWY2PTZe_23,'Fill in online form').
map(onlineSubmittedSucc,'onlineSubmittedSucc').
map(onlineSubmittedSucc_fl,'onlineSubmittedSucc (fl)').
map(onlineSubmittedwProblems,'onlineSubmittedwProblems').
map(onlineSubmittedwProblems_fl,'onlineSubmittedwProblems (fl)').
map(onlineFailed,'onlineFailed').
map(onlineFailed_fl,'onlineFailed (fl)').
map(d_hWYSzjRRIHH0uWY2PTZe_22,'Fill in paper form').
map(paperSubmittedSucc,'paperSubmittedSucc').
map(paperSubmittedSucc_fl,'paperSubmittedSucc (fl)').
map(paperSubmittedwProblems,'paperSubmittedwProblems').
map(paperSubmittedwProblems_fl,'paperSubmittedwProblems (fl)').
map(paperFailed,'paperFailed').
map(paperFailed_fl,'paperFailed (fl)').
map(d_hWYSzjRRIHH0uWY2PTZe_59,'Committee Authorizes').
map(cmtGranted,'cmtGranted').
map(cmtGranted_fl,'cmtGranted (fl)').
map(cmtDenied,'cmtDenied').
map(cmtDenied_fl,'cmtDenied (fl)').
map(d_hWYSzjRRIHH0uWY2PTZe_60,'Head Authorizes').
map(headGranted,'headGranted').
map(headGranted_fl,'headGranted (fl)').
map(headDenied,'headDenied').
map(headDenied_fl,'headDenied (fl)').
map(d_tTask,'Terminal Task').
map(terminalTaskS,'terminalTaskS').
map(terminalTaskS_fl,'terminalTaskS (fl)').
map(terminalTaskF,'terminalTaskF').
map(terminalTaskF_fl,'terminalTaskF (fl)').

%
% Rewards 
%
reward(d_hWYSzjRRIHH0uWY2PTZe_55,1.0,S):- (d_hWYSzjRRIHH0uWY2PTZe_3(S),d_hWYSzjRRIHH0uWY2PTZe_56(S)).
reward(d_hWYSzjRRIHH0uWY2PTZe_55,0.0,S):- \+(d_hWYSzjRRIHH0uWY2PTZe_3(S),d_hWYSzjRRIHH0uWY2PTZe_56(S)).
reward(d_hWYSzjRRIHH0uWY2PTZe_1,1.0,S):- (d_hWYSzjRRIHH0uWY2PTZe_55(S),d_hWYSzjRRIHH0uWY2PTZe_2(S)).
reward(d_hWYSzjRRIHH0uWY2PTZe_1,0.0,S):- \+(d_hWYSzjRRIHH0uWY2PTZe_55(S),d_hWYSzjRRIHH0uWY2PTZe_2(S)).
reward(d_hWYSzjRRIHH0uWY2PTZe_2,1.0,S):- (d_hWYSzjRRIHH0uWY2PTZe_6(S);d_hWYSzjRRIHH0uWY2PTZe_7(S)).
reward(d_hWYSzjRRIHH0uWY2PTZe_2,0.0,S):- \+(d_hWYSzjRRIHH0uWY2PTZe_6(S);d_hWYSzjRRIHH0uWY2PTZe_7(S)).
reward(refTixSucc,0.0,s0):-!.
reward(refTixSucc,0.0,do(A,S)):- \+ (A = refTixSucc).
reward(refTixSucc,1.0,do(A,S)):- (A = refTixSucc).
reward(refTixFailed,0.0,s0):-!.
reward(refTixFailed,0.0,do(A,S)):- \+ (A = refTixFailed).
reward(refTixFailed,1.0,do(A,S)):- (A = refTixFailed).
reward(d_hWYSzjRRIHH0uWY2PTZe_6,0.0,S):- \+(refTixSucc_fl(S)).
reward(d_hWYSzjRRIHH0uWY2PTZe_6,1.0,S):- (refTixSucc_fl(S)).
reward(nonRefTixSucc,0.0,s0):-!.
reward(nonRefTixSucc,0.0,do(A,S)):- \+ (A = nonRefTixSucc).
reward(nonRefTixSucc,1.0,do(A,S)):- (A = nonRefTixSucc).
reward(nonRefTixFailed,0.0,s0):-!.
reward(nonRefTixFailed,0.0,do(A,S)):- \+ (A = nonRefTixFailed).
reward(nonRefTixFailed,1.0,do(A,S)):- (A = nonRefTixFailed).
reward(d_hWYSzjRRIHH0uWY2PTZe_7,0.0,S):- \+(nonRefTixSucc_fl(S)).
reward(d_hWYSzjRRIHH0uWY2PTZe_7,1.0,S):- (nonRefTixSucc_fl(S)).
reward(onlineSubmittedSucc,0.0,s0):-!.
reward(onlineSubmittedSucc,0.0,do(A,S)):- \+ (A = onlineSubmittedSucc).
reward(onlineSubmittedSucc,1.0,do(A,S)):- (A = onlineSubmittedSucc).
reward(onlineSubmittedwProblems,0.0,s0):-!.
reward(onlineSubmittedwProblems,0.0,do(A,S)):- \+ (A = onlineSubmittedwProblems).
reward(onlineSubmittedwProblems,1.0,do(A,S)):- (A = onlineSubmittedwProblems).
reward(onlineFailed,0.0,s0):-!.
reward(onlineFailed,0.0,do(A,S)):- \+ (A = onlineFailed).
reward(onlineFailed,1.0,do(A,S)):- (A = onlineFailed).
reward(d_hWYSzjRRIHH0uWY2PTZe_23,0.0,S):- \+(onlineSubmittedSucc_fl(S);onlineSubmittedwProblems_fl(S)).
reward(d_hWYSzjRRIHH0uWY2PTZe_23,1.0,S):- (onlineSubmittedSucc_fl(S);onlineSubmittedwProblems_fl(S)).
reward(d_hWYSzjRRIHH0uWY2PTZe_3,1.0,S):- (d_hWYSzjRRIHH0uWY2PTZe_23(S);d_hWYSzjRRIHH0uWY2PTZe_22(S)).
reward(d_hWYSzjRRIHH0uWY2PTZe_3,0.0,S):- \+(d_hWYSzjRRIHH0uWY2PTZe_23(S);d_hWYSzjRRIHH0uWY2PTZe_22(S)).
reward(paperSubmittedSucc,0.0,s0):-!.
reward(paperSubmittedSucc,0.0,do(A,S)):- \+ (A = paperSubmittedSucc).
reward(paperSubmittedSucc,1.0,do(A,S)):- (A = paperSubmittedSucc).
reward(paperSubmittedwProblems,0.0,s0):-!.
reward(paperSubmittedwProblems,0.0,do(A,S)):- \+ (A = paperSubmittedwProblems).
reward(paperSubmittedwProblems,1.0,do(A,S)):- (A = paperSubmittedwProblems).
reward(paperFailed,0.0,s0):-!.
reward(paperFailed,0.0,do(A,S)):- \+ (A = paperFailed).
reward(paperFailed,1.0,do(A,S)):- (A = paperFailed).
reward(d_hWYSzjRRIHH0uWY2PTZe_22,0.0,S):- \+(paperSubmittedSucc_fl(S);paperSubmittedwProblems_fl(S)).
reward(d_hWYSzjRRIHH0uWY2PTZe_22,1.0,S):- (paperSubmittedSucc_fl(S);paperSubmittedwProblems_fl(S)).
reward(d_hWYSzjRRIHH0uWY2PTZe_56,1.0,S):- (d_hWYSzjRRIHH0uWY2PTZe_59(S);d_hWYSzjRRIHH0uWY2PTZe_60(S)).
reward(d_hWYSzjRRIHH0uWY2PTZe_56,0.0,S):- \+(d_hWYSzjRRIHH0uWY2PTZe_59(S);d_hWYSzjRRIHH0uWY2PTZe_60(S)).
reward(cmtGranted,0.0,s0):-!.
reward(cmtGranted,0.0,do(A,S)):- \+ (A = cmtGranted).
reward(cmtGranted,1.0,do(A,S)):- (A = cmtGranted).
reward(cmtDenied,0.0,s0):-!.
reward(cmtDenied,0.0,do(A,S)):- \+ (A = cmtDenied).
reward(cmtDenied,1.0,do(A,S)):- (A = cmtDenied).
reward(d_hWYSzjRRIHH0uWY2PTZe_59,0.0,S):- \+(cmtGranted_fl(S)).
reward(d_hWYSzjRRIHH0uWY2PTZe_59,1.0,S):- (cmtGranted_fl(S)).
reward(headGranted,0.0,s0):-!.
reward(headGranted,0.0,do(A,S)):- \+ (A = headGranted).
reward(headGranted,1.0,do(A,S)):- (A = headGranted).
reward(headDenied,0.0,s0):-!.
reward(headDenied,0.0,do(A,S)):- \+ (A = headDenied).
reward(headDenied,1.0,do(A,S)):- (A = headDenied).
reward(d_hWYSzjRRIHH0uWY2PTZe_60,0.0,S):- \+(headGranted_fl(S)).
reward(d_hWYSzjRRIHH0uWY2PTZe_60,1.0,S):- (headGranted_fl(S)).
reward(d_hWYSzjRRIHH0uWY2PTZe_75,0,s0):-!.
reward(d_hWYSzjRRIHH0uWY2PTZe_75,R,S) :-
     reward(refTixSucc,R0,S), R is 1.0*R0.
reward(d_hWYSzjRRIHH0uWY2PTZe_77,0,s0):-!.
reward(d_hWYSzjRRIHH0uWY2PTZe_77,R,S) :-
     reward(paperSubmittedSucc,R0,S), reward(paperSubmittedwProblems,R1,S), reward(onlineSubmittedSucc,R2,S), reward(onlineSubmittedwProblems,R3,S), R is 0.7*R0 + 0.4*R1 + 1.0*R2 + 0.2*R3.
reward(d_hWYSzjRRIHH0uWY2PTZe_82,0,s0):-!.
reward(d_hWYSzjRRIHH0uWY2PTZe_82,R,S) :-
     reward(cmtGranted,R0,S), reward(cmtDenied,R1,S), reward(headGranted,R2,S), reward(headDenied,R3,S), R is 0.3*R0 + 0.3*R1 + 0.8*R2 + 0.8*R3.
reward(d_hWYSzjRRIHH0uWY2PTZe_87,0,s0):-!.
reward(d_hWYSzjRRIHH0uWY2PTZe_87,R,S) :-
     reward(d_hWYSzjRRIHH0uWY2PTZe_75,R0,S), reward(d_hWYSzjRRIHH0uWY2PTZe_77,R1,S), reward(d_hWYSzjRRIHH0uWY2PTZe_82,R2,S), R is 0.7*R0 + 0.1*R1 + 0.3*R2.
reward(terminalTaskS,0.0,s0):-!.
reward(terminalTaskS,0.0,do(A,S)):- \+ (A = terminalTaskS).
reward(terminalTaskS,1.0,do(A,S)):- (A = terminalTaskS).
reward(terminalTaskF,0.0,s0):-!.
reward(terminalTaskF,0.0,do(A,S)):- \+ (A = terminalTaskF).
reward(terminalTaskF,1.0,do(A,S)):- (A = terminalTaskF).
reward(d_tTask,0.0,S):- \+(terminalTaskS_fl(S)).
reward(d_tTask,1.0,S):- (terminalTaskS_fl(S)).
reward(d_gRoot,1.0,S):- (d_hWYSzjRRIHH0uWY2PTZe_1(S),d_tTask(S)).
reward(d_gRoot,0.0,S):- \+(d_hWYSzjRRIHH0uWY2PTZe_1(S),d_tTask(S)).

%
% Roots 
%
reward(R,S) :- reward(d_hWYSzjRRIHH0uWY2PTZe_87,R,S).
root(d_gRoot).
