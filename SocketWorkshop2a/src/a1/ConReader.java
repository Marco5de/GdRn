package a1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.concurrent.LinkedBlockingQueue;

public class ConReader implements Runnable {
    private Controller con;
    private Socket client;
    private LinkedBlockingQueue<String> queue = null;

    public ConReader(Controller con, LinkedBlockingQueue queue, Socket client) {
        this.con = con;
        this.queue = queue;
        this.client = client;
    }

    @Override
    public void run() {
        while (true) {
            String str;
            try {
                queue.put(str = (new BufferedReader(new InputStreamReader(client.getInputStream())).readLine()));
                con.add(str);
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }

    }
}
