package gg;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import ch.aplu.jgamegrid.Location;
import ch.aplu.util.Monitor;

public class Solver {

	/** Set this to true if you want to reach the solutions as fast as possible */
	private static final boolean fastForward = true;
	private static CardGrid gg;
	private static long steps;
	private static String status;
	private static ArrayList<SolutionGrid> solutions = new ArrayList<SolutionGrid>();
	
	public static void main(String[] args) {
		gg = new CardGrid();
		solve(gg.getCards());
		new SolutionConsole(gg, solutions);
	}

	/**
	 * Puts the thread to sleep, but only when the slider
	 * isn't all the way to the right. If it is all the 
	 * way to the right, it isn't put to sleep at all for performance reasons.
	 * @param really  if true, the thread is put to sleep anyway, ignoring the slider settings. If
	 * the global variable fastForward is set to true, this parameter is ignored.
	 */
	private static void sleep(boolean really) {
		if ((really || gg.getSimulationPeriod() > 0) && !fastForward) {
			gg.setStatusText(status);
			gg.setTitle("Tricky Turtle (www.java-online.ch) -- Steps: " + steps);
			gg.refresh();
			Monitor.putSleep();
		}
	}
	
	private static void solve(List<TurtleCard> availableCards) {
		if (gg.isSolved()) { //=> done!
			gg.showSolution();
			solutions.add(new SolutionGrid(gg.getGrid()));
			status = "Found solution in " + steps + " steps! Click again on run to find another...";
			sleep(true);
		}
		for (TurtleCard tc: new LinkedList<TurtleCard>(availableCards)) {
			Location p = gg.putDownCard(tc);
			status = "No conflict -> Added new card";
			boolean initialRotation = false;
			while (!initialRotation) {
				steps++;
				sleep(false);
				if (!gg.isThereConflict(p)) {
					List<TurtleCard> leftCards = new LinkedList<TurtleCard>(availableCards);
					leftCards.remove(tc);
					solve(leftCards);
				}
				initialRotation = gg.rotateCardAt(p);
				status = "Conflict -> Turned card";
			}
			gg.removeLastCard();
			status = "Conflict & tried all orientations -> Go one step back";
			sleep(false);
		}	
	}
}
