// TcpPearl_5.java

import ch.aplu.tcp.*;
import ch.aplu.jgamegrid.*;
import ch.aplu.util.Monitor;
import java.awt.*;
import javax.swing.JOptionPane;

public class TcpPearl_5 extends GameGrid
  implements TcpNodeListener, GGMouseListener
{

  private final String myNodeName = "Luka";
  private TcpNode node;
  private final static int size = 6;
  private boolean isMyMove = false;
  private int activeRow;
  private int nbPearl = 0;
  private int nbTakenPearl;

  public TcpPearl_5()
  {
    super(size, size, 60, false);
    setBgColor(new Color(80, 15, 247));
    setTitle("Remove any number of pearls from same row and right click if finish");
    addStatusBar(30);
    show();
    init();
  }

  public void init()
  {
    int nb = size;
    for (int k = 0; k < size; k++)
    {
      for (int i = 0; i < nb; i++)
      {
        Actor pearl = new Actor("sprites/pearl.gif");
        addActor(pearl, new Location(i, k));
        addMouseListener(this, GGMouse.lPress | GGMouse.rPress);
        nbPearl++;
      }
      nb--;
    }
    System.out.println("nbPearl = " + nbPearl);
    activeRow = -1;
    nbTakenPearl = 0;
    refresh();

    node = new TcpNode();
    String sessionID = requestString("Enter unique number", "");
    node.addTcpNodeListener(this);
    setStatusText("Connecting to relay '" + node.getRelay() + "'...");
    node.connect("awq" + sessionID, myNodeName);
    Monitor.putSleep(4000);
    if (node.getNodeState() == TcpNodeState.CONNECTED)
    {
      setStatusText("Connection established.");
    }
    else
      setStatusText("Connection failed");
  }

  private String requestString(String prompt, String init)
  {
    String entry = JOptionPane.showInputDialog(null, prompt, init);
    if (entry == null)
      System.exit(0);
    return entry.trim();
  }

  public void nodeStateChanged(TcpNodeState state)
  {
    if (state == TcpNodeState.CONNECTED)
      Monitor.wakeUp();
    if (state == TcpNodeState.DISCONNECTED)
      setStatusText("Connection broken.");
  }

  public boolean mouseEvent(GGMouse mouse)
  {
    if (!isMyMove)
      return true;

    if (mouse.getEvent() == GGMouse.lPress)
    {
      Location loc = toLocationInGrid(mouse.getX(), mouse.getY());
      int x = loc.x;
      int y = loc.y;
      if (activeRow != -1 && activeRow != y)
        setStatusText("You mus remove pearls from the same row");
      else
      {
        Actor actor = getOneActorAt(loc);
        if (actor != null)
        {
          actor.removeSelf();
          nbPearl--;
          node.sendMessage("" + x + y);
          activeRow = y;
          nbTakenPearl++;
          System.out.println("nbPearl " + nbPearl);
          if (nbPearl == 0)
          {  
            setStatusText("You lost!");  
            isMyMove = false;
          }  
        }
      }
      refresh();
    }
    if (mouse.getEvent() == GGMouse.rPress)
    {
      if (nbTakenPearl == 0)
        setStatusText("You must remove at least 1 pearl.");
      else
      {
        node.sendMessage("88");
        isMyMove = false;
        setStatusText("Wait.");
        activeRow = -1;
        nbTakenPearl = 0;
      }
    }
    return true;
  }

  public void messageReceived(String sender, String text)
  {
    System.out.println("Message received: " + text);
    int x = text.charAt(0) - 48; // We get ASCII code of number
    int y = text.charAt(1) - 48;
    if (x == 8)
    {
      isMyMove = true;
      setStatusText("It's your turn");
    }
    else
    {
      Location loc = new Location(x, y);
      Actor actor = getOneActorAt(loc);
      actor.removeSelf();
      nbPearl--;
      if (nbPearl == 0)
        setStatusText("You won!");  
      refresh();
    }
  }

  public void statusReceived(String text)
  {
    System.out.println("Status: " + text);
    if (text.contains("(0)"))
      isMyMove = true;
    if (text.contains("(1)"))
      setStatusText("Partner connected" + (isMyMove ? " Play" : " Wait"));
  }

  public static void main(String[] args)
  {
    new TcpPearl_5();
  }
}
