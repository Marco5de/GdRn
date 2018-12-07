package de.uulm.in.vs.grn.workshop;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class EchoServer {

    private static int PORT = 5555;
    private static int MAX_THREADS = 20;

    private static ServerSocket server = null;
    private static Executor executor = Executors.newFixedThreadPool(MAX_THREADS);


    public static void main(String args[]) {
        try {
            server = new ServerSocket(PORT);
            System.out.println("Server started!");

            for (int i = 0; i < MAX_THREADS; i++) {
                while (!server.isClosed()) {
                    Socket soc = server.accept();
                    System.out.println("Client connected to Server");
                    Runnable workerThread = new EchoWorker(soc);
                    executor.execute(workerThread);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Server Error!");
        }
    }


}

