package de.uulm.in.vs.grn.chatService;

import de.uulm.in.vs.grn.chatService.upConnection.CommandConnection;
import de.uulm.in.vs.grn.chatService.upConnection.PubSubConnection;


//todo klasse ist noch komplett unfertig
public class GRNCPclient {
    private CommandConnection commandConnection;
    private PubSubConnection pubSubConnection;
    private Controller controller;

    public GRNCPclient(String host, int commandPort, int pubsubPort,Controller controller){
        this.controller = controller;
        this.commandConnection = new CommandConnection(host,commandPort,this.controller);
        this.pubSubConnection = new PubSubConnection(host,pubsubPort,this.controller);
    }


    public boolean login(String username){

    }



}
