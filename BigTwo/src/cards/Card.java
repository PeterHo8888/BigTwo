package cards;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.ImageIcon;

import utilities.Coordinates;
import utilities.ImageUtil;

public class Card
{
    // Might want to make this conform with internal dimensions
    public final static int inCard_width  = 70;
    public final static int inCard_height = (int) (inCard_width / 2.5 * 3.5);

    public final static Coordinates card_dimensions = Coordinates
            .toScreen(inCard_width, inCard_height);

    public final static int card_width  = card_dimensions.getX();
    public final static int card_height = card_dimensions.getY();

    public static enum Type
    {
        DIAMOND, CLUB, HEART, SPADE
    };

    private int   value = -1;
    private Type  suit  = Type.SPADE;
    BufferedImage img   = null;

    public Card()
    {
        this(0, Type.SPADE);
    }

    public Card(int value, Type suit)
    {
        this.value = value;
        this.suit = suit;
        this.img = getImg(value, suit);
    }

    private BufferedImage getImg(int value, Type suit)
    {
        BufferedImage img = null;
        String s = "";
        switch (value) {
        case 0:
            s += "A";
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

        s += ".png";

        img = ImageUtil.toBufferedImage(
                new ImageIcon(getClass().getResource("/cards/" + s)));

        img = ImageUtil.getScaledImage(img, card_width, card_height);

        return img;
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

    public static int getHighSuit(ArrayList<Card> cards)
    {
        int high = -1;
        for (Card c : cards) {
            if (c.getSuit() > high)
                high = c.getSuit();
        }
        return high;
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

    public void draw(Graphics g, int x, int y)
    {
        g.drawImage(img, x, y, null);
    }
}
