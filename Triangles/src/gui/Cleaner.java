package gui;

import ch.aplu.jgamegrid.Actor;
import ch.aplu.jgamegrid.GGPanel;

/**
 * Wipes the whole panel in every act();
 * Beware that this class must be first in act order
 * to do its job properly. You can do so be calling
 * <code>
 * setActOrder(Cleaner.class) after adding every Actor.
 * </code>
 */
public class Cleaner extends Actor {

	private GGPanel panel;

	/**
	 * @see Cleaner
	 */
	public Cleaner(GGPanel p) {
		super();
		this.panel = p;
	}
	
	public void act() {
		panel.clear();
	}
}
