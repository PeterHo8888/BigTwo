package game;

import java.util.ArrayList;

import game.Card.Type;

public class Hand
{
    public ArrayList<Card> cards = new ArrayList<Card>();
    
    public Hand(Deck deck)
    {
        for (int i = 0; i < 13; ++i) {
            cards.add(deck.pop());
        }
    }
    
    public Hand()
    {
        
    }
    
    public Card getLowest()
    {
        Card c = new Card(1, Type.SPADE);
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
    
    public int getLowestIndex()
    {
        int low = -1;
        Card c = new Card(1, Type.SPADE);
        for (int i = 0; i < cards.size(); ++i) {
            if (cards.get(i).getVal() < c.getVal())  {
                c = cards.get(i);
                low = i;
            }
            else if (cards.get(i).getVal() > c.getVal()) {
                continue;
            }
            else if (cards.get(i).getVal() == c.getVal()) {
                if (cards.get(i).getSuit() < c.getSuit()) {
                    c = cards.get(i);
                    low = i;
                } else {
                    continue;
                }
            }
        }
        return low;
    }
    
}
