package vechio;
import java.io.*;
import java.net.*;
import java.util.*;

public class BlackjackServerThread extends Thread {
    private ServerSocket serverSocket;
    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;

    private String playerHand;
    private String dealerHand;
    private String result;

    private Random random;

    private List<String> cards;

    public BlackjackServerThread(Socket socket, ArrayList<ObjectOutputStream> outputStreams, Random random2) throws IOException {
        serverSocket = new ServerSocket(5000);
        playerHand = "";
        dealerHand = "";
        result = "";
        random = new Random();
        cards = new ArrayList<>();
    }

    @Override
    public void run() {
        try {
            clientSocket = serverSocket.accept();
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                if (inputLine.equals("HIT")) {
                    // hit the player
                    playerHand = hit(playerHand);
                    out.println("PLAYER " + playerHand);
                } else if (inputLine.equals("STAND")) {
                    // stand the player and hit the dealer
                    dealerHand = hit(dealerHand);
                    out.println("DEALER " + dealerHand);
                    // determine the result of the game
                    result = determineResult(playerHand, dealerHand);
                    out.println("RESULT " + result);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private int parseHand(String hand) {
        // code to parse the hand and determine the total value of the cards in the hand
        String[] cards = hand.split(" ");
        int total = 0;
        int numAces = 0;
        for (String card : cards) {
          String[] parts = card.split(" of ");
          String rank = parts[0];
          int value = 0;
          if (rank.equals("Ace")) {
            numAces++;
            value = 11;
          } else if (rank.equals("Jack") || rank.equals("Queen") || rank.equals("King")) {
            value = 10;
          } else {
            value = Integer.parseInt(rank);
          }
          total += value;
        }
        // if the total is over 21, try changing the value of an ace from 11 to 1
        while (total > 21 && numAces > 0) {
          total -= 10;
          numAces--;
        }
        return total;
      }
      

    private String hit(String hand) {
        // code to deal a card to the given hand
        String rank = getRandomRank();
        String suit = getRandomSuit();
        String card = rank + " of " + suit;
        if (cards.contains(card)) {
            // card has already been dealt, try again
            return hit(hand);
        } else {
            cards.add(card);
            hand += card + " ";
            return hand;
        }
    }

    private String getRandomRank() {
        // code to generate a random rank
        int rank = random.nextInt(13) + 1;
        switch (rank) {
            case 1:
                return "Ace";
            case 2:
                return "Two";
            case 3:
                return "Three";
            case 4:
                return "Four";
            case 5:
                return "Five";
            case 6:
                return "Six";
            case 7:
                return "Seven";
            case 8:
                return "Eight";
            case 9:
                return "Nine";
            case 10:
                return "Ten";
            case 11:
                return "Jack";
            case 12:
                return "Queen";
            case 13:
                return "King";
            default:
                return "";
        }
    }

    private String getRandomSuit() {
        // code to generate a random suit
        int suit = random.nextInt(4) + 1;
        switch (suit) {
            case 1:
                return "Spades";
            case 2:
                return "Hearts";
            case 3:
                return "Clubs";
            case 4:
                return "Diamonds";
            default:
                return "";
        }
    }

    private String determineResult(String playerHand, String dealerHand) {
        // code to determine the result of the game based on the player's and dealer's
        // hands
        int playerTotal = parseHand(playerHand);
        int dealerTotal = parseHand(dealerHand);
        if (playerTotal == 21 && dealerTotal != 21) {
            return "Blackjack! You win with a hand of 21!";
        } else if (dealerTotal == 21 && playerTotal != 21) {
            return "Dealer gets a blackjack. Dealer wins.";
        } else if (playerTotal > 21) {
            return "You bust! Dealer wins.";
        } else if (dealerTotal > 21) {
            return "Dealer busts! You win with a hand of " + playerTotal + "!";
        } else if (playerTotal > dealerTotal) {
            return "You win with a hand of " + playerTotal + "!";
        } else if (dealerTotal > playerTotal) {
            return "Dealer wins with a hand of " + dealerTotal + ".";
        } else {
            return "It's a tie. Both hands have a total of " + playerTotal + ".";
        }
    }

}
