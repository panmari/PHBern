package gg;

import ch.aplu.jgamegrid.GGNavigationListener;
import ch.aplu.util.Monitor;

public class SolutionNavigation implements GGNavigationListener {
	@Override
	public boolean paused() {
		// do nothing
		return true;
	}

	@Override
	public boolean periodChanged(int arg0) {
		//do nothing
		return true;
	}

	@Override
	public boolean resetted() {
		// do nothing
		return true;
	}

	@Override
	public boolean started() {
		// do nothing
		return true;
	}

	@Override
	public boolean stepped() {
		Monitor.wakeUp();
		return true;
	}

}
