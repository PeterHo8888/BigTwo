package server;

import java.util.ArrayList;

import game.Card;
import game.Game;
import game.Hand;
import game.Player;

public class GameRoom
{
    public static int        counter_id  = 0;
    public int               gr_id       = 0;
    public Game              game;
    public ArrayList<Player> players     = new ArrayList<Player>();
    public int               currentTurn = 0;
    public int               skipCounter = 0;
    public boolean           newStart    = false;
    public boolean           gameDone    = false;

    public GameRoom(int gr_id)
    {
        this.gr_id = gr_id;
        game = new Game();
    }

    public void addPlayer(ServerThread st)
    {
        // player.size() returns 0 for first player
        // player assigned serverThread, then
        // serverThread assigned its player
        // might not be a good idea
        players.add(new Player(players.size(), this, st));
        st.setPlayer(players.get(players.size() - 1));

        Hand hand = new Hand();
        // start of game, lowest card goes first
        if (game.getPile().cards.size() == 0) {
            for (int i = 0; i < players.size(); ++i) {
                System.out.println(players.get(i).hand.cards);
                System.out.println(players.get(i).hand.getLowest());
                hand.cards.add(players.get(i).hand.getLowest());
            }
            int low = hand.getLowestIndex();
            for (int i = 0; i < players.size(); i++) {
                if (i == low) {
                    players.get(i).st.doAction("set_turn");
                } else {
                    players.get(i).st.doAction("stop_turn");
                }
            }
        }

    }

    public int getNumPlayers()
    {
        return players.size();
    }

    public void playDone(int id)
    {
        players.get(currentTurn).st.doAction("stop_turn");
        if (id != players.size() - 1)
            currentTurn = id + 1;
        else
            currentTurn = 0;

        players.get(currentTurn).st.doAction("set_turn");
        if (newStart) {
            players.get(currentTurn).st.doAction("new");
            newStart = false;
        }
    }

    public void disconnect(Player player)
    {
        players.remove(player.id);
        if (players.size() == 0) {
            GameServer.removeRoom(this);
            return;
        }

        for (int i = 0; i < players.size(); ++i) {
            players.get(i).id = i;
        }

        // if game is done there's no point in setting who's next
        if (gameDone)
            return;

        // currentTurn goes back 1 after reassignment
        // note that for currentTurn == id and currentTurn < id
        // there's no effect
        if (player.id <= currentTurn) {
            --currentTurn;

        }

        if (currentTurn != 0)
            playDone(currentTurn - 1);
        else
            playDone(players.size());

        // add player's cards back to deck
        for (Card c : player.hand.cards) {
            game.deck.deck.add(c);
        }

    }

    public void won(int player_id)
    {
        for (int i = 0; i < players.size(); ++i) {
            players.get(i).st.doAction("stop_turn");
        }
        for (int i = 0; i < players.size(); ++i) {
            if (i != player_id)
                players.get(i).st.doAction("lost");
        }
        //currentTurn = -100;

        // destroy room
        /*
        players.clear();
        GameServer.gr.remove(this);
        */
        // or maybe set flag?
        gameDone = true;
    }

    public void skip(Player player)
    {
        ++skipCounter;
        // back to first person
        if (skipCounter >= players.size() - 1) {
            skipCounter = 0;
            newStart = true;
        }
        playDone(player.id);
    }

    public void echoAll(String str)
    {
        for (Player p : players) {
            p.st.echo(str);
        }
    }

}
