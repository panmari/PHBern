package generator;

import java.awt.Color;
import java.awt.Point;
import java.util.List;

import ch.aplu.jgamegrid.GGMouse;
import ch.aplu.jgamegrid.GameGrid;
import ch.aplu.jgamegrid.Location;

public class CardField extends GameGrid {

	private List<DragHalfTurtle> availableTurtles;
	private MagneticEdgesListener mouseListener;
	private Card[][] cardGrid;
	
	CardField(List<DragHalfTurtle> availableTurtles) {
		super(4, 3, 166, Color.gray, false);
		this.availableTurtles = availableTurtles;
		this.cardGrid = new Card[3][3];
		this.mouseListener = new MagneticEdgesListener(this, cellSize, cardGrid);
		setBgColor(Color.white);
		setTitle("Turtles Generator");
		initiateTurtles();
		initiateCardGrid();
		addMouseListener(mouseListener, GGMouse.lDrag);
		show();
	}

	private void initiateCardGrid() {
		for (int x = 0; x < cardGrid.length; x++)
			for (int y = 0; y < cardGrid[x].length; y++)
				cardGrid[x][y] = new Card();
	}

	private void initiateTurtles() {
		int x = 0;
		int y = 0;
		boolean newRow = true;
		for (DragHalfTurtle ht : availableTurtles) {
			addActor(ht, new Location(0, 0));
			ht.addMouseTouchListener(mouseListener, GGMouse.lPress | GGMouse.lRelease);
			if (newRow) {
				x = 535;
				y = y + 100;
				newRow = false;
			} else {
				x = x + 80;
				newRow = true;
			}
			ht.setPixelLocation(new Point(x, y));
		}
	}

}
