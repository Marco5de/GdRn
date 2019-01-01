package de.uulm.in.vs.grn.chatService;

public class Main {

/*
Host: grn-services.lxd-vs.uni-ulm.de
Command Port: 8122
Pub/Sub Port: 8123
IPv4-Adresse: 134.60.77.232
IPv6-Adresse: 2001:7c0:900:4d:216:3eff:fe2f:a514
(Hinweis: Nur aus dem Uni-Netz erreichbar!)
 */
    private static final int commandPort = 8122;
    private static final int pubsubPort = 8123;
    private static final String addr = "134.60.77.232";

    public static void main(String args[]){
        Controller controller = new Controller();
        GRNCPclient client = new GRNCPclient(addr,commandPort,pubsubPort,controller);
    }
}

