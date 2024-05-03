package marupekeParser;

/**
 * A simple enumeration to represent the possible commands that can be given to the
 * Marupeke game.  Note that the enumeration words all start with different characters.
 */

public enum CommandWord {
    QUIT("quit"),
    MARKX("xmark"),
    MARKO("omark"),
    CLEAR("clear"),
    NEW("new"),
    UNKNOWN("unknown");

    private String word;

    CommandWord(String word) {
        this.word = word;
    }


    /**
     * Given the current word that the user has typed, convert it to lower case and see
     * if it matches the beginning of any of our command words.  If so, return the word
     *
     * @param s The current user input word
     * @return The corresponding command word
     */
    public static CommandWord getCommandWord(String s) {
        for (CommandWord c : CommandWord.values()) {
            if (c.getWord().startsWith(s.toLowerCase())) {
                return c;
            }
        }
        return UNKNOWN;
    }

    /**
     * @return the string representing the command
     */
    public String getWord() {
        return this.word;
    }


}


