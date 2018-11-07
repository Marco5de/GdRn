package de.uulm.in.vs.grn.b1;

import java.io.*;
import java.net.*;
import java.util.regex.Pattern;

public class URLFetcher {
    public static void main(String[] args) throws IOException {
        Socket client = null;
        String out = null;
        InetAddress url = null;
        URL _url = null;
        FileWriter fr = null;
        BufferedWriter bw = null;

        String path = "www.whatsmyip.org";
        String[] parts = path.split(Pattern.quote("."));

        BufferedReader br = null;
        PrintWriter pw = null;

        File file = null;

        try{
            url = InetAddress.getByName(path);

            client = new Socket(url,80);

            pw = new PrintWriter(new OutputStreamWriter(client.getOutputStream()));
            br = new BufferedReader(new InputStreamReader(client.getInputStream()));
            pw.print("GET / HTTP/1.1\r\nHost: "+ url.getHostName()+"\r\n\r\n");
            pw.flush();

            String response = br.readLine();
            if(response.equals("HTTP/1.1 200 OK"))
                System.out.println("Success!");
            else
                throw new UnknownHostException("Unbekannter Host");
           while(!((out=br.readLine()).equals("")))  {
               //System.out.println(out);
               if(out.equals("")) {
                   break;
               }
           }
            file = new File(parts[1] + ".html");

            fr = new FileWriter(file);
            bw = new BufferedWriter(fr);

            while((out=br.readLine())!=null) {
                //System.out.println(out);
                bw.write(out);
                bw.newLine();
            }



        } catch (UnknownHostException e) {
            System.out.println("Unbekannter Host");
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            br.close();
            bw.close();
            pw.close();
            fr.close();
            client.close();
        }

    }
}
