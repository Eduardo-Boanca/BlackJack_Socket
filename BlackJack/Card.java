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

    public Card() {
        suit = 0;
        rank = 0;
        value = 0;
    }

    public Card(int s, int r, int v) {
        suit = s;
        rank = r;
        value = r + 1;
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
}
