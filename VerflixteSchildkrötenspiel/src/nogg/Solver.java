package nogg;

import java.util.LinkedList;
import java.util.List;

import ch.aplu.jgamegrid.Location;

public class Solver {

	static CardGrid gg;
	static Timer t;
	static long steps;
	
	public static void main(String[] args) {
		gg = new CardGrid();
		t = new Timer();
		t.reset();
		solve(gg.getCards());
		System.out.println("Finished after " + t.timeElapsed() + " ms");
		System.out.println("Steps:  " + steps);
	}
	
	private static void solve(List<TurtleCard> availableCards) {
		if (gg.isSolved()) { //=> done!
			System.out.println("Found Solution after " + t.timeElapsed() + " ms");
			System.out.println(gg);
		}
		for (TurtleCard tc: new LinkedList<TurtleCard>(availableCards)) {
			Location p = gg.putDownCard(tc);
			boolean initialRotation = false;
			while (!initialRotation) {
				if (!gg.isThereConflict(p)) {
					List<TurtleCard> leftCards = new LinkedList<TurtleCard>(availableCards);
					leftCards.remove(tc);
					solve(leftCards);
				}
				initialRotation = gg.rotateCardAt(p);
				steps++;
			}
			gg.removeLastCard();
		}			
	}
}
