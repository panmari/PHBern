package ph;
import java.util.ArrayList;
import java.util.List;


public class CardGrid {
	private List<TurtleCard> cardSet;
	TurtleCard[][] grid = new TurtleCard[3][3];
			
	public CardGrid() {
		cardSet = new ArrayList<TurtleCard>();
		HalfTurtle[] ta = new HalfTurtle[4];
		ta[0] = new HalfTurtle(Color.YELLOW, Orientation.BACK);
		ta[1] = new HalfTurtle(Color.RED, Orientation.BACK);
		ta[2] = new HalfTurtle(Color.YELLOW, Orientation.FRONT);
		ta[3] = new HalfTurtle(Color.GREEN, Orientation.FRONT);
		cardSet.add(new TurtleCard(ta));
	}
}
