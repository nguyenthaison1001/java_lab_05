package commands;

import data.Difficulty;
import data.LabWork;
import exceptions.CollectionIsEmptyException;
import exceptions.WrongFormatCommandException;
import utility.LabCollection;
import utility.ResponseOutputer;

/**
 * Command 'count_less_than_difficulty'.
 */
public class CountLessThanDiffCommand extends AbstractCommand {
    private final LabCollection labCollection;

    public CountLessThanDiffCommand(LabCollection labCollection) {
        super("count_less_than_difficulty","<difficulty> ", "display the number of elements whose difficulty field value is less than the specified one");
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

            Difficulty diff = Difficulty.valueOf(stringArg.toUpperCase());

            int count = 0;
            for (LabWork labWork : labCollection.getLabCollection()) {
                if (diff.compareTo(labWork.getDifficulty()) > 0)
                    count += 1;
            }
            ResponseOutputer.appendln("the number of elements = " + count);
            return true;

        } catch (WrongFormatCommandException exception) {
            ResponseOutputer.appendWarning("Using: '" + getName() + "'");
        } catch (CollectionIsEmptyException exception) {
            ResponseOutputer.appendWarning("Collection is empty!");
        } catch (IllegalArgumentException exception) {
            ResponseOutputer.appendError("This difficulty isn't in list!");
            ResponseOutputer.appendln("List of difficulties: " + Difficulty.nameList());
        }
        return false;
    }
}
