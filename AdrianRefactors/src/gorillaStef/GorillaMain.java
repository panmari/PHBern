package gorillaStef;

import ch.aplu.jgamegrid.Actor;
import ch.aplu.jgamegrid.GameGrid;
import ch.aplu.jgamegrid.Location;

public class GorillaMain extends GameGrid {

	private WindSock ws = new WindSock();
	private Location[] gorillaLoc = {new Location(35,565), new Location(1165,565)};
	private Location[] bananaLunchLoc = {new Location(70, 535), new Location(1130, 535)};
	private Location[] youWinLoc = {new Location(100, 500), new Location(1100, 500)};
	private Gorilla[] players = new Gorilla[2];
	private int currentPlayer = 0;
	private Actor youWin = new Actor("sprites/you_win.gif");
	private boolean gameOver;
	
	public GorillaMain() {
		super(1200, 600, 1, null, "sprites/townBig.jpg", false);
		setSimulationPeriod(30);
		addActor(ws, new Location(nbHorzCells/2, 40));
		for (int i = 0; i < 2; i++) {
			players[i] = new Gorilla(this, bananaLunchLoc[i], i);
			players[i].addActorCollisionListener(players[i]);
			addActor(players[i], gorillaLoc[i]);
		}
		show();
		doRun();
		while(true) {
			ws.setRandomWind();
			players[currentPlayer].lunchBanana();
			while (!getActors(Banana.class).isEmpty()) {
				if (gameOver) {
					addActor(youWin, youWinLoc[currentPlayer]);
					setTitle("Game finished! New game starts in 5 seconds.");
					refresh(); //should not be needed
					delay(5000);
					reset();
				}
			}
			
			currentPlayer = (currentPlayer + 1) % 2;
		}
	}

	public WindSock getWindSock() {
		return ws;
	}
	
	public Gorilla getEnemyGorilla(int player) {
		int enemyNb = (player + 1) % 2;
		return players[enemyNb];
	}
	
	public static void main(String[] args) {
		new GorillaMain();
	}
	
	public void reset() {
		removeActors(Banana.class);
		removeActor(youWin);
		getBg().clear();
		gameOver = false;
	}

	public void setGorillaTitle(boolean isRightGorilla) {
		if (isRightGorilla)
			setTitle("Gorilla on the right is throwing!");
		else setTitle("Gorilla on the left is throwing!");
	}

	public void gorillaWasHit() {
		gameOver = true;
	}
}
