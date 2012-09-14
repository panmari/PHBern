package gg;

import java.awt.Font;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import ch.aplu.util.Console;

/**
 * This class shows the given solutions in a console window.
 * It doesn't validate any of the solutions.
 */
public class SolutionConsole extends Console {

	HashMap<SolutionGrid, List<TurtleCard[][]>> solutionMap = new HashMap<SolutionGrid, List<TurtleCard[][]>>();
	
	public SolutionConsole(CardGrid gg, List<SolutionGrid> solutions) {
		super(null,null, new Font("Monospaced", Font.PLAIN, 16));
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
		gg.cycleThroughSolutions(solutionMap.keySet());
	}
	
	/**
	 * Creates a hashmap out of the given solution.
	 * Solutions which are the same as an already added solution (rotated)
	 * are added to the list belonging to this solution.
	 * @param solutions
	 */
	private void initializeSolutionMap(List<SolutionGrid> solutions) {
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
}
