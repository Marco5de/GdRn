package de.uulm.in.vs.grn.chatService.CCM.request;

import org.jetbrains.annotations.NotNull;

public class CCMRequest {

    public enum requestTypes {Bye, Login, Ping, Send}

    private final String VERSION = "GRNCP /0.1";

    private requestTypes requestType;
    private String content;

    public CCMRequest(requestTypes requestType,String content) {
        this.requestType = requestType;
        this.content = content;
    }


    protected String buildRequest() {
        switch (this.requestType) {
            case Bye:
                return buildBye();
            break;
            case Ping:
                return buildPing();
            break;
            case Send:
                return buildSend();
            break;
            case Login:
                return buildLogin();
            break;
            default:
                //Todo unknown type
        }
    }


    @NotNull
    private String buildBye() {
       StringBuilder message = new StringBuilder();
       message.append("BYE " + VERSION + "\r\n");
       message.append("\r\n");

       return message.toString();

    }

    @NotNull
    private String buildPing(){
        StringBuilder message = new StringBuilder();
        message.append("PING " + VERSION + "\r\n");
        message.append("\r\n");

        return message.toString();

    }

    @NotNull
    private String buildSend(){
        StringBuilder message = new StringBuilder();
        //Todo check size of message
        if(!checkSize()){
            System.out.println("message is to big todo handle");
        }

        message.append("SEND " + VERSION + "\r\n");
        message.append("Text: " + this.content + "\r\n");
        message.append("\r\n");

        return message.toString();
    }

    @NotNull
    private String buildLogin(){
        StringBuilder message = new StringBuilder();

        if(!checkUsername()){
            System.out.println("name nicht zuöäassig");
            System.exit(1);
        }
        message.append("LOGIN " + VERSION + "\r\n");
        message.append("Username: " + content + "\r\n");
        message.append("\r\n");

        return message.toString();


    }

    boolean checkUsername(){
        //Todo check if username is valid
    }

    boolean checkSize(){
        //todo check if bigger than 512 bytes
    }
}
