package cmg.gReason.outputs.dtGolog;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class PreconditionProcessorTest {

	@Test
	void test() {
		PreconditionProcessor f = new PreconditionProcessor("((this(x) AND (because OR onemorething(kala,krasia,file)) OR theother(z,c))");
		//f.extractPredicates();
		//for(Predicate p:f.getPredicates())
		//	System.out.println(p.getPredicate());
		//	f.translateToDT();
		assertEquals("((this(x,S),(because(S),onemorething(kala,krasia,file,S)),theother(z,c,S))",f.getDTFormula());
	}

}
