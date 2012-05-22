package generator;

import ch.aplu.jgamegrid.GameGrid;

public class CardGenerator extends GameGrid {

	public CardGenerator() {
		super(3, 3, 164);
		show();
	}
	
	public void main(String[] args) {
		new CardGenerator();
	}
}
