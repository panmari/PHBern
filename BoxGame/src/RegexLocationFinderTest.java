import static org.junit.Assert.*;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Before;
import org.junit.Test;


public class RegexLocationFinderTest {

	private Pattern locRegex;

	@Before
	public void setUp() throws Exception {
		locRegex = Pattern.compile("\\((\\d*),(\\d*)\\) (.*)");
	}

	@Test
	public void shouldFindSingleDigits() {
		String text = "(1,3) asdf";
		Matcher locFinder = locRegex.matcher(text);
		assertTrue(locFinder.matches());
    	String x = locFinder.group(1);
    	assertEquals("1", x);
    	String y = locFinder.group(2);
    	assertEquals("3", y);
	}

	@Test
	public void shouldFindMultipleDigits() {
		String text = "(121,331) asdf";
		
		Matcher locFinder = locRegex.matcher(text);
		assertTrue(locFinder.matches());
    	String x = locFinder.group(1);
    	assertEquals("121", x);
    	String y = locFinder.group(2);
    	assertEquals("331", y);
    	assertEquals("asdf", locFinder.group(3));
	}

}
