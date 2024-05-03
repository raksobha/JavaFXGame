package marupeke.part2;

public class MarupekeTile {

        private Mark mark = Mark.BLANK;
        private boolean editable = true;



        public Mark getMark() {
            return mark;
        }

        public void setMark(Mark mark) {
            if (isEditable()) {
                this.mark = mark;
            }
        }

        public boolean isEditable() {
            return editable;
        }

        public void setEditable(boolean editable) {
            this.editable = editable;
        }

        @Override
        public String toString() {
            return getMark().toString();
        }

    public enum Mark {
        BLANK("_"), SOLID("#"), CROSS("X"), NOUGHT("O");
        private String representation;

        private Mark(String s) {
            this.representation = s;
        }

        public String toString() {
            return representation;
        }
    }
}
