package commands;

import Data.LabWork;
import Exceptions.CollectionIsEmptyException;
import Exceptions.LabWorkNotFoundException;
import Exceptions.WrongFormatCommandException;
import ProgramUtility.Console;
import ProgramUtility.LabAsker;
import ProgramUtility.LabCollection;

/**
 * Command 'remove_by_id'.
 */
public class RemoveByIDCommand extends AbstractCommand {
    private final LabCollection labCollection;
    private final LabAsker labAsker;


    public RemoveByIDCommand(LabCollection labCollection, LabAsker labAsker) {
        super("remove_by_id <id>", "remove element from collection by ID");
        this.labCollection = labCollection;
        this.labAsker = labAsker;
    }

    /**
     * Executes the command.
     */
    @Override
    public void execute(String argument) {
        try {
            if (argument.isEmpty()) throw new WrongFormatCommandException();
            if (labCollection.getLabCollection().isEmpty()) throw new CollectionIsEmptyException();

            Integer id = Integer.parseInt(argument);
            LabWork labToRemove = labCollection.getLabByID(id);
            if (labToRemove == null) throw new LabWorkNotFoundException();

            if (labAsker.areYouSure()) {
                labCollection.getLabCollection().remove(labToRemove);
                Console.println("Removed successfully!");
            }
            else {
                Console.println("Remove failed!");
            }

        } catch (WrongFormatCommandException exception) {
            Console.printWarning("Using: '" + getName() + "'");
        } catch (CollectionIsEmptyException exception) {
            Console.printWarning("Collection is empty!");
        } catch (LabWorkNotFoundException exception) {
            Console.printError("LabWork ID not found!");
        } catch (NumberFormatException exception) {
            Console.printError("ID must be a number!");
        }
    }
}
