package cmg.gReason.outputs.dtGolog;

import java.util.HashSet;
import java.util.StringTokenizer;

import cmg.gReason.goalgraph.Predicate;

/**
 * Processes a precondition element, by identifying its constituent predicates
 * TODO: requires testing and improvement or replacement with a full-fledged parser.
 * @author Sotirios Liaskos
 *
 */
public class PreconditionProcessor {

	String input;
	String output;
	HashSet<Predicate> predicates = new HashSet<Predicate>();
	HashSet<String> strPreds = new HashSet<String>(); 
		
	/**
	 * Prepares a DT-Golog representation of a precondition formula.
	 * @param input
	 */
	public PreconditionProcessor(String input){
 		this.input = input;
 		cleanInput();
 		extractPredicates();
 		translateToDT();
	}

	
	
	/**
	 * Remove any bold formating tags from the element.
	 */
	private void cleanInput() {
		this.input = this.input.replace("<b>","");
		this.input = this.input.replace("</b>","");
	}
	
	/**
	 * Tokenize to identify the predicates 
	 */
	private void extractPredicates() {
		StringTokenizer tok = new StringTokenizer(input);
	    while (tok.hasMoreTokens()) {
	        String piece = tok.nextToken();
	        //Remove leading and trailing parentheses
	        piece = piece.replaceAll("^[(]+","");
	        piece = piece.replaceAll("[)]+$","");
	        if (piece.contains("("))
	        	piece += ")";
	        if (!piece.equals("AND") && (!piece.equals("OR")) && (!piece.equals("NOT"))) {
	        	strPreds.add(piece);
	        	predicates.add(new Predicate(piece));
	        }
	     }
	}
	
	
	/**
	 * Generate the DT-Golog formula.
	 */
	private void translateToDT() {
		String s = "";
		StringTokenizer tok = new StringTokenizer(input);
	    while (tok.hasMoreTokens()) {
	         String piece = tok.nextToken();
	         switch (piece) {
		         case "AND":
		        	 s+= ",";
		        	 break; 
		         case "OR":
		        	 s+= ";";
		        	 break;
		         case "NOT":
		        	 s+= "\\+";
		        	 break;
		         default:
		        	 s += piece;
		        	 break;
	         }
	    }
	    
	    for (Predicate p:predicates) {
	    	s = s.replace(p.getPredicateText(), p.getPredicateTextWithSituation(true));
	    }
	    this.output = s;
	}
	
	
	/**
	 * Get the predicates contained in the formula. 
	 * @return 
	 */
	public HashSet<Predicate> getPredicates() {
		return predicates;
	}


	/**
	 * Get the resulting DT-Golog formula.
	 * @return
	 */
	public String getDTFormula() {
		return this.output;
	}
	
	
}
 