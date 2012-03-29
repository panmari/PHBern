package ph;

import java.awt.Point;

public class Solver {

	static CardGrid gg;
	
	public static void main(String[] args) {
		gg = new CardGrid();
		Point p = gg.putDownNextCard();
		solve(p);
		
	}

	private static void solve(Point p) {
		if (p == null) //=> done!
			return;
		if (gg.isThereConflict(p)) {
			if (gg.rotateCardAt(p)) {
				solve(p);
			} else {
				Point oneBack = gg.removeCardAt(p);
				solve(oneBack);
			}
		} else {
			solve(gg.putDownNextCard());
		}
			
		
		
	}

}
