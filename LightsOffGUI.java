
/**
 * LightsOffGUI.java
 * based on code by Lewis/Chase
 */

import itsc2214.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Project1 - main program to implement a java GUI
 * wrapper around project 1.
 */
public class LightsOffGUI {
    public static void main(String[] args) {
        new LightsOffGUI();
    }

    private int BOX_WIDTH = 20;
    private final int BORDER = 10;

    private JFrame win = null;
    private JComponent drawArea;
    private JLabel message;

    private LightsOff game;
    private int boardId;

    /**
     * LightsOffGUI - creates the graphical user interface and sets listeners
     * so that the image can be evaluated.
     */
    public LightsOffGUI() {
        game = new Project1(LightsOff.boards[0]);
        boardId = 0;
        setupWindow();
    }

    /**
     * Set the title of the window to reflect either
     * a file that is loaded or a randomly generated
     * game...
     */
    private void setWindowTitle(String msg) {
        win.setTitle("LightsOff - " + msg + " - wins: " + game.numWins());
    }

    private void setMessage(String msg) {
        message.setText(msg + " | moves: " + game.numMoves());
    }

    /**
     * internal routine to setup the main window with all the buttons
     * and other options.
     * 
     * @param inFile
     */
    private void setupWindow() {
        // create window
        win = new JFrame("LightsOff");
        win.getContentPane().setLayout(new BorderLayout());

        setWindowTitle("New game");

        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout());

        // New game with a new board
        // if this was randomly generated, just regenerates it.
        JButton newGame = new JButton("New Game");
        newGame.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                boardId = (boardId + 1) % LightsOff.boards.length;
                setWindowTitle("New game started (" + boardId + ")");
                game.initialize(LightsOff.boards[boardId]);
                int s = game.size();
                setMessage(s + " x " + s + " board");
                drawArea.repaint();
            }
        });

        // New game button
        panel.add(newGame);
        win.getContentPane().add(panel, BorderLayout.NORTH);

        // The south part of the window shows 3 message panels...
        panel = new JPanel();
        int s = game.size();
        message = new JLabel("");
        setMessage(s + " x " + s + " board");
        panel.add(message);

        win.getContentPane().add(panel, BorderLayout.SOUTH);

        // The center part of the border layout contains
        // the grid area of the image.
        drawArea = new JComponent() {
            public void paintComponent(Graphics g) {
                BOX_WIDTH = (Math.min(getWidth(), getHeight()) - 2 * BORDER) / game.size();
                for (int i = 0; i < game.size(); i++) {
                    for (int j = 0; j < game.size(); j++) {
                        // draw square
                        drawCell(g, i, j);
                    }
                }
            }

            public void drawCell(Graphics g, int row, int col) {
                int cell = row * game.size() + col;
                if (game.isLightOn(row, col))
                    g.setColor(Color.GREEN);
                else
                    g.setColor(Color.WHITE);

                int x = BORDER + col * BOX_WIDTH;
                int y = BORDER + row * BOX_WIDTH;
                g.fillRect(x, y, BOX_WIDTH, BOX_WIDTH);
                g.setColor(Color.black);
                g.drawRect(x, y, BOX_WIDTH, BOX_WIDTH);
                g.setColor(Color.GRAY);
                g.drawString("" + cell, x + 2, y + BOX_WIDTH - 2);
            }
        };
        drawArea.addMouseListener(new MouseAdapter() {
            public void mouseReleased(MouseEvent e) {
                if (game.isGameOver())
                    setMessage("Game is Over - restart the game");
                else {
                    int col = (e.getX() - BORDER) / BOX_WIDTH;
                    int row = (e.getY() - BORDER) / BOX_WIDTH;
                    if (game.validPosition(row, col)) {
                        game.play(row, col);
                        if (game.isGameOver()) {
                            setMessage("Game Over!");
                            setWindowTitle("Game Over!");
                        } else if (game.isLightOn(row, col)) {
                            setMessage(" Toggle " + row + " x " + col + "...now ON");
                        } else {
                            setMessage(" Toggle " + row + " x " + col + "...now OFF");
                        }
                        drawArea.repaint();
                    }
                }
            }
        });

        win.getContentPane().add(drawArea, BorderLayout.CENTER);

        // Set the window size to fit everything snuggly
        win.setSize(400, 500);
        win.setVisible(true);
    }
}
