package marupeke.part2;

import marupeke.part3.MarupekeGUI;
import marupekeParser.Command;
import marupekeParser.CommandWord;
import marupekeParser.Parser;

import java.io.InputStream;

/*
  This is the Game class which integrates all parts to implement
  a playable game from the console.
*/


public class Game {
    private MarupekeGrid game;
    private Parser parser;

    public MarupekeGrid getGame() {
        return game;
    }


    public Game() {
        parser = new Parser();
    }

    // this is where the magic happens
    void execute(Command c) {

        switch (c.getCommand()) {
            case MARKX:
                if (!game.markX(c.getColumn(), c.getRow())) c.setMsg(c.getMsg() + " uneditable tile");
                break;
            case MARKO:
                if (!game.markO(c.getColumn(), c.getRow())) c.setMsg(c.getMsg() + " uneditable tile");
                break;
            case CLEAR:
                if (!game.unmark(c.getColumn(), c.getRow())) c.setMsg(c.getMsg() + " uneditable tile");
                break;
            case QUIT: // exiting now
                break;
            default:
                System.out.println(c);
        }
        if (!game.isLegalGrid()) {
            System.out.println("illegal move");
            game.unmark(c.getColumn(), c.getRow());
        }
        printPrompt(c.getMsg());
    }

    public void initialiseCommand() throws TooManyMarkedSquares {
        int size = 0;
        Command c = new Command(CommandWord.UNKNOWN, 1, 1); // dummy initialisation
        // get the size loop
        while (c.getCommand() != CommandWord.NEW || (size < 4) || (size > 10)) {
            System.out.println("New Game:  enter \"new <n>\" where n is size of grid> ");
            c = parser.getCommand();
            size = c.getColumn();  // use column for size
        }
        // start the game proper
        game = MarupekeGrid.buildGameGrid(size);
        printPrompt(c.getMsg());
    }

    public void commandLine() {
        Command c = parser.getCommand();
        while (c.getCommand() != CommandWord.QUIT) {
            execute(c);
            // printPrompt(c.getMsg());
            c = parser.getCommand();
        }
    }

    private void printPrompt(String msg) {
        System.out.println(game);
        if (game.isPuzzleComplete()) {
            System.out.println("Congrats! You have completed the puzzle!");
            System.exit(0);
        } else {
            System.out.println(msg);
            System.out.print(">");
        }
    }

    // for testing only
    public void setParser(InputStream in) {
        parser = new Parser(in);
    }


    public static void main(String args[]) throws TooManyMarkedSquares {
        Game ct = new Game();
        ct.initialiseCommand();
        ct.commandLine();
    }
}

