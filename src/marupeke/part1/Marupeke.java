package marupeke.part1;

import java.util.Random;


/**
 * The part 1 Marupeke grid. This is a simple implementation of a grid, where
 * the squares of the grid can be set editable as required.
 *
 */
public class Marupeke {


    // Not strictly necessary, since implicit in grid
    private final int size;
    private final char grid[][];
    private final boolean editable[][];


    /* the constructor */
    public Marupeke(int size) {
        // minimum and maximum size have been requested in the brief
        if (size < 3) {
            size = 3;
        } else if (size > 10) {
            size = 10;
        }
        this.size = size;
        grid = new char[this.size][this.size];
        editable = new boolean[this.size][this.size];
        for (int i = 0; i < editable.length; i++) {
            for (int j = 0; j < editable[i].length; j++) {
                editable[i][j] = true;
            }
        }
    }

    /**
     * getter for Size needed for testing
     *
     * @return the size of the grid
     */
    public int getSize() {
        return size;
    }

    /**
     * Extract a character from a location
     *
     * @param x
     * @param y
     * @return the character at that location
     */
    public char get(int x, int y) {
        return grid[y][x];
    }

    /**
     * editable status for a location
     *
     * @param x
     * @param y
     * @return the character at that location
     */
    public boolean getEditable(int x, int y) {
        return editable[y][x];
    }

    /**
     * Set the given square solid
     *
     * @param x
     * @param y
     * @return true if able to edit and set, false if not
     */
    public boolean setSolid(int x, int y) {
        return setGrid(x, y, false, '#');
    }

    /**
     * Set the given square X
     *
     * @param x
     * @param y
     * @return true if able to edit and set, false if not
     */
    public boolean setX(int x, int y, boolean canEdit) {
        return setGrid(x, y, canEdit, 'x');
    }

    /**
     * Set the given square )
     *
     * @param x
     * @param y
     * @return true if able to edit and set, false if not
     */
    public boolean setO(int x, int y, boolean canEdit) {
        return setGrid(x, y, canEdit, 'o');
    }

    // A convenience method since the public API does pretty much the same
    // thing.  Note that x and y are transposed in referencing the arrays
    private boolean setGrid(int x, int y, boolean canEdit, char c) {
        if (!editable[y][x]) {
            return false;
        }
        grid[y][x] = c;
        editable[y][x] = canEdit;
        return true;
    }

    public String toString() {
        String s = "";
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                if (grid[i][j] == 0) {
                    s += "_";   // one could have initialised the char-grid with '_' alternatively
                } else {
                    s += grid[i][j];
                }

            }
            s += "\n";
        }
        return s;
    }

    /**
     * Generate a random puzzle, filled with initial positions of the specified
     * numbers
     *
     * @param size
     * @param numFill
     * @param numX
     * @param numO
     * @return
     */
    public static Marupeke randomPuzzle(int size,
                                        int numFill,
                                        int numX,
                                        int numO) {
        Marupeke mp = new Marupeke(size);
            /* The following specified in the brief
                   if (size*size - numFill - numX -numO) > size*size)
                       { return null; }
                can only happen  if
                      numFill + numX + num0
               add up to a negative number.
               So one could put  the abvoe conditional in (and probably should according to spec!)
               I commented this out, however, because actually one probably
               wants to test that numFill+numX+num0 <= size*size.
               In any case one should be lenient regaridng this issue when marking.
             */
        Random rand = new Random();
        // There is repetition of code here, but removing the repetition
        // requires some functional manipulation which we haven't covered yet
        /* note that there is a problem if the filled numbers are larger than
           the grid this should not happen.
         */
        int countSolid = 0;
        while (countSolid < numFill) {
            if (mp.setSolid(rand.nextInt(size), rand.nextInt(size))) {
                countSolid++;
            }
        }
        int countX = 0;
        while (countX < numX) {
            if (mp.setX(rand.nextInt(size), rand.nextInt(size), false)) {
                countX++;
            }
        }
        int countO = 0;
        while (countO < numO) {
            if (mp.setO(rand.nextInt(size), rand.nextInt(size), false)) {
                countO++;
            }
        }
        return mp;
    }

}


