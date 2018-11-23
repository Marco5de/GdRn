package de.uulm.in.vs.grn.c1;


import java.io.*;
import java.net.Socket;
import java.nio.ByteBuffer;

/**
 * message to the server should include:
 * type of filter as byte
 * byte count of payload as int
 * payload as byte array
 * response from the server includes:
 * status byte as byte
 * byte count of payload
 * payload as byte array
 * <p>
 * Anfrage ist an folgenden Server zu senden
 * Host: grn-services.lxd-vs.uni-ulm.de
 * TCP-Port: 7777
 * IPv4-Adresse: 134.60.77.232
 * IPv6-Adresse: 2001:7c0:900:4d:216:3eff:fe2f:a514
 * FilterCodes:
 * NOFILTER: 0
 * BLACKANDWHITE: 1
 * EIGHTBIT: 2
 * YOLO: 3
 * SWAG: 4
 * SUMMER: 5
 * SEPIA: 6
 */


public class SockgramClient {
    private Socket client = null;
    private String host;
    private int port;
    private byte filtertype;
    private File file;


    public SockgramClient(String host,int port,byte filtertype,File file) throws IOException {
        this.host = host;
        this.port = port;
        this.filtertype = filtertype;
        this.file = file;
        this.client = new Socket(host,port);
    }
    public SockgramClient(byte filtertype,File file) throws IOException {
        this.port= 7777;
        this.host = "grn-services.lxd-vs.uni-ulm.de";
        this.filtertype = filtertype;
        this.file = file;
        this.client = new Socket(host,port);
    }

    private void sndImg() throws IOException {
        byte[] payload = new FileInputStream(file).readAllBytes();
        ByteBuffer byteBuffer = ByteBuffer.allocate(5+payload.length);
        byteBuffer.put(filtertype);
        byteBuffer.putInt(payload.length);
        byteBuffer.put(payload);

        this.client.getOutputStream().write(byteBuffer.array());
    }

    private void rcvImg() throws IOException, FilterException {
        //parsen der Response
        FileOutputStream fileOutputStream = new FileOutputStream("Filter-"+file.getName());
        ByteBuffer header = ByteBuffer.wrap(this.client.getInputStream().readNBytes(5));
        byte status = header.get();
        int len = header.getInt();
        byte[] img = client.getInputStream().readNBytes(len);
        if(status != 0)
            throw new FilterException("Beim Anwenden des Filters ist ein Fehler aufgetreten!");
        fileOutputStream.write(img);

        client.close();

    }
    public void action(){
        try {
            this.sndImg();
            this.rcvImg();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (FilterException e) {
            e.printStackTrace();
        }
    }

}
