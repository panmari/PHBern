// PokerApr16.java

// Modifications see: Modified AP

/* Test:
 - Klicken auf obere Hand rechts (Kreuz 10)
 - Bewegt sich in mitte und Schaufel 6 kommet nach oben
 */

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import ch.aplu.jcardgame.*;
import ch.aplu.jcardgame.Hand.SortType;
import ch.aplu.jgamegrid.Location;

public class PokerApr16 extends CardGame {
	public enum ComboType {
		SingleCard, Pair, Trips, Straight, FullHouse, Flush, Quads, StraightFlush, 
	}
	
	//TODO: look up right order of Suits
	//TODO: look up how that offhand-stuff works
	public enum Suit {
		HERZ, KREUZ, KARO, PIK
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

	public PokerApr16() {
		super(660, 660);
		initBurntCards();
		initHands();
		stock = hands[2];
		stock.setVerso(true);
		stock.setView(this, new StackLayout(new Location(300, 300)));
		stock.draw();
	}

	private void initBurntCards() {
		burntCards = new Hand(deck);
		burntCards.setView(this, new StackLayout(burntCardsLocation));
	}

	private void initHands() {
		hands = deck.dealingOut(nbPlayers, nbCards, true);
		for (int i = 0; i < nbPlayers; i++) {
			final int k = i;
			hands[i].addCardListener(new CardAdapter() {
				public void leftDoubleClicked(Card card) {
					card.setVerso(true);
					card.transfer(burntCards, true);
					Card newCard = stock.getLast();
					newCard.transfer(hands[k], true);
					// Modified AP
					newCard.setVerso(false);
					System.out.println("Highest Value Hand " + k + "\n"
							+ getHighestCombo(hands[k]) + "\n");
					System.out.println("Is player0 (top) winner? "
							+ isPlayer0Winner());
				}
			});
			hands[i].setTargetArea(new TargetArea(burntCardsLocation));
			hands[i].setTouchEnabled(true);
			hands[i].setView(this, new RowLayout(handLocations[i], 450));
			hands[i].sort(SortType.RANKPRIORITY, false);
			hands[i].draw();
			System.out.println("Highest Value Hand " + i + "\n"
					+ getHighestCombo(hands[i]) + "\n");
		}
	}

	/**
	 * TODO: implement full houses and straights! test for bugs
	 * 
	 * @param hand
	 * @return
	 */
	private PokerCombo getHighestCombo(Hand hand) {
		for (Suit s : Suit.values())
			if (hand.getSequences(s, 5).length > 0)
				return new PokerCombo(hand.getSequences(s, 5)[0], 
						ComboType.StraightFlush);
		if(hand.getQuads().length > 0)
			return new PokerCombo(hand.getQuads()[0], 
					ComboType.Quads, hand);
		
		if(!getFlush(hand).isEmpty())
			return new PokerCombo(getFlush(hand).get(0), 
				ComboType.Flush);
		
		if (getHighestFullHouse(hand) != null)
			return new PokerCombo(getHighestFullHouse(hand), 
					ComboType.FullHouse);
		
		if (hand.getSequences(5).length > 0) // straight
			return new PokerCombo(hand.getSequences(5)[0], 
					ComboType.Straight);
		
		if (hand.getTrips().length > 0)
			return new PokerCombo(hand.getTrips()[0], 
					ComboType.Trips, hand);
		
		if (hand.getPairs().length > 0)
			return new PokerCombo(hand.getPairs()[0], 
					ComboType.Pair, hand);

		Hand handHiCard = new Hand(deck);
		int posHighestCard = hand.getMaxPosition(SortType.RANKPRIORITY);
		Card hiCard = hand.get(posHighestCard).clone();
		handHiCard.insert(hiCard, false);
		return new PokerCombo(handHiCard, ComboType.SingleCard);
	}

	private List<Hand> getFlush(Hand hand) {
		LinkedList<Hand> flushes = new LinkedList<Hand>();
		for (Suit t: Suit.values()) {
			int cardCounter = 0;
			Hand currentCheckingCards = new Hand(deck);
			for (Card a: hand.getCardList())  {
				if (a.getSuit() == t) {
					currentCheckingCards.insert(a.clone(), false);
					cardCounter++;
					if (cardCounter == 5) {
						flushes.add(currentCheckingCards);
						break;
					}
				}
				
			}
		}
		return flushes;
	}

	
	private boolean isPlayer0Winner() {
		PokerCombo combo0 = getHighestCombo(hands[0]);
		PokerCombo combo1 = getHighestCombo(hands[1]);
		//better combo?
		if (combo0.comboType.ordinal() > combo1.comboType.ordinal())
			return true;
		else if (combo0.comboType.ordinal() < combo1.comboType.ordinal())
			return false;
		//same combo -> higher rank of combo?
		else if (combo0.hand.getFirst().getRankId() < 
				combo1.hand.getFirst().getRankId())
			return true;
		else if (combo0.hand.getFirst().getRankId() > 
				combo1.hand.getFirst().getRankId())
			return false;
		//same combo, same rank -> higher suit of combo?
		else if (combo0.offCard == null) 
			return (combo0.hand.getFirst().getSuitId() < 
				combo1.hand.getFirst().getSuitId());
		else return (combo0.offCard.getRankId() < 
				combo1.offCard.getRankId());
	}

	public static void main(String[] args) {
		new PokerApr16();
	}

	private Hand getHighestFullHouse(Hand hand) {
		Hand fullHouse = null;
		Hand[] trips = hand.getTrips();
		Hand[] pairs = hand.getPairs();
		if (trips.length > 0 && pairs.length > 0) {
			fullHouse = new Hand(this.deck);
			fullHouse.insert(trips[0], false);
			fullHouse.insert(pairs[0], false);
		}
		return fullHouse;
	}
	
	class PokerCombo {
		Hand hand;
		ComboType comboType;
		Card offCard;
		
		public PokerCombo(Hand hand, ComboType comboType) {
			this.hand = hand;
			this.comboType = comboType;
		}
		
		public PokerCombo(Hand hand, ComboType comboType, Hand wholeHand) {
			//Also transmitts whole hand for computing offCard
			// only for pairs, quads & trips
			this(hand, comboType);
			ArrayList<Card> leftCards = wholeHand.getCardList();
			leftCards.removeAll(hand.getCardList());
			//0 is highest card, coz it's ranksort
			this.offCard = leftCards.get(0);
		}
		
		public String toString() {
			String str = "Type of Combo: " + comboType + "\n";
			if (offCard != null)
				str+= "Offcard: " + offCard + "\n";
			str += hand;
			return str;
		}
	}
}
