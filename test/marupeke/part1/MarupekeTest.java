package marupeke.part1;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;



public class MarupekeTest {

    private final String basicTopBlock = "_#__\n____\n____\n____\n";
    private final String basicTopX = "_#_x\n____\n____\n____\n";
    private final String basicXoXo = "_#_x\n_o__\nx___\n___o\n";
    private final String basicXoXoEdit = "_#_x\n_o__\nx_x_\n___o\n";
    private final String fullGrid = "o#xx\nxoxx\nxoxo\no#oo\n";
    private Marupeke mp, mp_solidBlock, mp_basicFull;


    @Before
    // initialise some grids for testing
    public void setup() {
        mp = new Marupeke(4);
        mp_solidBlock = new Marupeke(4);
        mp_solidBlock.setSolid(1, 0);
        mp_basicFull = new Marupeke(4);
        mp_basicFull.setSolid(1, 0);
        mp_basicFull.setX(3, 0, false);
        mp_basicFull.setO(1, 1, false);
        mp_basicFull.setO(3, 3, false);
        mp_basicFull.setX(0, 2, false);
    }


    // auxiliary method for constructor testing
    public void templateTestConstructor(int size, int expected_size) {
        Marupeke grid = new Marupeke(size);
        assertNotNull(grid);
        assertEquals(expected_size, grid.getSize());
        for (int i = 0; i < grid.getSize(); i++) {
            for (int j = 0; j < grid.getSize(); j++) {
                assertEquals(0, grid.get(i, j));
                assertEquals(true, grid.getEditable(i, j));
            }
        }

    }

    //auxiliary method for randomPuzzle Testing
    public void templateTestRandomPuzzle(Marupeke mg, int numFill, int numX, int numO) {
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

    @Test
    public void testConstructor0() {
        templateTestConstructor(0, 3);

    }

    @Test
    public void testConstructor42() {
        templateTestConstructor(42, 10);

    }



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
        assertEquals('x', mp_solidBlock.get(3, 0));
        assertFalse(mp_solidBlock.setO(3, 0, true));
        assertEquals('x', mp_solidBlock.get(3, 0));
        // Make sure everything else is still as before after setting the above
        // three tests here:
        assertEquals(0, mp_solidBlock.get(0, 3)); // unset means 0 char
        assertEquals(0, mp_solidBlock.get(3, 3)); // unset means 0 char
        assertEquals('#', mp_solidBlock.get(1, 0));
        // but one should BETTER test systematically that ALL other squares are still the same too:
        // We can do this in one go with toString (we need to test its correctness separately:
        assertEquals(basicTopX, mp_solidBlock.toString());
    }

    @Test
    public void testSet0andX(){
        mp_solidBlock.setX(3, 0, false); // as with other test but now
        // Make sure the following now works else works unaffected by the above
        assertTrue(mp_solidBlock.setO(1, 1, false));
        assertTrue(mp_solidBlock.setO(3, 3, false));
        assertTrue(mp_solidBlock.setX(0, 2, false));
        assertEquals('o',mp_solidBlock.get(1,1));
        assertEquals('o',mp_solidBlock.get(3,3));
        assertEquals('x',mp_solidBlock.get(0,2));
        // again instead of just checking those three, check also the rest is unaffected,
        // use toString:
        assertEquals(basicXoXo,mp_basicFull.toString());
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
    public void testToStringSolid(){
        String s = mp_solidBlock.toString();
        assertEquals(basicTopBlock,s);
    }

    @Test
    public void testToStringSolidAndCornerX(){
        mp_solidBlock.setX(3, 0, false);
        String s = mp_solidBlock.toString();
        assertEquals(basicTopX,s);
    }

    @Test
    public void testToStringSeveral(){
        String s = mp_basicFull.toString();
        assertEquals(basicXoXo,s);
    }



    // Fourth Group: makeRandomPuzzle Tests
    // requires all the other things to work fine

    /* Important; the spec did not specify what should happen if
       numFill + numX + numO > size^2.
       The sentence in the brief had a typo so asked for
       size^2 - (numFill + numX + numO) > size^2
       which is automatically true :-)
       So one only needs to test for correct arguments.
     */

    @Test
    public void testRandomPuzzle10_33() {

        Marupeke mpr = Marupeke.randomPuzzle(10, 33, 33, 33);
        templateTestRandomPuzzle(mpr, 33, 33, 33);
    }

    @Test
    public void testRandomPuzzle4_FullGrid() {

        Marupeke mpr = Marupeke.randomPuzzle(4, 3, 11, 2);
        templateTestRandomPuzzle(mpr, 3, 11, 2);
    }

    @Test
    public void testRandomPuzzle4_NonFullGrid() {

        Marupeke mpr = Marupeke.randomPuzzle(4, 6, 2, 1);
        templateTestRandomPuzzle(mpr, 6, 2, 1);
    }

    @Test
    public void testRandomPuzzle4_Nothing() {

        Marupeke mpr = Marupeke.randomPuzzle(4, 0, 0, 0);
        templateTestRandomPuzzle(mpr, 0, 0, 0);
    }

    @Test
    public void testRandomPuzzle10_FullGrid() {

        Marupeke mpr = Marupeke.randomPuzzle(10, 30, 40, 30);
        templateTestRandomPuzzle(mpr, 30, 40, 30);
    }


}
