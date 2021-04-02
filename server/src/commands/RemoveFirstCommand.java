package commands;

import exceptions.CollectionIsEmptyException;
import exceptions.WrongFormatCommandException;
import utility.LabCollection;
import utility.ResponseOutputer;

/**
 * Command 'remove_first'.
 */
public class RemoveFirstCommand extends AbstractCommand {
    private final LabCollection labCollection;

    public RemoveFirstCommand(LabCollection labCollection) {
        super("remove_first","" ,"delete the first element");
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
                labCollection.getLabCollection().removeFirst();
                ResponseOutputer.appendln("Removed first successfully!");
//            }
//            else {
//                Console.println("Remove first failed!");
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
