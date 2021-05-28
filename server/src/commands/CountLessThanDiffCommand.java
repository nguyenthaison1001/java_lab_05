package commands;

import data.Difficulty;
import data.LabWork;
import exceptions.CollectionIsEmptyException;
import exceptions.WrongFormatCommandException;
import interaction.User;
import utility.CollectionManager;
import utility.ResponseOutputer;

/**
 * Command 'count_less_than_difficulty'.
 */
public class CountLessThanDiffCommand extends AbstractCommand {
    private final CollectionManager collectionManager;

    public CountLessThanDiffCommand(CollectionManager collectionManager) {
        super("count_less_than_difficulty","<difficulty> ", "display the number of elements whose difficulty field value is less than the specified one");
        this.collectionManager = collectionManager;
    }

    /**
     * Executes the command.
     */
    @Override
    public boolean execute(String stringArg, Object objectArg, User user) {
        try {
            if (stringArg.isEmpty() || objectArg != null) throw new WrongFormatCommandException();
            if (collectionManager.getLabCollection().isEmpty()) throw new CollectionIsEmptyException();

            Difficulty diff = Difficulty.valueOf(stringArg.toUpperCase());

            int count = 0;
            for (LabWork labWork : collectionManager.getLabCollection()) {
                if (diff.compareTo(labWork.getDifficulty()) > 0)
                    count += 1;
            }
            ResponseOutputer.append("NumberOfLabs");
            ResponseOutputer.appendArgs(String.valueOf(count));
            return true;

        } catch (WrongFormatCommandException exception) {
            ResponseOutputer.append("Using");
            ResponseOutputer.appendArgs(getName() + " " + getUsage() + "'");
        } catch (CollectionIsEmptyException exception) {
            ResponseOutputer.appendWarning("CollectionIsEmptyException");
        } catch (IllegalArgumentException exception) {
            ResponseOutputer.appendError("NotInList");
        }
        return false;
    }
}
