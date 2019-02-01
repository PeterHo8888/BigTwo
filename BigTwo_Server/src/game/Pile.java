package game;

import java.util.ArrayList;


public class Pile
{
    public ArrayList<Card> cards = new ArrayList<Card>();

    public Pile()
    {

    }

    public ArrayList<Card> get()
    {
        return this.cards;
    }
    
    public void set(ArrayList<Card> cards)
    {
        this.cards = cards;
        
    }
    

}
