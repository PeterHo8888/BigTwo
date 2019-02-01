package game;

public class Game
{
	
	public Deck deck;
	public Pile pile;
	
	public Game()
	{
		deck = new Deck();
		pile = new Pile();
	}
	
	public Hand getHand()
	{
		return new Hand(deck);
	}
	
	public Pile getPile()
	{
		return pile;
	}
	
	public void setPile(Pile pile)
	{
		this.pile = pile;
	}
}
