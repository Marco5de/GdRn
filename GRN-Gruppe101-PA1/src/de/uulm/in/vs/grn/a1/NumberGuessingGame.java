package de.uulm.in.vs.grn.a1;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.ThreadLocalRandom;

public class NumberGuessingGame {
    private static final int PORT = 5555;
    private static final int MAX_TRIES = 6;
    private final static byte MAX_DIGITS = 5;

    public static void main(String[]args){
        //creating new ServerSocket
        try {
            ServerSocket Ssoc = new ServerSocket(PORT);
            System.out.println("Server started!");
            System.out.println("Server is waiting for Client-request:");

            while (!Ssoc.isClosed()){
                Socket soc = Ssoc.accept();
                System.out.println("Client connected");

                OutputStream os = soc.getOutputStream();
                InputStream is = soc.getInputStream();

                //create a random number between 0 and 50
                int number = ThreadLocalRandom.current().nextInt(50);
                System.out.println("RandomNumber selected: " + number + "\n");
                os.write("Random Number selected. Enter your guess: \n".getBytes());

                for(int i = 0; i < MAX_TRIES; i++){
                    try {
                        int inputNumber = getNumberFromClient(is);
                        System.out.println("Guess #" + i + 1 + " is " + inputNumber + "\n");
                        if (number < inputNumber)
                            os.write("Your guess was bigger than the random number. \n".getBytes());
                        else if (number > inputNumber)
                            os.write("Your guess was smaller than the random number \n".getBytes());
                        else {
                            os.write("You guessed correct.".getBytes());
                            break;
                        }
                        os.write(("You have " + (MAX_TRIES - i - 1) + " remaining try/tries.\n").getBytes());

                        if (i == 5) {
                            os.write("No tries remaining. YOU LOST\n".getBytes());
                        }
                    }catch (NumberFormatException e){
                        System.out.println("User input was invalid. \n");
                        os.write("User input was invalid. \n".getBytes());
                        i--; //invalid input does not effect the game
                    }
                }
                os.close();
                is.close();
                soc.close();
            }

        } catch (SocketException ex) {
            ex.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static int getNumberFromClient(InputStream is) throws IOException {
        byte[] input = new byte[MAX_DIGITS*2];
        for(int i=0; i < MAX_DIGITS ; i++){
            input[i] = (byte)is.read();
            //jump out at new line
            if(input[i] == '\n')
                break;
        }
        return Integer.parseInt(new String(input).trim());
    }


}
