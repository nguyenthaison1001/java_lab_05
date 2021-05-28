package commands;

import exceptions.WrongFormatCommandException;
import interaction.User;
import utility.ResponseOutputer;

/**
 * Command 'exit'.
 */
public class ExitCommand extends AbstractCommand {
    public ExitCommand() {
        super("exit", "", "shutdown the client (automatically saving to file)");
    }

    /**
     * Executes the command.
     */
    @Override
    public boolean execute(String stringArg, Object objectArg, User user) {
        try {
            if (!stringArg.isEmpty() || objectArg != null) throw new WrongFormatCommandException();
            return true;
        } catch (WrongFormatCommandException exception) {
            ResponseOutputer.append("Using");
            ResponseOutputer.appendArgs(getName() + " " + getUsage() + "'");
        }
        return false;
    }
}
