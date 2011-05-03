package tcpBauernKrieg;

// CardTable.java

import ch.aplu.jcardgame.*;
import ch.aplu.jgamegrid.*;
import ch.aplu.tcp.*;
import ch.aplu.util.Monitor;

public class CardTable extends CardGame {
	public enum Suit {
		KREUZ, HERZ, KARO, PIK
	}

	public enum Rank {
		ASS, KOENIG, DAME, BAUER, ZEHN, NEUN, ACHT, SIEBEN, SECHS
	}

	//
	protected static Deck deck = new Deck(Suit.values(), Rank.values(), "cover");
	private final int nbPlayers = 2;
	private final int nbStartCards = 18;

	private final Location[] handLocations = { new Location(300, 525),
			new Location(300, 75), };
	private final Location[] bidLocations = { new Location(180, 300),
			new Location(420, 300), };
	private final Location[] stockLocations = { new Location(90, 400),
			new Location(510, 200), };
	private Hand[] hands = new Hand[nbPlayers];
	private Hand[] bids = new Hand[nbPlayers];
	private Hand[] stocks = new Hand[nbPlayers];
	private int currentPlayerIndex;
	private String[] playerNames;
	private TcpAgent agent;

	public CardTable(TcpAgent agent, String[] playerNames,
			int currentPlayerIndex) {
		super(600, 600, 30);
		this.agent = agent;
		this.playerNames = new String[nbPlayers];
		for (int i = 0; i < nbPlayers; i++)
			this.playerNames[i] = playerNames[i];
		this.currentPlayerIndex = currentPlayerIndex;
		setTitle("Current player's name: " + playerNames[currentPlayerIndex]);
	}

	protected void initHands(int[] cardNumbers) {
		for (int i = 0; i < nbPlayers; i++) {
			hands[i] = new Hand(deck);
			for (int k = 0; k < nbStartCards; k++)
				hands[i].insert(cardNumbers[i * nbStartCards + k], false);
		}

		hands[currentPlayerIndex].sort(Hand.SortType.SUITPRIORITY, false);
		hands[currentPlayerIndex].addCardListener(new CardAdapter() {
			public void leftDoubleClicked(Card card) {
				hands[currentPlayerIndex].setTouchEnabled(false);
				agent.sendCommand("", CardPlayer.Command.CARD_TO_BID,
						currentPlayerIndex, card.getCardNumber());
				card.setVerso(false);
				card.transfer(bids[currentPlayerIndex], true);
				if ((bids[0].getNumberOfCards() + bids[1].getNumberOfCards()) % 2 == 0) {
					if (isSameRank() && !hands[currentPlayerIndex].isEmpty()) {
						for (int i = 0; i < nbPlayers; i++) {
							Card c = hands[i].getLast();
							c.transfer(bids[i], true);
						}
					} else {
						setStatusText("Evaluating round...");
						agent.sendCommand("", CardPlayer.Command.CARDS_TO_WINNER);
						transferToWinner();
					}
				} else {
					agent.sendCommand("", CardPlayer.Command.READY_TO_PLAY);
				}
				if (hands[currentPlayerIndex].isEmpty()) {
					//gameOver();
					System.out.println("game over");
				}
			}

			private boolean isSameRank() {
				if (bids[0].getLast() == null || bids[1].getLast() == null)
					return false;
				else
					return bids[0].getLast().getRank() == bids[1].getLast()
							.getRank();
			}

		});

		StackLayout[] layouts = new StackLayout[nbPlayers];
		for (int i = 0; i < nbPlayers; i++) {
			int k = (currentPlayerIndex + i) % nbPlayers;
			layouts[k] = new StackLayout(handLocations[i]);
			layouts[k].setRotationAngle(180 * i);
			hands[k].setVerso(true);
			hands[k].setView(this, layouts[k]);
			hands[k].setTargetArea(new TargetArea(bidLocations[i]));
			hands[k].draw();
			//initiate bids:
			bids[k] = new Hand(deck);
			bids[k].setView(this, new RowLayout(bidLocations[i], 200));
			stocks[k] = new Hand(deck);
			stocks[k].setView(this, new StackLayout(stockLocations[i]));
		}
		agent.sendCommand("", CardPlayer.Command.READY_TO_PLAY);
	}

	public void transferToWinner() {
		delay(1000);
		int nbWinner = 0;
		for (int i = 1; i < nbPlayers; i++)
			if (bids[i].getLast().getRankId() < bids[nbWinner].getLast().getRankId())
				nbWinner = i;
		transferToStock(nbWinner);
		this.currentPlayerIndex = nbWinner;
	}

	private void transferToStock(int player) {
		for (int i = 0; i < nbPlayers; i++) {
			bids[i].setTargetArea(new TargetArea(stockLocations[player]));
			while (!bids[i].isEmpty()) {
				Card c = bids[i].getLast();
				c.setVerso(true);
				bids[i].transferNonBlocking(c, stocks[player]);
			}
		}
		agent.sendCommand("", CardPlayer.Command.READY_TO_PLAY);
	}

	protected void moveCardToBid(int playerIndex, int cardNumber) {
		Card card = hands[playerIndex].getCard(cardNumber);
		card.setVerso(false);
		hands[playerIndex].transfer(card, bids[playerIndex], true);
		agent.sendCommand("", CardPlayer.Command.READY_TO_PLAY);
	}

	protected void stopGame(String client) {
		setStatusText(client + " disconnected. Game stopped.");
		setMouseEnabled(false);
		doPause();
	}

	protected void setMyTurn() {
		setStatusText("It's your turn. Double click on one of your cards to play it.");
		hands[currentPlayerIndex].setTouchEnabled(true);
	}

	protected void setOtherTurn() {
		setStatusText("Wait for you turn.");
	}
}
