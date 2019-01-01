package de.uulm.in.vs.grn.chatService.CCM.response;

import de.uulm.in.vs.grn.chatService.Controller;
import org.jetbrains.annotations.NotNull;

public class CCMResponse {
    private String response;
    private Controller controller;

    private final String VERSION = "GRNCP /0.1";

    //Todo diese klasse soll reponse in String form von der connection bekommen
    //todo verson wo hinstecken sodass im gesamten code die versions nummer nur an einer stelle steht
    public CCMResponse(String response,Controller controller) {
        this.response = response;
        this.controller = controller;
    }

    public void handleResponse() {
        String lines[] = this.response.split("\r\n");
        String firstLine[] = lines[0].split("\\s+");

        if (!firstLine[0].equals(VERSION)) {
            //Todo sauberse handling
            System.out.println("Version is not correct");
            System.exit(1);
        }

        switch (firstLine[1]) {
            case "ERROR":
                handleError(lines);
                break;
            case "LOGGEDIN":
                handeLoggedin(lines);
                break;
            case "EVENT":
                handleEvent(lines);
                break;
            case "MESSAGE":
                handleMessage(lines);
                break;
            case "SENT":
                handleSent(lines);
                break;
            case "BYEBYE":
                handleByeBye(lines);
                break;
            case "PONG":
                handlePong(lines);
                break;
            case "EXPIRED":
                handleExpired(lines);
                break;

        }

    }
    //todo schnittstelle für den output steht noch nicht fest, finde weg das ganze als getrenntes zu betrachten, setze controller in die mitte
    //bspw eine klasse display welche sämtlichen output übergeben bekommt und dies an eine weitere instanz gibt
    //Todo beachte ob man hier evtl noch ein new line braucht
    private void handleError(@NotNull String lines[]){
        System.out.println("A error occured: " + lines[1] + " " + lines[2]);
        controller.displayContent("A error occured: " + lines[1] + " " + lines[2]);
    }

    private void handeLoggedin(@NotNull String lines[]){
        //todo event that somone joined
        System.out.println("LOGGEDIN: " + lines[1]);
        controller.displayContent("You successfully connected to the Chat " + lines[1]);
    }

    private void handleEvent(@NotNull String lines[]){
        //todo sauberes event handling
        System.out.println("AN EVENT OCCURED: " + lines[1] + " " + lines[2]);
        controller.displayContent(lines[1] + ": " + lines[2]);

    }
    //todo remove text from input, can be done later
    private void handleMessage(@NotNull String lines[]){
        System.out.println("Message received: " + lines[1] + "---" + lines[2] + ": " + lines[3]);
        controller.displayContent(lines[1] + "---" + lines[2] + ": " + lines[3]);
    }

    //todo what to do about this ack
    private void handleSent(@NotNull String lines[]){
        System.out.println("Message succesfully sent: " + lines[1]);
    }

    private void handleByeBye(@NotNull String lines[]){
        System.out.println("Succesfully disconnected: " + lines[1]);
        controller.displayContent(lines[1] + " you disconnected");
    }

    private void handlePong(@NotNull String lines[]){
        System.out.println("Users on Server " + lines[1] + " " + lines[2]);
        controller.displayContent(lines[1] + ": " + lines[2]);
    }

    private void handleExpired(@NotNull String lines[]){
        System.out.println("You timed out " + lines[1]);
        controller.displayContent("You timed out " + lines[1]);
    }

}
