package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Vector;

public class GameServer
{

    static Vector<ServerThread>       serverThreads;
    public static ArrayList<GameRoom> gr    = new ArrayList<GameRoom>();
    public static String[]            names = new String[256];

    ServerSocket ss;

    public GameServer()
    {

        for (int i = 0; i < names.length - 1; i++) {
            names[i] = "!removed";
        }

        //int id = 0;
        //int gr_id = 0;

        System.out.printf("BigTwo Server v.01b\n\n");

        try {
            serverThreads = new Vector<ServerThread>();
            final int port = 20000;

            System.out.printf("Initializing socket...\n");
            ss = new ServerSocket(port);
            System.out.printf("Socket opened on port " + port + "\n");

            gr.add(new GameRoom(gr.size()));

            while (true) {

                System.out.printf("Waiting for connections...\n");
                Socket s = ss.accept();
                System.out.printf("Connection made!\n");

                // If all gamerooms are gone
                if (gr.size() == 0) {
                    gr.add(new GameRoom(gr.size()));
                    System.out.println("Created GR with gr_id: "
                            + gr.get(gr.size() - 1).gr_id);
                }

                if (gr.get(gr.size() - 1).getNumPlayers() >= 4) {
                    gr.add(new GameRoom(gr.size()));
                    System.out.println("Created GR with gr_id: "
                            + gr.get(gr.size() - 1).gr_id);
                }

                // after .add() GR id is size + 1
                ServerThread st = new ServerThread(s, gr.size() - 1);
                System.out.printf("Sent hello message...Successful\n");
                //System.out.println(id);

                // gr_id is assigned per GameRoom
                // each GameRoom is given a ServerThread
                // each ServerThread is assigned a Player
                // each Player assigned is from GameRoom

                gr.get(gr.size() - 1).addPlayer(st);

                st.start();

                serverThreads.addElement(st);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error 325695207078194, not sure");
        }
        try {
            ss.close();
        } catch (IOException e) {
            System.out.println("error 4, could not close socket.");
        }
    }

    public static void removeRoom(GameRoom g)
    {
        gr.remove(g);
        for (int i = 0; i < gr.size(); ++i) {
            gr.get(i).gr_id = i;
        }
        System.out.println("Removed GR with gr_id: " + g.gr_id);
    }

    public static void main(String args[])
    {
        new GameServer();
    }

    public synchronized static void echoAll(String str)
    {
        Enumeration<ServerThread> e = serverThreads.elements();
        while (e.hasMoreElements()) {
            try {
                ServerThread st = e.nextElement();
                st.echo(str);
            } catch (Exception ex) {
                System.out.println("error 1, problem echoing.");
            }
        }
    }

}
