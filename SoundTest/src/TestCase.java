
class TestCase {
	
	private String fileName;
	private SoundTest gg;

	public TestCase(SoundTest gg, String fileName) {
		this.fileName = fileName;
		this.gg = gg;
	}
	
	public void runTest() {
		gg.setTitle("Playing: " + fileName);
		gg.playSound("wav/" + fileName);
	}
}