// CardGameEx14.java

import ch.aplu.jcardgame.*;
import ch.aplu.jgamegrid.*;

public class CardGameEx14edited extends CardGame
{
  public enum Suit
  {
    KREUZ, KARO, HERZ, PIK
  }

  public enum Rank
  {
    ASS, KOENIG, DAME, BAUER, ZEHN, NEUN, ACHT, SIEBEN, SECHS
  }
  private Deck deck = new Deck(Suit.values(), Rank.values(), "cover");
  private final int nbPlayers = 4;
  private final Location[] handLocations =
  {
    new Location(300, 525),
    new Location(75, 300),
    new Location(300, 75),
    new Location(525, 300),
    new Location(300, 300)
  };
  private final Location[] bidLocations =
  {
    new Location(300, 350),
    new Location(250, 300),
    new Location(300, 250),
    new Location(350, 300),
  };
  private final Location[] stockLocations =
  {
    new Location(400, 500),
    new Location(100, 400),
    new Location(200, 100),
    new Location(500, 200),
  };
  private Hand[] hands;
  private Hand[] bids = new Hand[nbPlayers];
  private Hand[] stocks = new Hand[nbPlayers];
  private Hand talon;

  public CardGameEx14edited()
  {
    super(600, 600, 30);
    dealingOut();
    setStatusText("Double click on hand to play. Double click on own bid to collect cards.");
    initBids();
    initStocks();
  }

  private void dealingOut()
  {
    setStatusText("Dealing out...");

    hands = deck.dealingOut(nbPlayers, 7);
    talon = hands[nbPlayers];
    talon.setVerso(true);

    for (int i = 0; i < nbPlayers; i++)
    {
      hands[i].setView(this, new StackLayout(handLocations[i], 90 * i));
      hands[i].setVerso(i == 0 ? false : true);
      hands[i].draw();
      hands[i].setTouchEnabled(true);
    }

    talon.setView(this, new StackLayout(handLocations[nbPlayers]));
    talon.draw();

    while (!talon.isEmpty())
    {
      for (int i = 0; i < nbPlayers; i++)
      {
        Card top = talon.getLast();
        top.setVerso(i == 0 ? false : true);
        talon.transfer(top, hands[i], true);
      }
    }
  }

  private void initBids()
  {
    for (int i = 0; i < nbPlayers; i++)
    {
      bids[i] = new Hand(deck);
      bids[i].setView(this, new StackLayout(bidLocations[i]));
      hands[i].setTargetArea(new TargetArea(bidLocations[i]));
      hands[i].setTouchEnabled(true);
      final int k = i;
      hands[i].addCardListener(new CardAdapter()
      {
        public void leftDoubleClicked(Card card)
        {
          card.setVerso(false);
          card.transfer(bids[k], true);
        }
      });
      bids[i].setTouchEnabled(true);
      bids[i].addCardListener(new CardAdapter()
      {
        public void leftDoubleClicked(Card card)
        {
        	//TODO: check if everyone played a card
          transferToStock(k);
        }
      });
    }
  }

  private void initStocks()
  {
    for (int i = 0; i < nbPlayers; i++)
    {
      stocks[i] = new Hand(deck);
      double rotationAngle;
      if (i == 0 || i == 2)
        rotationAngle = 0;
      else
        rotationAngle = 90;
      stocks[i].setView(this, new StackLayout(stockLocations[i], rotationAngle));
    }
  }

  private void transferToStock(int player)
  {
     Hand eval = new Hand(deck);
    for (int i = 0; i < nbPlayers; i++)
      eval.insert(bids[i].getFirst(), false);
    int nbWinner = eval.getMaxPosition(Hand.SortType.RANKPRIORITY);
    if (nbWinner != player)
      return;
  
    for (int i = 0; i < nbPlayers; i++)
    {
      bids[i].setTargetArea(new TargetArea(stockLocations[player]));
      Card c = bids[i].getLast();
      if (c == null)
        continue;
      c.setVerso(true);
      bids[i].transfer(c, stocks[player], false, true);  // Non blocking
    }
  }

  public static void main(String[] args)
  {
    new CardGameEx14edited();
  }
}
