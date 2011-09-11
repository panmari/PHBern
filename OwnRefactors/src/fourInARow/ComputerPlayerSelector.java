package fourInARow;

import java.awt.Color;
import java.util.LinkedList;
import java.util.List;

import ch.aplu.jgamegrid.*;

public class ComputerPlayerSelector implements GGButtonListener{

	private GameGrid dlg;
	private List<ComputerPlayer> availableCP = new LinkedList<ComputerPlayer>();
	private GGRadioButtonGroup rbGroup;
	private FourInARowVsComputer gg;
	
	public ComputerPlayerSelector(FourInARowVsComputer gg, 
			ArrayManager am, int player) {
		this.gg = gg;
		createComputerPlayers(am, player);
		createRadioButtons();
	}
	
	  private void showDialog()
	  {
	    dlg = new GameGrid(15, availableCP.size() + 2, 30, null, false);
	    dlg.setTitle("Select computer player");
	    dlg.setBgColor(Color.white);
	    dlg.show();
	  }

	private void createComputerPlayers(ArrayManager am, int player) {
		availableCP.add(new EasyBot(am, player));
		availableCP.add(new DBot(am, player));
		availableCP.add(new MMBot(am, player));
	}
	
	private void createRadioButtons() {
		rbGroup = new GGRadioButtonGroup();
		int y = 0;
		for (ComputerPlayer cp: availableCP) {
			GGRadioButton rb = new GGRadioButton(cp.getNameAndDescription());
			dlg.addActor(rb, new Location(0, y));
			rbGroup.add(rb);
			y++;
		}
		GGButton okBtn = new GGButton("sprites/ok.gif", true);
		okBtn.addButtonListener(this);
		dlg.addActor(okBtn, new Location(dlg.nbHorzCells - 2, y));
	}
	@Override
	public void buttonClicked(GGButton button) {
		int counter = 0;
		//this is ugly:
		for (GGRadioButton rb: rbGroup.getButtons()) {
			if (rb == rbGroup.getSelectedButton()) {
				gg.setComputerPlayer(availableCP.get(counter));
				gg.show();
				dlg.hide();
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
