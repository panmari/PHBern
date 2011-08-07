package tcpBauernKrieg1;

// CardTable.java
// Cycle 7: Add blinking player's name
// TODO: Put code for displaying player's names
// Solved

import ch.aplu.jcardgame.*;
import ch.aplu.jgamegrid.*;
import ch.aplu.tcp.*;
import java.util.*;
import java.awt.*;

public class CardTable extends CardGame
{
	public enum Suit {
		KREUZ, HERZ, KARO, PIK
	}

	public enum Rank {
		ASS, KOENIG, DAME, BAUER, ZEHN, NEUN, ACHT, SIEBEN, SECHS
	}
  //
  protected static Deck deck = new Deck(Suit.values(), Rank.values(), "cover");
  private final int nbPlayers = BauernKriegTcpMain.nbPlayers;
  private final int handWidth = 300;
  private final Location textLocation = new Location(300, 350);
  private final Location[] handLocations =
  {
    new Location(300, 520),
    new Location(300, 80)
  };
  private final Location[] bidLocations = {
		  new Location(300, 400),
		  new Location(300, 200)
  };
  private final Location[] stockLocations =
  {
    new Location(520, 540),
    new Location(80, 50),
  };
  private final Location[] nameLocations =
  {
    new Location(300, 440),
    new Location(300, 160)
  };
  private Hand[] hands = new Hand[nbPlayers];
  private Hand[] bids = new Hand[nbPlayers];
  private Hand[] stocks = new Hand[nbPlayers];
  private String[] playerNames;
  private TcpAgent agent;
  private int myPlayerId;
  private boolean isMyTurn = false;
  private BlinkingName[] blinkingNames = new BlinkingName[nbPlayers];

  public CardTable(TcpAgent agent, String[] playerNames, int myPlayerId)
  {
    super(600, 600, 30);
    this.agent = agent;
    this.playerNames = new String[nbPlayers];
    for (int i = 0; i < nbPlayers; i++)
      this.playerNames[i] = playerNames[i];
    this.myPlayerId = myPlayerId;
    setTitle("TCP Bauernkrieg ");
    for (int i = 0; i < playerNames.length; i++)
      blinkingNames[i] = new BlinkingName(playerNames[i]);
  }

  protected void initHands(int[] cardNumbers)
  {
    for (int i = 0; i < nbPlayers; i++)
    {
      stocks[i] = new Hand(deck);
      bids[i] = new Hand(deck);
      hands[i] = new Hand(deck);
      hands[i].setTouchEnabled(true);
      RowLayout handLayout = new RowLayout(handLocations[i], handWidth);
      handLayout.setRotationAngle(180 * i);
      hands[i].setView(this, handLayout);
      bids[i].setView(this, handLayout);
      StackLayout stockLayout = new StackLayout(stockLocations[i]);
      stockLayout.setRotationAngle(180 * i);
      stocks[i].setView(this, stockLayout);
      addActor(blinkingNames[toPlayerId(i)], nameLocations[i]);
      // TODO: add blinkingNames
      if (i == 0 || i == 2)  // Center align text
        blinkingNames[toPlayerId(i)].setLocationOffset(
          new Point(-blinkingNames[toPlayerId(i)].getTextWidth() / 2, 0));
      if (i == 3)  // Right align text
        blinkingNames[toPlayerId(i)].setLocationOffset(
          new Point(-blinkingNames[toPlayerId(i)].getTextWidth(), 0));
      if (i != 0)
      {
        hands[i].addCardListener(new CardAdapter()
        {
          public void leftDoubleClicked(Card card)
          {
            // Quit if not clicked on own cards
            if (myPlayerId != getPlayerId(card))
              return;

            if (!isMyTurn)
              return;
            isMyTurn = false;

            agent.sendCommand("", Command.TRANSFER_TO_BID_OF_PLAYER,
              getPlayerId(card), myPlayerId, card.getCardNumber());

            // Transfer double-clicked card to own bid
            card.setVerso(false);
            card.getHand().setTargetArea(new TargetArea(bidLocations[0]));
            card.transfer(bids[0], true);

            //dispose of cards if it applies:
            if ((bids[0].getNumberOfCards() + bids[1].getNumberOfCards()) % 2 == 0) {
				if (isSameRank() && !hands[1].isEmpty()) {
					for (int i = 0; i < nbPlayers; i++) {
						Card c = hands[i].getLast();
						//impossible it works that easily >.<
						c.transfer(bids[i], true);
						/* 
						 * actually, it doesn't matter who puts his card 
						 * first onto the bid, As long as both player do it. 
						 * It could work like that!
						 */
						agent.sendCommand("", Command.TRANSFER_TO_BID_OF_PLAYER,
								getPlayerId(card), myPlayerId, card.getCardNumber());
					}
				} else {
					setStatusText("Evaluating round...");
					agent.sendCommand("", Command.CARDS_TO_WINNER);
					transferToWinner();
				}
			} else agent.sendCommand("", CardPlayer.Command.READY_TO_PLAY);
				
				
			if (hands[currentPlayerIndex].isEmpty()) {
				//gameOver();
				System.out.println("game over");
			}
            if (hands[0].getNumberOfCards() == 0)
            {
              agent.sendCommand("", Command.DROP_OUT, myPlayerId);
              setStatusText("Dropped out");
              // TODO: stop blinking
              setBlinking(-1);
            }
            agent.sendCommand("", Command.READY_TO_PLAY);
            checkOver();
          }
          
          private boolean isSameRank() {
				if (bids[0].getLast() == null || bids[1].getLast() == null)
					return false;
				else
					return bids[0].getLast().getRank() == bids[1].getLast()
							.getRank();
          }
        });
      }
    }

    // Insert cards into hands
    setStatusText("Dealing out...");
    int playerId = 0;
    for (int i = 0; i < cardNumbers.length; i++)
    {
      Card card = new Card(deck, cardNumbers[i]);
//      if (card.equals(uglyCardTwin))
//        continue;
      if (toHandId(playerId) != 0)
        card.setVerso(true);
      hands[toHandId(playerId)].insert(card, true);
      delay(200);
      playerId = (playerId + 1) % nbPlayers;
    }
    agent.sendCommand("", Command.READY_TO_PLAY);
  }

