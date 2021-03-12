package commands;

import Exceptions.WrongFormatCommandException;
import ProgramUtility.Console;

/**
 * Command 'execute_script'.
 */
public class ExecuteScriptCommand extends AbstractCommand {
    public ExecuteScriptCommand() {
        super("execute_script <file_name>", "execute script from specified file");
    }

    /**
     * Executes the command.
     */
    @Override
    public void execute(String argument) {
        try {
            if (argument.isEmpty()) throw new WrongFormatCommandException();
            Console.println("Executing the script '" + argument + "'...");
        } catch (WrongFormatCommandException exception) {
            Console.println("Using: '" + getName() + "'");
        }
    }
}
