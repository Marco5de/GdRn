package de.uulm.in.vs.grn.pa4;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

public class Service {


    private static ServerSocket server = null;
    private static BufferedReader br = null;
    private static PrintWriter pw = null;
    private static Socket client = null;

    private static HashMap<String, String> directory = new HashMap<>();

    public static void main(String args[]) {
        try {

            server = new ServerSocket(5555);
            while (!server.isClosed()) {
                client = server.accept();
                br = new BufferedReader(new InputStreamReader(client.getInputStream()));
                pw = new PrintWriter(new OutputStreamWriter(client.getOutputStream()), true);

                while (!client.isClosed()) {
                    String input = br.readLine();

                    String arr[] = input.split(" ");
                    for (String x : arr) {
                        System.out.println(x);
                        x.trim();
                    }

                    switch (arr[0]) {
                        case "GET":
                            if (arr.length != 2) {
                                pw.println("ERR: wrong number of args");
                                break;
                            } else {
                                if (directory.containsKey(arr[1])) {
                                    pw.println("RES: " + directory.get(arr[1])); break;
                                }else
                                    pw.println("RES: UNKOEN KRY");
                            }
                            break;
                        case "PUT":
                            if (arr.length != 3) {
                                pw.println("ERR: wrong number of args");
                                break;
                            } else {
                                directory.put(arr[1], arr[2]);
                                pw.println("RES: OK");
                            }break;
                        case "EXIT":
                            pw.println("RES: BYE");
                            client.close();
                            break;
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }


    }

}
