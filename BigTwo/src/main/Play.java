package main;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.ImageIcon;

import cards.Card;
import cards.Pile;
import utilities.Coordinates;
import utilities.ImageUtil;

public class Play
{
    BufferedImage go  = null;
    Coordinates   dim = Coordinates.toScreen(75, 75);

    private boolean okRemove = false;
    private Player  player;
    private Pile    pile;

    private boolean myTurn   = false;
    public boolean  newStart = false;
    public boolean  won      = false;
    public boolean  lost     = false;
    /*
     * 
     * This Class is responsible for communication with the server.
     * 
     * Server sends list of cards which will get translated here to become
     * Player's hand
     * 
     * 
     */

    public Play(Player player, Pile pile)
    {
        this.player = player;
        this.pile = pile;
        go = ImageUtil.toBufferedImage(
                new ImageIcon(getClass().getResource("/go.png")));
        go = ImageUtil.getScaledImage(go, dim.getX(), dim.getY());

        pile.physics();
        System.out.println("Pile: " + pile.get());
    }

    public Play(Player player)
    {
        this.player = player;
        go = ImageUtil.toBufferedImage(
                new ImageIcon(getClass().getResource("/go.png")));
        go = ImageUtil.getScaledImage(go, dim.getX(), dim.getY());
    }

    public void setPile(Pile pile)
    {
        this.pile = pile;
    }

    public void setTurn(boolean b)
    {
        this.myTurn = b;
    }

    public void physics()
    {
        if (myTurn) {
            ArrayList<Card> cards = player.getCards();
            ArrayList<Card> p = pile.get();

            cards = Card.sort(cards);

            if (p.size() != 0 && cards.size() == 0 && !newStart) {
                okRemove = false;
                Screen.room.gs.send("skip");
            }

            Type t = getType(cards);

            if (newStart && t != Type.NONE) {
                okRemove = true;
                Screen.room.gs.send(cards.toString());
                newStart = false;
            }

            else if (cards.size() == p.size()) {
                if (t == getType(p) && compVal(cards, p, t) == 0) {
                    okRemove = true;
                    // pile.set(cards);
                    System.out.println(cards);
                } else {
                    System.out.println("Type : " + t);
                    okRemove = false;
                    System.out.println("Illegal");
                }

            } else if (p.size() == 0 && t == Type.ONE) {
                if (player.hand.getLowest().getVal() == cards.get(0).getVal()
                        && player.hand.getLowest().getSuit() == cards.get(0)
                                .getSuit()) {
                    okRemove = true;
                    Screen.room.gs.send(cards.toString());
                    System.out.println(cards);
                }
            } else {
                okRemove = false;
                System.out.println("Illegal, wrong amount of cards");
            }
            pile.physics();
        }
    }

    private int compVal(ArrayList<Card> c, ArrayList<Card> p, Type t)
    {
        switch (t) {
        /*
         * Possibilities: - Same value, different suit - Different value
         */
        case ONE:
            if (c.get(0).getVal() < p.get(0).getVal()) {
                return 1;
            } else if (c.get(0).getVal() > p.get(0).getVal()) {
                return 0;
            }
            // same val
            if (c.get(0).getSuit() < p.get(0).getSuit()) {
                return 1;
            } else {
                return 0;
            }
            /*
             * Possibilities: - Different value - Same value different suit -
             * Highest suit takes precedence
             */
        case TWO:
        case THREE:
            // Different value
            if (c.get(0).getVal() < p.get(0).getVal())
                return 1;
            else if (c.get(0).getVal() > p.get(0).getVal())
                return 0;
            // Same value take highest suit
            // never taken by three of a kind
            else if (c.get(0).getVal() == p.get(0).getVal()) {
                if (Card.getHighSuit(c) < Card.getHighSuit(p)) {
                    return 1;
                } else {
                    return 0;
                }
            }
        case FOUR:
            return -1; // no such thing as 4 card hand

        case STRAIGHT:
        case FLUSH:
        case STRAIGHTFLUSH:
            if (c.get(4).getVal() == p.get(4).getVal()) {
                if (Card.getHighSuit(c) < Card.getHighSuit(p)) {
                    return 1;
                } else {
                    return 0;
                }
            } else if (c.get(4).getVal() < p.get(4).getVal()) {
                return 1;
            } else {
                return 0;
            }

        case FULLHOUSE:
        case FOURONE:
            // Two people can't have same set of 3 or 4
            if (c.get(0).getVal() < p.get(0).getVal()) {
                return 1;
            } else {
                return 0;
            }

        default: // Type.NONE
            break;
        }
        return -1;
    }

    private enum Type
    {
        NONE, ONE, TWO, THREE, FOUR, STRAIGHT, FLUSH, FULLHOUSE, FOURONE, STRAIGHTFLUSH
    };

    private boolean high = false;

