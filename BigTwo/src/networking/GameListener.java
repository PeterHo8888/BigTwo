package networking;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;

import cards.Card;
import cards.Card.Type;
import main.Screen;

public class GameListener extends Thread
{
	private BufferedReader br;
	
	public GameListener(Socket socket)
	{
		try {
			InputStream is = socket.getInputStream();
			InputStreamReader isr = new InputStreamReader(is);
			br = new BufferedReader(isr);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void run()
	{
		try {
			while (true) {
				String str = br.readLine();
				
				if (str.startsWith("hand:")) {
					str = str.substring(5);
					Screen.room.player.setHand(parseCards(str));
					Screen.room.player.define();
				}
				if (str.startsWith("updh:")) {
				    str = str.substring(5);
				    
				    // You won!
				    if (str.equals("[]")) {
				    	Screen.room.player.setHand(new ArrayList<Card>());
				    	Screen.room.gs.send("won");
				    	Screen.room.play.won = true;
				    } else {
				    	Screen.room.player.setHand(parseCards(str));
				    }
				}
				if (str.startsWith("pile:")) {
					str = str.substring(5);
					if (!str.equals("[]")) {
						Screen.room.pile.set(parseCards(str));
					}
					Screen.room.pile.physics();
					Screen.room.play.setPile(Screen.room.pile);
				}
				if (str.startsWith("turn")) {
				    Screen.room.play.setTurn(true);
				}
				if (str.startsWith("!turn")) {
				    Screen.room.play.setTurn(false);
				}
				if (str.startsWith("new")) {
					Screen.room.play.newStart = true;
				}
				if (str.startsWith("lost")) {
				    Screen.room.play.lost = true;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public ArrayList<Card> parseCards(String str)
	{
		ArrayList<Card> hand = new ArrayList<Card>();
		str = str.substring(1, str.length() - 1);
		String val[] = str.split(", ");
		for (String s : val) {
			Card c;
			int value = 0;
			switch (s.charAt(0)) {
			case 'A':
				value = 0;
				break;
			case 'T':
				value = 9;
				break;
			case 'J':
				value = 10;
				break;
			case 'Q':
				value = 11;
				break;
			case 'K':
				value = 12;
				break;
			default:
				value = Integer.parseInt(s.substring(0, 1)) - 1;
			}
			
			Card.Type type = Type.SPADE;
			switch (s.charAt(1)) {
			case 'D':
				type = Type.DIAMOND;
				break;
			case 'C':
				type = Type.CLUB;
				break;
			case 'H':
				type = Type.HEART;
				break;
			case 'S':
				type = Type.SPADE;
			}
			c = new Card(value, type);
			hand.add(c);
		}
		return hand;
	}
}
