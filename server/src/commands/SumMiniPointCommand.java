package commands;

import exceptions.CollectionIsEmptyException;
import exceptions.WrongFormatCommandException;
import interaction.User;
import utility.CollectionManager;
import utility.ResponseOutputer;

/**
 * Command 'sum_of_minimal_point'.
 */
public class SumMiniPointCommand extends AbstractCommand {
    private final CollectionManager collectionManager;

    public SumMiniPointCommand(CollectionManager collectionManager) {
        super("sum_of_minimal_point","", "display the sum of the values of the minimalPoint field for all elements of the collection");
        this.collectionManager = collectionManager;
    }


    /**
     * Executes the command.
     */
    @Override
    public boolean execute(String stringArg, Object objectArg, User user) {
        try {
            if (!stringArg.isEmpty() || objectArg != null) throw new WrongFormatCommandException();
            if (collectionManager.getLabCollection().isEmpty()) throw new CollectionIsEmptyException();
            long sum = collectionManager.getSumMiniPoint();
            ResponseOutputer.append("SumOfMiniPoint");
            ResponseOutputer.appendArgs(String.valueOf(sum));
            return true;
        } catch (WrongFormatCommandException exception) {
            ResponseOutputer.append("Using");
            ResponseOutputer.appendArgs(getName() + " " + getUsage() + "'");
        } catch (CollectionIsEmptyException exception) {
            ResponseOutputer.appendError("CollectionIsEmptyException");
        }
        return false;
    }
}
