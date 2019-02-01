package server;

import java.io.*;
import java.net.*;
import java.util.ArrayList;

import game.Card;
import game.Player;

public class ServerThread extends Thread
{
    private BufferedReader br;
    private PrintWriter    pw;
    private int            id;

    private Player player;

    public ServerThread(Socket socket, int tempid)
    {
        this.id = tempid;
        try {
            InputStream is = socket.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            br = new BufferedReader(isr);
            OutputStream os = socket.getOutputStream();
            pw = new PrintWriter(os, true);
            echo("Hello");
        } catch (Exception e) {
            System.out.println("error 2, could not create server thread.");
        }
    }

    public void setPlayer(Player player)
    {
        this.player = player;
        player.init();
        if (player.id == 0)
            doAction("set_turn");
        output("Added player with id: " + player.id + " to GR: "
                + player.gr.gr_id);
    }

    public void run()
    {
        try {

            doAction("set_hand");
            doAction("set_pile");

            while (true) {
                String str = br.readLine();
                if (str.startsWith("[")) {
                    if (!str.equals("[]"))
                        player.gr.game.pile.set(parseCards(str));
                    doAction("set_pile");

                }
                if (str.startsWith("toss:")) {
                    str = str.substring(5, str.length() - 1);
                    String posHeld[] = str.split(",");

                    for (int i = posHeld.length - 1; i >= 0; --i) {
                        if (posHeld[i] != "") {
                            player.hand.cards
                                    .remove(Integer.parseInt(posHeld[i]));
                        }
                    }
                    doAction("update_hand");
                    player.gr.skipCounter = 0;
                    player.gr.playDone(player.id);
                }
                if (str.startsWith("skip")) {
                	player.gr.skip(player);
                }
                if (str.startsWith("won")) {
                	player.gr.won(player.id);
                	output("Player " + player.id + " won!");
                }
                if (str.equals("!exit")) {
                    output("A client requests to disconnect.\n");
                    GameServer.echoAll(str + id);
                    break;
                }

                else {
                    GameServer.echoAll(str);
                }
            }
            output("A client has disconnected.\n");
        } catch (Exception e) {
            //e.printStackTrace();
            output("GR: " + player.gr.gr_id + "  ID: " + player.id
                    + " has disconnected.");
            player.gr.disconnect(player);
        }
        GameServer.names[id] = "!removed";
    }

    public void doAction(String str)
    {
        if (str.equals("set_hand")) {
            echo("hand:" + player.hand.cards.toString());
            output("Sent hand: " + player.hand.cards.toString());
        }
        if (str.equals("set_pile")) {
            player.gr.echoAll(
                    "pile:" + player.gr.game.getPile().cards.toString());
            output("Sent pile: " + player.gr.game.getPile().cards.toString());
        }
        if (str.equals("update_hand")) {
            echo("updh:" + player.hand.cards.toString());
            output("Sent updated hand: " + player.hand.cards.toString());
        }
        if (str.equals("set_turn")) {
            echo("turn");
            output("Set turn to player " + player.id);
        }
        if (str.equals("stop_turn")) {
            echo("!turn:");
            output("Took turn away from player " + player.id);
        }
        if (str.equals("new")) {
        	echo("new");
        	output("New start on player " + player.id);
        }
        if (str.equals("lost")) {
            echo("lost");
            output("Player " + player.id + " lost");
        }
    }

    public void echo(String str)
    {
        try {
            pw.println(str);
        } catch (Exception e) {
            System.out.println("error 5, could not echo.");
        }
    }

    public void output(String str)
    {
        System.out.println(str);
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

            Card.Type type = Card.Type.SPADE;
            switch (s.charAt(1)) {
            case 'D':
                type = Card.Type.DIAMOND;
                break;
            case 'C':
                type = Card.Type.CLUB;
                break;
            case 'H':
                type = Card.Type.HEART;
                break;
            case 'S':
                type = Card.Type.SPADE;
            }
            c = new Card(value, type);
            hand.add(c);
        }
        return hand;
    }
}
