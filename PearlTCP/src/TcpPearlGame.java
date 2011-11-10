// TcpPearlGame.java
/**
 * Version on Clab2 -> Download source
 */
import ch.aplu.jgamegrid.*;
import ch.aplu.tcp.*;
import javax.swing.JOptionPane;
import java.awt.*;

public class TcpPearlGame extends GameGrid
  implements GGMouseTouchListener, GGButtonListener,
  TcpNodeListener, GGExitListener
{
  interface Command
  {
    char change = 'c'; // change player
    char move = 'm';   // move pearl
    char over = 'o';   // game over
    char start = 's';  // start game
    char terminate = 't'; // terminate game
  }

  private String sessionID = "PearlGame: &41*()";
  private final String nickname = "tic";
  private TcpNode tcpNode = new TcpNode();
  private boolean isMyMove;
  private int nbPearl = 0;
  private int nbTakenPearl = 0;
  private GGButton okBtn = new GGButton("sprites/ok.gif", true);
  private GGButton newBtn = new GGButton("sprites/new.gif", true);
  private int activeRow;
  private int nbRows = 4;
  private final String moveInfo =
    "Click to remove any number of pearls from same row and press OK.";

  public TcpPearlGame()
  {
    super(8, 6, 60, false);
    setBgColor(new Color(80, 15, 247));
    setTitle("Pearl Game");
    addActor(okBtn, new Location(6, 4));
    okBtn.addButtonListener(this);
    addActor(newBtn, new Location(6, 4));
    newBtn.addButtonListener(this);
    addStatusBar(30);
    tcpNode.addTcpNodeListener(this);
    init();
    okBtn.setEnabled(false);
    show();
    connect();
    addExitListener(this);
  }

  private void init()
  {
    okBtn.show();
    newBtn.hide();
    int nb = 6;
    for (int k = 0; k < nbRows; k++)
    {
      nbPearl += nb;
      for (int i = 0; i < nb; i++)
      {
        Actor pearl = new Actor("sprites/pearl.gif");
        addActor(pearl, new Location(1 + i, 1 + k));
        pearl.addMouseTouchListener(this, GGMouse.lPress);
      }
      nb--;
    }
    activeRow = 0;
    refresh();
  }

  public void mouseTouched(Actor actor, GGMouse mouse, Point pix)
  {
    if (!isMyMove)
      return;
    int x = actor.getX();
    int y = actor.getY();
    if (activeRow != 0 && activeRow != y)
      setStatusText("You must remove pearls from the same row.");
    else
    {
      activeRow = y;
      actor.removeSelf();
      tcpNode.sendMessage("" + Command.move + x + y);
      nbPearl--;
      setStatusText(nbPearl + " pearls remaining. " + moveInfo);
      nbTakenPearl++;
      if (nbPearl == 0)
      {
        setStatusText("You lost. Press 'New Game' to play again.");
        isMyMove = false;
        tcpNode.sendMessage("" + Command.over);
        okBtn.hide();
        newBtn.show();
      }
      refresh();
    }
  }

  public void buttonClicked(GGButton button)
  {
    if (button == newBtn)
    {
      init();
      if (isMyMove) {
        setStatusText("Game started. " + moveInfo);
        okBtn.setEnabled(true);
      } else {
        setStatusText("Game started. Wait for the partner's move.");
        okBtn.setEnabled(false);
      }
      tcpNode.sendMessage("" + Command.start);
    }
    else
    {
      if (nbTakenPearl == 0)
        setStatusText("You must remove at least 1 pearl.");
      else
      {
        isMyMove = false;
        tcpNode.sendMessage("" + Command.change);
        setStatusText(nbPearl + " pearls remaining. Wait for the partner's move.");  
        okBtn.setEnabled(false);
      }
    }
  }

  public void buttonPressed(GGButton button)
  {
  }

  public void buttonReleased(GGButton button)
  {
  }

  private void connect()
  {
    String id = requestEntry("Enter unique game room ID (more than 3 characters)");  
    sessionID = sessionID + id;
    tcpNode.connect(sessionID, nickname);
  }

  public void messageReceived(String sender, String text)
  {
    char command = text.charAt(0);
    switch (command)
    {
      case Command.start:
        init();
        if (isMyMove) {
          setStatusText("Game started. " + moveInfo);
          okBtn.setEnabled(true);
        } else {
          setStatusText("Game started. Wait for the partner's move.");
          okBtn.setEnabled(false);
        }
        break;
      case Command.terminate:
        setStatusText("Partner exited game room. Terminating now...");
        TcpTools.delay(4000);
        System.exit(0);
        break;
      case Command.move:
        okBtn.setEnabled(false);
        int x = text.charAt(1) - 48; // We get ASCII code of number
        int y = text.charAt(2) - 48;
        Location loc = new Location(x, y);
        getOneActorAt(loc).removeSelf();
        nbPearl--;
        setStatusText(nbPearl + " pearls remaining. Wait for the partner's move.");
        break;
      case Command.over:
        okBtn.hide();
        newBtn.show();
        setStatusText("You won. Press 'New Game' to play again.");
        isMyMove = true;
        break;
      case Command.change:
        isMyMove = true;
        okBtn.setEnabled(true);
        setStatusText(nbPearl + " pearls remaining. " + moveInfo);
        nbTakenPearl = 0;
        activeRow = 0;
        break;
    }
    refresh();
  }

  public void nodeStateChanged(TcpNodeState state)
  {
  }

  public void statusReceived(String text)
  {
    if (text.contains("In session:--- (0)"))  // we are first player
    {
      setStatusText("Connected. Wait for partner.");
    }
    else if (text.contains("In session:--- (1)")) // we are second player
    {
      setStatusText(moveInfo);
      okBtn.show();
      isMyMove = true;  // Second player starts
      okBtn.setEnabled(true);
    }
    else if (text.contains("In session:--- ")) // we are next player
    {
      setStatusText("Game in progress. Terminating now...");
      TcpTools.delay(4000);
      System.exit(0);
    }
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

  public boolean notifyExit()
  {
    tcpNode.sendMessage("" + Command.terminate);
    return true;
  }

  public static void main(String[] args)
  {
    new TcpPearlGame();
  }
}
