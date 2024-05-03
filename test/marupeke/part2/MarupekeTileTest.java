package marupeke.part2;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;




public class MarupekeTileTest {

    private MarupekeTile t;

    @Before
    public void setUp() {
        t = new MarupekeTile();
    }

    @Test
    public void testMPTile() {
        assertTrue(t.isEditable());
        assertEquals(MarupekeTile.Mark.BLANK, t.getMark());
    }

    @Test
    public void testMPTileSetSolidThenBlank() {
        t.setMark(MarupekeTile.Mark.SOLID);
        assertEquals(MarupekeTile.Mark.SOLID, t.getMark());
        t.setEditable(false);
        assertTrue(!t.isEditable());
        t.setMark(MarupekeTile.Mark.BLANK);
        assertEquals(MarupekeTile.Mark.SOLID, t.getMark());
    }

    @Test
    public void testMPTileSetXThenBlank() {
        t.setMark(MarupekeTile.Mark.CROSS);
        assertEquals(MarupekeTile.Mark.CROSS, t.getMark());
        t.setEditable(false);
        assertTrue(!t.isEditable());
        t.setMark(MarupekeTile.Mark.BLANK);
        assertEquals(MarupekeTile.Mark.CROSS, t.getMark());
    }

    @Test
    public void testMPTileSetOThenBlank() {
        t.setMark(MarupekeTile.Mark.NOUGHT);
        assertEquals(MarupekeTile.Mark.NOUGHT, t.getMark());
        t.setEditable(false);
        assertTrue(!t.isEditable());
        t.setMark(MarupekeTile.Mark.BLANK);
        assertEquals(MarupekeTile.Mark.NOUGHT, t.getMark());
    }

    @Test
    public void testMPTileSetSequence() {
        t.setMark(MarupekeTile.Mark.NOUGHT);
        assertEquals(MarupekeTile.Mark.NOUGHT, t.getMark());
        t.setMark(MarupekeTile.Mark.BLANK);
        assertEquals(MarupekeTile.Mark.BLANK, t.getMark());
        t.setMark(MarupekeTile.Mark.CROSS);
        assertEquals(MarupekeTile.Mark.CROSS, t.getMark());
    }


}