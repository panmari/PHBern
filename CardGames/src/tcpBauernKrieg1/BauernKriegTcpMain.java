package tcpBauernKrieg1;

// SchwarzPeter.java
// Cycle 7: Add blinking player's name
// Solved

import javax.swing.JOptionPane;

public class BauernKriegTcpMain
{
  protected static final boolean debug = true;

  private static final String sessionPrefix = "hyakujounosensou++";
  protected static final String serverName = "CardServer";
  protected static String sessionID;
  protected static String playerName;
  protected static final int nbPlayers = 2;

  public static void main(String[] args)
  {
    String roomName = debug ? "123"
      : requestEntry("Enter a unique room name (ASCII 3..15 chars):");
    sessionID = sessionPrefix + roomName;
    playerName = debug ? "max"
      : requestEntry("Enter your name (ASCII 3..15 chars):");
    new Player();
  }

  private static String requestEntry(String prompt)
  {
    String entry = "";
    while (entry.length() < 3 || entry.length() > 15)
    {
      entry = JOptionPane.showInputDialog(null, prompt, "");
      if (entry == null)
        System.exit(0);
    }
    return entry.trim();
  }
}
