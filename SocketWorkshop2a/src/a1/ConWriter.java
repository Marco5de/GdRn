package a1;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.LinkedBlockingQueue;

public class ConWriter implements Runnable {
    private Controller con;
    private Socket client;
    private LinkedBlockingQueue<String> queue = new LinkedBlockingQueue<>();

    public ConWriter(Controller con, Socket client) {
        this.con = con;
        this.client = client;
        con.add(this);
    }

    public void addToQ(String s) {
        try {
            queue.put(s);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        while (true) {
            try {
                new PrintWriter(new OutputStreamWriter(client.getOutputStream()), true).println("ECHO: " + queue.take());
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
