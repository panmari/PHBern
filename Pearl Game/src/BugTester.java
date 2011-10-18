// PearlG2.java

import ch.aplu.jgamegrid.*;
import java.awt.*;
	
public class BugTester extends GameGrid implements GGMouseListener,
		GGButtonListener {
	private int nbInit = 0;
	private GGBackground bg;
	private GGButton okBtn = new GGButton("sprites/ok.gif", true);
	private GGButton newBtn = new GGButton("sprites/new.gif", true);

	public BugTester() {
		super(8, 6, 60, false);
		setBgColor(new Color(80, 15, 247));
		bg = getBg();
		addMouseListener(this, GGMouse.lPress);
		addActor(okBtn, new Location(6, 4));
		okBtn.addButtonListener(this);
		addActor(newBtn, new Location(6, 4));
		newBtn.addButtonListener(this);
		bg.setPaintColor(Color.red);
		bg.setFont(new Font("Arial", Font.BOLD, 32));
		init();
		show();
		addActor(new Pearl(), getRandomEmptyLocation());
	}

	public void init() {
		okBtn.show();
		newBtn.hide();
		refresh();
		setTitle("init was called " + ++nbInit + " times");
	}

	public boolean mouseEvent(GGMouse mouse) {
		Location loc = toLocationInGrid(mouse.getX(), mouse.getY());
		Actor pearlAtClick = getOneActorAt(new Location(loc), Pearl.class);
		if (pearlAtClick != null) {
			pearlAtClick.removeSelf();
		}
		if (getActors(Pearl.class).size() == 0)
			gameOver("Game Over!");
		refresh();
		return true;
	}

	public void gameOver(String msg) {
		setTitle("Press 'new Game' to play again.");
		bg.drawText(msg, new Point(toPoint(new Location(2, 5))));
		okBtn.hide();
		newBtn.show();
		refresh();
	}
	
	public void buttonClicked(GGButton button) {
		
		if (getActors(Pearl.class).size() == 0)
			init();
		else addActor(new Pearl(), getRandomEmptyLocation());
	}

	public void buttonPressed(GGButton button) {
	}

	public void buttonReleased(GGButton button) {
	}

	public void reset() {
		
	}

	public static void main(String[] args) {
		new BugTester();
	}

}