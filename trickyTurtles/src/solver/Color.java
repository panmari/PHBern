package solver;

public enum Color {
	YELLOW('y'),
	GREEN('g'),
	RED('r'),
	BLUE('b');
	
	private Color(final char charRepresentation) {
		this.charRepresentation = charRepresentation;
	}

	private final char charRepresentation;
	
	public static Color getByCharacter(char character) {
		for (Color c: Color.values())
			if (character == c.getCharRepresentation())
				return c;
		throw new IllegalArgumentException(character + " is not a valid color");
	}

	public char getCharRepresentation() {
		return charRepresentation;
	}
}
