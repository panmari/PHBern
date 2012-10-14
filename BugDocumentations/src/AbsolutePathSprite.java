import ch.aplu.jgamegrid.Actor;
import ch.aplu.jgamegrid.GameGrid;
import ch.aplu.jgamegrid.Location;


public class AbsolutePathSprite extends GameGrid {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new AbsolutePathSprite();
	}

	public AbsolutePathSprite() {
		super(10,10, 30, null, null, true, 4);
		addActor(new Actor("C:\\Users\\Stefan\\tako_trans.gif", 2), new Location(0,0));
		show();
	}
}
