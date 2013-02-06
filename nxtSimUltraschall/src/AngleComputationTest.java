

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

/**
 * Test names are referencing to the position of the robot respective to the middle.
 * Keep in mind that the middle is (0,0), the upper left corner (-250, -250) and the lower right corner (250, 250).
 * @author mazzzy
 */
public class AngleComputationTest {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testUpperLeftQuadrant() {
		int[] middleDist = { -150, -150 };
		int angle = SearchMiddleEffectively.computeTurnAngleToMiddle(middleDist);
		assertEquals(-135, angle);
	}
	
	@Test
	public void testlowerLeftQuadrant() {
		int[] middleDist = { 150, -150 };
		int angle = SearchMiddleEffectively.computeTurnAngleToMiddle(middleDist);
		assertEquals(-45, angle);
	}
	
	@Test
	public void testLowerRightQuadrant() {
		int[] middleDist = { 150, 150 };
		int angle = SearchMiddleEffectively.computeTurnAngleToMiddle(middleDist);
		assertEquals(45, angle);
	}
	
	@Test
	public void testUpperRightQuadrant() {
		int[] middleDist = { -150, 150 };
		int angle = SearchMiddleEffectively.computeTurnAngleToMiddle(middleDist);
		assertEquals(135, angle);
	}
	


}
