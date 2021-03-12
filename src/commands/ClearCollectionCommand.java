package commands;

import Exceptions.CollectionIsEmptyException;
import Exceptions.WrongFormatCommandException;
import ProgramUtility.Console;
import ProgramUtility.LabAsker;
import ProgramUtility.LabCollection;

/**
 * Command 'clear'.
 */
public class ClearCollectionCommand extends AbstractCommand {
    private final LabCollection labCollection;
    private final LabAsker labAsker;

    public ClearCollectionCommand(LabCollection labCollection, LabAsker labAsker){
        super("clear", "clear collection");
        this.labCollection = labCollection;
        this.labAsker = labAsker;
    }

    /**
     * Executes the command.
     */
    @Override
    public void execute(String argument) {
        try {
            if (!argument.isEmpty()) throw new WrongFormatCommandException();
            if (labCollection.getLabCollection().isEmpty()) throw new CollectionIsEmptyException();
            if (labAsker.areYouSure()) {
                labCollection.getLabCollection().clear();
                Console.println("Cleared successfully!");
            }
            else {
                Console.println("Clear failed!");
            }
        } catch (WrongFormatCommandException exception) {
            Console.printWarning("Using: '" + getName() + "'");
        } catch (CollectionIsEmptyException exception) {
            Console.printWarning("Collection is empty!");
        }
    }
}
