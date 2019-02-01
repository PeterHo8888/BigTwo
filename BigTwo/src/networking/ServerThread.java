package networking;

import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

public class ServerThread {
    private PrintWriter pw;
    
    public ServerThread(Socket socket){
        try{
            OutputStream os = socket.getOutputStream();
            pw = new PrintWriter(os, true);
        }
        catch(Exception e){
            System.out.println("error 2, could not create server thread.");
        }
    }
    

    
    
    public void echo(String str){
        try{
            pw.println(str);
        }
        catch(Exception e){
            System.out.println("error 5, could not echo.");
        }
    }
    
    public void output(String str){
        System.out.println(str);
    }
}
