package fourInARowTest;

import static org.junit.Assert.*;
import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import fourInARow.ArrayManager;
import fourInARow.DBot;

public class DBotTest {

	private ArrayManager am;
	private DBot cp;

	@Before
	public void setUp() throws Exception {
		am = new ArrayManager(7, 6);
		cp = new DBot(am, 1);
	}

	@Test
	public void testIntegerFunctionality() {
		assertTrue(2 == new Integer(2));
		ArrayList<Integer> testList = new ArrayList<Integer>();
		testList.add(3);
		testList.add(3);
		testList.remove(new Integer(1));
		assertEquals(2, testList.size());
		testList.remove(new Integer(3));
		assertEquals(1, testList.size());
		}

}
