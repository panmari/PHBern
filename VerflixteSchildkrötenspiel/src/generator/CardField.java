package generator;

import java.awt.Color;

import ch.aplu.jgamegrid.GameGrid;

public class CardField extends GameGrid {
	
	CardField() {
		super(3,3,164, Color.gray, false);
		setBgColor(Color.white);
		setTitle("Turtles");
		show();
	}
}
