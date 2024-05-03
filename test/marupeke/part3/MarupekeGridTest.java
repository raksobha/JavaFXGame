package marupeke.part3;

import marupeke.part2.MarupekeGrid;
import marupeke.part2.MarupekeTile;
import marupeke.part2.Reason;
import marupeke.part2.TooManyMarkedSquares;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class MarupekeGridTest {

    private final String basicTopBlock = "_#__\n____\n____\n____\n";
    private final String basicTopX = "_#_x\n____\n____\n____\n";
    private final String basicXoXo = "_#_x\n_o__\nx___\n___o\n";
    private final String basicXoXoEdit = "_#_x\n_o__\nx_x_\n___o\n";
    private final String fullGrid = "o#xx\nxoxx\nxoxo\no#oo\n";
    private MarupekeGrid mp, mp_solidBlock, mp_basicFull, mp_legal;


    @Before
    // initialise some grids for testing
    public void setup() {
        mp = new MarupekeGrid(4);
        mp_legal = new MarupekeGrid(4);
        mp_solidBlock = new MarupekeGrid(4);
        mp_solidBlock.setSolid(1, 0);
        mp_basicFull = new MarupekeGrid(4);
        mp_basicFull.setSolid(1, 0);
        mp_basicFull.setX(3, 0, false);
        mp_basicFull.setO(1, 1, false);
        mp_basicFull.setO(3, 3, false);
        mp_basicFull.setX(0, 2, false);

        // three x across the top row for mp_legal used in illegality tests
        for (int i = 0; i < 2; i++) {
            mp_legal.setX(i, 0, true);
        }
    }

    @Test
    public void testSolvePuzzle() {
        //solvable puzzle
        assertTrue(mp_basicFull.solvePuzzle());

        //not solvable - creates a space at 1,1 which fails for both X and 0
        mp.setO(0,0,false);
        mp.setO(2,2, false);
        mp.setX(0,2, false);
        mp.setX(2,0, false);
        assertFalse(mp.solvePuzzle());

        //solvable puzzle, larger size
        MarupekeGrid mp2 = new MarupekeGrid(5);
        mp2.setSolid(3,1);
        mp2.setX(3,2, false);
        mp2.setX(1,4,false);
        mp2.setSolid(2,4);
        assertTrue(mp2.solvePuzzle());

        //not solvable - 3,0 - 2,1 - 1,2 all require a O to stop three x's but in return creates a diagonal
        MarupekeGrid mp3 = new MarupekeGrid(4);
        mp3.setX(0,1, false);
        mp3.setX(2,0, false);
        mp3.setX(2,2,false);
        mp3.setX(3,1, false);
        mp3.setX(3,2, false);
        mp3.setO(3,3,false);
        assertFalse(mp3.solvePuzzle());



    }

    @Test
    public void testThreeInARow() {
        //test vertical
        mp_basicFull.markO(3,2);
        mp_basicFull.markO(3,1);
        assertTrue(mp_basicFull.threeInARow(3,3, MarupekeTile.Mark.NOUGHT));

        //test horizontal
        mp_basicFull.unmark(3,2);
        mp_basicFull.unmark(3,1);
        mp_basicFull.markO(2,3);
        mp_basicFull.markO(1,3);
        assertTrue(mp_basicFull.threeInARow(3,3, MarupekeTile.Mark.NOUGHT));

        //test diagonal left
        mp_basicFull.unmark(2,3);
        mp_basicFull.unmark(1,3);
        mp_basicFull.markO(2,2);
        assertTrue(mp_basicFull.threeInARow(3,3, MarupekeTile.Mark.NOUGHT));

        //test diagonal right
        mp_basicFull.unmark(2,2);
        mp_basicFull.markO(0,3);
        mp_basicFull.markO(1,2);
        mp_basicFull.markO(2,1);
        assertTrue(mp_basicFull.threeInARow(0,3, MarupekeTile.Mark.NOUGHT));

        //test boundary
        assertFalse(mp_basicFull.threeInARow(0,0, MarupekeTile.Mark.NOUGHT));
    }

    @Test
    public void testOnlyFinishablePuzzle(){
        try {
            MarupekeGrid game = MarupekeGrid.buildGameGrid(5);
            assertTrue(game.isLegalGrid());
            MarupekeGrid game2 = MarupekeGrid.buildGameGrid(4);
            assertTrue(game2.isLegalGrid());
            MarupekeGrid game3 = MarupekeGrid.buildGameGrid(7);
            assertTrue(game3.isLegalGrid());
            MarupekeGrid game4 = MarupekeGrid.buildGameGrid(6);
            assertTrue(game4.isLegalGrid());
            MarupekeGrid game5 = MarupekeGrid.buildGameGrid(9);
            assertTrue(game5.isLegalGrid());
        }
        catch (TooManyMarkedSquares ignored){

        }

    }

    @Test
    public void testSaveToString(){
        mp.setSolid(1,0);
        mp.markO(3,0);
        mp.markO(1,1);
        mp.setX(3,1, false);
        mp.setO(0,2, false);
        mp.markX(2,2);
        mp.markX(0,3);
        mp.setO(2,3, false);
        mp.setSolid(3,3);
        assertEquals("_$_O\n_O_Y\nP_X_\nX_P$\n", mp.saveToString());
    }
}