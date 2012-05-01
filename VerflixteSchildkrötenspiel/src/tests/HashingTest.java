package tests;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class HashingTest {

	
	private Object grid;
	
	@Before
	public void setUp() throws Exception {
		int[][] grid = {{ 1, 2, 3}, {4, 5, 6}, {7,8,9}};
		this.grid = grid;
	}

	@Test
	public void test() {
		fail("Not yet implemented");
	}

}
