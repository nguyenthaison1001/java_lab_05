package commands;

import exceptions.WrongFormatCommandException;
import interaction.User;
import utility.ResponseOutputer;

/**
 * Command 'help'.
 */
public class HelpCommand extends AbstractCommand {

    public HelpCommand() {
        super("help", "", "display help for available commands");
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