    private Type getType(ArrayList<Card> cards)
    {
        if (cards.size() == 0)
            return Type.NONE;
        if (cards.size() == 1)
            return Type.ONE;
        if (cards.size() == 2)
            if (cards.get(0).getVal() == cards.get(1).getVal())
                return Type.TWO;
            else
                return Type.NONE;
        if (cards.size() == 3)
            if (cards.get(0).getVal() == cards.get(1).getVal()
                    && cards.get(1).getVal() == cards.get(2).getVal())
                return Type.THREE;
            else
                return Type.NONE;
        if (cards.size() == 4)
            if (cards.get(0).getVal() == cards.get(1).getVal()
                    && cards.get(1).getVal() == cards.get(2).getVal()
                    && cards.get(2).getVal() == cards.get(3).getVal())
                return Type.FOUR;
            else
                return Type.NONE;

        // GET REDDY
        /*
         * STRAIGHT, FLUSH, FULLHOUSE, FOURONE, STRAIGHTFLUSH
         */
        if (cards.size() == 5) {
            high = false;
            boolean flush = false;
            boolean straight = false;

            // FLUSH
            if (cards.get(0).getSuit() == cards.get(1).getSuit()
                    && cards.get(1).getSuit() == cards.get(2).getSuit()
                    && cards.get(2).getSuit() == cards.get(3).getSuit()
                    && cards.get(3).getSuit() == cards.get(4).getSuit()) {
                flush = true;
            }

            // STRAIGHT
            if (cards.get(0).getVal() + 1 == cards.get(1).getVal()
                    && cards.get(1).getVal() + 1 == cards.get(2).getVal()
                    && cards.get(2).getVal() + 1 == cards.get(3).getVal()
                    && cards.get(3).getVal() + 1 == cards.get(4).getVal()) {
                straight = true;
            }

            // FOUR + ONE
            if ((cards.get(0).getVal() == cards.get(1).getVal()
                    && cards.get(1).getVal() == cards.get(2).getVal()
                    && cards.get(2).getVal() == cards.get(3).getVal())
                    || (cards.get(1).getVal() == cards.get(2).getVal()
                            && cards.get(2).getVal() == cards.get(3).getVal()
                            && cards.get(3).getVal() == cards.get(4)
                                    .getVal())) {
                high = (cards.get(3).getVal() == cards.get(4).getVal());
                if (high) {
                    cards.add(cards.get(0));
                    cards.remove(0);
                }
                return Type.FOURONE;
            }

            // Four + One takes precedence over full house
            // so no need to check if 2 == 3
            // FULL HOUSE
            if ((cards.get(0).getVal() == cards.get(1).getVal()
                    && cards.get(1).getVal() == cards.get(2).getVal()
                    && cards.get(3).getVal() == cards.get(4).getVal())
                    || (cards.get(0).getVal() == cards.get(1).getVal()
                            && cards.get(2).getVal() == cards.get(3).getVal()
                            && cards.get(3).getVal() == cards.get(4)
                                    .getVal())) {
                high = (cards.get(2).getVal() == cards.get(4).getVal());
                if (high) {
                    // check if objects passed by reference
                    cards.add(cards.get(0));
                    cards.add(cards.get(1));
                    cards.remove(0);
                    cards.remove(0);

                }
                return Type.FULLHOUSE;
            }

            if (straight && flush)
                return Type.STRAIGHTFLUSH;
            else if (straight)
                return Type.STRAIGHT;
            else if (flush)
                return Type.FLUSH;

        }

        return Type.NONE;
    }

    private Coordinates coord = null;

    public void draw(Graphics g)
    {
        coord = Coordinates.toScreen(700, 435);
        if (go != null) {
            g.drawImage(go, coord.getX(), coord.getY(), null);
        }

        if (MouseHandle.clicked) {
            if (Screen.mse.x >= 700 && Screen.mse.x <= 700 + 75) {
                if (Screen.mse.y >= 435 && Screen.mse.y <= 435 + 75) {
                    physics();
                    player.removeCards(okRemove);
                    MouseHandle.clicked = false;
                    okRemove = false;
                }
            }
        }

        // won or myTurn?
        // won takes priority over myTurn
        if (won) {
            g.setFont(new Font("Times New Roman", Font.PLAIN, Frame.width / 9));
            Coordinates coord = Coordinates.toScreen(Screen.myWidth / 2 - 200,
                    Screen.myHeight / 2 + 120);
            g.setColor(Color.WHITE);
            g.drawString("YOU WON!", coord.getX(), coord.getY());

            coord = Coordinates.toScreen(500, 333);
            // coord = Coordinates.toScreen(300,  200);
            /*
            BufferedImage img = ImageUtil.toBufferedImage(
                    new ImageIcon(getClass().getResource("/win.png")));

            img = ImageUtil.getScaledImage(img, coord.getX(), coord.getY());
            coord = Coordinates.toScreen(-150, 267);
            //coord = Coordinates.toScreen(0,  400);
            g.drawImage(img, coord.getX(), coord.getY(), null);
*/
        } else if (lost) {
            g.setFont(new Font("Times New Roman", Font.PLAIN, Frame.width / 9));
            Coordinates coord = Coordinates.toScreen(Screen.myWidth / 2 - 200,
                    Screen.myHeight / 2 + 120);
            g.setColor(Color.WHITE);
            g.drawString("YOU LOST!", coord.getX(), coord.getY());
/*
            coord = Coordinates.toScreen(500, 500);
            BufferedImage img = ImageUtil.toBufferedImage(
                    new ImageIcon(getClass().getResource("/lose.png")));

            img = ImageUtil.getScaledImage(img, coord.getX(), coord.getY());

            coord = Coordinates.toScreen(-100, 100);
            g.drawImage(img, coord.getX(), coord.getY(), null);*/
        } else if (myTurn) {
            g.setFont(
                    new Font("Times New Roman", Font.PLAIN, Frame.width / 18));
            Coordinates coord = Coordinates.toScreen(Screen.myWidth / 2 - 90,
                    Screen.myHeight / 2 + 100);
            g.setColor(Color.WHITE);
            g.drawString("Your turn", coord.getX(), coord.getY());
        }

    }
}
