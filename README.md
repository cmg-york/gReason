# gReason

gReason is toolset for modeling temporally and decision-theoretically extended goal models and transforming them into Decision Theoretic Golog (DT-Golog) specifications. The latter can be used for reasoning within DT-Golog or for various other querying and simulaton tasks. 

In its current stage gReason consists of:
1. A **draw.io shape library** for preparing diagrams.
2. A **java-based translation program** that converts draw.io diagram files into a DT-Golog formal specification.

## Video Presentation

A [video presentation](https://youtu.be/DkVb_mgLsi8) of the tool can be found [here](https://youtu.be/DkVb_mgLsi8). 


## Prerequisites and Installation (MS Windows)

To run the tool you will need SWI Prolog (tested under ver. 8.4.2), Java runtime (tested in Java(TM) SE Runtime Environment (build 1.8.0_181)) and DT-Golog (see below).

1. Install Prolog and Java runtime.
2. Clone or download as is this repository to a local directory [RepoDir]
3. Open a command line within `[RepoDir]\gReason` and ensure `java` and `swipl` (the Prolog exectuable) are in your PATH.
4. Download DT-Golog. DT-Golog is available under a separate license at [Mikhail Soutchanski web page](https://www.cs.ryerson.ca/~mes/publications/appendix/appendixC/dtgolog) as an appendix to [his PhD thesis](https://www.cs.ryerson.ca/~mes/publications/mainLetter.pdf) which also contains much more information about the tool.
5. Place the `DT-Golog.pl` file under `[RepoDir]\gReason\resources`
5. For DT-Golog to run properly under SWI-Prolog it needs to be updated. Specifically add/update as follows (make a back-up copy first for line number referencing):
	1. Add on top of the file:
	```prolog
	:- style_check(-discontiguous).
	:- style_check(-singleton).
	:- discontiguous(bestDo/6).
	:- op(900, fy, [not]).
	```
	2. Later on after the `op` clauses:
	```prolog
	(not X) := (\+ X).
	cputime(5).
	```
	3. Rule staring from line 61 of the original file (the main bp call), replace with:
	```prolog
	bp(E,H,Pol,Util,Prob,_) :- integer(H), H >= 0, 
      bestDo(E : nil,s0,H,Pol,Val,Prob),
      Util is float(Val),
      write(Pol).
	```
	4. Rule starting from line 134 of the original file, replace:
	```prolog
		 ( RestPol = nil, Pol = A ; 
          not RestPol=nil, Pol = (A : RestPol) 
         )
	```
	with 
	```prolog
		( RestPol = nil, map(A,G), Pol = G ; 
          not RestPol=nil, map(A,G), Pol = (G : RestPol) 
         )
	```
	5. Rule starting from line 146 of the original file, replace:
	```prolog
	Pol=(A : senseEffect(A) : (RestPol)).
	```

	with

	```prolog
	map(A,G), Pol=(G : senseEffect(G) : (RestPol)).
	```
		   
	6. Add the following (maybe before the rule of line 208) :
	```prolog
	greatereq(V1,Prob1,V2,Prob2) :-  Pr1 is float(Prob1), (Pr1 = 0.0) ,
         Pr2 is float(Prob2), 
         ( (Pr2 \= 0.0) ; 
           (Pr2 = 0.0) ,  V2 =< V1
          ).
	```
	
## Usage

### To prepare a diagram

1. Go to [Draw.io](https://www.drawio.com/)
2. Click *Start*
3. Create a new *Blank Diagram*
4. *File* -> *Open Library From Device* -> Select `iStar-A.xml` from `[RepoDir]\gReason\lib`
5. *File* -> *Import* -> *From Device* -> Select `[RepoDir]\gReason\files\OrganizeTravel.xml`
6. Update the diagram accordingly (or create your own) and save in device as `[RepoDir]\gReason\files\[Diagram Name].xml`

### To translate and analyze

1. Export your diagram under `[RepoDir]\gReason\files\[Diagram Name].xml` - ensure the "compressed" option remains unchecked.
2. Open a Windows command prompt inside the `[RepoDir]\gReason` directory.
3. A precompiled JAR is under `.\dist\`, *use at own risk* as follows (compilation examples below):
4. Run `java -jar .\dist\gReason.jar -c .\config\config.txt -f .\files\[Diagram Name].xml`
5. Observe that a `[Diagram Name].pl` file has been created in `.\files`
6. Run `swipl .\files\[Diagram Name].pl`
7. Inside the prolog environment enter `run.` (don't forget the dot!)
8. Observe optimal policy; press `.`. To exit SWI Prolog, Ctr^C and `e`.


### Compilation

1.  `javac -sourcepath ./src/ -d ./bin/ ./src/cmg/gReason/g2dt`
2.  Can then run `java cmg.gReason.g2dt -c .\config\config.txt -f .\files\[Diagram Name].xml` as above and continue to step (5) above.

