package de.uulm.in.vs.grn.chatService.upConnection;

import de.uulm.in.vs.grn.chatService.Controller;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

public class CommandConnection implements Runnable{
    Socket socket = null;
    Controller controller = null;

    public CommandConnection(String host, int port, Controller controller){
        this.socket = new Socket(host,port);
        this.controller = controller;
    }
    @Override
    public void run() {
        try(PrintWriter pw = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()))){
            while(true){
                //wait for message to send
            }
        }catch (IOException e){
            e.printStackTrace();
            System.out.println("Error establishing connection");
        }
    }
}
