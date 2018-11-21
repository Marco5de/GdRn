package de.uulm.in.vs.grn.c1;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
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
public class SochagramClient {
    /*
        ToDo fuer testzwecke hardcoded values, wandle später in commandline app um
        SocketClosed exeption in rcv image function
     */
    static Socket client = null;
    static OutputStream outputStream;
    static InputStream inputStream;


    public static void main(String args[]) throws IOException {
        final byte filterType = 1;


        try {
            client = new Socket("grn-services.lxd-vs.uni-ulm.de", 7777);
            outputStream = client.getOutputStream();
            inputStream = client.getInputStream();
        } catch (IOException e) {
            System.out.println("Unknwown Host");
        }

        if (sndImg(filterType))
            System.out.println("Sending was succesfull");
        else
            System.out.println("Failure while sending the image");

        /*try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/

        if (rcvImg())
            System.out.println("Rcv picture with filter from server");
        else
            System.out.println("Failure rcv picture from server");

        client.close();

    }

    /**
     * rcv previously send img, now with the applied filter
     *
     * @return
     * @throws IOException
     */
    private static boolean rcvImg() throws IOException {
        byte statusCode;
        System.out.println("RCV PIC");

        if ((statusCode = inputStream.readNBytes(1)[0]) != 0)
            return false;
        byte[] img = new byte[convertToInt(inputStream.readNBytes(4))];

        System.out.println("reading img");
        BufferedImage buffIm = ImageIO.read(new ByteArrayInputStream(img));
        ImageIO.write(buffIm, "jpg", new File("output.jpg"));
        inputStream.close();
        client.close();

        return true;

    }

    /**
     * sending a picture over the socket to kwnown server
     *
     * @return
     * @throws IOException
     */
    private static boolean sndImg(byte filterType) throws IOException {

        BufferedImage bufferedImage = ImageIO.read(new File("infoday_poster.png"));
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ImageIO.write(bufferedImage, "png", byteArrayOutputStream);

        byte[] header = ByteBuffer.allocate(5).array();
        byte[] payload = byteArrayOutputStream.toByteArray();

        header[0] = filterType;
        byte[] arr = convertIntToByteArr(payload.length);
        header[0] = arr[0];
        header[1] = arr[1];
        header[2] = arr[2];
        header[3] = arr[3]; rea

        //concat the two byte arrays
        byte[] output = new byte[header.length + payload.length];
        System.arraycopy(header, 0, output, 0, header.length);
        System.arraycopy(payload, 0, output, header.length, payload.length);


        outputStream.write(output);
        outputStream.flush();


        return true;
    }


    /**
     * converting int to bayte array by bitshifting
     */
    private static byte[] convertIntToByteArr(int integer) {
        byte intByteArr[] = new byte[4];
        intByteArr[0] = (byte) (integer >> 24);
        intByteArr[1] = (byte) (integer >> 16);
        intByteArr[2] = (byte) (integer >> 8);
        intByteArr[3] = (byte) (integer >> 0);

        return intByteArr;
    }

    /**
     * converting byte array to int
     *
     * @param bytes
     * @return
     */
    private static int convertToInt(byte[] bytes) {
        return ByteBuffer.wrap(bytes).getInt();
    }

}
