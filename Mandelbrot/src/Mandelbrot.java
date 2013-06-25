import java.awt.Color;
import java.awt.Point;

import ch.aplu.jgamegrid.GGMouse;
import ch.aplu.jgamegrid.GGMouseListener;
import ch.aplu.jgamegrid.GGPanel;
import ch.aplu.jgamegrid.GameGrid;
import ch.aplu.jgamegrid.Location;


public class Mandelbrot extends GameGrid implements GGMouseListener{
	
	private final double RADIUS_SQUARE = Math.pow(2, 2);
	private int maxIterations = 50;
	GGPanel p;
	private Point start, end;
	public Mandelbrot() {
		super(600, 600, 1, false);
		show();
		draw(-2.2, 1.0, -1.2, 1.2);
		addMouseListener(this, GGMouse.lPress | GGMouse.lDrag | GGMouse.lRelease);
	}
	
	private void draw(double xmin, double xmax, double ymin, double ymax) {
		// might need some reordering, depending on how selection was made
		p = getPanel(Math.min(xmin, xmax), Math.max(xmin, xmax), 
						Math.min(ymin, ymax), Math.max(ymin, ymax));
		setTitle(String.format("Mandelbrot set -- xmin: %.4f xmax: %.4f ymin: %.4f ymax: %.4f", 
				p.getXmin(), p.getXmax(), p.getYmin(), p.getYmax()));
		
		p.setPaintMode();
		for (int xPixel = 0; xPixel < getNbHorzPix(); xPixel++) {
			for(int yPixel = 0; yPixel < getNbVertPix(); yPixel++) {
				double x_p = p.toUserX(xPixel);
				double y_p = p.toUserY(yPixel);
				int iterCount = getIterCount(x_p, y_p);
				Color drawColor = getColorForIterCount(iterCount);
				p.setPaintColor(drawColor);
				p.drawPoint(new Point(xPixel, yPixel));
			}
			if (xPixel % 50 == 0) // refresh every 50 columns
				refresh();
		}
	}

	private Color getColorForIterCount(int iterCount) {
		if (iterCount == maxIterations)
			return Color.black;
		if (iterCount > maxIterations*0.8)
			return Color.yellow;
		if (iterCount > maxIterations*0.6)
			return Color.blue;
		if (iterCount > maxIterations*0.4)
			return Color.red;
		if (iterCount > maxIterations*0.2)
			return Color.orange;
		return Color.white;
	}



	private int getIterCount(double x_p, double y_p) {
		double x = 0, y = 0;
		int iter = 0;
		while (isInCircle(x, y) && iter < maxIterations) {
			double xt = x*x - y*y + x_p;
			double yt = 2*x * y + y_p;
			x = xt;
			y = yt;
			iter++;
		}
		return iter;
	}



	private boolean isInCircle(double x, double y) {
		return Math.pow(x, 2) + Math.pow(y, 2) < RADIUS_SQUARE;
	}

	public static void main(String[] args) {
		new Mandelbrot();
	}



	@Override
	public boolean mouseEvent(GGMouse mouse) {
		Location l = toLocation(mouse.getX(), mouse.getY());
		switch (mouse.getEvent()) {
		case GGMouse.lPress:
			start = new Point(l.x, l.y);
			end = new Point(l.x, l.y);
			break;
		case GGMouse.lDrag:
			Point current = new Point(l.x, l.y);
			getBg().setXORMode(Color.white);
			if (!current.equals(end)) {
				getBg().drawRectangle(start, end);
				end = current;
				getBg().drawRectangle(start, end);
				refresh();
			}
			break;
		case GGMouse.lRelease:
			draw(p.toUserX(start.x), p.toUserX(end.x), p.toUserY(start.y), p.toUserY(end.y));
			break;
		}
		return true;
	}

}
