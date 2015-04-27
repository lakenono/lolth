package lolth.pacuto.bbs;

import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;

public class PacutoBBSListTaskProducerTest {
	private PacutoBBSListTaskProducer  producer;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@Test
	public void test() {
		int expectResult = 42;
		
		   producer = new PacutoBBSListTaskProducer(null, null);
		
		
		int actualResult = producer.getMaxPage();
		System.out.println(actualResult);
		
		assertTrue("失败，取出的最大页数不是 : " + expectResult, expectResult==actualResult);
		
	}

}
