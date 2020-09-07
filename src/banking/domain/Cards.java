package banking.domain;

import java.util.ArrayList;

public class Cards {
    private ArrayList<CreditCard> cards;

    public Cards() {
        this.cards = new ArrayList<>();
    }

    public ArrayList<CreditCard> getCards() {
        return this.cards;
    }

    public void addCard(CreditCard card) {
        this.cards.add(card);
    }
    public void clearCards(){
        this.cards.clear();
    }

}
