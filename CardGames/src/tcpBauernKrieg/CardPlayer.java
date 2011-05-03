package tcpBauernKrieg;
// CardPlayer.java

import ch.aplu.tcp.*;
import ch.aplu.util.*;
import javax.swing.JOptionPane;

import java.awt.Color;
import java.util.*;

public class CardPlayer
{
  private final static boolean debug = true;
  private final String serverName = "CardServer BauernKrieg";
  private String sessionID = "Cardsadsdd**()";
  private TcpAgent agent;
  private CardServer cardServer;
  private CardTable cardTable;
  private String[] playerNames = null;
  private String currentPlayerName;
  private int currentPlayerIndex;
  private final int nbPlayers = 2;

  // Protocol tags
  public static interface Command
  {
    int REPORT_NAMES = 0;
    int READY_FOR_TALON = 1;
    int DISCONNECT = 2;
    int TALON_DATA = 3;
    int READY_TO_PLAY = 4;
    int MY_TURN = 5;
    int OTHER_TURN = 6;
    int CARD_TO_BID = 7;
	int CARDS_TO_WINNER = 8;
  }

  private class MyTcpAgentAdapter extends TcpAgentAdapter
  {
    public void dataReceived(String source, int[] data)
    {
      switch (data[0])
      {
        case Command.REPORT_NAMES:
          playerNames = TcpTools.split(TcpTools.intAryToString(data, 1), ",");
          break;

        case Command.DISCONNECT:
          String client = TcpTools.intAryToString(data, 1);
          agent.disconnect();
          cardServer.disconnect();
          if (cardTable != null)
            cardTable.stopGame(client);
          break;

        case Command.TALON_DATA:
          int size = data.length - 1;
          int[] cardNumbers = new int[size];
          System.arraycopy(data, 1, cardNumbers, 0, size);
          cardTable.initHands(cardNumbers);
          break;

        case Command.MY_TURN:
          cardTable.setMyTurn();
          break;

        case Command.OTHER_TURN:
          cardTable.setOtherTurn();
          break;

        case Command.CARD_TO_BID:
          cardTable.moveCardToBid(data[1], data[2]);
          break;
          
        case Command.CARDS_TO_WINNER:
        	cardTable.transferToWinner();
        	break;
      }
    }

    public void notifyBridgeConnection(boolean connected)
    {
      if (!connected && cardTable != null)
      {
        cardTable.
          setStatusText("Client with game server disconnected. Game stopped.");
        agent.disconnect();
      }
    }
  }

  public CardPlayer()
  {
   String gameRoom = debug ? "123"
      : requestEntry("Enter a unique room name (ASCII 3..15 chars):");
    sessionID = sessionID + gameRoom;
    agent = new TcpAgent(sessionID, serverName);
    String requestedName = debug ? "max"
      : requestEntry("Enter your name (ASCII 3..15 chars):");
    agent.addTcpAgentListener(new MyTcpAgentAdapter());
    connect(gameRoom, requestedName);
  }

  private void connect(String gameRoom, String requestedName)
  {
    ModelessOptionPane mop = new ModelessOptionPane("Trying to connect to relay...");
    cardServer = new CardServer(sessionID, gameRoom, serverName);
    ArrayList<String> connectList = agent.connectToRelay(requestedName, 6000);
    if (connectList.isEmpty())
    {
      mop.setText("Connection to relay failed. Terminating now...");
      CardTable.delay(3000);
      System.exit(0);
    }

    int nb = connectList.size();
    currentPlayerName = connectList.get(0);
    if (nb > nbPlayers)
    {
      mop.setText("Game room full. Terminating now...");
      CardTable.delay(3000);
      System.exit(0);
    }

    if (nb < nbPlayers)
      mop.setText("Connection established. Name: " + currentPlayerName
        + "\nCurrently " + nb + " player(s) in game room."
        + "\nWaiting for " + (nbPlayers - nb) + " more player(s)...");

    while (playerNames == null)  // Wait until player names are reported
      TcpTools.delay(10);

    for (int i = 0; i < nbPlayers; i++)
    {
      if (playerNames[i].equals(currentPlayerName))
      {
        currentPlayerIndex = i;
        break;
      }
    }
    mop.setVisible(false);
    cardTable = new CardTable(agent, playerNames, currentPlayerIndex);
    agent.sendCommand("", CardPlayer.Command.READY_FOR_TALON);
  }

  private String requestEntry(String prompt)
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

  public static void main(String[] args)
  {
    new CardPlayer();
  }
}
