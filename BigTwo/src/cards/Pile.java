package cards;

import java.awt.Graphics;
import java.util.ArrayList;

import main.Screen;
import utilities.Coordinates;

public class Pile
{
    public ArrayList<Card> cards = new ArrayList<Card>();
    
    int internal_pos[][] = new int[5][5];

    public Pile()
    {

    }

    public ArrayList<Card> get()
    {
        return this.cards;
    }
    
    public void set(ArrayList<Card> cards)
    {
        System.out.println("set: Cards: " + cards);
        this.cards = cards;
    }
    
    public void physics()
    {
        int numCards = this.cards.size();
        int offset = Screen.myWidth / 2 - numCards * 75 / 2;
        setSpacing(offset, 75);
    }
    
    public void setSpacing(int offset, int spacing)
    {
        int x = 0;
        for (int i = 0; i < cards.size(); ++i) {
            internal_pos[i][0] = x + offset;
            internal_pos[i][1] = (int)(Screen.myHeight / 2.0 - Card.inCard_height / 2.0);

            x += spacing;
        }
    }
    
    Coordinates pos[] = new Coordinates[5];
    // draw the last play in center
    public void draw(Graphics g)
    {
        for (int i = 0; i < cards.size(); ++i) {
            pos[i] = Coordinates.toScreen(internal_pos[i][0],
                    internal_pos[i][1]);

            cards.get(i).draw(g, pos[i].getX(), pos[i].getY());

        }
    }
}
