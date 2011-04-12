// BlackJack.java

// Adapted by AP:

// Card to dealer in verso, improved, see //1
// No rollover because button is changed, see //2
// setRefreshEnabled(false) (refresh is done by simulation cycle), see //3
// okBtn: 30 pixel button, only 2 sprite images (not 3), see //4
// Instead of background text, use text actor
//   (getBg.clear() removes all actors, is somewhat dangerous), see //5

import ch.aplu.jcardgame.*;
import ch.aplu.jgamegrid.*;
import ch.aplu.util.Monitor;
import java.awt.*;

public class BlackJack24MAR extends CardGame
{
  public enum Suit
  {
    SPADES, HEARTS, DIAMONDS, CLUBS
  }

  public enum Rank
  {
    ACE, KING, QUEEN, JACK, TEN, NINE, EIGHT, SEVEN, SIX, FIVE, FOUR, THREE, TWO
  }
  private final int START_MONEY = 100;
  private Deck deck = new Deck(Suit.values(), Rank.values(), "cover");
  private Hand dealer = new Hand(deck);
  private Hand player = new Hand(deck);
  private Location upperLocation = new Location(300, 120);
  private Location bottomLocation = new Location(300, 470);
  private Location middleLocation = new Location(300, 300);
  private Actor textActor = new Actor("sprites/gameover.gif");
  private GGButton standButton = new GGButton("sprites/standBtn.gif");  //2
  private GGButton restartButton = new GGButton("sprites/restartBtn.gif");  //2
  private Hand talon;
  private int currentBet = 0, funds = START_MONEY;
  private TextActor currentBets =  null;
  private TextActor currentFunds = null;

  //TODO: Implement "BlackJack": Ace + 10 is best way to get 21 points
  //TODO: hide toolBar after setting bet.
  //TODO: Fatal Error, why?
  //TODO: rightclick for counting downwards?

  /*
   * Changelog:
   *  Implemented bets!
   *  Cards are now transfered as verso, then only shown in hand.
   *  
   * 
   */
  public BlackJack24MAR()
  {
    super(600, 600, 30);
    setTitle("BlackJack - Extended Version");
    setStatusText("Dealing out. Please wait...");
    showBet();  //5
    initButtons();
    initToolBar();
    while (true)
    {
      init();
      Monitor.putSleep();
    }
  }

