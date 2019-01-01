package de.uulm.in.vs.grn.d1;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JPanel;

public class VoidRunnerBoard extends JPanel {

    private final int DOT_SIZE = 21;

    private boolean[][] board;

    protected int boardWidth;
    protected int boardHeight;

    private HashMap<Integer, int[]> playerMap;
    private HashMap<Integer, Color> colorMap;

    private boolean inGame = true;
    private int playerId = 0;

    private ServerConnection gameServer;

    public VoidRunnerBoard() throws IOException {
        addKeyListener(new TAdapter());
        setBackground(Color.black);
        setFocusable(true);

        gameServer = new ServerConnectionImpl(this);

        playerMap = new HashMap<>();
        colorMap = new HashMap<>();
        gameServer.initializeGame();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        // draw tiles
        g.setColor(Color.GRAY);
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                if (!board[i][j]) {
                    g.fillRect(i * DOT_SIZE, j * DOT_SIZE, DOT_SIZE - 1, DOT_SIZE - 1);
                }
            }
        }

        // draw player avatars
        for (Map.Entry<Integer, int[]> entry : playerMap.entrySet()) {
            g.setColor(colorMap.get(entry.getKey()));
            int[] xy = entry.getValue();
            g.fillOval(xy[0] * DOT_SIZE, xy[1] * DOT_SIZE, DOT_SIZE - 1, DOT_SIZE - 1);

            // highlight own player avatar
            if (entry.getKey() == playerId) {
                g.setColor(Color.WHITE);
                g.fillOval(xy[0] * DOT_SIZE + DOT_SIZE/4, xy[1] * DOT_SIZE + DOT_SIZE/4, DOT_SIZE/2, DOT_SIZE/2);
            }
        }

        // draw game over screen, when the game is over
        if (!inGame) {
            gameOver(g);
        }

        Toolkit.getDefaultToolkit().sync();
    }

    private void gameOver(Graphics g) {
        String msg = "Game Over";
        String msg2 = "(press ESC to exit)";
        Font font = new Font("Helvetica", Font.BOLD, 24);
        FontMetrics metrics = getFontMetrics(font);

        g.setColor(Color.white);
        g.setFont(font);
        g.drawString(msg, (boardWidth - metrics.stringWidth(msg)) / 2, boardHeight / 2 - metrics.getHeight());
        g.drawString(msg2, (boardWidth - metrics.stringWidth(msg2)) / 2, boardHeight / 2 + metrics.getHeight());
    }

    public void setPlayerId(int id) {
        this.playerId = id;
    }

    public void setBoard(boolean[][] board) {
        this.board = board;

        boardHeight = (int)(DOT_SIZE * (board[0].length+1.5));
        boardWidth = DOT_SIZE * board.length - 1;
        //boardHeight = DOT_SIZE * board[0].length - 1;

        setPreferredSize(new Dimension(boardWidth, boardHeight));
    }

    public void handleUpdate(int id, boolean alive, int x, int y) {
        // ensure that alive flag is not part of the id
        id &= 0xFFFFFF00;

        if (playerMap.containsKey(id)) {
            if (alive) {
                // update player
                int[] playerCoords = playerMap.get(id);
                playerCoords[0] = x;
                playerCoords[1] = y;

                board[x][y] = true;
            } else {
                // remove player
                playerMap.remove(id);

                if (playerId == id) {
                    // game over
                    inGame = false;
                }
            }
        } else {
            if (alive) {
                // create player
                playerMap.put(id, new int[] {x, y});
                colorMap.put(id, new Color(id >> 8));
            } else {
                // ignore update
            }
        }

        repaint();
    }

    private class TAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            int direction = 0;
            switch (e.getKeyCode()) {
                case KeyEvent.VK_UP:
                    direction = 0;
                    break;

                case KeyEvent.VK_RIGHT:
                    direction = 1;
                    break;

                case KeyEvent.VK_DOWN:
                    direction = 2;
                    break;

                case KeyEvent.VK_LEFT:
                    direction = 3;
                    break;

                case KeyEvent.VK_ESCAPE:
                    System.exit(0);
            }

            // send an update packet to the server
            try {
                gameServer.sendUpdate(playerId, direction);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }

    public interface ServerConnection {
        void initializeGame() throws IOException;
        void sendUpdate(int playerId, int direction) throws IOException;
    }

}
