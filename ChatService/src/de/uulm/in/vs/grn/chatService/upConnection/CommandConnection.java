package de.uulm.in.vs.grn.chatService.upConnection;

import de.uulm.in.vs.grn.chatService.Controller;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

public class CommandConnection implements Runnable {
    Socket socket = null;
    Controller controller = null;
    PrintWriter pw = null;

    public CommandConnection(String host, int port, Controller controller) throws IOException {
        this.socket = new Socket(host, port);
        this.controller = controller;
        this.pw = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
    }

    public void sendMessage(String message) {
        System.out.println("Sending message to Server");
        this.pw.println(message);

    }
}
