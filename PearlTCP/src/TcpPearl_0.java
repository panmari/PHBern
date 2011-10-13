// TcpPearl_0.java

import ch.aplu.tcp.*;
import ch.aplu.jgamegrid.*;
import ch.aplu.util.Monitor;
import java.awt.Color;
import javax.swing.JOptionPane;

public class TcpPearl_0 extends GameGrid
  implements TcpNodeListener, GGMouseListener
{
  private final String myNodeName = "Luka";
  private TcpNode node;
  private final static int size = 6;
  
  public TcpPearl_0()
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
    
    node = new TcpNode();
    String sessionID = requestString("Enter unique number", "");
    node.addTcpNodeListener(this);
    setStatusText("Connecting to relay '" + node.getRelay() + "...");
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
    if (mouse.getEvent() == GGMouse.lPress)
    {
      
      
    }

    return true;
  }

  public void messageReceived(String sender, String text)
  {
    
 
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
      setTitle("It is you to play");
    }
  }

  public static void main(String[] args)
  {
    new TcpPearl_0();
  }
}
