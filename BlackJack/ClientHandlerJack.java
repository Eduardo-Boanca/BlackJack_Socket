package BlackJack;

import java.io.*;
import java.net.*;
import java.text.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*
 * Eduardo Boanca
 * Calcolatrice Multithread
 */

public class ClientHandlerJack implements Runnable {

  private Socket client;
  private BufferedReader in;
  private PrintWriter out;
  private ArrayList<ClientHandlerJack> clients;
  public boolean faceDown; // Questo boolean serve per capire se la carta è girata o no
  public boolean dealerWon; // Questo boolena serve per capire se il mazziere ha vinto
  public volatile boolean roundOver; // this boolean value will tell the program if the round is over.
  ArrayList<Card> dealerHand; // this is the arraylist for the dealer's hand.
  ArrayList<Card> playerHand; // this is the arraylist for the player's hand.
  Deck deck; // we have our deck.
  private final int minPuntata = 1;
  private final int maxPuntata = 100;
  private int playerBet, guadagnoPlayer = 0;
  private boolean continuare = true;
  private static int wallet = 1000;
  private static int round = 0;

  public ClientHandlerJack(Socket clientSocket, ArrayList<ClientHandlerJack> clients) throws IOException {
    this.client = clientSocket;
    this.clients = clients;
    in = new BufferedReader(new InputStreamReader(client.getInputStream()));
    out = new PrintWriter(client.getOutputStream(), true);
  }

  @Override
  public void run() {
    try {
      Scanner in = new Scanner(socket.getInputStream());
      PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
      while (true) {
          // Handle new round
          if (round == 0) {
              // ask for bet
              out.println("Enter a bet between 1 and 100:");
              bet = Integer.parseInt(in.nextLine());
              if (bet > wallet) {
                  out.println("Bet exceeds wallet balance, please enter a valid bet");
                  continue;
              }
              wallet -= bet;
              deck.shuffle();
              hand.addCard(deck.deal());
              hand.addCard(deck.deal());
              out.println("Your hand: " + hand.toString());
              broadcast("New round started!");
              round++;
          }
          
          // Handle client input
          String input = in.nextLine();
          if (input.equalsIgnoreCase("hit")) {
              hand.addCard(deck.deal());
              out.println("Your hand: " + hand.toString());
              if (hand.getValue() > 21) {
                  out.println("You bust! Game over.");
                  broadcast("Player busts! Game over.");
                  round = 0;
              }
          } else if (input.equalsIgnoreCase("stand")) {
              broadcast("Player stands. Game over.");
              round = 0;
          }
      }
    } catch (Exception e) {
      System.err.println("Error from ClientHandler");
      e.printStackTrace();
    } finally {
      out.close();
    }
    try {
      in.close();
    } catch (IOException e) {
      System.err.println("Error from ClientHandler");
      e.printStackTrace();
    }
  }
}
