package cmg.gReason.outputs.dtGolog;

import org.junit.jupiter.api.Test;

import cmg.gReason.goalgraph.Formula;

class TranslatorTest {

	Formula f;
	
	@Test
	void testGenSatisfactionFormula() {
		Formula f1 = new Formula();
		Formula f2 = new Formula();
		Formula f3 = new Formula();
		
		f = new Formula();
		
		f.addAndItem(f1);
		f.addAndItem(f2);
		f.addAndItem(f3);
		
		f1.addOrItem(new Formula("Effect I"));
		f1.addOrItem(new Formula("Effect II"));
		
		f2.addOrItem(new Formula("Effect III"));
		f2.addOrItem(new Formula("Effect IV"));
		
		f3.negate();
		f3.addOrItem(new Formula("Neg Effect I"));
		f3.addOrItem(new Formula("Neg Effect II"));
		
		String[] seps = {" , "," ; ", " NOT "};
		
		System.out.println(f.stringPrint(seps));
		
		
		
	}


}
