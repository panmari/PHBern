package tcpBauernKrieg1;

// Command.java
// Cycle 7: Add blinking player's name
// Solved

public interface Command
{
  int REPORT_NAMES = 0;
  int READY_FOR_TALON = 1;
  int DISCONNECT = 2;
  int DECK_DATA = 3;
  int READY_TO_PLAY = 4;
  int TRANSFER_TO_BID_OF_PLAYER = 5;
  int TRANSFER_TO_STOCK = 6;
  int MY_TURN = 7;
  int OTHER_TURN = 8;
  int DROP_OUT = 9;
}
