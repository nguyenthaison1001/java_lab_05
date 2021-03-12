package commands;

import Exceptions.CollectionIsEmptyException;
import Exceptions.WrongFormatCommandException;
import ProgramUtility.Console;
import ProgramUtility.LabAsker;
import ProgramUtility.LabCollection;

/**
 * Command 'remove_first'.
 */
public class RemoveFirstCommand extends AbstractCommand {
    private final LabCollection labCollection;
    private final LabAsker labAsker;

    public RemoveFirstCommand(LabCollection labCollection, LabAsker labAsker) {
        super("remove_first", "delete the first element");
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
                labCollection.getLabCollection().removeFirst();
                Console.println("Removed first successfully!");
            }
            else {
                Console.println("Remove first failed!");
            }
        } catch (WrongFormatCommandException exception) {
            Console.printWarning("Using: '" + getName() + "'");
        } catch (CollectionIsEmptyException exception) {
            Console.printWarning("Collection is empty!");
        }
    }
}
