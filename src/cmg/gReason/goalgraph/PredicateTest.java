package cmg.gReason.goalgraph;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class PredicateTest {

	@Test
	void test() {
		Predicate p = new Predicate ("red(apple)");
		assertEquals("red(apple)", p.getPredicateText());
		assertEquals("red(apple,S)", p.getPredicateTextWithSituation(false));
		assertEquals(1, p.getArity());
		
		p = new Predicate ("r ed(apple)");
		assertEquals("r ed(apple)", p.getPredicateText());
		assertEquals("r ed(apple,S)", p.getPredicateTextWithSituation(false));
		assertEquals(1, p.getArity());
		
		p = new Predicate ("has(mary,apple)");
		assertEquals("has(mary,apple)", p.getPredicateText());
		assertEquals("has_fl(mary,apple,S)", p.getPredicateTextWithSituation(true));
		assertEquals(2, p.getArity());
		
		p = new Predicate ("world");
		assertEquals("world", p.getPredicateText());
		assertEquals("world_fl(S)", p.getPredicateTextWithSituation(true));
		assertEquals(0, p.getArity());
		
		p = new Predicate("hello(my,friedn)");
		Predicate q = new Predicate("hello(my,friend)");
		assertTrue(p.equals(q));
	}

}
