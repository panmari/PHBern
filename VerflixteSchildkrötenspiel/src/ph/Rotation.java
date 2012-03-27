package ph;

public enum Rotation {
	LEFT,
	UP,
	RIGHT,
	DOWN;
	
	public Rotation getNext() {
		return Rotation.values()[(this.ordinal() + 1) % Rotation.values().length];
		}

}
