package generator;

import java.awt.Color;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.naming.NoPermissionException;
import javax.swing.JFileChooser;

import ch.aplu.jgamegrid.GGBitmap;
import ch.aplu.jgamegrid.GGButton;
import ch.aplu.jgamegrid.GGButtonListener;
import ch.aplu.jgamegrid.GGInputString;
import ch.aplu.jgamegrid.GGMouse;
import ch.aplu.jgamegrid.GameGrid;
import ch.aplu.jgamegrid.Location;

public class CardField extends GameGrid implements GGButtonListener {

	private List<DragHalfTurtle> availableTurtles;
	private MagneticEdgesListener mouseListener;
	private Card[][] cardGrid;
	
	CardField(List<DragHalfTurtle> availableTurtles) {
		super(4, 3, 166, null, "sprites/generator_bg.jpg", false);
		this.availableTurtles = availableTurtles;
		this.cardGrid = new Card[3][3];
		this.mouseListener = new MagneticEdgesListener(this, cellSize, cardGrid);
		setBgColor(Color.white);
		setTitle("Turtles Generator (www.java-online.ch)");
		initiateTurtles();
		initiateCardGrid();
		initiateButton();
		addMouseListener(mouseListener, GGMouse.lDrag | GGMouse.lRelease);
		addStatusBar(20);
		setStatusText("Drag Turtles to the field to place them.");
		show();
	}

	private void initiateButton() {
		GGButton generateBtn = new GGButton("sprites/generate_button.jpg");
		generateBtn.addButtonListener(this);
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

	@Override
	public void buttonClicked(GGButton button) {
		exportDataSet();
	}
	
	/**
	 * This method exports the turtles to various images and a textfile containing
	 * the configuration as string.
	 * These is saved in the users home folder in the subfolder gamegrid.
	 */
	private void exportDataSet()  {
		String prefix = "";
		String gridString = "";
		PrintWriter out;
		if (!isGridFullyOccupied()) {
			setStatusText("Not all cards are fully occupied!");
			// return;
		}
		
		GGInputString popup = new GGInputString("Name?", 
				"What's the name of your new set of cards?",
				"custom_turtles");
		
		JFileChooser chooser = new JFileChooser(); 
	    chooser.setCurrentDirectory(new java.io.File("."));
	    chooser.setDialogTitle("Chose a directory to save your set");
	    chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
	    String trickyHome;
		try {
			while (prefix.isEmpty())
				prefix = popup.show();
		    if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) 
		    	trickyHome = chooser.getCurrentDirectory().getAbsolutePath() + "trickyTurtles/";
		    else throw new NoPermissionException();
			String spriteDirectory = trickyHome + "sprites/";
			System.out.println(spriteDirectory);
			new File(spriteDirectory).mkdirs();
			int imgCounter = 0;
			for (int y = 0; y < cardGrid.length; y++)
				for (int x = 0; x < cardGrid.length; x++)
					gridString += cardGrid[x][y] + " sprites/" + prefix + imgCounter++ + ".jpg" + "\n";
			
			out = new PrintWriter(new FileWriter(trickyHome + prefix + ".data"));
			out.println(gridString);
			out.close();
			//TODO: Remove grid before getting image
			BufferedImage bi = getImage().getSubimage(0,0,cellSize*3, cellSize*3);
			GGBitmap.writeImage(bi, spriteDirectory + prefix + "allCards.jpg", "jpg");
			imgCounter = 0;
			for (int y = 0; y < 3*cellSize; y+=cellSize) {
				for (int x = 0; x < 3*cellSize; x+=cellSize) {
					BufferedImage card = bi.getSubimage(x, y, cellSize, cellSize);
					GGBitmap.writeImage(card, spriteDirectory + prefix + imgCounter++ + ".jpg", "jpg");
				}
			}
			setStatusText("Done! Saved files under " + trickyHome);
			new SolverLauncher(trickyHome + prefix + ".data").start();
		} catch (IOException e) {
			setStatusText("Could not write files!");
		} catch (NoPermissionException e) {
			setStatusText("No Permissions for this folder");
		} catch (NullPointerException e) {
			setStatusText("This shouldn't happen anymore..");
		} catch (RuntimeException e) { //TODO: throw exception instead of terminating!
			setStatusText("You clicked on cancel! I wonder why?");
		}
		
	}

	private boolean isGridFullyOccupied() {
		for (int y = 0; y < cardGrid.length; y++)
			for (int x = 0; x < cardGrid.length; x++)
				if (!cardGrid[x][y].isFullyOccupied())
					return false;
		return true;
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
