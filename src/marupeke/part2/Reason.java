package marupeke.part2;

public abstract class Reason {

    public enum ViolationType {
        HORIZONTAL("horizontal"), VERTICAL("vertical"), DIAGONAL("diagonal");
        private String representation;

        private ViolationType(String s) {
            this.representation = s;
        }

        public String toString() {
            return representation;
        }
    }

      int xpos;
      int ypos;
      MarupekeTile.Mark m;

    public int getXpos() {
        return xpos;
    }


    public int getYpos() {
        return ypos;
    }

    public abstract ViolationType getVio();

    public MarupekeTile.Mark getM() {
        return m;
    }


}
