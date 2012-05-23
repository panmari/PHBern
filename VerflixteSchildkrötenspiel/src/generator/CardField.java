package generator;

import java.awt.Color;

import ch.aplu.jgamegrid.GGMouse;
import ch.aplu.jgamegrid.GameGrid;

public class CardField extends GameGrid {
	
	CardField(TurtleDragger turtleDragger) {
		super(3,3,164, Color.gray, false);
		setBgColor(Color.white);
		setTitle("Turtles");
		addMouseListener(turtleDragger, GGMouse.lPress | GGMouse.lDrag | GGMouse.lRelease);
		show();
	}
}
