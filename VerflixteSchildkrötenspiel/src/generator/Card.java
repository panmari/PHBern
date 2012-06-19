package generator;

import gg.CardPosition;

public class Card {
	DragHalfTurtle[] cardSetting;
	
	public Card(){
		cardSetting = new DragHalfTurtle[4];
	}
	
	public void setTurtle(DragHalfTurtle turtle, CardPosition pos) {
		if (cardSetting[pos.ordinal()] != null)
			cardSetting[pos.ordinal()].removeSelf();
		cardSetting[pos.ordinal()] = turtle;
	}
}
