package generator;

import gg.Solver;

/**
 * Own thread for the Solver to run.
 * The cards are shuffled before solving the set.
 */
public class SolverLauncher extends Thread {

	private String file;
	
	public SolverLauncher(String file) {
		super();
		this.file = file;
	}
	@Override
	public void run() {
		new Solver(file, true);
	}
}
