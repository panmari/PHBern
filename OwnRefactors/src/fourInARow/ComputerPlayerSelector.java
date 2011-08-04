package fourInARow;

import java.awt.Color;
import java.util.LinkedList;
import java.util.List;

import ch.aplu.jgamegrid.*;

public class ComputerPlayerSelector extends GameGrid implements GGButtonListener{

	private List<ComputerPlayer> availableCP = new LinkedList<ComputerPlayer>();
	private GGRadioButtonGroup rbGroup;
	private FourInARowVsComputer gg;
	
	public ComputerPlayerSelector(FourInARowVsComputer gg, 
			ArrayManager am, int player) {
		super(15, 5, 30, null, false);
		this.gg = gg;
		setTitle("Select computer player");
		availableCP.add(new EasyBot(am, player));
		availableCP.add(new DBot(am, player));
		availableCP.add(new MMBot(am, player));
		createRadioButtons();
		setBgColor(Color.white);
		show();
	}
	
	private void createRadioButtons() {
		rbGroup = new GGRadioButtonGroup();
		int y = 0;
		for (ComputerPlayer cp: availableCP) {
			GGRadioButton rb = new GGRadioButton(cp.getNameAndDescription());
			addActor(rb, new Location(0, y));
			rbGroup.add(rb);
			y++;
		}
		GGButton okBtn = new GGButton("sprites/ok.gif", true);
		okBtn.addButtonListener(this);
		addActor(okBtn, new Location(nbHorzCells - 2, y));
		setNbVertCells(y + 2); //leaves black 
	}
	@Override
	public void buttonClicked(GGButton button) {
		int counter = 0;
		//this is ugly:
		for (GGRadioButton rb: rbGroup.getButtons()) {
			if (rb == rbGroup.getSelectedButton()) {
				gg.setComputerPlayer(availableCP.get(counter));
				hide();
			}
			counter++;
		}
	}
	@Override
	public void buttonPressed(GGButton button) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void buttonReleased(GGButton button) {
		// TODO Auto-generated method stub
		
	}

}
