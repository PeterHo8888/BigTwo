package game;

import java.util.ArrayList;

public class Card
{


    public static enum Type
    {
        DIAMOND, CLUB, HEART, SPADE
    };

    private int   value = -1;
    private Type  suit  = Type.SPADE;

    public Card()
    {
        this(0, Type.SPADE);
    }

    public Card(int value, Type suit)
    {
        this.value = value;
        this.suit = suit;
    }


    public String toString()
    {
        String s = "";
        switch (this.value) {
        case 0:
            s += "A";
            break;
        case 9:
        	s += "T";
        	break;
        case 10:
            s += "J";
            break;
        case 11:
            s += "Q";
            break;
        case 12:
            s += "K";
            break;
        default:
            s += Integer.toString(value + 1);
        }

        switch (suit) {
        case DIAMOND:
            s += "D";
            break;
        case CLUB:
            s += "C";
            break;
        case HEART:
            s += "H";
            break;
        case SPADE:
            s += "S";
            break;
        }
        
        return s;
    }

    public int getSuit()
    {
        return (suit == Type.DIAMOND ? 0
                : suit == Type.CLUB ? 1
                        : suit == Type.HEART ? 2 : suit == Type.SPADE ? 3 : -1);
        // return this.suit;
    }

    public int getVal()
    {
        if (this.value >= 2 && this.value <= 12) {
            return this.value - 2;
        } else {
            switch (this.value) {
            case 0:
                return 11;
            case 1:
                return 12;
            }
        }
        return -1;
    }
    
    public static ArrayList<Card> sort(ArrayList<Card> cards)
    {
        int min = 13;
        int pos = 0;
        for (int j = 0; j < cards.size(); ++j) {
            for (int i = j; i < cards.size(); ++i ) {
                if (cards.get(i).getVal() <= min) {
                    pos = i;
                    min = cards.get(i).getVal();
                }
            }
            Card a = cards.get(j);
            Card b = cards.get(pos);
            cards.set(j, b);
            cards.set(pos, a);
            min = 13;
            pos = 0;
        }
        return cards;
    }
    
}
