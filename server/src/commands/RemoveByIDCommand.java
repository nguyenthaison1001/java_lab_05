package commands;

import data.LabWork;
import exceptions.CollectionIsEmptyException;
import exceptions.LabWorkNotFoundException;
import exceptions.WrongFormatCommandException;
import utility.LabCollection;
import utility.ResponseOutputer;

/**
 * Command 'remove_by_id'.
 */
public class RemoveByIDCommand extends AbstractCommand {
    private final LabCollection labCollection;


    public RemoveByIDCommand(LabCollection labCollection) {
        super("remove_by_id <id>", "", "remove element from collection by ID");
        this.labCollection = labCollection;
    }

    /**
     * Executes the command.
     */
    @Override
    public boolean execute(String stringArg, Object objectArg) {
        try {
            if (stringArg.isEmpty() || objectArg != null) throw new WrongFormatCommandException();
            if (labCollection.getLabCollection().isEmpty()) throw new CollectionIsEmptyException();

            Integer id = Integer.parseInt(stringArg);
            LabWork labToRemove = labCollection.getLabByID(id);
            if (labToRemove == null) throw new LabWorkNotFoundException();

//            if (labAsker.areYouSure()) {
                labCollection.getLabCollection().remove(labToRemove);
            ResponseOutputer.appendln("Removed successfully!");
//            }
//            else {
//                Console.println("Remove failed!");
//            }
            return true;
        } catch (WrongFormatCommandException exception) {
            ResponseOutputer.appendWarning("Using: '" + getName() + "'");
        } catch (CollectionIsEmptyException exception) {
            ResponseOutputer.appendWarning("Collection is empty!");
        } catch (LabWorkNotFoundException exception) {
            ResponseOutputer.appendError("LabWork ID not found!");
        } catch (NumberFormatException exception) {
            ResponseOutputer.appendError("ID must be a number!");
        }
        return false;
    }
}
