import ch.aplu.jcardgame.*;
import ch.aplu.jgamegrid.*;
import ch.aplu.util.Monitor;
import java.awt.*;

// Version von Clab1
/*
 * Changelog:
 * 
 * added gameOver() - evaluates game using stocks[]
 * -> a lot of instance variables could be removed.
 * 
 * added Textactor winnerLabel @gameOver
 * 
 * adapted to new library, uses now "transferNonBlocking()"
 * rewrote loop in TransferToStock(), it works now without break.
 */
public class BauernKrieg extends CardGame {
	public enum Suit {
		KREUZ, HERZ, KARO, PIK
	}

	public enum Rank {
		ASS, KOENIG, DAME, BAUER, ZEHN, NEUN, ACHT, SIEBEN, SECHS
	}

	private final Deck deck = new Deck(Suit.values(), Rank.values(), "cover");
	private final int nbPlayers = 2;
	private final int nbCards = 18;
	private final Location[] handLocations = { new Location(210, 440),
			new Location(390, 440), };
	private final Location[] bidLocations = { new Location(210, 200),
			new Location(390, 200), };
	private final Location[] stockLocations = { new Location(90, 400),
			new Location(510, 400), };
	private Hand[] hands;
	private Hand[] bids = new Hand[nbPlayers];
	private Hand[] stocks = new Hand[nbPlayers];
	private int currentPlayer = 0;
	
	public BauernKrieg() {
		super(600, 600, 30);
		initHands();
		setStatusText("Doubleclick to play card" + ". Starting player: "
				+ currentPlayer);
		TextActor player0 = new TextActor("player 0 ", Color.white,
				getBgColor(), new Font("Arial", Font.BOLD, 16));
		addActor(player0, new Location(182, 530));
		TextActor player1 = new TextActor("player 1 ", Color.white,
				getBgColor(), new Font("Arial", Font.BOLD, 16));
		addActor(player1, new Location(360, 530));
		initBids();
		initStocks();
		hands[0].setTouchEnabled(true);
	}

	private void initHands() {
		hands = deck.dealingOut(nbPlayers, nbCards);
		for (int i = 0; i < nbPlayers; i++) {
			hands[i].setView(this, new StackLayout(handLocations[i]));
			//hands[i].setView(this, new RowLayout(handLocations[i], 300)); //use for testing
			hands[i].setVerso(true); //comment out for testing
			hands[i].draw();
		}
	}

	private void initBids() {
		for (int i = 0; i < nbPlayers; i++) {
			bids[i] = new Hand(deck);
			bids[i].setView(this, new RowLayout(bidLocations[i], 130));
			hands[i].setTargetArea(new TargetArea(bidLocations[i]));
			final int k = i;
			hands[i].addCardListener(new CardAdapter() {
				public void leftDoubleClicked(Card card) {
					card.setVerso(false);
					card.transfer(bids[k], true);
					hands[currentPlayer].setTouchEnabled(false);
					currentPlayer = (currentPlayer + 1) % nbPlayers;
					if (nrCardsInBids() % nbPlayers == 0) //every player placed a card
					{
						if (isSameRank() && !hands[currentPlayer].isEmpty()) {
							for (int i = 0; i < nbPlayers; i++) {
								Card c = hands[i].getLast();
								c.transfer(bids[i], true);
							}
						} else {
							setStatusText("Evaluating round...");
							currentPlayer = transferToWinner();
						}
					}
					if (hands[currentPlayer].isEmpty())
						gameOver();
					else {
						setStatusText("Current player: " + currentPlayer);
						hands[currentPlayer].setTouchEnabled(true);
					}
				}

				private boolean isSameRank() {
					if (bids[0].getLast() == null || bids[1].getLast() == null)
						return false;
					else return bids[0].getLast().getRank() == bids[1].getLast()
							.getRank();
				}
			});
		}
	}
	private int nrCardsInBids(){
		int nrCards = 0;
		for (int i = 0; i < nbPlayers; i++)
			nrCards += bids[i].getNumberOfCards();
		return nrCards;
	}

	protected void gameOver() {
		int nbCards0 = stocks[0].getNumberOfCards();
		int nbCards1 = stocks[1].getNumberOfCards();
		TextActor winnerLabel = new TextActor("Winner!", Color.yellow,
				getBgColor(), new Font("Arial", Font.BOLD, 16));
		winnerLabel.setLocationOffset(new Point(-30, 90));
		if (nbCards0 > nbCards1) {
			setStatusText("Game over. Player 0 won with "
					+ nbCards0 + " cards");
			addActor(winnerLabel, stockLocations[0]);
		}
		else if (nbCards0 < nbCards1) {
			setStatusText("Game over. Player 1 won with "
					+ nbCards1 + " cards");
			addActor(winnerLabel, stockLocations[1]);
		}
		else
			setStatusText("Game over. Tie. Both players have "
					+ nbCards0 + " cards");
	}

	private void initStocks() {
		for (int i = 0; i < nbPlayers; i++) {
			stocks[i] = new Hand(deck);
			stocks[i].setView(this, new StackLayout(stockLocations[i]));
		}
	}

	private int transferToWinner() {
		delay(1000);
		int nbWinner = 0;
		for (int i = 1; i < nbPlayers; i++)
			if (bids[i].getLast().getRankId() < bids[nbWinner].getLast().getRankId())
				nbWinner = i;
		transferToStock(nbWinner);
		return nbWinner;
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
	}

	public static void main(String[] args) {
		new BauernKrieg();
	}
}