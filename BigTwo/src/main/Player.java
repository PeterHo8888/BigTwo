package main;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import cards.Card;
import cards.Deck;
import cards.Hand;
import utilities.Coordinates;
import utilities.ImageUtil;

public class Player
{
	// Properties of Player
	private static BufferedImage avatar		   = null;
	public Hand					 hand		   = null;
	private static boolean		 avatarChanged = false;
	private boolean				 defined	   = false;
	
	// Properties of Avatar
	private final static int inAvatar_width	 = 80;
	private final static int inAvatar_height = 80;
	
	private final static Coordinates avatar_dimensions = Coordinates
			.toScreen(inAvatar_width, inAvatar_height);
	
	private final static int avatar_width  = avatar_dimensions.getX();
	private final static int avatar_height = avatar_dimensions.getY();
	
	// Properties of Hand
	/*
	 * pos - card screen position internal_pos - card position relative to
	 * 800x600 card_img - used for .contains(Screen.mse) cardsClicked - state of
	 * cards (clicked, not clicked)
	 */
	
	private Coordinates pos[] = new Coordinates[13];
	
	private int internal_pos[][] = new int[13][2];
	
	private Rectangle[] card_img = new Rectangle[13];
	
	private boolean cardsClicked[] = new boolean[13];
	
	// Temporary use by any method
	private Coordinates coord;
	
	// Used for determining redrawing cards
	// private int numCards = 12;
	
	public Player(Deck deck)
	{
		avatar = ImageUtil.toBufferedImage(
				new ImageIcon(Player.class.getResource("/avatar.jpg")));
		
		hand = new Hand(deck);
		define();
	}
	
	public Player()
	{
		avatar = ImageUtil.toBufferedImage(
				new ImageIcon(Player.class.getResource("/default.png")));
	}
	
	public void setHand(ArrayList<Card> cards)
	{
		this.hand = new Hand(cards);
		int offset = Screen.myWidth / 2 - (this.hand.cards.size() - 1) * 43 / 2;
		setSpacing(offset - 30, 43);
	}
	
	public void define()
	{
		// the card to be drawn
		// 43 is spacing
		// 110 is initial offset
		setSpacing(110, 43);
		
		avatar = ImageUtil.getScaledImage(avatar, avatar_width, avatar_height);
		defined = true;
	}
	
	public void physics()
	{
		if (avatarChanged) {
			avatar = ImageUtil.getScaledImage(avatar, avatar_width,
					avatar_height);
			avatarChanged = false;
		}
		
	}
	
	/*
	 * setSpacing: sets card spacing
	 */
	public void setSpacing(int offset, int spacing)
	{
		int x = 0;
		for (int i = 0; i < hand.cards.size(); ++i) {
			internal_pos[i][0] = x + offset;
			internal_pos[i][1] = 425;
			cardsClicked[i] = false;
			
			card_img[i] = new Rectangle(x + offset, 425, Card.inCard_width,
					Card.inCard_height);
			
			x += spacing;
		}
	}
	
	public void draw(Graphics g)
	{
		if (defined) {
			// draw the cards
			for (int i = 0; i < hand.cards.size(); ++i) {
				pos[i] = Coordinates.toScreen(internal_pos[i][0],
						internal_pos[i][1]);
				
				hand.cards.get(i).draw(g, pos[i].getX(), pos[i].getY());
				
				g.setColor(cardsClicked[i] ? new Color(0, 0, 0, 90)
						: new Color(0, 0, 0, 0));
				g.fillRect(pos[i].getX(), pos[i].getY(), Card.card_width,
						Card.card_height);
			}
			
			// iterate through cards for locations
			for (int i = 0; i < hand.cards.size(); ++i) {
				if (card_img[i].contains(Screen.mse) && MouseHandle.clicked) {
					// indexOutOfBounds error if i+1 = numCards
					if (i != hand.cards.size() - 1
							&& card_img[i + 1].contains(Screen.mse))
						cardsClicked[i + 1] ^= true;
					else
						cardsClicked[i] ^= true;
					MouseHandle.clicked = false;
				}
			}
			
			// draw avatar
			coord = Coordinates.toScreen(10,
					425 + Card.inCard_height - inAvatar_height);
			if (!avatarChanged) {
				g.drawImage(avatar, coord.getX(), coord.getY(), null);
			}
			g.setColor(Color.black);
			g.drawRect(coord.getX(), coord.getY(), avatar_width, avatar_height);
		}
	}
	
	public static void setPlayerAvatarFile(File file)
	{
		try {
			avatar = ImageIO.read(file);
			avatarChanged = true;
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (avatar == null) {
			avatar = ImageUtil.toBufferedImage(new ImageIcon(Player.class
					.getResource("/70df84159c05666dc72d0d4d6d2b575e.jpg")));
		}
	}
	
	public ArrayList<Card> getCards()
	{
		ArrayList<Card> cards = new ArrayList<Card>();
		for (int i = 0; i < cardsClicked.length; ++i) {
			if (cardsClicked[i])
				cards.add(hand.cards.get(i));
		}
		return cards;
	}
	
	// Cards to be removed are selected
	public void removeCards(boolean okRemove)
	{
		// int numHeld = 0;
		int posHeld[] = new int[13];
		ArrayList<Card> heldCards = getCards();
		
		// set posHeld[] array
		for (int i = 0; i < posHeld.length; ++i)
			posHeld[i] = -1;
		
		if (okRemove) {
			// Determine number held cards
			for (int i = 0, j = 0; i < cardsClicked.length; ++i) {
				if (cardsClicked[i]) {
					// ++numHeld;
					posHeld[j++] = i;
					// This is so they don't stay clicked
					cardsClicked[i] = false;
				}
			}
			
			String s = "toss:";
			for (int i : posHeld) {
				if (i != -1)
					s += i + ",";
			}
			
			// Send tossed hand to update server's hand
			Screen.room.gs.send(s);
			// Send held cards to update to server's pile
			Screen.room.gs.send(heldCards.toString());
			Screen.room.play.setTurn(false);
			
		}
	}
}
