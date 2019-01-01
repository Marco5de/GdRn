package de.uulm.in.vs.grn.chatService.upConnection;


import de.uulm.in.vs.grn.chatService.CCM.request.CCMRequest;
import de.uulm.in.vs.grn.chatService.CCM.response.CCMResponse;
import de.uulm.in.vs.grn.chatService.Controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Objects;

public class PubSubConnection implements Runnable {
    Controller controller = null;
    Socket socket = null;

    public PubSubConnection(String host, int port,Controller controller){
        this.socket = new Socket(host,port);
        this.controller = controller;
    }

    @Override
    public void run() {
        try(BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()))){
            while (!socket.isClosed()){
                StringBuilder input = new StringBuilder();
                String tmp;
                while(!Objects.equals(tmp = bufferedReader.readLine(),"")){
                    input.append(tmp);
                    input.append("\r\n");
                }

                String content = input.toString();
                CCMResponse response = new CCMResponse(content,controller);
                response.handleResponse();
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
