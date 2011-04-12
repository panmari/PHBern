import ch.aplu.jcardgame.*;
import ch.aplu.jgamegrid.*;
import ch.aplu.util.Monitor;

public class BlackJackBasic extends CardGame {

	public enum Suit {

		SPADES, HEARTS, DIAMONDS, CLUBS
	}

	public enum Rank {

		ACE, KING, QUEEN, JACK, TEN, NINE, EIGHT, SEVEN, SIX, FIVE, FOUR, THREE, TWO
	}

	private Deck deck = new Deck(Suit.values(), Rank.values(), "cover");
	private Hand dealer = new Hand(deck);
	private Hand player = new Hand(deck);
	private Location upperLocation = new Location(300, 120);
	private Location bottomLocation = new Location(300, 470);
	private Location middleLocation = new Location(300, 300);
	private Actor textActor = new Actor("sprites/gameover.gif");
	private GGButton standButton = new GGButton("sprites/standBtn.gif", true);
	private GGButton restartButton = new GGButton("sprites/restartBtn.gif",
			true);
	private Hand talon;

	public BlackJackBasic() {
		super(600, 600, 30);
		setTitle("BlackJack - Basic Version");
		setStatusText("Dealing out. Please wait...");
		initButtons();
		while (true) {
			init();
			Monitor.putSleep();
		}
	}

	private void showHands() {
		RowLayout rowLayout = new RowLayout(bottomLocation, 350);
		player.setView(this, rowLayout);
		player.draw();
		RowLayout rowLayout1 = new RowLayout(upperLocation, 350);
		dealer.setView(this, rowLayout1);
		dealer.draw();
		talon.setVerso(true);
		talon.setView(this, new StackLayout(middleLocation));
		talon.draw();
	}

	private void init() {
		talon = deck.dealingOut(0, 0)[0];
		showHands();

		talon.setTouchEnabled(true);
		Card card = talon.getLast();
		card.setVerso(false);
		talon.transfer(card, player, true);
		card = talon.getLast();
		card.setVerso(true);
		talon.transfer(card, dealer, true);
		card = talon.getLast();
		card.setVerso(false);
		talon.transfer(card, player, true);
		card = talon.getLast();
		card.setVerso(false);
		talon.transfer(card, dealer, true);

		CardAdapter cardAdapter = new CardAdapter() {

			public void leftDoubleClicked(Card card) {
				card.setVerso(false);
				card.transfer(player, true);
				int value = getHandValue(player);
				setStatusText(value);
				if (value > 21) {
					gameOver("You bust! Your card values " + value
							+ " exceed 21  ---  Please click OK to restart.");
				}
			}
		};

		talon.addCardListener(cardAdapter);
		setStatusText(getHandValue(player));
		standButton.show();
	}

	private void initButtons() {
		restartButton.addButtonListener(new GGButtonAdapter() {

			public void buttonClicked(GGButton button) {
				cleanUp();
				restartButton.hide();
				Monitor.wakeUp();
			}
		});
		restartButton.hide();
		addActor(restartButton, new Location(300, 575));

		standButton.addButtonListener(new GGButtonAdapter() {

			public void buttonClicked(GGButton button) {
				dealersTurn();
				evaluateRound();
			}
		});
		standButton.hide();
		addActor(standButton, new Location(300, 575));
	}

	private void cleanUp() {
		textActor.removeSelf();
		talon.removeAll(false);
		player.removeAll(false);
		dealer.removeAll(false);
	}

	private void evaluateRound() {
		int dealerValue = getHandValue(dealer);
		int playerValue = getHandValue(player);
		String score = playerValue + " (you) versus " + dealerValue
				+ " (dealer)  ---  Please click OK to restart.";

		if (dealerValue > 21)
			gameOver("Dealer bust! His card values " + dealerValue
					+ " exceed 21  ---  Please click OK to restart.");
		else {
			if (playerValue > dealerValue)
				gameOver("You won! " + score);
			else
				gameOver("You lost! " + score);
		}
	}

	private void gameOver(String reason) {
		setStatusText(reason);
		talon.setTouchEnabled(false);
		addActor(textActor, new Location(300, 300));
		standButton.hide();
		restartButton.show();
	}

	private void dealersTurn() {
		dealer.setVerso(false); //show all cards in dealers hand
		delay(500);
		talon.setTargetArea(new TargetArea(dealer.getHandLocation()));
		while (getHandValue(dealer) < 17) {
			Card newCard = talon.getLast();
			newCard.transfer(dealer, true);
			newCard.setVerso(false);
		}
	}

	private int getHandValue(Hand hand) {
		int value = 0;
		for (Card card : hand.getCardList()) {
			switch ((Rank) card.getRank()) {
			case TWO:
				value += 2;
				break;
			case THREE:
				value += 3;
				break;
			case FOUR:
				value += 4;
				break;
			case FIVE:
				value += 5;
				break;
			case SIX:
				value += 6;
				break;
			case SEVEN:
				value += 7;
				break;
			case EIGHT:
				value += 8;
				break;
			case NINE:
				value += 9;
				break;
			case TEN:
				value += 10;
				break;
			case JACK:
				value += 10;
				break;
			case QUEEN:
				value += 10;
				break;
			case KING:
				value += 10;
				break;
			case ACE:
				if (value + 11 > 21)
					value += 1;
				else
					value += 11;
				break;
			}
		}
		return value;
	}

	private void setStatusText(int value) {
		setStatusText(value + " points in your hand.    "
				+ "Double-click card talon to Hit or press Stand.");
	}

	public static void main(String[] args) {
		new BlackJackBasic();
	}

}
