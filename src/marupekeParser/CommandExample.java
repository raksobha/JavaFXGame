package marupekeParser;

/**
 * A very simple example of how the command parser can be used
 */

public class CommandExample {
    Parser parser = new Parser();

    public CommandExample() {

    }

    private void execute(Command c) {

        switch (c.getCommand()) {
            case MARKO:
            case MARKX:
                System.out.println(c);
                break;
            case QUIT:
                break;
            default:
                System.out.println(c);
        }
        printPrompt(c.getMsg());
    }

    private void commandLine() {
        printPrompt("New Game");
        Command c = parser.getCommand();
        while (c.getCommand() != CommandWord.QUIT) {
            execute(c);
            c = parser.getCommand();
        }
        System.out.println("Bye bye");

    }

    private void printPrompt(String msg) {
        System.out.println(msg);
        System.out.print(">");
    }

    public static void main(String args[]) {
        CommandExample ct = new CommandExample();
        ct.commandLine();
    }
}
