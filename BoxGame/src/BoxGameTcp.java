import java.awt.Color;
import java.awt.Point;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JOptionPane;

import ch.aplu.jgamegrid.Actor;
import ch.aplu.jgamegrid.GGExitListener;
import ch.aplu.jgamegrid.GGInputInt;
import ch.aplu.jgamegrid.GGMouse;
import ch.aplu.jgamegrid.GGMouseTouchListener;
import ch.aplu.jgamegrid.GameGrid;
import ch.aplu.jgamegrid.Location;
import ch.aplu.tcp.TcpNode;
import ch.aplu.tcp.TcpNodeListener;
import ch.aplu.tcp.TcpNodeState;
import ch.aplu.tcp.TcpTools;


public class BoxGameTcp extends GameGrid implements GGMouseTouchListener, TcpNodeListener, GGExitListener{


	/**
	 * If set to true, an arbitrary grid size can be chosen at the start of the game.
	 */
	private static final boolean customizableGrid = false;
	/**
	 * For every location of the valid grid, the strokes surrounding it are saved in this hashtable.
	 * It is then used for look up after a new stroke is drawn.
	 */
	Hashtable<Location, LinkedList<Stroke>> BoxMap = new Hashtable<Location, LinkedList<Stroke>>();
	private Player currentPlayer;
	private Player[] players = new Player[2];
	private static int playerCounter = 0;
	private String sessionID = "$BoxGame";
	private final String nickname = "plr";
	private TcpNode tcpNode = new TcpNode();
	private boolean isMyMove = false;
	interface Command
	{
		char change = 'c'; // change player
	    char move = 'm';   // move pearl
	    char over = 'o';   // game over
	    char start = 's';  // start game
	    char terminate = 't'; // terminate game
	}
	  
	public BoxGameTcp(int height, int width) {
		super(width + 2, height + 2, 75, Color.WHITE, false);
		getBg().clear(Color.WHITE);
		players[0] = new Player(Color.BLUE, "Blue");
		players[1] = new Player(Color.RED, "Red");
		currentPlayer = players[0]; //blue begins;
		for (int x = 1; x < getNbHorzCells(); x++) {
			for (int y = 1; y < getNbVertCells(); y++) {
				Location loc = new Location(x, y);
				BoxMap.put(loc, new LinkedList<Stroke>());
				for (StrokeDirection d: StrokeDirection.values()) {
					//prevent loop from drawing unnecessary strokes
					if (y == getNbVertCells() - 1 && d == StrokeDirection.VERTICAL
							|| x == getNbHorzCells() - 1 && d == StrokeDirection.HORIZONTAL)
						continue;
					Stroke s = new Stroke(this, d);
					addActorNoRefresh(s, new Location(x,y));
					s.addMouseTouchListener(this, GGMouse.lClick | GGMouse.enter | GGMouse.leave);
					for (Location l: s.getPossibleFillLocations())
						BoxMap.get(l).add(s);
				}
			}
		}
		addStatusBar(20);
		setTitle("The box game -- www.java-online.ch"); 
		tcpNode.addTcpNodeListener(this);
		show();
		connect();
		//setStatusText("Click on an edge to start");
	}
		
	public static void main(String[] args) {
		int height = 3;
		int width = 3;
		if (customizableGrid) {
			height = new GGInputInt("Height", "Choose the height of the grid.").show();
			width = new GGInputInt("Width", "Choose the width of the grid.").show();
		}
		
		new BoxGameTcp(height, width);
	}
	
	
	@Override
	public void mouseTouched(Actor actor, GGMouse mouse, Point spot) {
		Stroke s = (Stroke) actor;
		if (s.isDrawn() || !isMyMove) 
			return;
		switch (mouse.getEvent()) {
			case GGMouse.enter:
				s.show(1 + currentPlayer.id); //important, that not s.draw is called!
				break;
			case GGMouse.leave:
				s.show(0);
				break;
			case GGMouse.lClick:
				s.draw(currentPlayer.id);
				tcpNode.sendMessage("" + Command.move + s);
				boolean nextPlayer = true;
				for (Location loc: s.getPossibleFillLocations()) {
					if (players[currentPlayer.id].tryToFillBoxes(loc))
						nextPlayer = false;
				}
				if (nextPlayer)
					currentPlayer = currentPlayer.nextPlayer();
				break;
		}
		refresh();
	}

