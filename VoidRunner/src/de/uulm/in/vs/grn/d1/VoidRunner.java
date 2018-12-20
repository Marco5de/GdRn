package de.uulm.in.vs.grn.d1;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;

public class VoidRunner extends JFrame {

    public VoidRunner() throws IOException {
        VoidRunnerBoard board = new VoidRunnerBoard();
        add(board);

        setSize(board.boardWidth, board.boardHeight);
        setResizable(false);

        setTitle("GRN VoidRunner");
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }


    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                JFrame ex = new VoidRunner();
                ex.setVisible(true);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

}
