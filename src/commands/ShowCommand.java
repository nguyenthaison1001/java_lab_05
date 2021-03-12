package commands;

import Exceptions.WrongFormatCommandException;
import ProgramUtility.LabCollection;
import ProgramUtility.Console;

/**
 * Command 'show'.
 */
public class ShowCommand extends AbstractCommand {
    private final LabCollection labCollection;

    public ShowCommand(LabCollection labCollection) {
        super("show", "display all elements of the collection");
        this.labCollection = labCollection;
    }

    /**
     * Executes the command.
     */
    @Override
    public void execute(String argument) {
        try {
            if (!argument.isEmpty()) throw new WrongFormatCommandException();
            Console.println(labCollection);
        } catch (WrongFormatCommandException exception) {
            Console.printWarning("Using: '" + getName() + "'");
        }
    }
}
