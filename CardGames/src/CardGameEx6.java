// CardGameEx6.java

import ch.aplu.jcardgame.*;
import ch.aplu.jgamegrid.*;

public class CardGameEx6 extends CardGame
{
  public enum Suit
  {
    KREUZ, KARO, HERZ, PIK
  }

  public enum Rank
  {
    ASS, KOENIG, DAME, BUBE, ZEHN, NEUN, ACHT, SIEBEN, SECHS
  }
  private Deck deck = new Deck(Suit.values(), Rank.values(), "cover");
  private final int nbPlayers = 2;
  private final int nbCards = 7;
  private Hand[] hands;
  private Hand stock;
  private final Location upperLocation = new Location(300, 150);
  private final Location bottomLocation = new Location(300, 450);

  public CardGameEx6()
  {
    super(600, 600, 30);
    addStatusBar(30);
    setStatusText("Left click to set on top, doubleclick to move ");
    stock = new Hand(deck);
    stock.setView(this, new RowLayout(new Location(300,300), 350));
    hands = deck.dealingOut(nbPlayers, nbCards);

    CardAdapter cardAdapter = new CardAdapter()
    {
      public void atTarget(Card card, Location targetLocation)
      {
        showHands();
        hands[0].sort(Hand.SortType.SUITPRIORITY, true);
        hands[1].sort(Hand.SortType.SUITPRIORITY, true);
      }

      public void leftDoubleClicked(Card card)
      {
        if (card.isInHand(hands[0]))
        {
          card.transfer(stock, true);
          setStatusText("Moved " + card);
          return;
        }
        if (card.isInHand(hands[1]))
        {
          card.transfer(stock, true);
          setStatusText("Moved " + card);
        }
      }
    };

    hands[0].addCardListener(cardAdapter);
    hands[1].addCardListener(cardAdapter);
    hands[0].setTouchEnabled(true);
    hands[1].setTouchEnabled(true);
    showHands();
  }

  private void showHands()
  {
    RowLayout rowLayout = new RowLayout(bottomLocation, 350);
    hands[0].setView(this, rowLayout);
    hands[0].draw();
    RowLayout rowLayout1 = new RowLayout(upperLocation, 350);
    hands[1].setView(this, rowLayout1);
    hands[1].draw();
  }

  public static void main(String[] args)
  {
    new CardGameEx6();
  }
}
