package solution;

import java.awt.Font;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import solver.CardGrid;
import solver.TurtleCard;
import ch.aplu.util.Console;
import ch.aplu.util.ExitListener;

/**
 * This class shows the given solutions in a console window.
 * It doesn't validate any of the solutions.
 * The console is deployed as singleton. (which may or may not be a good idea..)
 */
public class SolutionConsole extends Console implements ExitListener {

	HashMap<SolutionGrid, List<TurtleCard[][]>> solutionMap;
	private CardGrid gg;
	private static SolutionConsole sc;
	
	public static SolutionConsole getInstance() {
		if (sc == null)
			sc = new SolutionConsole();
		else {
			clear();
			sc.show();
		}
		return sc;
	}
	
	public void printSolutions(CardGrid gg, List<SolutionGrid> solutions) {
		getFrame().setVisible(true);
		clear();
		this.gg = gg;
		initializeSolutionMap(solutions);
		
		println("Computation finished, found following solutions: ");
		int i = 1;
		for (SolutionGrid sg: solutionMap.keySet()) {
			println("Solution #" + i + " (and its rotations): ");
			println("With hash: " + sg.hashCode());
			for (TurtleCard[][] tg: solutionMap.get(sg)) {
				print(toIdString(tg));
				println();
			}
			i++;
		}
	}
	
	private SolutionConsole() {
		super(null,null, new Font("Monospaced", Font.PLAIN, 16));
		addExitListener(this);
		setTitle("Solutions for TrickyTurtle");
	}
	
	/**
	 * Creates a hashmap out of the given solution.
	 * Solutions which are the same as an already added solution (rotated)
	 * are added to the list belonging to this solution.
	 * @param solutions
	 */
	private void initializeSolutionMap(List<SolutionGrid> solutions) {
		solutionMap = new HashMap<SolutionGrid, List<TurtleCard[][]>>();
		for (SolutionGrid sg: solutions) {
			if (solutionMap.containsKey(sg)) {
				solutionMap.get(sg).add(sg.getGrid());
			} else {
				LinkedList<TurtleCard[][]> list = new LinkedList<TurtleCard[][]>();
				list.add(sg.getGrid());
				solutionMap.put(sg, list);
			}
		}
	}

	/**
	 * Helper method that gives back a grid of turtlecards as a String.
	 * Is best viewed with a monospace font.
	 * @param grid
	 * @return
	 */
	public static String toIdString(TurtleCard[][] grid) {
		int rotMaxSpace = 5;
		StringBuilder sb = new StringBuilder();
		for (int y = 0; y < grid.length; y++) {
			for (int x = 0; x < grid[y].length; x++) {
				if (grid[x][y] != null) {
					sb.append(" | ");
					sb.append(grid[x][y].getId());
					sb.append(" ");
					String rotString = grid[x][y].getRotation().toString();
					sb.append(rotString);
					//Lazy method to add spaces:
					sb.append("      ".substring(0, rotMaxSpace - rotString.length()));
				} else sb.append(" |        ");
			}
			
			sb.append(" | ");
			sb.append("\n");
		}
		return sb.toString();
	}

	@Override
	public void notifyExit() {
		if (!gg.notifyExit()) {
			clear();
			hide();
		}
		else System.exit(0);	
	}
}
