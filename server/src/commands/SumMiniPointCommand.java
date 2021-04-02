package commands;

import exceptions.CollectionIsEmptyException;
import exceptions.WrongFormatCommandException;
import utility.LabCollection;
import utility.ResponseOutputer;

/**
 * Command 'sum_of_minimal_point'.
 */
public class SumMiniPointCommand extends AbstractCommand {
    private final LabCollection labCollection;

    public SumMiniPointCommand(LabCollection labCollection) {
        super("sum_of_minimal_point","", "display the sum of the values of the minimalPoint field for all elements of the collection");
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

            long sum = labCollection.getSumMiniPoint();
            ResponseOutputer.appendln("Sum of minimal point = " + sum);
            return true;
        } catch (WrongFormatCommandException exception) {
            ResponseOutputer.appendWarning("Using: '" + getName() + "'");
        } catch (CollectionIsEmptyException exception) {
            ResponseOutputer.appendError("Collection is empty!");
        }
        return false;
    }
}
