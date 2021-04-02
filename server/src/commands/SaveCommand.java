package commands;

import exceptions.WrongFormatCommandException;

import utility.FileManager;
import utility.LabCollection;
import utility.ResponseOutputer;

/**
 * Command 'save'.
 */
public class SaveCommand extends AbstractCommand {
    private final FileManager fileManager;

    public SaveCommand(LabCollection labCollection, String fileName) {
        super("save","", "save the collection to file");
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
            System.out.println("Saved the collection.");
            return true;
        } catch (WrongFormatCommandException exception) {
            ResponseOutputer.appendWarning("Using: '" + getName() + "'");
        }
        return false;
    }
}
