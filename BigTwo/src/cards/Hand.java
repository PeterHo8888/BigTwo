package cards;

import java.util.ArrayList;


public class Hand
{
    public ArrayList<Card> cards = new ArrayList<Card>();
    
    public Hand(Deck deck)
    {
        for (int i = 0; i < 13; ++i) {
            cards.add(deck.pop());
        }
    }
    
    public Hand(ArrayList<Card> cards) {
    	this.cards = cards;
    }
    
    public Card getLowest()
    {
        Card c = new Card(1, Card.Type.SPADE);
        for (int i = 0; i < cards.size(); ++i) {
            //System.out.println("Hand: " + cards);
            //System.out.println("Lowest card: " + c);
            if (cards.get(i).getVal() < c.getVal())  {
                c = cards.get(i);
            }
            else if (cards.get(i).getVal() > c.getVal()) {
                continue;
            }
            else if (cards.get(i).getVal() == c.getVal()) {
                if (cards.get(i).getSuit() < c.getSuit()) {
                    c = cards.get(i);
                } else {
                    continue;
                }
            }
        }
        return c;
    }
}
