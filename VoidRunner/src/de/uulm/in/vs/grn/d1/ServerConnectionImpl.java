package de.uulm.in.vs.grn.d1;

import java.io.IOException;
import java.net.*;
import java.nio.ByteBuffer;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * width of messages is usually 4bytes / 32bit
 */
public class ServerConnectionImpl implements VoidRunnerBoard.ServerConnection {


    private final int PORT = 6666;
    private final String addr = "134.60.77.232";
    private final InetAddress iaddr = InetAddress.getByName(addr);

    private int direction;
    private int playerId;
    private int width;
    private int height;
    private int x;
    private int y;

    private VoidRunnerBoard voidRunnerBoard;
    private ScheduledExecutorService updateHandler;

    private DatagramSocket soc = null;

    public ServerConnectionImpl(VoidRunnerBoard voidRunnerBoard) throws UnknownHostException, SocketException {
        this.voidRunnerBoard = voidRunnerBoard;

        updateHandler = Executors.newSingleThreadScheduledExecutor();
        direction = 0;
        playerId = 0;
        width = 32;
        height = 32;
        x = (int) (Math.random() * width);
        y = (int) (Math.random() * height);
        this.soc = new DatagramSocket();
    }

    public void initializeGame() throws IOException {
        voidRunnerBoard.setPlayerId(playerId);
        voidRunnerBoard.setBoard(new boolean[32][32]);
        voidRunnerBoard.handleUpdate(playerId, true, x, y);


        updateHandler.scheduleAtFixedRate(() -> {
            switch (direction) {
                case 0:
                    y -= 1;
                    break;
                case 1:
                    x += 1;
                    break;
                case 2:
                    y += 1;
                    break;
                case 3:
                    x -= 1;
                    break;
            }

            boolean alive = !(x < 0 || y < 0 || x >= 32 || y >= 32);
            voidRunnerBoard.handleUpdate(playerId, alive, x, y);
        }, 500, 500, TimeUnit.MILLISECONDS);

        contactServer();
        receiveConfig();
        // todo start a background thread to receive serverUpdates, call handleUpdate for every update
        this.updateHandler = Executors.newSingleThreadScheduledExecutor();
        updateHandler.execute(()->{
            //todo execute server update in here
            try {
                while(true) {
                    handleUpdate();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void sendUpdate(int playerId, int direction) throws IOException {
        this.direction = direction;
        byte clientID[] = integerTo24Bit(this.playerId);
        byte dir = (byte) direction;

        ByteBuffer bb = ByteBuffer.allocate(4);
        bb.put(clientID);
        bb.put(dir);
        soc.send(new DatagramPacket(bb.array(),4,iaddr,PORT));
        System.out.println("---UPDATE SEND TO SERVER---");
    }

    //not receiving any updates without connecting to university network
    public void handleUpdate() throws IOException {
        //generic buffersize to handle max size of +120bytes
        DatagramPacket packet = new DatagramPacket(ByteBuffer.allocate(256).array(),256);
        //blocks until it receives Datagram from Server
        System.out.println("Waiting for update from Server");
        soc.receive(packet);
        byte data[] = packet.getData();
        ByteBuffer dataBuffer = ByteBuffer.wrap(packet.getData());
        //parsing response
        int noUpdates = dataBuffer.getInt();
        System.out.println("Received Update for " + noUpdates + " clients");
        for(int i=0; i<noUpdates;i++){
                byte tmp[] = new byte[3];
                System.arraycopy(data,i*12,tmp,0,tmp.length);
                int playerid = byteArrTo24BitInt(tmp);
                System.out.println("Received playerID: " + playerid);

                if(data[4]!=0){
                    System.out.println("Still alive TODO HANDLE");
                }

                //reads x and y coordinates
                ByteBuffer intBuffer = ByteBuffer.wrap(data,(i*12)+4,4);
                int xpos = intBuffer.getInt();
                intBuffer = ByteBuffer.wrap(data,(i*12)+8,4);
                int ypos = intBuffer.getInt();
                System.out.println("res: x positon = " + xpos + " and y postition= " + ypos);
            }

        System.out.println("\n\n");
    }


    /**
     * sends message to Server to init connection » sends 0x00 0x00 0x00 0x00
     */
    void contactServer() {
        // byte buffer[],len inet addr,port
        try {
            System.out.println("Contacting Server");
            soc.send(new DatagramPacket(ByteBuffer.allocate(4).putInt(0).array(),4,iaddr,PORT));
            System.out.println("Message to server sent");
        } catch (IOException e) {
            System.out.println("Error sending message to server for init");
        }
    }

    /** toDo some of the values are off why? game is not working properly (seems to be a off by one error in positon coords)
     * size of message dependant on size of playing field
     * @throws IOException
     */
    void receiveConfig() throws IOException {

        //generic buffer size/ check which size is really needed
        DatagramPacket packet = new DatagramPacket(ByteBuffer.allocate(256).array(),256);
        //blocks until it receives a Datagram. Client has to wait anyways so this does not need its own thread
        System.out.println("Waiting for repsonse from Server");
        soc.receive(packet);
        System.out.println("Received config message from Server");
        byte data[] = packet.getData();

        //reading player id from res mesasage
        byte[] tmp = new byte[3];
        System.arraycopy(data,0,tmp,0,tmp.length);
        this.playerId = byteArrTo24BitInt(tmp);
        System.out.println("Res: player id = " + playerId);

        if(data[4]!=0){
            System.out.println("Invalid response from Server. Nullbyte is not null");
            System.exit(1);
        }
        //reads x and y coordinates
        ByteBuffer intBuffer = ByteBuffer.wrap(data,4,4);
        this.x = intBuffer.getInt();
        intBuffer = ByteBuffer.wrap(data,8,4);
        this.y = intBuffer.getInt();
        System.out.println("res: y positon = " + this.y + " and x postition= " + this.x);

        //ready height and width of voidRunnderBoard to init bitmap
        intBuffer = ByteBuffer.wrap(data,12,4);
        this.width = intBuffer.getInt();
        intBuffer = ByteBuffer.wrap(data,16,4);
        this.height = intBuffer.getInt();
        System.out.println("Received fieldsize height x width = " + this.height + "x" + this.width);

        //setting bitmap for playing field
        //voidRunnerBoard.setBoard();
        //first compute size of board
        int start = 20;
        int end = (this.height * this.width)/8;
        byte set[] = new byte[end];
        //todo fixing index out of boundse exception
        ByteBuffer setbb = ByteBuffer.wrap(data);
        ByteBuffer buffer = setbb.get(data,20,end);
        set = buffer.array();
        System.out.println("length of array " + set.length);

        boolean board[][] = new boolean[this.height][this.width];
        int index = 0;
        //todo check if this is acutally working!
        for(int i=0; i<set.length; i++) {
            int col = i/this.width;
            byte cur = set[i];
            for(int j=0; j<8;j++){
                board[col][index++%this.width] = isSet(cur,j);
            }
        }

        System.out.println("---INNIT DONE LEAVING FUNCTION NOW INTO GAMELOPP---");
    }

    /**
     * todo checks if kth bit is set, should be done more efficiently by returning array of boolean so this only has to be called once per byte
     * @param b
     * @param bit
     * @return
     */
    boolean isSet(byte b,int bit){
        if((b & (1<<bit)) != 0){
            return true;
        }
        return false;
    }

    /** convert to UNSIGNED INT
     * converting 24Bit from server reponse to integer by bitshifting
     * @return
     */
    int byteArrTo24BitInt(byte input[]) {
        if (input.length != 3)
            throw new NumberFormatException();
        return ((input[2] & 0xFF) << 16 | (input[1] & 0xFF) << 8 | (input[0] & 0xFF) << 0);
    }
    byte[] integerTo24Bit(int id){
        byte[] out = new byte[3];
        out[0] = (byte)(id>>0);
        out[1] = (byte)(id>>8);
        out[2] = (byte)(id>>16);

        return out;
    }
}




