package BlackJack;

import java.util.ArrayList;

public class Hand {

    private ArrayList<Card> cards;

    public Hand() {
        cards = new ArrayList<Card>();
    }

    public void addCard(Card card) {
        cards.add(card);
    }

    public int getValue() {
        int value = 0;
        int aceCount = 0;
        for (Card card : cards) {
            value += card.getValue();
            if (card.getRank() == 0) {
                aceCount++;
            }
        }
        // Handle aces
        while (value > 21 && aceCount > 0) {
            value -= 10;
            aceCount--;
        }
        return value;
    }

    public boolean isBlackjack() {
        if (cards.size() != 2) {
            return false;
        }
        return (cards.get(0).getValue() + cards.get(1).getValue()) == 21;
    }
}
