package networking;

import java.net.Socket;

import main.Screen;

public class GameServer
{
	private static Socket s;
	private ServerThread server;
	
	public GameServer()
	{
		try {
			s = new Socket(Screen.ipAddr, 20000);
			//s = new Socket("127.0.0.1", 20000);
			GameListener gl = new GameListener(s);
			server = new ServerThread(s);
			gl.start();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void send(String s)
	{
	    server.echo(s);
	}
}
