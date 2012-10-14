package solution;

import solver.CardGrid;
import ch.aplu.jgamegrid.GGNavigationListener;
import ch.aplu.util.Monitor;

/**
 * Disables all buttons while showing the solution screen
 * @author mazzzy
 */
public class SolutionNavigation implements GGNavigationListener {
	
	private CardGrid gg;

	public SolutionNavigation(CardGrid cardGrid) {
		this.gg = cardGrid;
	}

	@Override
	public boolean paused() {
		closeInfo();
		return true;
	}

	@Override
	public boolean periodChanged(int arg0) {
		closeInfo();
		return true;
	}

	@Override
	public boolean resetted() {
		closeInfo();
		return true;
	}

	@Override
	public boolean started() {
		closeInfo();
		return true;
	}

	@Override
	public boolean stepped() {
		closeInfo();
		return true;
	}

	public void closeInfo() {
		gg.setStatusText("Close window to return to generator.");
	}
}
