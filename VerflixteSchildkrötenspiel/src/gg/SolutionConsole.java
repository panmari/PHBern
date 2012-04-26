package gg;

import java.util.HashMap;
import java.util.List;

import ch.aplu.util.Console;

public class SolutionConsole extends Console {

	HashMap<TurtleCard[][], List<TurtleCard[][]>> rotatedsolutions;
	public SolutionConsole(List<TurtleCard[][]> solutions) {
		super();
		println("Computation finished, found following solutions: ");
		for (int i = 0; i < solutions.size(); i++) {
			println("Solution #" + (i+1) + ": ");
			print(toIdString(solutions.get(i)));
			println();
		}
		println("Of which some may be rotated versions of others");
	}
	
	private void checkForRotatedSolutions() {
		//TODO LULZ i duno
		//except: n^2
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
