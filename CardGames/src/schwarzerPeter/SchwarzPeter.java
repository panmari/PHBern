package schwarzerPeter;

// SchwarzPeter.java
// Cycle 7: Add blinking player's name
// Solved

import javax.swing.JOptionPane;

public class SchwarzPeter
{
  protected static final boolean debug = true;

  private static final String sessionPrefix = "BackPeter &4-;>**&/**??==";
  protected static final String serverName = "CardServer";
  protected static String sessionID;
  protected static String playerName;
  protected static int nbPlayers;

  public static void main(String[] args)
  {
    nbPlayers = debug ? 4
      : requestNumber("Enter number of players (2..4):");
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

  private static int requestNumber(String prompt)
  {
    while (true)
    {
      String entry = JOptionPane.showInputDialog(null, prompt, "");
      if (entry == null)
        System.exit(0);
      try
      {
        int nb = Integer.parseInt(entry);
        if (nb >= 2 && nb <= 4)
          return nb;
      }
      catch (NumberFormatException ex)
      {
      }
    }
  }
}
