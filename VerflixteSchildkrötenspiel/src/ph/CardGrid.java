package ph;
import java.util.ArrayList;
import java.util.List;


public class CardGrid {
	private List<TurtleCard> cardSet;
	private TurtleCard[][] grid = new TurtleCard[3][3];
			
	public CardGrid() {
		cardSet = new ArrayList<TurtleCard>();
		TurtleCardFactory tf = TurtleCardFactory.getInstance();
		cardSet.add(tf.makeTurtleCard("yf;gb;rb;bf"));
		cardSet.add(tf.makeTurtleCard("rf;yb;gb;gf"));
		cardSet.add(tf.makeTurtleCard("gf;bb;yb;bf"));
		cardSet.add(tf.makeTurtleCard("bf;yb;rb;gf"));
		cardSet.add(tf.makeTurtleCard("rf;gb;yb;gf"));
		cardSet.add(tf.makeTurtleCard("yf;bb;rb;gf"));
		cardSet.add(tf.makeTurtleCard("rf;bb;yb;gf"));
		cardSet.add(tf.makeTurtleCard("yf;gb;rb;bf"));
		cardSet.add(tf.makeTurtleCard("yf;bb;rb;bf"));
	}
	
	boolean isThereConflict() {
		for (TurtleCard[] row: grid) {
			for (TurtleCard tc: row) {
				for (Orientation o: Orientation.values()) {
					checkMatch(tc, o);
				}
			}
		}
	}
}
