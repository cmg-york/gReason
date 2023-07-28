package cmg.gReason.goalgraph;

import java.util.ArrayList;
import java.util.StringTokenizer;

/**
 * @author Sotirios Liaskos
 *
 */
public class Predicate {
	private String signature;
	private ArrayList<String> args = new ArrayList<String>();
	private String delims = "(,)";
		
	
	/**
	 * Creates a new Predicate object from a string.
	 * @param str The string representation of the predicate.
	 */
	public Predicate(String str) {
		StringTokenizer tok = new StringTokenizer(str,delims);
		this.signature = tok.nextToken();
	    while (tok.hasMoreTokens()) {
	        this.addParam(tok.nextToken());
	    }
	}
	
	/**
	 * Returns the textual representation of the predicate.
	 * @return The predicate text..
	 */
	public String getPredicateText() {
		String s = this.signature + "(";
		String trailing = "";
		for (String t: args) {
			s+= t + ",";
			trailing = ")";
		}
		return (s.substring(0,s.length()-1) + trailing);
	}

	
	/**
	 * As {@linkplain Predicate#getPredicateText()} but with an added argument in the end and potentially a "_fl" ending.
	 * @param arg The argument to append in the end.
	 * @param addFl Whether to also include a "_fl" ending.
	 * @return The predicate text after making the above additions.
	 */
	private String getPredicateTextWithAddedArgument(String arg, boolean addFl) {
		String s;
		if (addFl) {
			s = this.signature + "_fl(";
		} else {
			s = this.signature + "(";
		}
		String trailing = "(" + arg + ")";
		for (String t: args) {
			s+= t + ",";
			trailing = ","+ arg + ")";
		}
		return (s.substring(0,s.length()-1) + trailing);
		
	}
	
	/**
	 * Like {@linkplain Predicate#getPredicateTextWithAddedArgument(String, boolean)} but with the added argument fixed to "S"
	 * @param addFl 
	 * @param addFl Whether to also include a "_fl" ending.
	 * @return The predicate text after making the above additions.
	 */
	public String getPredicateTextWithSituation(boolean addFl) {
		return(getPredicateTextWithAddedArgument("S",addFl));
	}
	

	/**
	 * Like {@linkplain Predicate#getPredicateTextWithAddedArgument(String, boolean)} but with the added argument fixed to "_"
	 * @param addFl 
	 * @param addFl Whether to also include a "_fl" ending.
	 * @return The predicate text after making the above additions.
	 */
	public String getPredicateTextWithPlaceHolder(boolean addFl) {
		return(getPredicateTextWithAddedArgument("_",addFl));
	}
	
	
	/**
	 * Checks is predicate is equal to another predicate, but checking signature and arity equality and 
	 * @param p The predicate to check against.
	 * @return True if the predicates are the same false otherwise.
	 */
	public boolean equals(Predicate p) {
		return (p.getSignature().equals(this.getSignature())) && (p.getArity() == this.getArity());
	}
	
	
	/**
	 * Returns the signature of the predicate without the parameter, e.g. "owns(actor, object)" returns "owns" 
	 * @return The signature of the predicate without the parameter
	 */
	public String getSignature() {
		return signature;
	}
	
	
	/**
	 * Adds a parameter to the predicate. 
	 * @param arg The parameter to be added.
	 */
	public void addParam(String arg) {
		this.args.add(arg);
	}
	
	/**
	 * Returns the arity (number of parameters) of the predicate.
	 * 
	 * @return The arity of the predicate.
	 */
	public int getArity() {
		return args.size();
	}
	
}
