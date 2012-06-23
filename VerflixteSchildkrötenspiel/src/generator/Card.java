package generator;

public class Card {
	DragHalfTurtle[] cardSetting;
	
	public Card(){
		cardSetting = new DragHalfTurtle[4];
	}
	
	public void setTurtle(DragHalfTurtle turtle) {
		int posOrdinal = turtle.getPos().ordinal();
		if (cardSetting[posOrdinal] != null)
			cardSetting[posOrdinal].removeSelf();
		cardSetting[posOrdinal] = turtle;
	}
	
	public String toString() {
		String result = "";
		for (DragHalfTurtle ht: cardSetting)
			result += ht.toString() + ";";
		return result;
	}
}
