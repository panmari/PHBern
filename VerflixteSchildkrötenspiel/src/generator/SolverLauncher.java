package generator;

import gg.Solver;

/**
 * Own thread for the Solver to run.
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
