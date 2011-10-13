// TcpPearl_1.java

import ch.aplu.tcp.*;
import ch.aplu.jgamegrid.*;
import ch.aplu.util.Monitor;
import java.awt.Color;
import javax.swing.JOptionPane;

public class TcpPearl_1 extends GameGrid
  implements TcpNodeListener, GGMouseListener
{
  private final String myNodeName = "Luka";
  private TcpNode node;
  private final static int size = 6;
  private boolean isMyMove = false;

  public TcpPearl_1()
  {
    super(size, size, 60, Color.red, false);
    setBgColor(new Color(80, 15, 247));
    setTitle("Pearl Game");
    addStatusBar(30);
    show();
    init();
  }

  public void init()
  {
    for (int k = 0; k < size; k++)
    {
      for (int i = 0; i < size; i++)
      {
        Actor pearl = new Actor("sprites/pearl.gif");
        addActor(pearl, new Location(i, k));
        addMouseListener(this, GGMouse.lPress);
      }
    }
    node = new TcpNode();
    String sessionID = requestString("Enter unique number", "");
    node.addTcpNodeListener(this);
    setStatusText("Connecting to relay '" + node.getRelay() + "'...");
    node.connect("aqw" + sessionID, myNodeName);
    Monitor.putSleep(4000);
    if (node.getNodeState() == TcpNodeState.CONNECTED)
      setStatusText("Connection established.");
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
      Location location = toLocationInGrid(mouse.getX(), mouse.getY());
      Actor actor = getOneActorAt(location);
      if (actor != null)
      {
        actor.removeSelf();
        node.sendMessage("" + location.x + location.y);
        setTitle("Wait for partner.");
        isMyMove = false;        
      }
      refresh();
    }
    return true;
  }

  public void messageReceived(String sender, String text)
  {
    System.out.println("Message received: " + text);
    int x = text.charAt(0) - 48; // We get ASCII code of number
    int y = text.charAt(1) - 48;
    Location loc = new Location(x, y);
    Actor actor = getOneActorAt(loc);
    actor.removeSelf();
    setTitle("It is you to play");
    isMyMove = true;
    refresh();
  }

  public void statusReceived(String text)
  {
    System.out.println("Status: " + text);
    if (text.contains("In session:--- (0)"))  // we are first player
    {
      setTitle("Connected. Wait for partner.");
    }
    if (text.contains("In session:--- (1)"))  // we are second player
    {
      isMyMove = true;  // Second player starts
      setTitle("It is you to play");
    }
  }

  public static void main(String[] args)
  {
    new TcpPearl_1();
  }
}