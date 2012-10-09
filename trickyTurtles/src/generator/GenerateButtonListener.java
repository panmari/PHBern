package generator;

import gg.CardNotReadyException;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import javax.naming.NoPermissionException;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import ch.aplu.jgamegrid.GGBitmap;
import ch.aplu.jgamegrid.GGButton;
import ch.aplu.jgamegrid.GGButtonListener;
import ch.aplu.jgamegrid.GGInputString;
/**
 * Handles the generation of a dataset. Needs a folder with 
 * write permissions for this purpose, which is supplied by the user.
 * After successfully creating a dataset, the solver is started.
 */
public class GenerateButtonListener implements GGButtonListener {

	private GeneratorCardGrid gg;
	private int cellSize;
	private Card[][] cardGrid;

	public GenerateButtonListener(GeneratorCardGrid gg) {
		this.gg = gg;
		this.cellSize = gg.getCellSize();
		this.cardGrid = gg.getCardGrid();
	}

	@Override
	public void buttonClicked(GGButton button) {
		exportDataSet();
	}
	
	/**
	 * This method spawns various dialogs concerning the creation of a dataset,
	 * then hands over this information to generateDataSetFiles()
	 * @see generateDataSetFiles
	 */
	private void exportDataSet()  {
		if (!isGridFullyOccupied()) {
			gg.setStatusText("Not all cards are fully occupied!");
			return;
		}
		JFileChooser chooser = new JFileChooser(); 
	    chooser.setCurrentDirectory(new java.io.File("."));
	    chooser.setDialogTitle("Chose a directory to save your set");
	    chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
	    String trickyHome = "";
		try {
			String prefix = JOptionPane.showInputDialog(gg, 
					"What's the name of your new set of cards?",
					"Choose a name", JOptionPane.QUESTION_MESSAGE);
		    if (prefix == null) //clicked on "Cancel"
		    	throw new InterruptedException();
		    if (prefix.isEmpty() || prefix.contains(" "))
		    	throw new IllegalArgumentException("Invalid name");
			if (chooser.showOpenDialog(gg) == JFileChooser.APPROVE_OPTION) 
		    	trickyHome = new File(chooser.getSelectedFile(), "trickyTurtles").getAbsolutePath();
		    else throw new InterruptedException();
			String spriteDirectory = new File(trickyHome, "sprites").getAbsolutePath();
			System.out.println(spriteDirectory);
			new File(spriteDirectory).mkdirs();
			if (new File(spriteDirectory).canWrite())
				generateDataSetFiles(trickyHome, spriteDirectory, prefix);
			else throw new NoPermissionException();
		} catch (InterruptedException e) {
			gg.setStatusText("Generation of cards canceled by user");
		} catch (NoPermissionException e) {
			gg.setStatusText("No write permissions for " + trickyHome);
		} catch (IllegalArgumentException e) {
			gg.setStatusText(e.getMessage());
		}
	}
	
	/**
	 * Generates the files for a dataset in the given directories with the given presets.
	 * @param trickyHome
	 * @param spriteDirectory
	 * @param prefix
	 */
	private void generateDataSetFiles(String trickyHome, String spriteDirectory, String prefix) {
		try {
			PrintWriter out;
			String gridString = "";
			int imgCounter = 0;
			for (int y = 0; y < cardGrid.length; y++)
				for (int x = 0; x < cardGrid.length; x++) 
					gridString += cardGrid[x][y].toString() + " " + 
									new File(spriteDirectory, prefix + imgCounter++ + ".jpg") + 
									"\n";
			File datasetFile = new File(trickyHome, prefix + ".data");
			out = new PrintWriter(new FileWriter(datasetFile));
			out.println(gridString);
			out.close();
			BufferedImage bi = gg.getImage().getSubimage(0,0,cellSize*3, cellSize*3);
			File allCards = new File(spriteDirectory, prefix + "allCards.jpg");
			GGBitmap.writeImage(bi, allCards.getAbsolutePath(), "jpg");
			imgCounter = 0;
			for (int y = 0; y < 3*cellSize; y+=cellSize) {
				for (int x = 0; x < 3*cellSize; x+=cellSize) {
					BufferedImage card = bi.getSubimage(x, y, cellSize, cellSize);
					File f = new File(spriteDirectory, prefix + imgCounter++ + ".jpg");
					GGBitmap.writeImage(card, f.getAbsolutePath(), "jpg");
				}
			}
			boolean fastForward = JOptionPane.showConfirmDialog(gg, 
							"Turtle set successfully created in " + trickyHome +
							". \n Do you want to fast forward to the solution(s)?", 
							"Fast forward?", 
							JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION;
			gg.setStatusText("Done! Saved files under " + trickyHome);
			new SolverLauncher(datasetFile.getAbsolutePath(), fastForward).start();
			gg.hide();
		} catch (IOException e) {
			gg.setStatusText("Could not write files in " + trickyHome);
		} catch (CardNotReadyException e) {
			//this condition is already checked in the preceding method
			gg.setStatusText("Not all cards are fully occupied yet.");
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
		// nothing
	}

	@Override
	public void buttonReleased(GGButton button) {
		// nothing
	}

}