  private int getPlayerId(Card card)
  {
    for (int i = 0; i < nbPlayers; i++)
    {
      if (hands[i].getCardList().contains(card))
        return toPlayerId(i);
    }
    return -1;
  }

  protected void stopGame(String client)
  {
    setStatusText(client + " disconnected. Game stopped.");
    setMouseEnabled(false);
    doPause();
  }

  private int toHandId(int playerId)
  {
    int n = playerId - myPlayerId;
    return n >= 0 ? n : (nbPlayers + n);
  }

  private int toPlayerId(int handId)
  {
    return (myPlayerId + handId) % nbPlayers;
  }

  private ArrayList<Card> searchPairs(Hand hand)
  {
    ArrayList<Card> list = new ArrayList<Card>();
    ArrayList<Card[]> pairs = hand.getPairs();
    for (Card[] pair : pairs)
    {
      list.add(pair[0]);
      list.add(pair[1]);
    }
    return list;
  }
  
  protected void transferToBidOfPlayer(int sourceId, int destId, int cardNb)
  {
    Hand src = hands[toHandId(sourceId)];
    Hand dst = bids[toHandId(destId)];
    Card card = src.getCard(cardNb);
    src.setTargetArea(new TargetArea(bidLocations[toHandId(destId)]));
    card.transfer(dst, true);
    card.setVerso(true);
  }

  protected void transferToStock(int playerId, int cardNb)
  {
    Hand src = hands[toHandId(playerId)];
    Hand dst = stocks[toHandId(playerId)];
    Card card = src.getCard(cardNb);
    src.setTargetArea(new TargetArea(stockLocations[toHandId(playerId)]));
    card.transfer(dst, true);
    card.setVerso(false);
  }

  protected void setMyTurn()
  {
    setStatusText("It's your turn. Double click on one of your cards to play it.");
    isMyTurn = true;
    // TODO: Enable blinking
    setBlinking(myPlayerId);
  }

  protected void setOtherTurn(int currentPlayerId)
  {
    setStatusText("Wait for you turn. Current player is " + playerNames[currentPlayerId]);
    // TODO: Enable blinking
    setBlinking(currentPlayerId);
  }

  protected int getMyNumberOfCards()
  {
    return hands[toHandId(myPlayerId)].getNumberOfCards();
  }

  /**
   * TODO: rewrite this!
   * @return
   */
  protected boolean checkOver()
  {
    for (int i = 0; i < nbPlayers; i++)
    {
      if (hands[i].getNumberOfCards() == 1)
      {
        Card card = hands[i].getFirst();
        if (card.equals(uglyCard))
        {
          card.setVerso(false);
          addActor(new Actor("sprites/gameover.gif"), textLocation);
          setStatusText("Game over. Loser is player: "
            + playerNames[toPlayerId(i)]);
          doPause();
          return true;
        }
      }
    }
    return false;
  }

  protected void setBlinking(int playerId)
  {
    for (int i = 0; i < nbPlayers; i++)
      blinkingNames[i].setBlinkingEnabled(false);
    if (playerId > -1)
      blinkingNames[playerId].setBlinkingEnabled(true);
  }
}
