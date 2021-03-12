package commands;

import Data.LabWork;
import Exceptions.CollectionIsEmptyException;
import Exceptions.WrongFormatCommandException;
import ProgramUtility.Console;
import ProgramUtility.LabCollection;

/**
 * Command 'sum_of_minimal_point'.
 */
public class SumMiniPointCommand extends AbstractCommand {
    private final LabCollection labCollection;

    public SumMiniPointCommand(LabCollection labCollection) {
        super("sum_of_minimal_point", "display the sum of the values of the minimalPoint field for all elements of the collection");
        this.labCollection = labCollection;
    }


    /**
     * Executes the command.
     */
    @Override
    public void execute(String argument) {
        try {
            if (!argument.isEmpty()) throw new WrongFormatCommandException();
            if (labCollection.getLabCollection().isEmpty()) throw new CollectionIsEmptyException();

            long sum = 0;
            for (LabWork labWork : labCollection.getLabCollection()) {
                sum += labWork.getMinimalPoint();
            }

            Console.println("Sum of minimal point = " + sum);
        } catch (WrongFormatCommandException exception) {
            Console.printWarning("Using: '" + getName() + "'");
        } catch (CollectionIsEmptyException exception) {
            Console.printError("Collection is empty!");
        }
    }
}
