package generator;

import gg.CardNotReadyException;


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
		for (DragHalfTurtle ht: cardSetting) {
			if (ht == null)
				throw new CardNotReadyException();
			result += ht.toString() + ";";
		}
		return result.substring(0, result.length()-1);
	}

	public boolean isFullyOccupied() {
		for (DragHalfTurtle ht: cardSetting)
			if (ht == null)
				return false;
		return true;
	}
}
