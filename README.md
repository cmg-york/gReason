# gReason

gReason is toolset for modeling temporally and decision-theoretically extended goal models and transforming them into Decision Theoretic Golog (DT-Golog) specifications. The latter can be used for reasoning within DT-Golog or for various other querying and simulaton tasks. 

In its current stage gReason consists of:
1. A draw.io shape library for preparing diagrams.
2. A java-based translator program that converts draw.io diagram files into a DT-Golog formal specification.

## Video Presentation

A [video presentation](https://youtu.be/DkVb_mgLsi8) of the tool can be found [here](https://youtu.be/DkVb_mgLsi8). 


## Installation and Prerequisites

To run the tool you will need SWI Prolog (tested under ver. 8.4.2), Java Run Time (tested under )  and DT-Golog (see below).

1. Install Prolog and Java Run time. 
2. Clone or download as is this repository to a local directory [RepoDir]
3. Open a command line within `[RepoDir]\gReason` and ensure `java` and `swipl` (the Prolog exectuable) are in your PATH. Fix issues.
4. Download DT-Golog. DT-Golog is available for download at [Mikhail Soutchanski web page](https://www.cs.ryerson.ca/~mes/publications/appendix/appendixC/dtgolog) as an appendix to [his PhD thesis](https://www.cs.ryerson.ca/~mes/publications/mainLetter.pdf) which also contains much more information about the tool.
5. For examples to run properly in SWI-Prolog, DT-Golog needs to be updated. Specifically make a copy of the spec and add/update as follows:
	1. Add on top of the file
	```
	:-style_check(-discontiguous).
	:-style_check(-singleton).
	:- discontiguous(bestDo/6).
	:- op(900, fy, [not]).
	```
	2. Later on after the `op` clauses:
	```
	(not X) := (\+ X).
	cputime(5).
	```
	3. Rule staring from line 61 of the original file (the main bp call), replace with:
	```
	bp(E,H,Pol,Util,Prob,_) :- integer(H), H >= 0, 
      bestDo(E : nil,s0,H,Pol,Val,Prob),
      Util is float(Val),
      write(Pol).
	```
	4. Rule starting from line 134 of the original file, replace:
	```
		 ( RestPol = nil, Pol = A ; 
          not RestPol=nil, Pol = (A : RestPol) 
         )
	```
	with 
	```
		( RestPol = nil, map(A,G), Pol = G ; 
          not RestPol=nil, map(A,G), Pol = (G : RestPol) 
         )
	```
	5. Rule starting from line 146 of the original file, replace:
	       ```Pol=(A : senseEffect(A) : (RestPol)).```
		   with
		   ``` map(A,G), Pol=(G : senseEffect(G) : (RestPol)).```
		   
	6. Add the following (maybe before the rule of line 208) :
	```greatereq(V1,Prob1,V2,Prob2) :-  Pr1 is float(Prob1), (Pr1 = 0.0) ,
         Pr2 is float(Prob2), 
         ( (Pr2 \= 0.0) ; 
           (Pr2 = 0.0) ,  V2 =< V1
          ).
	```
	
## Use Instructions 

Firstly clone repository to a local directory [RepoDir]

### To prepare a diagram

1. Go to [Draw.io](https://www.drawio.com/)
2. Click Start
3. Create a new Blank Diagram
4. File -> Open Library From Device
5. Select iStar-A.xml from `[RepoDir]\gReason\lib`
6. File -> Import -> From Device 
7. Import `[RepoDir]\gReason\files\OrganizeTravel.xml`
8. Update the diagram accordingly (or create your own) and save in device as `[RepoDir]\gReason\files\[Diagram Name].xml`

### To translate and analyze

1. Export your diagram under `[RepoDir]\gReason\files\[Diagram Name].xml` - ensure the "compressed" option remains unchecked.
2. Open a command line inside the `[RepoDir]\gReason` directory.
4. Run `java -jar .\dist\gReason.jar -c .\config\config.txt .\files\[Diagram Name].xml`
5. Observe that a `[Diagram Name].pl` file has been created in `.\files`
6. Run `swipl .\files\[Diagram Name].pl`
7. Inside the prolog environment give `run.` (don't forget the dot)
8. To exit SWI Prolog Ctr^C and `e`

