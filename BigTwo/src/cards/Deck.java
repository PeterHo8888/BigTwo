package cards;

import java.util.ArrayList;

public class Deck
{

    private static ArrayList<Card> deck = new ArrayList<Card>();
    
    public Deck()
    {
        populate();
        shuffle();
    }
    
    private void shuffle()
    {
        for (int i = 0; i < 1240; ++i) {
            int j = (int)(Math.random() * 52);
            int k = (int)(Math.random() * 52);
            Card a = deck.get(j);
            Card b = deck.get(k);
            deck.set(j, b);
            deck.set(k, a);
        }
    }
    
    public Card pop()
    {
        Card c = deck.get(0);
        deck.remove(0);
        return c;
    }
    
    private void populate()
    {
        for (Card.Type t : Card.Type.values()) {
            for (int i = 0 ; i < 13; ++i) {
                deck.add(new Card(i, t));
            }
        }
    }
    
    public String toString()
    {
        return deck.toString();
    }
}
