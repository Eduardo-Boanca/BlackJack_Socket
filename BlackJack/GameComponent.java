package BlackJack;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class GameComponent extends JComponent implements MouseListener {
    public BufferedImage backgroundImage;
    public BufferedImage logo;
    public BufferedImage chip;
    private ArrayList<Card> dealerHand;
    private ArrayList<Card> playerHand;
    private int dealerScore;
    private int playerScore;
    public boolean faceDown = true;
    public static boolean betMade = false; // Server per dire al programma se il giocatore ha piazzato la scommessa
    private int currentBalance;
    public static int currentBet; // Contine il valore attuale della scommessa

    public GameComponent(ArrayList<Card> dealerH, ArrayList<Card> playerH) {
        dealerHand = dealerH;
        playerHand = playerH;
        dealerScore = 0;
        playerScore = 0;
        currentBalance = 1000;
        addMouseListener(this);// Si aggiunge il mouseListener al component
    }

    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;

        try {
            backgroundImage = ImageIO.read(new File("images/background.png")); //we read a file which is the png image of a poker table for our background image.
            logo = ImageIO.read(new File("images/blackjackLogo.png")); //we read a file which is the png image of a blackjack logo for the logo on the poker table.
            chip = ImageIO.read(new File("images/chip.png")); //we read a file which is the png image of a poker chip for the chip on the poker table.
          }
          catch(IOException e) {}
    }
    
}

