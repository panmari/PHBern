// TcpPearl_3.java

import ch.aplu.tcp.*;
import ch.aplu.jgamegrid.*;
import ch.aplu.util.Monitor;
import java.awt.Color;
import javax.swing.JOptionPane;

public class TcpPearl_3 extends GameGrid
  implements TcpNodeListener, GGMouseListener
{
  private final String myNodeName = "Luka";
  private TcpNode node;
  private final static int size = 6;
  private boolean isMyMove = false;

  public TcpPearl_3()
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
      }
      nb--;
    }
    addMouseListener(this, GGMouse.lPress | GGMouse.rPress);
    node = new TcpNode();
    String sessionID = requestString("Enter unique number", "");
    node.addTcpNodeListener(this);
    setStatusText("Connecting to relay '" + node.getRelay() + "'...");
    node.connect(sessionID, myNodeName);
    Monitor.putSleep(10000);
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
      //  showToast("Connection establised.");
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
      }
      refresh();
    }
    if (mouse.getEvent() == GGMouse.rPress)
    {
      node.sendMessage("88");
      isMyMove = false;
      setStatusText("Wait.");
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
    new TcpPearl_3();
  }
}
