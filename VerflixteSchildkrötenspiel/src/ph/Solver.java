package ph;

import java.awt.Point;
import java.util.ArrayList;

public class Solver {

	static CardGrid gg;
	
	public static void main(String[] args) {
		gg = new CardGrid();
		ArrayList<TurtleCard> deadCards = new ArrayList<TurtleCard>(); //empty to begin with
		Point p = gg.putDownNextAliveCard(deadCards);
		solve(p, deadCards);
	}

	private static void solve(Point p, ArrayList<TurtleCard> deadCards) {
		System.out.println(gg.toString());
		if (gg.isSolved()) //=> done!
			return;
		if (gg.isThereConflict(p)) {
			if (!gg.rotateCardAt(p)) {
				solve(p, deadCards);
			} else {
				deadCards.add(gg.getCardAt(p));
				Point oneBack = gg.removeCardAt(p);
				solve(oneBack, deadCards);
			}
		} else {
			solve(gg.putDownNextAliveCard(deadCards), new ArrayList<TurtleCard>());
		}
	}

}
