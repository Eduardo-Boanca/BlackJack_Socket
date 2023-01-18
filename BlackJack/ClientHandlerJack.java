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

  public ClientHandlerJack(Socket clientSocket, ArrayList<ClientHandlerJack> clients) throws IOException {
    this.client = clientSocket;
    this.clients = clients;
    in = new BufferedReader(new InputStreamReader(client.getInputStream()));
    out = new PrintWriter(client.getOutputStream(), true);
  }

  @Override
    public void run() {
        try {

            deck = new Deck();
            deck.shuffleDeck(); // we randomize the deck.
            dealerHand = new ArrayList<Card>();
            playerHand = new ArrayList<Card>();

            while(true) {
                String input = in.readLine();
                String move = input.split(";")[0];
                if(move.equals("hit")) {
                    for(int i = 0; i<2; i++) { //we add the first two cards on top of the deck to dealer's hand.
                    dealerHand.add(deck.getCard(i));
                  }
                  for(int i = 2; i<4; i++) { //we add the third and fourth card on top of the deck to the player's hand.
                    playerHand.add(deck.getCard(i));
                  }
                  for (int i = 0; i < 4; i++) { //we then remove these cards from the game. This way, we literally 'drew' the cards and those four cards are no longer in the deck.
                  deck.removeCard(0);
                }

                checkHand(dealerHand); //at the beginning of the game, we first check the hand of the dealer and the hand of the player to look for a blackjack. (ther can't be a bust)
                checkHand(playerHand);

                addCard(playerHand);
                } else if(move.equals("stand")) {
                    // End the player's turn
                    out.println("Player stands. Waiting for dealer's move...");
                    // start dealer's turn
                    while(getSumOfHand(dealerHand) < 17) {
                        // Draw a card and add it to the dealer's hand
                        dealerHand += getRandomCard();
                    }
                    out.println("Dealer's final hand is: " + dealerHand);
                    // Compare dealer's hand with player's hand to determine winner
                    if(getSumOfHand(playerHand) > getSumOfHand(dealerHand) || getSumOfHand(dealerHand) > 21) {
                        out.println("Player wins!");
                    } else if(getSumOfHand(playerHand) < getSumOfHand(dealerHand)) {
                        out.println("Dealer wins.");
                    } else {
                        out.println("It's a tie.");
                    }
                    break;
                } else {
                    out.println("Invalid move. Please enter 'hit' or 'stand'.");
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

  public void addCard(ArrayList<Card> hand) {//this method adds a card to the hand.
        hand.add(deck.getCard(0)); //gets a card from the deck to the hand.
        deck.removeCard(0); //removes the card from the deck.
        faceDown = true;
      }

  public boolean hasAceInHand(ArrayList<Card> hand) {//this method checks if the hand has ace.
        for (int i = 0; i < hand.size(); i++){ //we go through the hand that is given as a parameter and check for a card with a value of 11(Ace.)
          if(hand.get(i).getValue() == 11) {
            return true; //we return true if there is any.
          }
        }
        return false; //we return false if not.
      }

  public int aceCountInHand(ArrayList<Card> hand){//this method finds the total aces found in the hand. This is important for us to decide whether we will take ace's value as 1 or 11.
        int aceCount = 0; //we initialize an integer which will store the total ace count as 0.
        for (int i = 0; i < hand.size(); i++) { //we go through the hand.
          if(hand.get(i).getValue() == 11) { //each time we see a card with a value of 11,
            aceCount++; //we add one to the ace count.
          }
        }
        return aceCount; //we then return this ace count.   
      }

  public int getSumWithHighAce(ArrayList<Card> hand) {//this method gives the total value of the hand where the ace is counted as having a value of 11.
        int handSum = 0; //we initialize the integer in which the sum of hand is stored.
        for (int i = 0; i < hand.size(); i++){ //we go through the hand,
          handSum = handSum + hand.get(i).getValue(); //we add the values we encounter to the integer.
        }
        return handSum; //we return the integer.
      }

  // Metodo che controlla se ce un Blackjack oppure un "Bust"
  public void checkHand (ArrayList<Card> hand) {
        if (hand.equals(playerHand)) { //Controlla se il parametro è la mano del giocatore
          if(getSumOfHand(hand) == 21){ //Se è 21, il gioco finisce
            faceDown = false;
            dealerWon = false;
            rest();
            roundOver = true;
          }
          else if (getSumOfHand(hand) > 21) { //Se è maggiore di 21, il dealer ha vinto
            faceDown = false;;
            rest();
            roundOver = true;
          }
        }
        else { //Se non è la mano del giocatore, allora è quello del dealer
          if(getSumOfHand(hand) == 21) {
            faceDown = false;
            rest();
            roundOver = true;
          }
          else if (getSumOfHand(hand) > 21) {
            faceDown = false;
            dealerWon = false;
            rest();
            roundOver = true;
          }
        }
      }

      //Metodo che controlla la somma delle carte
  public int getSumOfHand (ArrayList<Card> hand) {
        if(hasAceInHand(hand)) { //Controlla se è un asso,
          if(getSumWithHighAce(hand) <= 21) {
            return getSumWithHighAce(hand); //we get the sum with the high ace case (taking aces as 11) if the total sum is smaller than 24 and return this sum.
          }
          else{
            //Se si prendono gli assi come 11 e il totale è oltre il 21, faccio calcoli per far contare l'asso come 1
            for (int i = 0; i < aceCountInHand(hand); i++) {
              //Per ogni asso nella mano, si sottrae
              int sumOfHand = getSumWithHighAce(hand)-(i+1)*10; //for each ace there is in hand, we subtract then to get 1 from 11.
              if(sumOfHand <= 21) {
                return sumOfHand; //we return this sum if it is smaller than 21.
              }
            }
          }
        }
        else { //if there is no ace in hand, we will directly go through the hand and sum up all of the values.
          int sumOfHand = 0;
          for (int i = 0; i < hand.size(); i++) {
            sumOfHand = sumOfHand + hand.get(i).getValue();
          }
          return sumOfHand;
        }
        return 22; //we basically set it to the 'bust' case if the method returns nothing up to this point.
      }

  public static void rest() {//this method sleeps the program. It basically serves as a time duration between events.
        try {
          Thread.sleep(500);//this sleeps the program for 1000 miliseconds which is equal to 1 second.
        }
        catch (InterruptedException e) {}
      }
}

  // public static boolean inputCheck(String str) {
  // for (String string : opzioni) {
  // if (str.equals(string))
  // return true;
  // }
  // return false;
  // }

  // public String checkMsg(String str) {
  // String err = "";

  // if (str.length() < 3) {
  // err = "505";
  // }
  // return err;
  // }

}
