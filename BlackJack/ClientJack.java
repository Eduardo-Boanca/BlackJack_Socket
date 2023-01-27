package BlackJack;

import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import javax.swing.*;

public class ClientJack extends JFrame {
    public static JFrame menuFrame = new JFrame(); // This is the frame which we will show when the user opens the game.
                                                   // It will contain basic options like 'Play' and 'Exit'.
    public static JFrame gameFrame = new JFrame(); // This is the frame in which the real blackjack game will be played.

    private static int playerScore = 0; // we have the player score, which starts as 0.
    private static int dealerScore = 0; // we have the dealer score, which starts as 0.
    public static int currentBalance = 1000; // we have the balance, which starts with 1000.

    public static Game newGame = new Game(gameFrame); // we initialize a 'Game' in order to control, start, and
                                                      // calculate the blackjack game.
    private static boolean isFirstTime = true; // this boolean value will check if the game is newly started for the
                                               // first time.

    public static enum STATE { // This enum represents the state of the game which is either menu or game.
                               // While it is menu, we will show the user the menu. While it is game, we will
                               // show the user the game.
        MENU,
        GAME
    };

    private JTextArea displayArea;
    private JTextField inputField;
    private JButton hitButton;
    private JButton standButton;
    private Socket socket;
    private BufferedReader serverIn;
    private PrintWriter serverOut;

    public ClientJack() {

    }

    public void connectToServer() {
        try {
            socket = new Socket("localhost", 1234);
            serverIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            serverOut = new PrintWriter(socket.getOutputStream(), true);
            displayArea.append("Connected to server\n");

            Thread t = new Thread(new ServerListener());
            t.start();
        } catch (IOException e) {
            System.out.println("Error: " + e);
        }
    }

    public static void main(String[] args) {
        ClientJack client = new ClientJack();
        client.connectToServer();
    }

    private class ServerListener implements Runnable {
        public void run() {
            try {
                while (true) {
                    String serverMessage = serverIn.readLine();
                    displayArea.append(serverMessage + "\n");
                }
            } catch (IOException e) {
                System.out.println("Error: " + e);
            }
        }
    }
}
