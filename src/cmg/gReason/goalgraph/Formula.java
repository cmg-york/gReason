package cmg.gReason.goalgraph;

import java.util.ArrayList;

/**
 * 
 * Abstract representation of logical formula. Used for encoding satisfaction and attainment formulae
 * 
 * @author Sotirios Liaskos
 *
 */
public class Formula {
	private ArrayList<Formula> andItems = null;
	private ArrayList<Formula> orItems = null;
	private String content = null;
	private boolean negated = false;

	/**
	 * Create an empty Formula. Reserved for intermediate nodes.
	 */
	public Formula() {
		andItems = new ArrayList<Formula>();
		orItems = new ArrayList<Formula>();
	}
	
	/**
	 * Create a Formula with a String content
	 * @param content The content to be added to the formula.
	 */
	public Formula(String content) {
		this();
		this.content = content;
	}
	
	/**
	 * Add an AND operand.
	 * @param item An AND operand to be part of the formula.
	 */
	public void addAndItem(Formula item) {
		this.andItems.add(item);
	}

	/**
	 * Add an OR operand.
	 * @param item An OR operand to be part of the formula.
	 */
	public void addOrItem(Formula item) {
		this.orItems.add(item);
	}
	
	/**
	 * Get the OR operands of the formula.  
	 * @return The operands of the fomrula. Null if this is an AND operation.
	 */
	public ArrayList<Formula> getOrItems() {
		return orItems;
	}
	
	/**
	 * Get the AND operands of the formula.  
	 * @return The operands of the formula. Null if this is an OR operation.
	 */
	public ArrayList<Formula> getAndItems() {
		return andItems;
	}
	
	
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	
	
	/**
	 * Check if the formula is empty (has not content and/or children)
	 * @return
	 */
	public boolean isEmpty() {
		return ((content == null) && (andItems == null) && (orItems == null));
	}



	/**
	 * Set formula as negated
	 */
	public void negate() {
		this.negated = true;
	}

	
	/**
	 * Check if formula is negated.
	 * @return Whether formula is negated.
	 */
	public boolean isNegated() {
		return negated;
	}
	
	
	/*
	 * 
	 * Printing the formula
	 * 
	 */
	
	/**
	 * Prints the formula according to given operators.
	 * 
	 * @param A string array containing the operators for the concepts [AND, OR, NOT] in this order.
	 * @return A string representation of the formula.
	 */
	public String stringPrint(String[] sep) {
		return sPrint(this,sep);
	}
	
	
	/**
	 * Prints a formula according to given operators.
	 * 
	 * @param f The formula to be printed.
	 * @param A string array containing the operators for the concepts [AND, OR, NOT] in this order.
	 * @return A string representation of the formula.
	 */
	private String sPrint(Formula f, String[] sep) {
		String res = "";
		String separator = "";
		
		for (Formula r:f.getOrItems()) {
			separator = sep[1];
			if (r.getContent()!=null)
				res = res + sep[1] + ((r.isNegated()) ? sep[2] : "") + r.getContent();
			else 
				res = res + sep[1] + ((r.isNegated()) ? sep[2] : "") + sPrint(r, sep);
		}
		
		
		for (Formula r:f.getAndItems()) {
			separator = sep[0];
			if (r.getContent()!=null)
				res = res + sep[0] + ((r.isNegated()) ? sep[2] : "") + r.getContent();
			else 
				res = res + sep[0] + ((r.isNegated()) ? sep[2] : "") + sPrint(r, sep);
		}
		if (res!=null) {
			res = trimmIt(res,separator);
			res = "(" + res + ")";
		}
		
		return(res);
	}
	
	
	/**
	 * Trims the end of a string by the length of a second string.
	 * @param form The string to be trimmed.
	 * @param sep The string whose length is to be used.
	 * @return THe trimmed string.
	 */
	private String trimmIt(String form, String sep) {
		return(form.substring(sep.length()));
	}
	
}
