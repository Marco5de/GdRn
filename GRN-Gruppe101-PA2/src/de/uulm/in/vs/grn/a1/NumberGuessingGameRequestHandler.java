package de.uulm.in.vs.grn.a1;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.concurrent.ThreadLocalRandom;

public class NumberGuessingGameRequestHandler implements Runnable {
    private static final int MAX_TRIES = 6;
    private static final byte MAX_DIGITS = 4;

    private Socket client;

    public NumberGuessingGameRequestHandler(Socket soc) {
        this.client = soc;
    }

    @Override
    public void run() {
        try {
            OutputStream os = client.getOutputStream();
            InputStream is = client.getInputStream();

            //create a random number between 0 and 50
            int number = ThreadLocalRandom.current().nextInt(50);
            System.out.println("RandomNumber selected: " + number + "\n");
            os.write("Random Number selected. Enter your guess: \n".getBytes());

            for (int i = 0; i < MAX_TRIES; i++) {
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
                } catch (NumberFormatException e) {
                    System.out.println("User input was invalid. \n");
                    os.write("User input was invalid. \n".getBytes());
                    i--; //invalid input does not effect the game
                }
            }

            os.close();
            is.close();
            client.close();
        } catch (IOException ignored) {
            //not handling IOException
        }


    }


    private static int getNumberFromClient(InputStream is) throws IOException {
        byte[] input = new byte[MAX_DIGITS + 2];
        for (int i = 0; i < MAX_DIGITS; i++) {
            input[i] = (byte) is.read();
            //jump out at new line
            if (input[i] == '\n')
                break;
        }
        return Integer.parseInt(new String(input).trim());
    }
}