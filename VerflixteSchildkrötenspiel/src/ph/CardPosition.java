package ph;

public enum CardPosition {
	LEFT (-1, 0),
	UP (0, -1),
	RIGHT (1, 0),
	DOWN (0, 1);
	
	int x;
	int y;

	private CardPosition(int xChange, int yChange) {
		this.x = xChange;
		this.y = yChange;
				
	}
	public CardPosition getNext() {
		return CardPosition.values()[(this.ordinal() + 1) % CardPosition.values().length];
	}
	
	public CardPosition getOpposite() {
		return CardPosition.values()[(this.ordinal() + 2) % CardPosition.values().length];
	}

}
