package commands;

/**
 * Command 'help'.
 */
public class HelpCommand extends AbstractCommand {

    public HelpCommand() {
        super("help", "display help for available commands");
    }

    /**
     * Executes the command.
     */
    @Override
    public void execute(String argument) {}
}
