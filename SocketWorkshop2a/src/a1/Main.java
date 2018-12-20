package a1;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;


/**
 * if client disconnects there is a null pointer exception thrown which is caught but not taken care of(inside ConReader)
 */

public class Main {

    private static ServerSocket server = null;
    private static final int PORT = 5555;

    private static Executor executor1 = Executors.newFixedThreadPool(3);
    private static Executor executor2 = Executors.newFixedThreadPool(3);
    private static Executor singleEx = Executors.newSingleThreadExecutor();

    public static void main(String args[]) throws IOException {
        server = new ServerSocket(PORT);
        Controller con = new Controller();
        singleEx.execute(con);

        System.out.println("Waiting for client to connect!");
        while(!server.isClosed()){
            Socket client = server.accept();
            System.out.println("Client connected to Server!");
            executor1.execute(new ConReader(con,new LinkedBlockingQueue<String>(),client));
            executor2.execute(new ConWriter(con,client));
        }


    }
}
