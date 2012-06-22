package generator;

import java.awt.Color;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.List;

import ch.aplu.jgamegrid.Actor;
import ch.aplu.jgamegrid.GGBitmap;
import ch.aplu.jgamegrid.GGButton;
import ch.aplu.jgamegrid.GGButtonListener;
import ch.aplu.jgamegrid.GGMouse;
import ch.aplu.jgamegrid.GameGrid;
import ch.aplu.jgamegrid.Location;

public class CardField extends GameGrid implements GGButtonListener {

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
		initiateButton();
		addMouseListener(mouseListener, GGMouse.lDrag);
		addStatusBar(20);
		setStatusText("Drag Turtles to the field to place them.");
		show();
	}

	private void initiateButton() {
		GGButton generateBtn = new GGButton("sprites/generate.jpg");
		generateBtn.addButtonListener(this);
		addActor(generateBtn, new Location(3, 2));
		generateBtn.setPixelLocation(new Point(580, 450));
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
			ht.addMouseTouchListener(mouseListener, GGMouse.lPress | GGMouse.lRelease);
			if (newRow) {
				x = 545;
				y = y + 100;
				newRow = false;
			} else {
				x = x + 80;
				newRow = true;
			}
			ht.setPixelLocation(new Point(x, y));
		}
	}

	@Override
	public void buttonClicked(GGButton button) {
		String gridString = "";
		PrintWriter out;
		try {
			for (int x = 0; x < cardGrid.length; x++) {
				for (int y = 0; y < cardGrid[x].length; y++)
					gridString += cardGrid[x][y] + "\n";
			}
			out = new PrintWriter(new FileWriter("data.txt"));
			out.println(gridString);
			out.close();
			BufferedImage bi = getImage();
			GGBitmap.writeImage(bi, "allCards.jpg", "jpg");
			int imgCounter = 1;
			for (int x = 0; x < 3*cellSize; x+=cellSize) {
				for (int y = 0; y < 3*cellSize; y+=cellSize) {
					BufferedImage card = bi.getSubimage(x, y, x+cellSize, y+cellSize);
					GGBitmap.writeImage(card, "card" + imgCounter++ + ".jpg", "jpg");
				}
			}
		} catch (NullPointerException e) {
			setStatusText("Not all cards are fully occupied!");
		} catch (IOException e) {
			setStatusText("Could not write files!");
		}
		
	}

	@Override
	public void buttonPressed(GGButton button) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void buttonReleased(GGButton button) {
		// TODO Auto-generated method stub
		
	}

}
