package game;

import server.GameRoom;
import server.ServerThread;

public class Player
{
	
	public Hand hand;
	public int id;
	public GameRoom gr;
	public ServerThread st;
	
	public Player(int id, GameRoom gr, ServerThread st)
	{
		this.id = id;
		this.gr = gr;
		this.st = st;
	}
	
	public void init()
	{
	    try {
	        hand = gr.game.getHand();
	        hand.cards = Card.sort(hand.cards);
	    } catch (Exception e) {
	        // Shouldn't happen with max limit
	        gr.game = new Game();
	        init();
	    }
	}
}
