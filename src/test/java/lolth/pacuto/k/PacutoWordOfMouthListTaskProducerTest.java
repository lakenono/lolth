package lolth.pacuto.k;

import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;

/**
 * 测试GetMaxPage方法
 * @author shi.lei
 *
 */
public class PacutoWordOfMouthListTaskProducerTest {
	private static PacutoWordOfMouthListTaskProducer producer;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		producer = new PacutoWordOfMouthListTaskProducer("test,","sg10400");
	}

	@Test
	public void testGetMaxPages() {
		int expect = 7;
		int maxPage = producer.getMaxPage();
		
		System.out.println(maxPage);
		assertEquals(expect, maxPage);
	}

}
