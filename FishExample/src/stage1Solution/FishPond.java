package stage1Solution;

import java.awt.Color;
import java.util.Random;

import ch.aplu.jgamegrid.GameGrid;
import ch.aplu.jgamegrid.Location;

public class FishPond extends GameGrid{
	
	private final int nrFish = 20;
	
	public FishPond() {
		super(800, 600, 1, null, false);
	    setTitle("Fishpond - Stage 1 - Solution");  
	    FishTrap trap = new FishTrap();
	    addActor(trap, getRandomCenterLocation());
	    for (int i = 0; i < nrFish; i++)
		      addActor(new Fish(trap), getRandomEmptyLocation());
	    setBgColor(Color.BLUE);
	    show();
	    doRun();
	    setSimulationPeriod(30);
	}

	
	private Location getRandomCenterLocation() {
		Random rnd = new Random();	
		int x = rnd.nextInt(nbHorzCells - 200) + 100;
		int y = rnd.nextInt(nbVertCells - 200) + 100;
		return new Location(x, y);
	}


	public static void main(String[] args) {
		new FishPond();
	}

}
