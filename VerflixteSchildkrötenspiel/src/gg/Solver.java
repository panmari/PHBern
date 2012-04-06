package gg;

import java.awt.Point;
import java.util.LinkedList;
import java.util.List;

import ch.aplu.jgamegrid.Location;
import ch.aplu.util.Monitor;

public class Solver {

	static CardGrid gg;
	
	public static void main(String[] args) {
		gg = new CardGrid();
		solve(gg.getCards());
	}
	
	private static void sleep() {
		gg.refresh();
		Monitor.putSleep();
	}
	
	private static void solve(List<TurtleCard> availableCards) {
		if (gg.isSolved()) { //=> done!
			System.out.println("Found Solution: ");
			System.out.println(gg);
		}
		for (TurtleCard tc: new LinkedList<TurtleCard>(availableCards)) {
			Location p = gg.putDownCard(tc);
			boolean initialRotation = false;
			while (!initialRotation) {
				sleep();
				if (!gg.isThereConflict(p)) {
					List<TurtleCard> leftCards = new LinkedList<TurtleCard>(availableCards);
					leftCards.remove(tc);
					solve(leftCards);
				}
				initialRotation = gg.rotateCardAt(p);
			}
			gg.removeLastCard();
			sleep();
		}			
	}
}