	private void updateStatusText(String additionalMessage) {
		String msg = players[0].getLabelledScore() + " vs " + players[1].getLabelledScore();
		if (Stroke.allDrawn())
			msg = "Final Score -- " + msg;
		setStatusText(additionalMessage + " " + msg);
	}

	private boolean outOfValidGrid(Location loc) {
		return loc.y >= getNbVertCells() - 1 || loc.x >= getNbHorzCells() -1 
				|| loc.y < 1 || loc.x < 1;
	}
	
	class Player {
		private int id;
		private Color color;
		private int score;
		private String name;
		
		public Player(Color color, String name) {
			this.name = name;
			this.id = playerCounter++;
			this.color = color;
			this.score = 0;
		}
		
		public String toString() {
			return name;
		}
		
		public Player nextPlayer() {
			return players[(id + 1) % playerCounter];
		}
		public String getLabelledScore() {
			return name + ": " + score;
		}
		
		/**
		 * Player tries to fill out the given location with own color, but first checks if it's surrounded
		 * by strokes. 
		 * @param loc
		 * @return true if the given location was filled
		 */
		private boolean tryToFillBoxes(Location loc) {
			if (outOfValidGrid(loc))
				return false;
			for (Stroke s: BoxMap.get(loc))
				if (!s.isDrawn())
					return false;
			getPanel().fillCell(loc, color);
			score++;
			return true;
		}
	}
	
	private void connect()
	  {
		setStatusText("Connecting...");
	    String id = requestEntry("Enter unique game room ID (more than 3 characters)");  
	    sessionID = sessionID + id;
	    tcpNode.connect(sessionID, nickname);
	  }
	
	private String requestEntry(String prompt)
	  {
	    String entry = "";
	    while (entry.length() < 3)
	    {
	      entry = JOptionPane.showInputDialog(null, prompt, "");
	      if (entry == null)
	        System.exit(0);
	    }
	    return entry.trim();
	  }
	
	  public void messageReceived(String sender, String text)
	  {
	    char command = text.charAt(0);
	    switch (command)
	    {
	      case Command.start:
	        if (isMyMove) { //not really needed, this player is always second
	          updateStatusText("Game started.");
	        } else {
	          setStatusText("Game started. Wait for the partner's move.");
	        }
	        break;
	      case Command.terminate:
	        setStatusText("Partner exited game room. Terminating now...");
	        TcpTools.delay(4000);
	        System.exit(0);
	        break;
	      case Command.move:
	    	//some regex magic for finding location:
	    	Pattern locRegex = Pattern.compile("\\((\\d*),(\\d*)\\) (.*)");
	    	Matcher locFinder = locRegex.matcher(text);
	        int x = Integer.parseInt(locFinder.group(1));
	        int y = Integer.parseInt(locFinder.group(2));
	        Location loc = new Location(x, y);
	        StrokeDirection dir;
	        if (locFinder.group(3).equals(StrokeDirection.HORIZONTAL.toString()))
	        	dir	 = StrokeDirection.HORIZONTAL;
	        else dir = StrokeDirection.VERTICAL;
	        getOneActorAt(loc).removeSelf();
	        updateStatusText("Wait for partner's move.");
	        break;
	      case Command.over:    	  
	        setStatusText("You won. Press 'New Game' to play again.");
	        break;
	      case Command.change:
	        isMyMove = true;
	        updateStatusText("Your move.");
	        break;
	    }
	    refresh();
	  }

	@Override
	public void nodeStateChanged(TcpNodeState arg0) {
		// TODO Auto-generated method stub
		
	}

	 public void statusReceived(String text)
	  {
	    if (text.contains("In session:--- (0)"))  // we are first player
	    {
	      setStatusText("Connected. Wait for partner.");
	    }
	    else if (text.contains("In session:--- (1)")) // we are second player
	    {
	      setStatusText("Connected. Make first move.");
	      isMyMove = true;  // Second player starts
	      tcpNode.sendMessage("" + Command.start);
	    }
	    else if (text.contains("In session:--- ")) // we are next player
	    {
	      setStatusText("Game in progress. Terminating now...");
	      TcpTools.delay(4000);
	      System.exit(0);
	    }
	  }
	
	 public boolean notifyExit()
	  {
	    tcpNode.sendMessage("" + Command.terminate);
	    return true;
	  }
}
