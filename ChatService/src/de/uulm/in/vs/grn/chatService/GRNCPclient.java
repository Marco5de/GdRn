package de.uulm.in.vs.grn.chatService;

import de.uulm.in.vs.grn.chatService.CCM.request.CCMRequest;
import de.uulm.in.vs.grn.chatService.upConnection.CommandConnection;
import de.uulm.in.vs.grn.chatService.upConnection.PubSubConnection;

import java.io.IOException;


//todo klasse ist noch komplett unfertig
public class GRNCPclient {
    private CommandConnection commandConnection;
    private PubSubConnection pubSubConnection;
    private Controller controller;

    public GRNCPclient(String host, int commandPort, int pubsubPort,Controller controller) throws IOException {
        this.controller = controller;
        this.commandConnection = new CommandConnection(host,commandPort,this.controller);
        this.pubSubConnection = new PubSubConnection(host,pubsubPort,this.controller);
    }


    public boolean login(String username){
        //Todo hard coded username for testing purposes
        CCMRequest request = new CCMRequest(CCMRequest.requestTypes.Login,username);
        String message = request.buildRequest();
        commandConnection.sendMessage(message);

        return true;
    }



}
