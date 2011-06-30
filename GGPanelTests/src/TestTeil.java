import java.awt.Color;
import java.awt.Point;
import java.util.Random;

import ch.aplu.jgamegrid.*;


public class TestTeil extends GameGrid{
	
	private GGBackground bg;
	public static void main(String[] args) {
		new TestTeil();
	}
	
	public TestTeil() {
		super(20, 20, 20);
		show();
		bg = this.getBg();
		//bg.setPaintColor(Color.getHSBColor(0,0,0));
		makeSomeLines();
		testSomeGGPanel();
		makeSomeArcs();
	}

	private void testSomeGGPanel() {
		bg.setPaintColor(Color.red);
		for (int i = 20; i < 20*20; i+=20) {
			bg.drawCircle(new Point(i,i), i);
		}
	}

	private void makeRandomColor() {
		Random rnd = new Random();
		bg.setPaintColor(Color.getHSBColor(rnd.nextInt(255), 
				rnd.nextInt(255), rnd.nextInt(255)));
	}
	
	private void makeSomeArcs() {
		bg.setPaintColor(Color.yellow);
		for (int i = 20*20; i > 0; i-=20) {
			bg.drawArc(new Point(i,i), i, 
					300, 60);
		}
	}
	
	private void makeSomeLines() {
		bg.setPaintColor(Color.orange);
		for (int i = 0; i <= 20*20; i+=40)
			for (int j = 0; j <= 20*20; j+= 40) {
				bg.drawLine(new Point(i, j), new Point(0,0));
			}
	}
}
