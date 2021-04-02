package commands;

import exceptions.WrongFormatCommandException;
import utility.FileManager;
import utility.LabCollection;
import utility.ResponseOutputer;

/**
 * Command 'exit'.
 */
public class ExitCommand extends AbstractCommand {
    private final FileManager fileManager;

    public ExitCommand(LabCollection labCollection, String fileName) {
        super("exit", "", "shutdown the client (automatically saving to file)");
        this.fileManager = new FileManager(fileName, labCollection);
    }

    /**
     * Executes the command.
     */
    @Override
    public boolean execute(String stringArg, Object objectArg) {
        try {
            if (!stringArg.isEmpty() || objectArg != null) throw new WrongFormatCommandException();
            fileManager.writeCollection();
            return true;
        } catch (WrongFormatCommandException exception) {
            ResponseOutputer.appendWarning("Using: '" + getName() + "'");
        }
        return false;
    }
}
