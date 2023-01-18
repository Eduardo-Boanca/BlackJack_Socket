package BlackJack;
import java.util.ArrayList;
import java.util.Collections;

public class Deck {
  private ArrayList<Card> deck; // Array List che contiene le 52 carte

  public Deck() {
    deck = new ArrayList<Card>();

    for (int suitI = 0; suitI < 4; suitI++) { // Con questi for si scorre tra le 52 carte
      for (int rankJ = 0; rankJ < 13; rankJ++) {
        if (rankJ == 0) { // se il Rank è 0, è un asso è viene dato valore 11
          Card card = new Card(suitI, rankJ, 11); // Si crea una carta con il valore di suitI e RankJ che in questo caso
                                                  // ha il valore di 11 (asso)
          deck.add(card); // Si aggiunge la carta appena creata al mazzo
        } else if (rankJ >= 10) { // Se il rank è maggiore di 10 (Jack, regina re), in base alle regole,
                                  // prenderanno il valore di 10
          Card card = new Card(rankJ, rankJ, 10);
          deck.add(card); // Si aggiunge la carta appena creata al mazzo
        } else { // Si ripete il procedimento per ogni altra carta tranne per quelle già gestite
          Card card = new Card(suitI, rankJ, rankJ + 1); // when j is 1 (for example), we have a two of a suit and this
                                                         // has a value of two. So for the value, we increment j by one.
          deck.add(card); // Si aggiunge la carta appena creata al mazzo
        }
      }
    }
  }

  // Questo metodo mescola il mazzo
  public void shuffleDeck() {
    Collections.shuffle(deck);
  }

  // Ritorna il numero (indice) della carta nel mazzo
  public Card getCard(int i) {
    return deck.get(i);
  }

  // Rimuove il numero (indice) della carta nel mazzo
  public Card removeCard(int i) {
    return deck.remove(i);
  }

}
