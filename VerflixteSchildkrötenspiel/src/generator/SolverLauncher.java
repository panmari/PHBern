package generator;

import gg.Solver;

public class SolverLauncher implements Runnable {

	private String file;
	
	public SolverLauncher(String file) {
		this.file = file;
	}
	@Override
	public void run() {
		Solver.initSolver(file, true);
	}

}
