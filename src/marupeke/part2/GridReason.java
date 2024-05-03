package marupeke.part2;

/* This is a single concrete class inherign abstract class Reason
   One could have different classes for th edifferent kinds of reaosn but it turns out
   this is better modelled by an enum type (compare this to the MOney lab examples.
 */

public class GridReason extends Reason {




    public ViolationType getVio() {
        return vio;
    }

    public void setVio(ViolationType vio) {
        this.vio = vio;
    }

    private ViolationType vio;


    public  GridReason(ViolationType v, MarupekeTile.Mark t, int j, int i) {
        this.vio = v;
        this.m = t;
        this.xpos = j;
        this.ypos = i;

    }

    @Override
    public String toString() {
        return "More than two " + m + " in a " + vio + " row at  " + xpos + " " + ypos;
    }
}
