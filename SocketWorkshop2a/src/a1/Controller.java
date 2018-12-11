package a1;

import java.util.Arrays;
import java.util.Observable;
import java.util.concurrent.LinkedBlockingQueue;

public class Controller implements Runnable {
    private LinkedBlockingQueue<ConWriter> writer = new LinkedBlockingQueue<>();

    private LinkedBlockingQueue<String> cont = new LinkedBlockingQueue<>();

    public void add(String s) {
        try {
            this.cont.put(s);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void add(ConWriter cw) {
        try {
            this.writer.put(cw);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void run() {
        while (true) {
            try {
                String s = cont.take();
                for (ConWriter cw : writer)
                    cw.addToQ(s);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
