package commands;

import exceptions.CollectionIsEmptyException;
import exceptions.WrongFormatCommandException;
import utility.LabCollection;
import utility.ResponseOutputer;

/**
 * Command 'clear'.
 */
public class ClearCollectionCommand extends AbstractCommand {
    private final LabCollection labCollection;

    public ClearCollectionCommand(LabCollection labCollection) {
        super("clear","", "clear collection");
        this.labCollection = labCollection;
    }

    /**
     * Executes the command.
     */
    @Override
    public boolean execute(String stringArg, Object objectArg) {
        try {
            if (!stringArg.isEmpty() || objectArg != null) throw new WrongFormatCommandException();
            if (labCollection.getLabCollection().isEmpty()) throw new CollectionIsEmptyException();
//            if (labAsker.areYouSure()) {
                labCollection.getLabCollection().clear();
            ResponseOutputer.appendln("Cleared successfully!");
//            }
//            else {
//                Console.println("Clear failed!");
//            }
            return true;
        } catch (WrongFormatCommandException exception) {
            ResponseOutputer.appendWarning("Using: '" + getName() + "'");
        } catch (CollectionIsEmptyException exception) {
            ResponseOutputer.appendWarning("Collection is empty!");
        }
        return false;
    }
}
