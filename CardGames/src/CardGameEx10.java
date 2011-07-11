// CardGameEx10.java

import ch.aplu.jcardgame.*;
import ch.aplu.jgamegrid.*;

public class CardGameEx10 extends CardGame
{
  public enum Suit
  {
    KREUZ, HERZ, KARO, PIK
  }

  public enum Rank
  {
    ASS, KOENIG, DAME, BAUER, ZEHN, NEUN, ACHT, SIEBEN, SECHS
  }

  private final Deck deck =
    new Deck(Suit.values(), Rank.values(), "cover");
  private final int nbPlayers = 4;
  private final Location[] handLocations =
  {
    new Location(300, 525),
    new Location(75, 300),
    new Location(300, 75),
    new Location(525, 300),
    new Location(300, 300)
  };
  private Hand[] hands;
  private Hand talon;

  public CardGameEx10()
  {
    super(600, 600, 30);
    hands = deck.dealingOut(nbPlayers, 0);  // All cards in talon
    talon = hands[nbPlayers]; // hand[4] is talon

    for (int i = 0; i < nbPlayers; i++)
    {
      hands[i].setView(this, new RowLayout(handLocations[i], 300, 90 * i));
      hands[i].draw();
      hands[i].setTouchEnabled(true);
    }

    talon.setView(this, new StackLayout(handLocations[nbPlayers]));
    talon.setVerso(true);
    talon.draw();

    while (!talon.isEmpty())
    {
      for (int i = 0; i < nbPlayers; i++)
      {
        Card top = talon.getLast();
        talon.transfer(top, hands[i], true);
        hands[i].setVerso(false);
        hands[i].sort(Hand.SortType.SUITPRIORITY, true);
      }
    }
  }

  public static void main(String[] args)
  {
    new CardGameEx10();
  }
}