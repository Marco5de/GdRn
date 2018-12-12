import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

//comment to test gitignore

public class IntegerAdder {
    private static final int PORT = 5555;
    private static final int N = 3;
    private static AtomicInteger aint = new AtomicInteger(0);

    private static Executor executor1 = Executors.newFixedThreadPool(N);
    private static ServerSocket server = null;
    private static CyclicBarrier cyclicBarrier = new CyclicBarrier(N);

    public static void main(String args[]) throws IOException {
        server = new ServerSocket(PORT);
        System.out.println("Server started!");


        for (int i = 0; i < N; i++)
            while (!server.isClosed()) {
                Socket soc = server.accept();
                System.out.println("Client connected to Server!");
                executor1.execute(() -> {
                    try {
                        aint.addAndGet(Integer.parseInt(new BufferedReader(new InputStreamReader(soc.getInputStream())).readLine()));

                        cyclicBarrier.await();

                        new PrintWriter(new OutputStreamWriter(soc.getOutputStream()),true).println(aint.get());
                        soc.close();
                    } catch (IOException | InterruptedException | BrokenBarrierException e) {
                        e.printStackTrace();
                    }
                });
            }

    }
}
