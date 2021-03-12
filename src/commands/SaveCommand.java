package commands;

import Exceptions.WrongFormatCommandException;
import ProgramUtility.Console;
import ProgramUtility.FileManager;
import ProgramUtility.LabCollection;

/**
 * Command 'save'.
 */
public class SaveCommand extends AbstractCommand {
    private final FileManager fileManager;

    public SaveCommand(LabCollection labCollection, String fileName) {
        super("save", "save the collection to file");
        this.fileManager = new FileManager(fileName, labCollection);
    }

    /**
     * Executes the command.
     */
    @Override
    public void execute(String argument) {
        try {
            if (!argument.isEmpty()) throw new WrongFormatCommandException();
            fileManager.writeCollection();
            Console.println("Collection is saved!");
        } catch (WrongFormatCommandException exception) {
            Console.printWarning("Using: '" + getName() + "'");
        }
    }
}
