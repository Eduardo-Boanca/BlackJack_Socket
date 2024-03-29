package BlackJack;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

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

          g2.drawImage(backgroundImage, 0, 0, null);
          g2.drawImage(logo, 510, 200, null);
          g2.drawImage(chip, 50, 300, null);
          g2.setColor(Color.WHITE);
          g2.setFont(new Font("Arial", Font.BOLD, 30));
          g2.drawString("DEALER", 515, 50);
          g2.drawString("PLAYER", 515, 380);
          g2.drawString("DEALER WON: ", 50, 100);
          g2.drawString(Integer.toString(dealerScore), 300, 100); //Disegna il punteggio del mazziere dopo averlo trasformato da intero a stringa
          g2.drawString("PLAYER WON: ", 50, 150);
          g2.drawString(Integer.toString(playerScore), 300, 150); //Disegna il punteggio del giocatore dopo averlo trasformato da intero a stringa
          g2.setFont(new Font("Arial", Font.BOLD, 15));
          g2.drawString("Prima di iniziare ogni round", 50, 250);
          g2.drawString("Devi scommetere una quantita cliccando la fiche", 50, 270);
          g2.setFont(new Font("Arial", Font.BOLD, 20));
          g2.drawString("BILANCIO CORRENTE: " + currentBalance, 50, 570);

          
          try { //we need to have the try and catch blocks here because the method printCard of a Card object draws images chopped off a spritesheet image from a 2D-array.
            for (int i = 0; i < dealerHand.size(); i++) {//we go through dealer's hand
              if (i == 0) { //if it is the first card,
                if(faceDown) { //we check if will be facedown or not.
                  dealerHand.get(i).drawCard(g2, true, true, i); //we then draw each individual card.
                }
                else {
                  dealerHand.get(i).drawCard(g2, true, false, i); //if it is not face down, we write the 3rd parameter as false and then draw each individual card in the hand again.
                }
              }
              else {
                dealerHand.get(i).drawCard(g2, true, false, i); //if it is not the first card, we write the 3rd parameter as false and then draw each individual card in the hand again.
              }
            }
          }
          catch (IOException e) {}
      
          try {
            for (int i = 0; i < playerHand.size(); i++) { //we do the same thing for the user hand with a foor loop again: we go through each of the cards in user's hand.
              playerHand.get(i).drawCard(g2, false, false, i); //we then draw each of the card on the component(screen). Extra information about parameters can be found in the Card class.
            }
          }
          catch (IOException e) {}
    }

    public void refresh(int cB, int uS, int dS, boolean fD) { //this refresh method will refresh the GameComponent when it is called.
      currentBalance = cB;
      playerScore = uS;
      dealerScore = dS;
      faceDown = fD;
      this.repaint();
    }
  
    public void mousePressed(MouseEvent e) {//in this void method, we will control mouse events of the user.
      int mouseX = e.getX(); //we first get the x and y coordinates of the user's mouse's current position.
      int mouseY = e.getY();
  
      if(mouseX>= 50 && mouseX<=200 && mouseY>=300 && mouseY<=450) {//we will only do something if the x and y coordinates fall on top of the chip image. The coordinates you see below give the end points of the chip image.
        betMade = true; //if the user clicks on the chip image, then the bet is made.
        String[] options = new String[] {"1", "5", "10", "25", "100"}; //we declare an array of options that will be given when the user clicks the chip and and JOptionPane dialog comes up.
        int response = JOptionPane.showOptionDialog(null, "Please enter your betting amount!", "PUNTATA", //This is the dialog that will popup when user clicks the chip.
                JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
        if(response == 0) {//if the user selects the first response, he selected the bet as 1.
          currentBet = 1; //we assign 1 to the current bet.
          currentBalance -= 1; //we decrement the current balance by the current bet.
        }
        else if(response == 1) {//the same thing goes on like this. The second respoonse is 5 so the bet is 5 and it is decreased from the current balance.
          currentBet = 5;
          currentBalance -= 5;
        }
        else if(response == 2) {
          currentBet = 10;
          currentBalance -= 10;
        }
        else if(response == 3) {
          currentBet = 25;
          currentBalance -= 25;
        }
        else if(response == 4) {
          currentBet = 100;
          currentBalance -= 100;
        }
        else { //if none of the options are selected, and if the user closes the option dialog without selecting, then we assign the bet as 1 because this is our default value.
          currentBet = 1;
          currentBalance -= 1;
          System.out.println("You haven't selected a proper bet. Thus, the bet is taken as 1."); //we inform the reader from console that we have selected bet as 1 because it is the default bet.
        }
  
        System.out.println("You have made your bet: " + currentBet + "." + " If you beat the dealer, you will increase your current balance by " + currentBet*2 +
                "; if the dealer beats you you will decrease your current balance by " + currentBet + "."); //each time the player bets, we inform the consequences from the console.
        Tester.newGame.startGame(); //then we start the game.
      }
    

    @Override
    public void mouseClicked(MouseEvent e) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void mouseExited(MouseEvent e) {
        // TODO Auto-generated method stub
        
    }
    
}

