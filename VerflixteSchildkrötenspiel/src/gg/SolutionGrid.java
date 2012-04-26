package gg;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;

/**
 * This class is ugly. Fix it!
 * @author panmari
 *
 */
public class SolutionGrid {

	private TurtleCard[][] sol;

	public TurtleCard[][] getSol() {
		return sol;
	}

	public SolutionGrid(TurtleCard[][] grid) {
		sol = new TurtleCard[grid.length][grid[0].length];
		for (int i = 0; i < 3; i++)
			for (int j = 0; j < 3; j++)
				sol[i][j] = grid[i][j].clone();
	}
	
	public int hashCode() {
		List<Point> cornerPoints = getCornerPoints();
		Point minimalCorner = cornerPoints.get(0);
		for (Point p: cornerPoints) {
			if (sol[p.x][p.y].getId() < sol[minimalCorner.x][minimalCorner.y].getId())
				minimalCorner = p;
		}
		return Integer.parseInt(goAround(minimalCorner));
	}
	
	private List<Point> getCornerPoints() {
		ArrayList<Point> cornerPoints = new ArrayList<Point>();
		int borderPos = sol.length - 1;
		cornerPoints.add(new Point(0,0));
		cornerPoints.add(new Point(borderPos,0));
		cornerPoints.add(new Point(0,borderPos));
		cornerPoints.add(new Point(borderPos,borderPos));
		return cornerPoints;
	}

	private String goAround(Point startElement) {
		LinkedList<Point> aroundPoints = new LinkedList<Point>();
		aroundPoints.addLast(new Point(0,0));
		aroundPoints.addLast(new Point(0,1));
		aroundPoints.addLast(new Point(0,2));
		aroundPoints.addLast(new Point(1,2));
		aroundPoints.addLast(new Point(2,2));
		aroundPoints.addLast(new Point(2,1));
		aroundPoints.addLast(new Point(2,0));
		aroundPoints.addLast(new Point(1,0));
		
		String result = "";
		
		Iterator<Point> iter = aroundPoints.listIterator(aroundPoints.indexOf(startElement));
		
		while (iter.hasNext()) {
			Point p = iter.next();
			result += sol[p.x][p.y];
		}
		return result;
	}
}
