package solver;

public enum CardPosition {
	LEFT (-1, 0, "LT"),
	UP (0, -1, "UP"),
	RIGHT (1, 0, "RT"),
	DOWN (0, 1, "DN");
	
	public int x;
	public int y;
	private String shortString;

	private CardPosition(int xChange, int yChange, String shortString) {
		this.x = xChange;
		this.y = yChange;
		this.shortString = shortString;
	}
	public CardPosition getNext() {
		return CardPosition.values()[(this.ordinal() + 1) % CardPosition.values().length];
	}
	
	public CardPosition getOpposite() {
		return CardPosition.values()[(this.ordinal() + 2) % CardPosition.values().length];
	}

	public String getShortString() {
		return shortString;
	}
}
