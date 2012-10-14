package generator;

import java.awt.Color;
import java.awt.Point;
import java.util.List;

import ch.aplu.jgamegrid.GGBackground;
import ch.aplu.jgamegrid.GGButton;
import ch.aplu.jgamegrid.GGMouse;
import ch.aplu.jgamegrid.GameGrid;
import ch.aplu.jgamegrid.Location;

@SuppressWarnings("serial")
public class GeneratorCardGrid extends GameGrid {

	private List<DragHalfTurtle> availableTurtles;
	private MagneticEdgesListener mouseListener;
	private Card[][] cardGrid;
	
	GeneratorCardGrid(List<DragHalfTurtle> availableTurtles) {
		super(4, 3, 166, null, "sprites/generator_bg.jpg", false);
		this.availableTurtles = availableTurtles;
		this.cardGrid = new Card[3][3];
		this.mouseListener = new MagneticEdgesListener(this, cellSize, cardGrid);
		setBgColor(Color.white);
		setTitle("Turtles Generator (www.java-online.ch)");
		initiateLines();
		initiateTurtles();
		initiateCardGrid();
		initiateButton();
		addMouseListener(mouseListener, GGMouse.lDrag | GGMouse.lRelease);
		addStatusBar(20);
		setStatusText("Drag Turtles to the field to place them.");
		show();
	}

	private void initiateLines() {
		GGBackground bg = getBg();
		bg.setPaintColor(Color.GRAY);
		for (int x = cellSize - 1; x <= cellSize*3; x+=cellSize) {
			bg.drawLine(x, 0, x, cellSize*3);
			bg.drawLine(0, x, cellSize*3, x);
		}
		

	}

	private void initiateButton() {
		GGButton generateBtn = new GGButton("sprites/generate_button.jpg");
		generateBtn.addButtonListener(new GenerateButtonListener(this));
		addActor(generateBtn, new Location(3, 2));
		generateBtn.setLocationOffset(new Point(7, 20));
	}

	private void initiateCardGrid() {
		for (int x = 0; x < cardGrid.length; x++)
			for (int y = 0; y < cardGrid[x].length; y++)
				cardGrid[x][y] = new Card();
	}

	private void initiateTurtles() {
		int x = 0;
		int y = -50;
		boolean newRow = true;
		for (DragHalfTurtle ht : availableTurtles) {
			addActor(ht, new Location(0, 0));
			ht.addMouseTouchListener(mouseListener, GGMouse.lPress);
			if (newRow) {
				x = 547;
				y = y + 100;
				newRow = false;
			} else {
				x = x + 80;
				newRow = true;
			}
			ht.setPixelLocation(new Point(x, y));
		}
	}

	public Card[][] getCardGrid() {
		return cardGrid;
	}

	public boolean isFullyOccupied() {
		for (int y = 0; y < cardGrid.length; y++)
			for (int x = 0; x < cardGrid.length; x++)
				if (!cardGrid[x][y].isFullyOccupied())
					return false;
		return true;
	}

}
