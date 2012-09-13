package gg;

import java.awt.Point;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * This class represents a solution of a tricky turtle game.
 * The creator of the instance ins responsible for providing a valid solution.
 * The validity of the given solutions is NOT checked by this class.
 * @author panmari
 */
public class SolutionGrid {

	private TurtleCard[][] grid;

	public TurtleCard[][] getGrid() {
		return grid;
	}

	public SolutionGrid(TurtleCard[][] solution) {
		grid = new TurtleCard[solution.length][solution[0].length];
		for (int i = 0; i < 3; i++)
			for (int j = 0; j < 3; j++)
				grid[i][j] = solution[i][j].clone();
	}
	
	/**
	 * This method returns the 4 corners of the rectangular grid which
	 * is saved as solution in this instance.
	 * @return the 4 boarder points assembled in a list.
	 */
	private List<Point> getCornerPoints() {
		ArrayList<Point> cornerPoints = new ArrayList<Point>();
		int borderPos = grid.length - 1;
		cornerPoints.add(new Point(0,0));
		cornerPoints.add(new Point(borderPos,0));
		cornerPoints.add(new Point(0,borderPos));
		cornerPoints.add(new Point(borderPos,borderPos));
		return cornerPoints;
	}

	/**
	 * Goes around the grid in a circular motion, starting at the given startElement,
	 * and puts all values into a string.
	 * @param startElement
	 * @return the string created by going around.
	 */
	private String goAround(Point startElement) {
		LinkedList<Point> aroundPoints = new LinkedList<Point>();
		//Ugly hardcode:
		aroundPoints.addLast(new Point(0,0));
		aroundPoints.addLast(new Point(0,1));
		aroundPoints.addLast(new Point(0,2));
		aroundPoints.addLast(new Point(1,2));
		aroundPoints.addLast(new Point(2,2));
		aroundPoints.addLast(new Point(2,1));
		aroundPoints.addLast(new Point(2,0));
		aroundPoints.addLast(new Point(1,0));
		
		String result = "";
		int posStartElement = aroundPoints.indexOf(startElement);
		List<Point> sortedAroundPoints = aroundPoints.subList(posStartElement, aroundPoints.size());
		sortedAroundPoints.addAll(aroundPoints.subList(0, posStartElement));
		
		for (Point p: sortedAroundPoints) {
			result += grid[p.x][p.y].getId();
		}
		return result;
	}

	/**
	 * A solution has the same hash code as in every rotation state.
	 * TODO: Orientation of the cards should also influence hashCode
	 */
	@Override
	public int hashCode() {
		List<Point> cornerPoints = getCornerPoints();
		Point maximalCorner = cornerPoints.get(0);
		for (Point p: cornerPoints) {
			if (grid[p.x][p.y].getId() > grid[maximalCorner.x][maximalCorner.y].getId())
				maximalCorner = p;
		}
		return Integer.parseInt(goAround(maximalCorner));
	}
	
	/**
	 * Two solutions are equal if the have the same hashcode.
	 * @see hashCode
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SolutionGrid other = (SolutionGrid) obj;
		if (!(other.hashCode() == this.hashCode()))
			return false;
		return true;
	}
}
