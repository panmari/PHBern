package stage3Solution;

import java.awt.Color;
import java.util.Random;

import ch.aplu.jgamegrid.GameGrid;
import ch.aplu.jgamegrid.Location;

public class FishPond extends GameGrid{
	
	private final int nrFish = 10;
	
	public FishPond() {
		super(800, 600, 1, null, false);
	    setTitle("Fishpond - Stage 3 - Solution");
	    addStatusBar(24);
	    FishTrap trap = new FishTrap();
	    addActor(trap, new Location(-100, -100));
	    for (int i = 0; i < nrFish/2; i++)
		      addActor(new Fish(this, trap), getRandomEmptyLocation());
	    for (int i = 0; i < nrFish/2; i++)
		      addActor(new AngryFish(this, trap), getRandomEmptyLocation());
	    setBgColor(Color.BLUE);
	    show();
	    doRun();
	    setSimulationPeriod(30);
	}


	public void act() {
		int fishNr = getActors(Fish.class).size();
		setStatusText(fishNr + " fish are in the pond.");
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
