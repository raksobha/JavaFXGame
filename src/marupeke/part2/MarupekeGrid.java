package marupeke.part2;



import javafx.util.Pair;
import marupeke.part3.MarupekeGUI;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

/**
 */
public class MarupekeGrid {

    public enum Mark {UNMARK, CROSS, NOUGHT}
    final int size;
    final MarupekeTile grid[][];
    private MarupekeGUI ui;

    public void setGUI(MarupekeGUI ui) {
        this.ui = ui;
    }

    public MarupekeGrid(int size) {
        this.size = size;
        grid = new MarupekeTile[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                grid[i][j] = new MarupekeTile();
            }
        }
    }

    public int getSize() {
        return size;
    }

    public MarupekeTile get(int x, int y) {
        return grid[y][x];
    }

    public boolean setSolid(int x, int y) {
        return setGrid(x, y, false, MarupekeTile.Mark.SOLID);
    }

    public boolean markX(int x, int y) {
        return setX(x, y, grid[y][x].isEditable());
    }

    public boolean markO(int x, int y) {
        return setO(x, y, grid[y][x].isEditable());
    }

    public boolean setX(int x, int y, boolean canEdit) {
        return setGrid(x, y, canEdit, MarupekeTile.Mark.CROSS);
    }

    public boolean setO(int x, int y, boolean canEdit) {
        return setGrid(x, y, canEdit, MarupekeTile.Mark.NOUGHT);
    }

    public boolean unmark(int x, int y) {
        return setGrid(x, y, true, MarupekeTile.Mark.BLANK);
    }

    private boolean setGrid(int x, int y, boolean canEdit, MarupekeTile.Mark c) {
        if (!grid[y][x].isEditable()) {
            return false;
        }
        grid[y][x].setMark(c);
        grid[y][x].setEditable(canEdit);
        return true;
    }

    public String toString() {
        String s = "";
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                s += grid[i][j];
            }
            s += "\n";
        }
        return s;
    }

    public boolean isLegalGrid() {
        return illegalitiesInGrid().size() == 0;
    }

    public List<Reason> illegalitiesInGrid() {
        List<Reason> problems = new ArrayList();
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                if (grid[i][j].getMark() != MarupekeTile.Mark.CROSS
                        && grid[i][j].getMark() != MarupekeTile.Mark.NOUGHT) {
                    continue;
                }
                if (i > 0 && i < grid.length - 1) {
                    // do vertical tests
                    if (grid[i - 1][j].getMark() == grid[i][j].getMark()
                            && grid[i][j].getMark() == grid[i + 1][j].getMark()) {
                        problems.add(new GridReason(Reason.ViolationType.VERTICAL, grid[i][j].getMark(), j, i));
                    }
                }
                if (j > 0 && j < grid.length - 1) {
                    // do horizontal tests
                    if (grid[i][j - 1].getMark() == grid[i][j].getMark()
                            && grid[i][j].getMark() == grid[i][j + 1].getMark()) {
                        problems.add(new GridReason(Reason.ViolationType.HORIZONTAL, grid[i][j].getMark(), j, i));
                    }
                    if (i > 0 && i < grid.length - 1) {
                        if (grid[i - 1][j - 1].getMark() == grid[i][j].getMark()
                                && grid[i][j].getMark() == grid[i + 1][j + 1].getMark()) {
                            problems.add(new GridReason(Reason.ViolationType.DIAGONAL, grid[i][j].getMark(), j, i));
                        }
                        if (grid[i + 1][j - 1].getMark() == grid[i][j].getMark()
                                && grid[i][j].getMark() == grid[i - 1][j + 1].getMark()) {
                            problems.add(new GridReason(Reason.ViolationType.DIAGONAL, grid[i][j].getMark(), j, i));

                        }
                    }
                }
            }

        }
        return problems;
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
    // this method will create only legal grids in the first place
    public static MarupekeGrid randomPuzzle(int size,
                                            int numFill,
                                            int numX,
                                            int numO) throws TooManyMarkedSquares {
        if (numX + numO > size * size / 2) {
            throw new TooManyMarkedSquares();
        }
        MarupekeGrid mp = new MarupekeGrid(size);
        Random rand = new Random();
        // There is repetition of code here, but removing the repetition
        // requires some functional manipulation which we haven't covered yet
        int countSolid = 0;
        while (countSolid < numFill) {
            if (mp.setSolid(rand.nextInt(size), rand.nextInt(size))) {
                countSolid++;
            }
        }


        int countX = 0;
        int countO = 0;
        while (countX < numX || countO < numO) {
            int x = rand.nextInt(size);
            int y = rand.nextInt(size);
            if (countX < numX && mp.setX(x, y, true)) {
                if (mp.isLegalGrid()) {
                    mp.setX(x, y, false);
                    countX++;
                } else {
                    mp.unmark(x, y);
                }

            }
            if (countO < numO && mp.setO(x, y, true)) {
                if (mp.isLegalGrid()) {
                    mp.setO(x, y, false);
                    countO++;
                } else {
                    mp.unmark(x, y);
                }

            }
        }
        return mp;
    }

    public int remainingSpaces() {
        int count = 0;
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                if (grid[i][j].getMark() == MarupekeTile.Mark.BLANK) {
                    count++;
                }
            }
        }
        return count;
    }

    public boolean isPuzzleComplete() {
        return remainingSpaces() == 0;
    }


    // static factory method for bulding a game grid
    public static MarupekeGrid buildGameGrid(int size) throws TooManyMarkedSquares{
        int nuFi,nuX,nuO;
        MarupekeGrid grid = null;
        Random rand = new Random();
        nuFi = rand.nextInt(size*size/2);
        nuX = rand.nextInt(size*size/2 - nuFi);
        nuO = rand.nextInt(size*size/2 - nuFi - nuX);
        // build the game grid
        grid=randomPuzzle(size, nuFi, nuX, nuO);
        // changed to solvePuzzle so that only completable grids can be created
        while (!grid.solvePuzzle()) {
            System.out.println(grid.illegalitiesInGrid());
            grid=randomPuzzle(size, nuFi, nuX, nuO);

        }
        return grid;
    }

    // this is auxiliary for testing only
    public Pair<Integer,Integer> findCoordinatesWith(MarupekeTile.Mark match) {
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                MarupekeTile t = get(i, j);
                MarupekeTile.Mark m = t.getMark();
                if (m == match) {
                    return new Pair(i, j);
                }

            }
        }
        return null;
    }

    // choosing what value to fill label with
    public String pressed(Mark s, int x, int y) {
        switch (s){
            case UNMARK:
                unmark(y,x);
                ui.listReasons(illegalitiesInGrid());
                return "_";
            case CROSS:
                markX(y,x);
                ui.listReasons(illegalitiesInGrid());
                return "X";
            case NOUGHT:
                markO(y,x);
                ui.listReasons(illegalitiesInGrid());
                return "O";
            default:
                ui.listReasons(illegalitiesInGrid());
                return "_";
        }
    }

    public boolean solvePuzzle(){
        int counter = 0;
        boolean forward = true;
        Stack<MarupekeTile> stack = new Stack<>();
        int x = 0;
        int y = 0;

        while(counter >= 0){
            x = counter % size;
            y = counter / size;
            MarupekeTile.Mark mark = grid[y][x].getMark();
            if(mark == MarupekeTile.Mark.CROSS && grid[y][x].isEditable()) {
                    counter--;
                    stack.pop();
                    unmark(x,y);
            }
            else if(mark == MarupekeTile.Mark.NOUGHT && grid[y][x].isEditable()) {
                markX(x,y);
                mark = MarupekeTile.Mark.CROSS;
                if(!threeInARow(x,y, mark)) {
                    counter++;
                    forward = true;
                }
                else {
                    counter--;
                    unmark(x,y);
                    stack.pop();
                    forward = false;
                }
            }
            else if(!grid[y][x].isEditable() && !forward) {
                counter--;
                stack.pop();
            }
            else if(!grid[y][x].isEditable() && forward) {
                if(!threeInARow(x,y,mark)){
                    counter++;
                    stack.push(grid[y][x]);
                }
                else {
                    counter--;
                    forward = false;
                }
            }
            else{
                markO(x,y);
                mark = MarupekeTile.Mark.NOUGHT;
                stack.push(grid[y][x]);
                if(!threeInARow(x,y,mark)) {
                    counter++;
                }
            }
            if(stack.size() == size*size && !threeInARow(x,y,mark)){
                //testing purposes - console prints out completed grid
                System.out.println(toString());
                for(int i = 0; i < size; i++) {
                    for (int j = 0; j < size; j++) {
                        unmark(j,i);
                    }
                }
                return true;
            }
        }
        return false;
    }

    //checks whether there are three in a row preceeding the tile at x, y
    public boolean threeInARow(int x, int y, MarupekeTile.Mark mark){
        try {
            if(mark == grid[y][x-1].getMark() && mark == grid[y][x-2].getMark()) {
                return true;
            }
        }
        catch (IndexOutOfBoundsException ignored){}
        try {
            if(mark == grid[y-1][x].getMark() && mark == grid[y-2][x].getMark()) {
                return true;
            }
        }
        catch (IndexOutOfBoundsException ignored){}
        try {
            if(mark == grid[y-1][x-1].getMark() && mark == grid[y-2][x-2].getMark()) {
                return true;
            }
        }
        catch (IndexOutOfBoundsException ignored){}
        try {
            if(mark == grid[y-1][x+1].getMark() && mark == grid[y-2][x+2].getMark()) {
                return true;
            }
        }
        catch (IndexOutOfBoundsException ignored){}
        return false;
    }

    //saves grid as string representation, only if filename is new
    public void saveGrid(String fileName){
        String s = saveToString();
        try{
            File newFile = new File(fileName);
            if (newFile.createNewFile()) {
                try {
                    FileWriter myWriter = new FileWriter(fileName);
                    myWriter.write(s);
                    myWriter.close();
                    ui.alert("File Created");
                } catch (IOException e) {
                    ui.alert("An error occurred");
                }
            } else {
               ui.alert("File already exists");
            }
        }
        catch (IOException ignore) {
            ui.alert("An error occurred");
        }
    }

    // creates string -- different to toString() as it identifies which tiles are editable/ uneditable
    public String saveToString(){
        String s = "";
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                if (grid[i][j].isEditable()) {
                    s += grid[i][j];
                }
                else {
                    //ascii shift by 1 -- O-P, X-Y, #-$
                    //only done for those that aren't editable
                    char c = grid[i][j].toString().charAt(0);
                    int index;
                    index = (int)c;
                    index++;
                    c = (char)index;
                    s+= c;
                }
            }
            s += "\n";
        }
        return s;
    }


    //creates and fills grid with string saved in file
    public static MarupekeGrid buildFromFile(String s) throws FileNotFoundException {
        MarupekeGrid grid;
        int sizeOfGrid;

        //reads the string
        StringBuilder data = new StringBuilder();
        File fileName = new File(s);
        Scanner myReader = new Scanner(fileName);
        while (myReader.hasNextLine()) {
            data.append(myReader.nextLine());
        }
        myReader.close();

        //converts to String, trimming leading/trailing whitespace
        // gets size by taking total characters and finding the square root
        String sGrid = data.toString();
        sGrid = sGrid.trim();
        sizeOfGrid = (int)Math.sqrt(sGrid.length());
        grid = new MarupekeGrid(sizeOfGrid);

        int counter = 0;
        for (Character a : sGrid.toCharArray()) {
            //x and y calculated using remainder, and integer division
            int x = counter % sizeOfGrid;
            int y = counter / sizeOfGrid;
            // Y is X shifted by 1 so is uneditable X
            if(a == 'Y') {
                grid.setX(x,y,false);
            }
            // P is O shifted by 1 so is uneditable O
            else if(a == 'P') {
                grid.setO(x,y,false);
            }
            // $ is # shifted by 1
            else if(a == '$') {
                grid.setSolid(x,y);
            }
            else if(a == 'X') {
                grid.setX(x,y,true);
            }
            else if(a == 'O') {
                grid.setO(x,y,true);
            }
            counter++;
        }
        return grid;
    }
}

