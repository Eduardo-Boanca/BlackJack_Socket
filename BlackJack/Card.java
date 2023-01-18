package BlackJack;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import javax.imageio.ImageIO;
import java.io.IOException;
import java.awt.Graphics2D;

public class Card {
    private int suit; // la suite delle carte (Fiori[0], Quadri[1], Cuori[2], Picche[3])
    private int rank; // il rank delle carte (Asso[0], 2[1], 3[2], 4[3], 5[4], 6[5], 7[6], 8[7], 9[8],
                      // 10[9], Jack[10], Regina[11], or Re[12])
    private int value;
    private int xCardPosition;
    private int yCardPosition;

    public Card() {
        suit = 0;
        rank = 0;
        value = 0;
    }

    public Card(int s, int r, int v) { // a constructor of Card that initializes the main attributes
        suit = s;
        rank = r;
        value = v;
    }

    // ritorna la suit della carta
    public int getSuit() {
        return suit;
    }

    // ritorna il rank delle carte
    public int getRank() {
        return rank;
    }

    // ritorna il valore delle carte
    public int getValue() {
        return value;
    }

    public void drawCard(Graphics2D brush, boolean turnoMazziere, boolean cartaCoperta, int cardNum)
            throws IOException {
        BufferedImage deckImg = ImageIO.read(new File("images/deckImg.png")); // Per leggere l'immagine delle carte

        int imgWidth = 950; // La larghezza dell'immagine in pixel
        int imgHeight = 392; // Altezza immagine in pixel

        // Un array bidimensionale per salvare la foto di ogni singola carta
        // l'immagine e composta da 4 righe e 13 colonne
        BufferedImage[][] cardPictures = new BufferedImage[4][13];

        // Immagine del retro della carta (per il mazziere)
        BufferedImage cartaGirata = ImageIO.read(new File("images/cartaGirata.jpg"));

         // For per assegnare le singole carte all'array bidimensionale
         // Il metodo getSubimage() serve per estrarre parti di un'immagine
        for (int column = 0; column < 4; column++) {
            for (int row = 0; row < 13; row++) {
                cardPictures[column][row] = deckImg.getSubimage(row * imgWidth / 13, column * imgHeight / 4, imgWidth / 13, imgHeight / 4);
            }
        }

        if(turnoMazziere) {
            yCardPosition = 75; // Se è il turno del mazziere, l'immagine è stampata nella zona della GUI del mazziere
        } else {
            xCardPosition = 400; // Se è il turno del giocatore, l'immagine è stampata nella zona della GUI del giocatore
        }
        // Volendo mettere le carte una di fianco all'altra, sposto le carte ogni volta per fare spazio alla nuova carta
        xCardPosition = 500 + 75 * cardNum; 

        if (cartaCoperta) {
            brush.drawImage(cartaGirata, xCardPosition, yCardPosition, null);
        } else {
            brush.drawImage(cardPictures[suit][rank], xCardPosition, yCardPosition, null);
        }
    }
}
