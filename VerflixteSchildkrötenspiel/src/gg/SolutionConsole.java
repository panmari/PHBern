package gg;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import ch.aplu.util.Console;

public class SolutionConsole extends Console {

	HashMap<SolutionGrid, List<TurtleCard[][]>> rotatedSolutions = new HashMap<SolutionGrid, List<TurtleCard[][]>>();
	public SolutionConsole(List<SolutionGrid> solutions) {
		super();
		checkForRotatedSolutions(solutions);
		
		println("Computation finished, found following solutions: ");
		int i = 1;
		for (SolutionGrid sg: rotatedSolutions.keySet()) {
			println("Solution #" + i + ": ");
			println("With hash: " + sg.hashCode());
			for (TurtleCard[][] tg: rotatedSolutions.get(sg)) {
				print(toIdString(tg));
				println();
			}
			i++;
		}
		println("Of which some may be rotated versions of others");
	}
	
	private void checkForRotatedSolutions(List<SolutionGrid> solutions) {
		for (SolutionGrid sg: solutions) {
			if (rotatedSolutions.containsKey(sg)) {
				rotatedSolutions.get(sg).add(sg.getSol());
			} else {
				LinkedList<TurtleCard[][]> list = new LinkedList<TurtleCard[][]>();
				list.add(sg.getSol());
				rotatedSolutions.put(sg, list);
			}
		}
	}

	public static String toIdString(TurtleCard[][] grid) {
		StringBuilder sb = new StringBuilder();
		for (int y = 0; y < grid.length; y++) {
			for (int x = 0; x < grid[y].length; x++) {
				sb.append(" | ");
				sb.append(grid[x][y].getId());
				sb.append(" ");
				sb.append(grid[x][y].getRotation());
			}
			sb.append(" | ");
			sb.append("\n");
		}
		return sb.toString();
	}
}
