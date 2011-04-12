
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ch.aplu.jcardgame.*;
import ch.aplu.jcardgame.Hand.SortType;
import ch.aplu.jgamegrid.Location;

public class PokerTest extends CardGame {

	public enum Suit {
		KREUZ, KARO, HERZ, PIK
	}

	public enum Rank {
		ASS, KOENIG, DAME, BUBE, ZEHN, NEUN, ACHT, SIEBEN, SECHS
	}
	
	private Deck deck = new Deck(Suit.values(), Rank.values(), "cover");
	private Hand[] hands;
	private Hand burntCards;
	private Hand stock;
	private Location burntCardsLocation = new Location(100, 300);
	private final int nbPlayers = 2;
	private final int nbCards = 5;
	private Location[] handLocations = { new Location(300, 100),
			new Location(300, 500), };
	
	public PokerTest() {
		super(660, 660);
		initBurntCards();
		initHands();
		stock = hands[2];
		stock.setVerso(true);
		stock.setView(this, new StackLayout(new Location (300, 300)));
		stock.draw();	
		
	}
	
	private void initBurntCards() {
		burntCards = new Hand(deck);
		burntCards.setView(this, new StackLayout(burntCardsLocation));
	}

	private void initHands() {
		hands = deck.dealingOut(nbPlayers, nbCards);
		for (int i = 0; i < nbPlayers; i++) {
			final int k = i;
			hands[i].addCardListener(new CardAdapter() {
			      public void leftDoubleClicked(Card card)
			      {
			    	  //card.removeFromHand(true); //it isn't removed... WHY?
			    	  //i want this to work: 
			    	  card.setVerso(true);
			    	  card.transfer(burntCards, true);
			    	  Card newCard = stock.getLast();
			    	  newCard.transfer(hands[k], true);
			    	  newCard.setVerso(false);
			    	  System.out.println("Highest Value Hand " + k +"\n" + 
			    			  getHighestCombo(hands[k]) + "\n");
			      }
			    });
			hands[i].setTargetArea(new TargetArea(burntCardsLocation));
			hands[i].setTouchEnabled(true);
			hands[i].setView(this, new RowLayout(handLocations[i], 450));
			hands[i].sort(SortType.RANKPRIORITY, false);
			hands[i].draw();
		}
	}
	/**
	 * TODO: implement full houses and straights!
	 * test for bugs
	 * 
	 * Breaks something! Doesn't leave the given hand as it was.
	 * I have no idea what <.<
	 * 
	 * @param hand
	 * @return
	 */
	private Hand getHighestCombo(Hand hand) {
		List<Hand> goodStuff = new ArrayList<Hand>();
		for (Suit s: Suit.values())
			goodStuff.addAll(toArrayList(hand.getSequences(s, 5))); //straight flush
		goodStuff.addAll(toArrayList(hand.getQuads()));
		//insert full house here
		//insert flush (alle 5 gleicher Suit)
		goodStuff.addAll(toArrayList(hand.getSequences(5))); //straight
		goodStuff.addAll(toArrayList(hand.getTrips()));
		goodStuff.addAll(toArrayList(hand.getPairs()));
		//somehow buggy:
		Hand handHiCard = new Hand(deck);
		Card hiCard = hand.get(0).clone();
		//int posHighestCard = hand.getMaxPosition(SortType.RANKPRIORITY);
		//Card hiCard = hand.get(posHighestCard).clone();
		handHiCard.insert(hiCard, false);
		goodStuff.add(handHiCard);
		return goodStuff.get(0);
	}
	
	private int getValueOfCombo(Hand combo) {
		return 0;
	}
	
	public static void main(String[] args) {
		new PokerTest();
	}
	
	private List<Hand> toArrayList(Hand[] hands) {
		return Arrays.asList(hands);
	}
	
	private List<Hand> getFullHouse() {
		return null;
	}
}
