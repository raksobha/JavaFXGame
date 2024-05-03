package marupeke.part2;


import javafx.util.Pair;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.assertEquals;

/*
   Test the actual game play, this uses redirection of output streams.
   The input stream can be a constant string using ByteArrayInputStream
   This was more sophisticated. Other ways to test and set up things may be possible.
   The difficulty is that the initial grid is random, so to be able to test the game play
   one has to check the game state after initialisation. This makes everything a bit more
   difficult.


 */

public class GameTest {

    private InputStream result;
    private Game ct;
    private ByteArrayOutputStream baos = new ByteArrayOutputStream();
    private PrintStream ps = new PrintStream(baos);
    private PrintStream old = System.out;

    // we use this method to create a game with a parser that has a fxied input stream
    // and a PrintStream as output stream (to read from)

    private void setUpGameTest(String str) {
        ByteArrayInputStream fakeInput = new ByteArrayInputStream(str.getBytes(StandardCharsets.UTF_8));
        ct = new Game();
        ct.setParser(fakeInput); // set the parser input stream to the constant result stream
        System.setOut(ps); // set the output stream to ps, so we can use it like a string
    }

    // we use this method to test individual commands after initialising a 4x4 grid.
    // editable is a parameter that is true if we check marking of an editable tile and
    // false if we check marking of an uneditable tile
    private void auxTestCommandFromCommandLine(char tile, boolean editable) throws TooManyMarkedSquares {
        setUpGameTest("n 4\n");
        Pair<Integer, Integer> p;
        ct.initialiseCommand();
        System.out.flush();
        MarupekeGrid g = ct.getGame();
        if (editable) {
            p = g.findCoordinatesWith(MarupekeTile.Mark.BLANK);
        } else {
            p = g.findCoordinatesWith(MarupekeTile.Mark.CROSS);
        }
        String cstr = tile + " " + p.getKey() + " " + p.getValue() + "\n q";
        result = new ByteArrayInputStream(cstr.getBytes(StandardCharsets.UTF_8));
        ct.setParser(result);
        ct.commandLine();
        System.out.flush();
        String b = baos.toString();
        System.setOut(old);  // set output stram back
        String[] tokens = b.split(">");
        assertEquals(ct.getGame().toString().trim()+(editable?"":"\n\n uneditable tile"), tokens[tokens.length - 1].trim());
        // trim removes leading and trailing whitespace (including new lines)
    }


    @Test
    public void testGameNew() throws TooManyMarkedSquares {
        setUpGameTest("n 4\n q");
        ct.initialiseCommand();
        System.out.flush();
        String b = baos.toString();
        System.setOut(old);
        assertEquals(4, ct.getGame().getSize());
        String[] tokens = b.split(">");
        assertEquals(ct.getGame().toString().trim(), tokens[tokens.length - 1].trim());
        // trim removes leading and trailing whitespace (including new lines)

    }


    @Test
    public void testGameMarkX() throws TooManyMarkedSquares {
        auxTestCommandFromCommandLine('x',true);
    }


    @Test
    public void testGameMarkXUneditable() throws TooManyMarkedSquares {
        auxTestCommandFromCommandLine('x',false);
    }

    @Test
    public void testGameMarkO() throws TooManyMarkedSquares {
        auxTestCommandFromCommandLine('o',true);
    }

    @Test
    public void testGameQuit() throws TooManyMarkedSquares {
        setUpGameTest("n 4\n");
        ct.initialiseCommand();
        String cstr = "q\n";
        result = new ByteArrayInputStream(cstr.getBytes(StandardCharsets.UTF_8));
        ct.setParser(result);
        ct.commandLine();
        System.out.flush();
        String b = baos.toString();
        int r = b.lastIndexOf('>');
        String compare = b.substring(r + 1); // what follows the last prompt >?
        System.setOut(old);  // set output stream back
        assertEquals("", compare);
    }

    @Test
    public void testGameUnknown() throws TooManyMarkedSquares {
        setUpGameTest("n 4\n");
        ct.initialiseCommand();
        String cstr = "yahoo 3 4\nq\n";
        result = new ByteArrayInputStream(cstr.getBytes(StandardCharsets.UTF_8));
        ct.setParser(result);
        ct.commandLine();
        System.out.flush();
        String b = baos.toString();
        String[] tokens = b.split(">");
        assertEquals("Command UNKNOWN, row=0, column=0\n" + ct.getGame().toString().trim() + "\n\nUnknown command: yahoo", tokens[tokens.length - 1].trim());
    }


}