  private void showHands()
  {
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

  private void init()
  {
    talon = deck.dealingOut(0, 0)[0];
    showHands();

    Card card = talon.getLast();
    talon.transfer(card, player, true);
    card.setVerso(false);
    card = talon.getLast();
    card.setVerso(true); //it's verso anyway
    talon.transfer(card, dealer, true);
    card = talon.getLast();
    talon.transfer(card, player, true);
    card.setVerso(false);
    card = talon.getLast();
    talon.transfer(card, dealer, true);
    card.setVerso(false);

    CardAdapter cardAdapter = new CardAdapter()
    {
      public void leftDoubleClicked(Card card)
      {
        card.transfer(player, true);
        card.setVerso(false);
        int value = getHandValue(player);
        setStatusText(value);
        if (value > 21)
        {
          gameOver("You bust! Your card values " + value
            + " exceed 21  ---  Please click to restart.", -currentBet);
        }
      }
    };

    talon.addCardListener(cardAdapter);
    setStatusText(getHandValue(player));
  }

  private void initButtons()
  {
    restartButton.addButtonListener(new GGButtonAdapter()
    {
      public void buttonClicked(GGButton button)
      {
        cleanUp();
        restartButton.hide();
        Monitor.wakeUp();
      }
    });
    restartButton.hide();
    addActor(restartButton, new Location(300, 575));
    restartButton.setRefreshEnabled(false);  //3

    standButton.addButtonListener(new GGButtonAdapter()
    {
      public void buttonClicked(GGButton button)
      {
        dealersTurn();
        evaluateRound();
      }
    });
    standButton.hide();
    addActor(standButton, new Location(300, 575));
    standButton.setRefreshEnabled(false); //3
  }

  private void cleanUp()
  {
    textActor.removeSelf();
    talon.removeAll(false);
    player.removeAll(false);
    dealer.removeAll(false);
    currentBet = 0;
    showBet();
  }

  private void evaluateRound()
  {
    int dealerValue = getHandValue(dealer);
    int playerValue = getHandValue(player);
    String score = playerValue + " (you) versus "
      + dealerValue + " (dealer)  ---  Please click to restart.";

    if (dealerValue > 21)
      gameOver("Dealer bust! His card values " + dealerValue
        + " exceed 21  ---  Please click to restart.", currentBet);
    else
    {
      if (playerValue > dealerValue)
        gameOver("You won! " + score, currentBet);
      if (playerValue == dealerValue)
        gameOver("Tie! " + score, 0);
      else
        gameOver("You lost! " + score, -currentBet);
    }
  }

  private void gameOver(String reason, int amount)
  {
    funds += amount;
    setStatusText(reason);
    talon.setTouchEnabled(false);
    addActor(textActor, new Location(300, 300));
    standButton.hide();
    restartButton.show();
  }

  private void dealersTurn()
  {
    //1
    talon.setTargetArea(new TargetArea(dealer.getHandLocation()));
    dealer.setVerso(false); //show all cards in dealers hand
    while (getHandValue(dealer) < 17)
    {
      Card newCard = talon.getLast();
//1      newCard.setVerso(true);
      newCard.transfer(dealer, true);
      newCard.setVerso(false);
    }
  }

  private int getHandValue(Hand hand)
  {
    int value = 0;
    for (Card card : hand.getCardList())
    {
      switch ((Rank)card.getRank())
      {
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

  private void setStatusText(int value)
  {
    if (currentBet == 0)
      setStatusText(value + " points in your hand.    "
        + "Place a bet.");
    else
      setStatusText(value + " points in your hand.    "
        + "Double-click card talon to Hit or press Stand.");
  }

  private void showBet()
  {
    if (currentBets != null)
      currentBets.removeSelf();
    if (currentFunds != null)
      currentFunds.removeSelf();
    if (currentBet != 0)
    {
      currentBets = new TextActor("Bet: " + currentBet, Color.white, getBgColor(), new
        Font("Arial", Font.BOLD, 16));
      addActor(currentBets, new Location(470, 330));
    }
    currentFunds = new TextActor("Funds: " + funds, Color.white, getBgColor(), new
        Font("Arial", Font.BOLD, 16));
    addActor(currentFunds, new Location(470, 300));
    
  }

  private void initToolBar()
  {
    final ToolBarStack number1 =
      new ToolBarStack("sprites/number30.gif", 10);
    final ToolBarStack number10 =
      new ToolBarStack("sprites/number30.gif", 10);
    ToolBarSeparator separator =
      new ToolBarSeparator(2, 30, Color.black);
    final ToolBarItem okBtn =
      new ToolBarItem("sprites/okBtn30.gif", 2);  //4
    ToolBar toolBar = new ToolBar(this);
    toolBar.addItem(number10, number1, separator, okBtn);
    toolBar.show(new Location(10, 300));

    toolBar.addToolBarListener(new ToolBarAdapter()
    {
      public void leftPressed(ToolBarItem item)
      {
        if (item == okBtn)
        {
          okBtn.show(1);
          int bet = number10.getItemId() * 10 + number1.getItemId();
          if (bet != 0)
          {
            currentBet = bet;
            standButton.show();
            talon.setTouchEnabled(true);
            setStatusText(getHandValue(player));
            showBet();  //5
          }
        }
        else
          ((ToolBarStack)item).showNext();
      }
      /*      public void rightPressed(ToolBarItem item)
      {
      if (item == number1 || item == number10) {
      ToolBarStack numberStack = ((ToolBarStack)item);
      if (numberStack.n)
      numberStack.showPreviousSprite();
      }
      }
       */

      public void leftReleased(ToolBarItem item)
      {
        if (item == okBtn)
          item.show(0);
      }
    });
  }

  public static void main(String[] args)
  {
    new BlackJack24MAR();
  }
}
