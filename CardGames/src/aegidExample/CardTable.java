package aegidExample;

// CardTable.java

import ch.aplu.jcardgame.*;
import ch.aplu.jgamegrid.*;
import ch.aplu.tcp.*;

public class CardTable extends CardGame
{
  public static enum Suit
  {
    SPADES, HEARTS, DIAMONDS, CLUBS
  }

  public static enum Rank
  {
    ACE, KING, QUEEN
  }
  //
  protected static Deck deck = new Deck(Suit.values(), Rank.values(), "cover");
  private final int nbPlayers = 2;
  private final int nbStartCards = 5;
  private final int handWidth = 300;
  private final Location[] handLocations =
  {
    new Location(300, 525),
    new Location(300, 75),
  };
  private final Location lineLocation = new Location(300, 300);
  private Hand[] hands = new Hand[nbPlayers];
  private Hand line = new Hand(deck);
  private int currentPlayerIndex;
  private String[] playerNames;
  private TcpAgent agent;

  public CardTable(TcpAgent agent, String[] playerNames, int currentPlayerIndex)
  {
    super(600, 600, 30);
    this.agent = agent;
    this.playerNames = new String[nbPlayers];
    for (int i = 0; i < nbPlayers; i++)
      this.playerNames[i] = playerNames[i];
    this.currentPlayerIndex = currentPlayerIndex;
    setTitle("Current player's name: " + playerNames[currentPlayerIndex]);
  }

  protected void initHands(int[] cardNumbers)
  {
    for (int i = 0; i < nbPlayers; i++)
    {
      hands[i] = new Hand(deck);
      for (int k = 0; k < nbStartCards; k++)
        hands[i].insert(cardNumbers[i * nbStartCards + k], false);
    }
    for (int i = nbStartCards * nbPlayers; i < cardNumbers.length; i++)
      line.insert(cardNumbers[i], true);
    line.setView(this, new RowLayout(lineLocation, 400));
    line.draw();
    
    hands[currentPlayerIndex].sort(Hand.SortType.SUITPRIORITY, false);
    hands[currentPlayerIndex].addCardListener(new CardAdapter()
    {
      public void leftDoubleClicked(Card card)
      {
        hands[currentPlayerIndex].setTouchEnabled(false);
        agent.sendCommand("", CardPlayer.Command.CARD_TO_LINE,
          currentPlayerIndex, card.getCardNumber());
        card.transfer(line, true);
        agent.sendCommand("", CardPlayer.Command.READY_TO_PLAY);
      }
    });

    RowLayout[] layouts = new RowLayout[nbPlayers];
    for (int i = 0; i < nbPlayers; i++)
    {
      int k = (currentPlayerIndex + i) % nbPlayers;
      layouts[k] = new RowLayout(handLocations[i], handWidth);
      layouts[k].setRotationAngle(180 * i);
      if (k != currentPlayerIndex)
        hands[k].setVerso(true);
      hands[k].setView(this, layouts[k]);
      hands[k].setTargetArea(new TargetArea(lineLocation));
      hands[k].draw();
    }
    agent.sendCommand("", CardPlayer.Command.READY_TO_PLAY);
  }

  protected void moveCardToLine(int playerIndex, int cardNumber)
  {
    Card card = hands[playerIndex].getCard(cardNumber);
    card.setVerso(false);
    hands[playerIndex].transfer(card, line, true);
    agent.sendCommand("", CardPlayer.Command.READY_TO_PLAY);
  }

  protected void stopGame(String client)
  {
    setStatusText(client + " disconnected. Game stopped.");
    setMouseEnabled(false);
    doPause();
  }

  protected void setMyTurn()
  {
    setStatusText("It's your turn. Double click on one of your cards to play it.");
    hands[currentPlayerIndex].setTouchEnabled(true);
  }

  protected void setOtherTurn()
  {
    setStatusText("Wait for you turn.");
  }
}
