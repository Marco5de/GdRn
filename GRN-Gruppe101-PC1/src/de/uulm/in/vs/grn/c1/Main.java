package de.uulm.in.vs.grn.c1;

import java.io.File;
import java.io.IOException;

public class Main {

    public static void main(String[]args) throws IOException {
        View view = new View(new SockgramClient((byte) 3,new File("out.png")));

    }
}
