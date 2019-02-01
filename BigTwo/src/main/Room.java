package main;

import java.awt.Graphics;

import cards.Deck;
import cards.Pile;
import networking.GameServer;

public class Room
{
	private Background bg;
	private Deck	   deck;
	
	public Player player;
	public Pile   pile;
	public Play   play;
	

	public GameServer gs;
	
	// deck will be handled by server
	// and dealt by server
	public Room()
	{
		define();
	}
	
	public void define()
	{
		if (!Screen.network) {
			bg = new Background();
			deck = new Deck();
			player = new Player(deck);
			
			pile = new Pile();
			play = new Play(player, pile);
		}
		
		else {
			bg = new Background();
			pile = new Pile();
			player = new Player();
			play = new Play(player);
			gs = new GameServer();
		}
		
	}
	
	public void physics()
	{
		player.physics();
		//pile.physics();
	}
	
	public void draw(Graphics g)
	{
		bg.draw(g);
		player.draw(g);
		pile.draw(g);
		play.draw(g);
	}
}
