package commands;

import Data.Difficulty;
import Data.LabWork;
import Exceptions.CollectionIsEmptyException;
import Exceptions.WrongFormatCommandException;
import ProgramUtility.Console;
import ProgramUtility.LabCollection;

/**
 * Command 'count_less_than_difficulty'.
 */
public class CountLessThanDiffCommand extends AbstractCommand {
    private final LabCollection labCollection;

    public CountLessThanDiffCommand(LabCollection labCollection) {
        super("count_less_than_difficulty difficulty", "display the number of elements whose difficulty field value is less than the specified one");
        this.labCollection = labCollection;
    }

    /**
     * Executes the command.
     */
    @Override
    public void execute(String argument) {
        try {
            if (argument.isEmpty()) throw new WrongFormatCommandException();
            if (labCollection.getLabCollection().isEmpty()) throw new CollectionIsEmptyException();

            Difficulty diff = Difficulty.valueOf(argument.toUpperCase());

            int count = 0;
            for (LabWork labWork : labCollection.getLabCollection()) {
                if (diff.compareTo(labWork.getDifficulty()) > 0)
                    count += 1;
            }
            Console.println("the number of elements = " + count);

        } catch (WrongFormatCommandException exception) {
            Console.printWarning("Using: '" + getName() + "'");
        } catch (CollectionIsEmptyException exception) {
            Console.printWarning("Collection is empty!");
        } catch (IllegalArgumentException exception) {
            Console.printError("This difficulty isn't in list!");
            Console.println("List of difficulties: " + Difficulty.nameList());
        }
    }
}
