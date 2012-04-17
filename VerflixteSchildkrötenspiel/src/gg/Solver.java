package gg;

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
			gg.showSolution();
			gg.setStatusText("Lösung gefunden! Klicke noch einmal auf Run um nächste zu finden");
			sleep();
		}
		for (TurtleCard tc: new LinkedList<TurtleCard>(availableCards)) {
			Location p = gg.putDownCard(tc);
			gg.setStatusText("Kein Problem -> Neue Karte hinzugefügt");
			boolean initialRotation = false;
			while (!initialRotation) {
				sleep();
				if (!gg.isThereConflict(p)) {
					List<TurtleCard> leftCards = new LinkedList<TurtleCard>(availableCards);
					leftCards.remove(tc);
					solve(leftCards);
				}
				initialRotation = gg.rotateCardAt(p);
				gg.setStatusText("Problem -> Karte gedreht");
			}
			gg.removeLastCard();
			gg.setStatusText("Problem & alle Drehmöglichkeiten versucht -> Gehe Schritt zurück");
			sleep();
		}	
	}
}
