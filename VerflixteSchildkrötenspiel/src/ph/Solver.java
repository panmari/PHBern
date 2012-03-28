package ph;

public class Solver {

	static CardGrid gg;
	
	public static void main(String[] args) {
		gg = new CardGrid();
		solve();
	}

	private static void solve() {
		gg.putDownNextCard();
	}

}
