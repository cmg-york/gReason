package cmg.gReason.outputs.dtGolog;

import java.util.HashSet;

import cmg.gReason.goalgraph.Predicate;

/**
 * Processes an initialization element.
 * 
 * @author Sotirios Liaskos
 *
 */
public class InitializerProcessor {
	HashSet<Predicate> preds = new HashSet<Predicate>();
	
	/**
	 * Splits the intialization element content by line and adds each item found in a HashSet of predicates. 
	 * @param text The initialization element text.
	 */
	public InitializerProcessor(String text) {
		String[] tokens = text.split("(<br.*>)");
		for (int i = 0; i < tokens.length; i++) {
			if ((!tokens[i].trim().isEmpty())) {
				preds.add(new Predicate(tokens[i].trim()));
			}
        }
	}

	/**
	 * Gets the set of predicates associated with this object.
	 * 
	 * @return The HashSet containing the {@linkplain Predicate} instances associated with this object.
	 */
	public HashSet<Predicate> getPreds() {
		return preds;
	}
	
}
