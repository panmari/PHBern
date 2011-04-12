
import java.util.ArrayList;
import ch.aplu.jcardgame.*;
import ch.aplu.jgamegrid.*;
import ch.aplu.jcardgame.Hand.SortType;

public class PokerTest1 extends CardGame
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
  private final int nbPlayers = 2;
  private final int nbCards = 5;
  private Hand[] hands;
  private final Location upperLocation = new Location(300, 120);
  private final Location bottomLocation = new Location(300, 470);

  public PokerTest1()
  {
    super(600, 600);
    addStatusBar(30);
    setStatusText("Left click to set on top, doubleclick to move ");
    setSimulationPeriod(30);

    hands = deck.dealingOut(nbPlayers, nbCards);
    hands[0].setTargetArea(new TargetArea(upperLocation));
    hands[1].setTargetArea(new TargetArea(bottomLocation));

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
        card.removeFromHand(true);
      }
    };

    hands[0].addCardListener(cardAdapter);
    hands[1].addCardListener(cardAdapter);
    hands[0].setTouchEnabled(true);
    hands[1].setTouchEnabled(true);
    showHands();

    hands[2].setVerso(true);
    hands[2].setView(this, new StackLayout(new Location(300, 300)));
    hands[2].draw();
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
  /**
   * hand needs to be sorted by rank!
   *
   * TODO: implement full houses and straights!
   * test for bugs
   * @param hand
   * @return
   */
  private Hand getHighestCombo(Hand hand)
  {
    ArrayList<Hand> goodStuff = new ArrayList<Hand>();
    goodStuff.addAll(getSequences(hand, 5, Suit.HERZ)); //straight flush
    goodStuff.addAll(getSequences(hand, 5, Suit.KARO));
    goodStuff.addAll(getSequences(hand, 5, Suit.KREUZ));
    goodStuff.addAll(getSequences(hand, 5, Suit.PIK));
    goodStuff.addAll(getQuads(hand));
    //insert full house here
    //insert flush (alle 5 gleicher Suit)
    goodStuff.addAll(getSequences(hand, 5)); //straight
    goodStuff.addAll(getTrips(hand));
    goodStuff.addAll(getPairs(hand));
    Hand handb = new Hand(deck);
    handb.insert(hand.getFirst(), false); //highest single card
    goodStuff.add(handb);
    return goodStuff.get(0);
  }

  public static void main(String[] args)
  {
    new PokerTest1();
  }

  /**
   * Returns all sequences found in the current hand that have given suit with
   * given length (>=2). The cards in the hands returned are ordered with
   * SortType.RANKPRIORITY. Does not contain shorter sublist, which would also
   * meet the length requirement.
   *
   * The returned sequences have always maximum size.
   *
   * TODO: return error if not length < 2
   *
   * @param suit
   * @param length of sequence, has to be bigger than 2
   *
   * @return a array of hands with all sequences found; if no sequence is
   *         found, the array has length = 0 (but is not null)
   */
  public ArrayList<Hand> getSequences(Hand hand, int length, Suit suit)
  {
    ArrayList<Hand> allSequences = new ArrayList<Hand>();
    Hand sameSuit = hand.getCardsWithSuit(suit);
    sameSuit.sort(SortType.RANKPRIORITY, true);
    ArrayList<Card> sameSuitList = sameSuit.getCardList();
    for (int i = 0; i <= sameSuitList.size() - length; i++)
    {
      Card previousCard = sameSuitList.get(i);
      Card currentCard = sameSuitList.get(i + 1);
      Hand currentSequence = new Hand(deck);
      currentSequence.insert(previousCard, false);
      while (previousCard.getRankId() + 1 == currentCard.getRankId())
      {
        previousCard = currentCard;
        currentSequence.insert(previousCard, false);
        if (!sameSuit.getLast().equals(currentCard))
          currentCard = sameSuitList.get(sameSuitList.indexOf(currentCard) + 1);
      }

      if (currentSequence.getNumberOfCards() >= length
        && !containsSublistOf(allSequences, currentSequence)) //or longer
        allSequences.add(currentSequence);
    }
    return allSequences;
  }

  /**
   * Returns true if possibleSubHand is in any form already present in any hand of handsList
   *
   * @param handsList ArrayList of Hands that is searched
   * @param possibleSubHand Hand that is suspected to be already present
   * @return true, if possibleSubHand is present
   */
  private boolean containsSublistOf(ArrayList<Hand> handsList, Hand possibleSubHand)
  {
    for (Hand a : handsList)
    {
      if (a.getCardList().containsAll(possibleSubHand.getCardList()))
        return true;
    }
    return false;
  }

  /**
   * Returns all sequences only considering Rank and ignoring Suit.
   * If there are multiple possibilities for a sequence, only one is shown.
   *
   * The returned sequences have always maximum size.
   *
   *
   * @param length has to be >= 2
   * @return a array of hands with all sequences found; if no sequence is
   *         found, the array has length = 0 (but is not null)
   */
  public ArrayList<Hand> getSequences(Hand hand, int length)
  {
    ArrayList<Hand> allSequences = new ArrayList<Hand>();
    SortType previousST = hand.getSortType();
    hand.sort(SortType.RANKPRIORITY, false);
    ArrayList<Card> handList = hand.getCardList();
    for (int i = 0; i < handList.size() - length; i++)
    {
      Card previousCard = handList.get(i);
      Card currentCard = handList.get(i + 1);
      Hand currentSequence = new Hand(deck);
      currentSequence.insert(previousCard, false);
      while (previousCard.getRankId() + 1 == currentCard.getRankId())
      {
        do
        {
          previousCard = currentCard;
          if (!hand.getLast().equals(currentCard))
          {
            currentCard = handList.get(handList.indexOf(currentCard) + 1);
            break;
          }
        }
        while (previousCard.getRank() == currentCard.getRank()); //skip cards with same rank
        currentSequence.insert(previousCard, false);
      }
      if (currentSequence.getNumberOfCards() >= length
        && !containsSublistOf(allSequences, currentSequence))
        allSequences.add(currentSequence);
    }
    hand.sort(previousST, false); //reset SortType
    return allSequences;
  }

  /**
   * Returns all pairs found in the current hand. If the cards are part of a
   * trips or quads they are not returned as pairs.
   *
   * @return a arrayList of hands with all pairs found; if no pair is found,
   *         the arrayList has length = 0 (but is not null)
   */
  public ArrayList<Hand> getPairs(Hand hand)
  {
    return getMultiples(hand, 2);
  }

  /**
   * Ugly method to make sure, a pair/tripple and so on isn't added a second time
   * Very ugly because it's based on string comparison.
   *
   * but it doesn't work with a simple .contains()
   * -> something is wrong with .equals in the Hand class?
   * @param hands
   * @param testHand
   * @return
   */
  private boolean isAlreadyInList(ArrayList<Hand> hands, Hand testHand)
  {
    for (Hand a : hands)
    {
      if (a.toString().equals(testHand.toString()))
        return true;
    }
    return false;
  }

  /**
   * Returns all trips (tree-of-a-kind) found in the current hand. If the
   * cards are part of a quads they are not returned as trips.
   *
   * @return a array of hands with all trips found; if no trip is found, the
   *         array has length = 0 (but is not null)
   */
  public ArrayList<Hand> getTrips(Hand hand)
  {
    return getMultiples(hand, 3);
  }

  private ArrayList<Hand> getMultiples(Hand hand, int amount)
  {
    SortType previousST = hand.getSortType();
    hand.sort(SortType.RANKPRIORITY, false);
    ArrayList<Hand> allMultiples = new ArrayList<Hand>();
    for (Card a : hand.getCardList())
    {
      Rank rankA = (Rank)a.getRank();
      Hand sameRankCards = hand.getCardsWithRank(rankA);
      if (sameRankCards.getCardList().size() == amount && !isAlreadyInList(allMultiples, sameRankCards))
        allMultiples.add(sameRankCards);
    }
    hand.sort(previousST, false); //reset SortType
    return allMultiples;
  }

  /**
   * Returns all quads (four-of-kind) found in the current hand.
   *
   * @return a array of hands with all quads found; if no quad is found, the
   *         array has length = 0 (but is not null)
   */
  public ArrayList<Hand> getQuads(Hand hand)
  {
    return getMultiples(hand, 4);
  }
}
