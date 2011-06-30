import java.awt.Color;
import java.awt.Point;

import ch.aplu.jgamegrid.*;


public class clickDraw extends GameGrid implements GGMouseListener{

	public static void main(String[] args) {
		new clickDraw();
	}

	private GGBackground bg;
	
	public clickDraw() {
		super(20, 20, 20);
		addMouseListener(this, GGMouse.lPress);
		bg = getBg();
		bg.setPaintColor(Color.white);
		show();
	}

	@Override
	public boolean mouseEvent(GGMouse mouse) {
		bg.drawCircle(new Point(mouse.getX(), mouse.getY()), 10);
		refresh();
		return true;
	}

}
