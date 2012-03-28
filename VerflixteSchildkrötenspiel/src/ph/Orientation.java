package ph;

public enum Orientation {
	FRONT('f'),
	BACK('b');

	private Orientation(final char charRepresenation){ 
		this.charRepresentation = charRepresenation; 
	}
	
    private final char charRepresentation;
    
	public static Orientation getByCharacter(char character) {
		for (Orientation o: Orientation.values())
			if (character == o.getCharRepresentation())
				return o;
		throw new IllegalArgumentException();
	}
	
	public char getCharRepresentation() {
		return charRepresentation;
	}

	public Orientation getOpposite() {
		return Orientation.values()[(this.ordinal() + 1) % 2];
	}
}
