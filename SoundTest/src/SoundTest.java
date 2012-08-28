import ch.aplu.jgamegrid.GGSound;
import ch.aplu.jgamegrid.GameGrid;
import ch.aplu.util.SoundPlayer;

/**
 * Seems to me like a buffering/reading error, because the errors
 * are very random/hard to reproduce exactly in the same fashion.
 * But hard to say, especially bc I have no inner knowledge of the
 * SoundPlayer class.
 */
public class SoundTest extends GameGrid {

	private int currentSound = 0;
	private SoundPlayer sp;
	private TestCase[] testList = {
			new TestCase(this, "beep.wav"),
			new TestCase(this, "beep200ms.wav"),
			//shorter sounds than 300ms can not be heard once in a while.
			new TestCase(this, "beep300ms.wav"),
			new TestCase(this, "beep400ms.wav"),
			new TestCase(this, "beep500ms.wav"),
			// the bitrate does not seem to have an influence:
			new TestCase(this, "Mono11_025KHz32bit.wav"),
			new TestCase(this, "Mono16KHz32bit.wav"),
			new TestCase(this, "Mono22_05KHz32bit.wav"),
			new TestCase(this, "Mono32KHz32bit.wav"),
			new TestCase(this, "Mono44_1KHz32bit.wav"),
			new TestCase(this, "Mono48KHz32bit.wav"),
			new TestCase(this, "Mono96KHz32bit.wav")
			};
	
	public SoundTest() {
		super(10,10, 50);
		setTitle("The ultimative GG sound test! Click on \"Step\"");
		show();
		sp = playSound(GGSound.DUMMY);
	}
	
	public void act() {
		if (currentSound < testList.length) {
			testList[currentSound].runTest();
			currentSound++;
		}
		else {
			// does crash when trying to play second time
			// BUT NOT ALWAYS
			setTitle("No more sounds available, starting again at first");
			currentSound = 0;
		}
		
	}
	
	public static void main(String[] args) {
		new SoundTest();
	}
}
