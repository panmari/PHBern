package generator;

import solver.Solver;

/**
 * Own thread for the Solver to run.
 * The cards are shuffled before solving the set.
 */
public class SolverLauncher extends Thread {

	private String file;
	private boolean fastForward;
	
	public SolverLauncher(String file, boolean fastForward) {
		super();
		this.file = file;
		this.fastForward = fastForward;
	}
	@Override
	public void run() {
		new Solver(file, true, fastForward);
	}
}
