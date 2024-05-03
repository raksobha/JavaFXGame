package marupeke.part2;

import org.junit.Before;
import org.junit.Test;

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


    // auxiliary method for constructor testing
    public void templateTestConstructor(int size, int expected_size) {
        MarupekeGrid grid = new MarupekeGrid(size);
        assertNotNull(grid);
        assertEquals(expected_size, grid.getSize());
        for (int i = 0; i < grid.getSize(); i++) {
            for (int j = 0; j < grid.getSize(); j++) {
                assertEquals(MarupekeTile.Mark.BLANK, grid.get(i, j).getMark());
            }
        }

    }

    //auxiliary method for randomPuzzle Testing
    public void templateTestRandomPuzzle(MarupekeGrid mg, int numFill, int numX, int numO) throws TooManyMarkedSquares {
        // requires toString to be tested correct already
        String s = mg.toString();
        int[] counter = new int[3];
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == '#') {
                counter[0]++;
            } else if (s.charAt(i) == 'x') {
                counter[1]++;
            }
            if (s.charAt(i) == 'o') {
                counter[2]++;
            }
        }
        // now test whether expect numbers found!
        assertEquals(numFill, counter[0]);
        assertEquals(numX, counter[1]);
        assertEquals(numO, counter[2]);
    }

// here come te actual tests

    // First group: constructor tests
    @Test
    public void testConstructor4() {
        templateTestConstructor(4, 4);

    }

  /*  These test are incorrect now due to change in spec

   @Test

    public void testConstructor0() {
        templateTestConstructor(0, 3);

    }

    @Test
    public void testConstructor42() {
        templateTestConstructor(42, 10);

    }
    */


    // Second Group: Set* Tests

    @Test
    public void testSetSolid() {

        assertTrue(mp.setSolid(1, 0));
        assertEquals(basicTopBlock, mp.toString());
        // Having set the solid, we shouldn't be able to overwrite/edit
        assertFalse(mp.setX(1, 0, false));
        assertEquals(basicTopBlock, mp.toString());
        assertFalse(mp.setO(1, 0, false));
        assertEquals(basicTopBlock, mp.toString());
    }

    @Test
    public void testSetX() {
        // Check we can edit and then not change on X
        assertTrue(mp_solidBlock.setX(3, 0, false));
        assertEquals(MarupekeTile.Mark.CROSS, mp_solidBlock.get(3, 0).getMark());
        assertFalse(mp_solidBlock.setO(3, 0, true));
        assertEquals(MarupekeTile.Mark.CROSS, mp_solidBlock.get(3, 0).getMark());
        // Make sure everything else is still as before after setting the above
        // three tests here:
        assertEquals(MarupekeTile.Mark.BLANK, mp_solidBlock.get(0, 3).getMark()); // unset means 0 char
        assertEquals(MarupekeTile.Mark.BLANK, mp_solidBlock.get(3, 3).getMark()); // unset means 0 char
        assertEquals(MarupekeTile.Mark.SOLID, mp_solidBlock.get(1, 0).getMark());
        // but one should BETTER test systematically that ALL other squares are still the same too:
        // We can do this in one go with toString (we need to test its correctness separately:
        assertEquals(basicTopX, mp_solidBlock.toString());
    }

    @Test
    public void testSet0andX() {
        mp_solidBlock.setX(3, 0, false); // as with other test but now
        // Make sure the following now works else works unaffected by the above
        assertTrue(mp_solidBlock.setO(1, 1, false));
        assertTrue(mp_solidBlock.setO(3, 3, false));
        assertTrue(mp_solidBlock.setX(0, 2, false));
        assertEquals(MarupekeTile.Mark.NOUGHT, mp_solidBlock.get(1, 1).getMark());
        assertEquals(MarupekeTile.Mark.NOUGHT, mp_solidBlock.get(3, 3).getMark());
        assertEquals(MarupekeTile.Mark.CROSS, mp_solidBlock.get(0, 2).getMark());
        // again instead of just checking those three, check also the rest is unaffected,
        // use toString:
        assertEquals(basicXoXo, mp_basicFull.toString());
    }

    @Test
    public void testSet0X_MakeEditableAndOverwrite() {
        // Can we reverse and back out
        // Here we use toString() (see above) in order to check that
        // nothing bad has happened on other squares too.
        assertTrue(mp_basicFull.setO(2, 2, true));
        assertNotEquals(basicXoXoEdit, mp_basicFull.toString());
        assertTrue(mp_basicFull.setX(2, 2, true));
        assertEquals(basicXoXoEdit, mp_basicFull.toString());
    }

    // Second Group: toString tests
    // requires Setters and Constructor to work fine.
    @Test
    public void testToStringSolid() {
        String s = mp_solidBlock.toString();
        assertEquals(basicTopBlock, s);
    }

    @Test
    public void testToStringSolidAndCornerX() {
        mp_solidBlock.setX(3, 0, false);
        String s = mp_solidBlock.toString();
        assertEquals(basicTopX, s);
    }

    @Test
    public void testToStringSeveral() {
        String s = mp_basicFull.toString();
        assertEquals(basicXoXo, s);
    }


    // Fourth Group: makeRandomPuzzle Tests
    // requires all the other things to work fine
    // Note how one can test for an exception being thrown
    // using   @Test(expected = TooManyMarkedSquares.class)
    // Something good students should have found out (there are also slides in my TDD lecture)

    @Test(expected = TooManyMarkedSquares.class)
    public void testRandomPuzzle10_33() throws TooManyMarkedSquares {

        MarupekeGrid mpr = MarupekeGrid.randomPuzzle(10, 33, 33, 33);
        templateTestRandomPuzzle(mpr, 33, 33, 33);
    }

    @Test(expected = TooManyMarkedSquares.class)
    public void testRandomPuzzle4_FullGrid() throws TooManyMarkedSquares {

        MarupekeGrid mpr = MarupekeGrid.randomPuzzle(4, 3, 11, 2);
        templateTestRandomPuzzle(mpr, 3, 11, 2);
    }

    @Test
    public void testRandomPuzzle4_NonFullGrid() throws TooManyMarkedSquares {

        MarupekeGrid mpr = MarupekeGrid.randomPuzzle(4, 6, 2, 1);
        templateTestRandomPuzzle(mpr, 6, 2, 1);
    }

    @Test
    public void testRandomPuzzle4_Nothing() throws TooManyMarkedSquares {

        MarupekeGrid mpr = MarupekeGrid.randomPuzzle(4, 0, 0, 0);
        templateTestRandomPuzzle(mpr, 0, 0, 0);
    }

    @Test(expected = TooManyMarkedSquares.class)
    public void testRandomPuzzle10_FullGrid() throws TooManyMarkedSquares {

        MarupekeGrid mpr = MarupekeGrid.randomPuzzle(10, 30, 40, 30);
        templateTestRandomPuzzle(mpr, 30, 40, 30);
    }

    /* now the newly added tests for isLegal
      uses mp_legal
     */



    @Test
    public void testLegalInit() {
        assertTrue(mp_legal.isLegalGrid());
    }

    @Test
    public void testLegalSetX20() {
        mp_legal.setX(2, 0, true);
        assertFalse(mp_legal.isLegalGrid());
        assertEquals(1, mp_legal.illegalitiesInGrid().size());
        assertEquals(1, mp_legal.illegalitiesInGrid().get(0).getXpos());
        assertEquals(0, mp_legal.illegalitiesInGrid().get(0).getYpos());
        assertEquals(GridReason.ViolationType.HORIZONTAL, mp_legal.illegalitiesInGrid().get(0).getVio());
    }

    @Test
    public void testLegalRightDiagonalX() {
        // three right sloping x
        mp_legal.setX(1, 3, true);
        mp_legal.setX(2, 2, true);
        assertTrue(mp_legal.isLegalGrid());
        mp_legal.setX(3, 1, true);
        assertFalse(mp_legal.isLegalGrid());
        assertEquals(2, mp_legal.illegalitiesInGrid().get(0).getXpos());
        assertEquals(2, mp_legal.illegalitiesInGrid().get(0).getYpos());
        assertEquals(Reason.ViolationType.DIAGONAL, mp_legal.illegalitiesInGrid().get(0).getVio());
    }

    @Test
    public void testLegalLeftDiagonalO() {
        // three left sloping o
        mp_legal.setO(0, 1, true);
        mp_legal.setO(1, 2, true);
        assertTrue(mp.isLegalGrid());
        mp_legal.setO(2, 3, true);
        assertFalse(mp_legal.isLegalGrid());
        assertEquals(1, mp_legal.illegalitiesInGrid().get(0).getXpos());
        assertEquals(2, mp_legal.illegalitiesInGrid().get(0).getYpos());
        assertEquals(Reason.ViolationType.DIAGONAL, mp_legal.illegalitiesInGrid().get(0).getVio());
    }

    @Test
    public void testLegalSetOVertical() {
        // three o across the right column
        for (int i = 1; i < 3; i++) {
            mp_legal.setO(3, i, true);
        }
        assertTrue(mp_legal.isLegalGrid());
        mp_legal.setO(3, 3, true);
        assertFalse(mp_legal.isLegalGrid());
        assertEquals(3, mp_legal.illegalitiesInGrid().get(0).getXpos());
        assertEquals(2, mp_legal.illegalitiesInGrid().get(0).getYpos());
        assertEquals(Reason.ViolationType.VERTICAL, mp_legal.illegalitiesInGrid().get(0).getVio());
        // three solid across the top
        mp_legal.unmark(3, 3);
        mp_legal.setSolid(0, 0);
        mp_legal.setSolid(1, 1);
        mp_legal.setSolid(2, 2);
        assertTrue(mp_legal.isLegalGrid());

    }


    @Test
    public void testMultipleIllegalities() {
        mp_legal.setX(2, 0, true);
        assertEquals(1, mp_legal.illegalitiesInGrid().size());
        // three right sloping x
        mp_legal.setX(1, 3, true);
        mp_legal.setX(2, 2, true);
        mp_legal.setX(3, 1, true);
        assertEquals(2, mp_legal.illegalitiesInGrid().size());
        // three left sloping x
        mp_legal.setX(0, 1, true);
        mp_legal.setX(1, 2, true);
        mp_legal.setX(2, 3, true);
        assertEquals(3, mp_legal.illegalitiesInGrid().size());
        // three o across the right column, removing right slope problem
        for (int i = 1; i < 4; i++) {
            mp_legal.setO(3, i, true);
        }
        assertEquals(3, mp_legal.illegalitiesInGrid().size());

    }
}
