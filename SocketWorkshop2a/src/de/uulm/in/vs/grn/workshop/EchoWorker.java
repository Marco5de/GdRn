package de.uulm.in.vs.grn.workshop;

import java.io.*;
import java.net.Socket;

public class EchoWorker implements Runnable {
    private Socket client;

    public EchoWorker(Socket soc){
        this.client = soc;
    }

    //ToDo sanitizize user input!

    @Override
    public void run() {
        try {
            while (!client.isClosed()) {
                BufferedReader br = new BufferedReader(new InputStreamReader(client.getInputStream()));
                PrintWriter pw = new PrintWriter(new OutputStreamWriter(client.getOutputStream()),true);

                String in = br.readLine();
                System.out.println(in);
                pw.println("ECHO: " + in);
                client.close();
                System.out.println("Client disconnected");
            }
        }catch (Exception e){
            System.out.println("Error in Thread!");
        }
    }
}
