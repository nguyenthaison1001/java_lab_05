package commands;

import exceptions.WrongFormatCommandException;
import interaction.User;
import utility.ResponseOutputer;

/**
 * Command 'execute_script'.
 */
public class ExecuteScriptCommand extends AbstractCommand {
    public ExecuteScriptCommand() {
        super("execute_script <file_name>", "", "execute script from specified file");
    }

    /**
     * Executes the command.
     */
    @Override
    public boolean execute(String stringArg, Object objectArg, User user) {
        try {
            if (stringArg.isEmpty() || objectArg != null) throw new WrongFormatCommandException();
            return true;
        } catch (WrongFormatCommandException exception) {
            ResponseOutputer.append("Using");
            ResponseOutputer.appendArgs(getName() + " " + getUsage() + "'");
        }
        return false;
    }
}
