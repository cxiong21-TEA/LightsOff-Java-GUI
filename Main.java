
/**
 * Implements a game similar to Lights Out, called LightsOff.
 * More info about the game: https://en.wikipedia.org/wiki/Lights_Out_(game)
 */
import java.util.Scanner;
import java.util.Random;

public class Project1Main implements LightsOffGUI
{
    private boolean[][] board;
    private int moves;
    private int wins;

    /* =========================
       Constructors
       ========================= */

    public Project1Main()
    {
        wins = 0;
        initialize();
    }

    public Project1Main(int boardIndex)
    {
        wins = 0;
        initialize(boardIndex);
    }

    public Project1Main(String boardStr)
    {
        wins = 0;
        initialize(boardStr);
    }

    /* =========================
       Initialization
       ========================= */

    public void initialize()
    {
        Random rand = new Random();
        int index = rand.nextInt(LightsOffGUI.boards.length);
        initialize(index);
    }

    public void initialize(int boardIndex)
    {
        if (boardIndex < 0 || boardIndex >= LightsOffGUI.boards.length)
        {
            boardIndex = 0;
        }
        initialize(LightsOffGUI.boards[boardIndex]);
    }

    public void initialize(String boardStr)
    {
        // reset moves
        moves = 0;

        // determine size by counting '|'
        int size = 0;
        for (int i = 0; i < boardStr.length(); i++)
        {
            if (boardStr.charAt(i) == '|')
            {
                size++;
            }
        }

        board = new boolean[size][size];

        Scanner sc = new Scanner(boardStr);

        int row = 0;
        int col = 0;

        while (sc.hasNext())
        {
            String token = sc.next();

            if (token.equals("|"))
            {
                row++;
                col = 0;
            }
            else if (token.equalsIgnoreCase("o"))
            {
                board[row][col] = true;
                col++;
            }
            else if (token.equalsIgnoreCase("x"))
            {
                board[row][col] = false;
                col++;
            }
        }

        sc.close();
    }

    /* =========================
       Gameplay
       ========================= */

    public void play(int row, int col)
    {
        if (isGameOver())
        {
            return;
        }

        toggle(row, col);
        toggle(row - 1, col);
        toggle(row + 1, col);
        toggle(row, col - 1);
        toggle(row, col + 1);

        moves++;

        if (isGameOver())
        {
            wins++;
        }
    }

    public void play(int buttonNumber)
    {
        int row = buttonNumber / size();
        int col = buttonNumber % size();
        play(row, col);
    }

    private void toggle(int row, int col)
    {
        if (validPosition(row, col))
        {
            board[row][col] = !board[row][col];
        }
    }

    /* =========================
       Accessors
       ========================= */

    public int numWins()
    {
        return wins;
    }

    public int size()
    {
        return board.length;
    }

    public int numMoves()
    {
        return moves;
    }

    public boolean validPosition(int row, int col)
    {
        return row >= 0 && row < size() &&
               col >= 0 && col < size();
    }

    public boolean isLightOn(int row, int col)
    {
        if (!validPosition(row, col))
        {
            return false;
        }
        return board[row][col];
    }

    public boolean isGameOver()
    {
        for (int r = 0; r < size(); r++)
        {
            for (int c = 0; c < size(); c++)
            {
                if (board[r][c])
                {
                    return false;
                }
            }
        }
        return true;
    }

    /* =========================
       toString
       ========================= */

    public String toString()
    {
        StringBuilder sb = new StringBuilder();

        for (int r = 0; r < size(); r++)
        {
            for (int c = 0; c < size(); c++)
            {
                sb.append(board[r][c] ? "o" : "x");
                if (c < size() - 1)
                {
                    sb.append(" ");
                }
            }
            sb.append(" |");
            if (r < size() - 1)
            {
                sb.append("\n");
            }
        }

        return sb.toString();
    }
}


    /**
     * Main method to show how to run the game.
     * 
     * @param args Command-line arguments not used in this project
     */
    public static void main(String[] args) {
        // 5 different examples of running the game of life
        // uncomment one and let it run

        Project1 game = new Project1("o o o | o x o | o o o |");
        System.out.println("\n\nShall we play a game?");
        System.out.println(game);
        System.out.println("Playing at 1,1");
        game.play(1, 1);
        System.out.println(game);
        System.out.println("number of moves: " + game.numMoves());
        System.out.println("number of wins: " + game.numWins());
        System.out.println("game over? " + game.isGameOver());

        System.out.println("\n\nLet's play another game");
        game.initialize(5);
        System.out.println(game);
        System.out.println("Playing at 1,1");
        game.play(1, 1);
        if (game.isGameOver())
            System.out.println("You won!");
        System.out.println(game);

        System.out.println("number of moves: " + game.numMoves());
        System.out.println("number of wins: " + game.numWins());
        System.out.println("game over? " + game.isGameOver());
    }
}
