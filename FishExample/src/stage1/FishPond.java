package stage1;

import java.awt.Color;

import ch.aplu.jgamegrid.GameGrid;

public class FishPond extends GameGrid{
	
	private final int nrFish = 20;
	
	public FishPond() {
		super(800, 600, 1, null, false);
	    setTitle("Fishpond - Stage 1");  
	    FishTrap trap = new FishTrap();
	    addActor(trap, getRandomEmptyLocation());
	    for (int i = 0; i < nrFish; i++)
		      addActor(new Fish(trap), getRandomEmptyLocation());
	    setBgColor(Color.BLUE);
	    show();
	    doRun();
	    setSimulationPeriod(30);
	}

	
	public static void main(String[] args) {
		new FishPond();
	}

}
