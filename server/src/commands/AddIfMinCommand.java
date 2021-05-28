package commands;

import data.LabWork;
import exceptions.DatabaseHandlingException;
import exceptions.WrongFormatCommandException;
import interaction.LabRaw;
import interaction.User;
import utility.DatabaseCollectionManager;
import utility.CollectionManager;
import utility.ResponseOutputer;

import java.util.Comparator;

/**
 * Command 'add_if_min'.
 */
public class AddIfMinCommand extends AbstractCommand {
    private final CollectionManager collectionManager;
    private final DatabaseCollectionManager databaseCollectionManager;

    public AddIfMinCommand(CollectionManager collectionManager, DatabaseCollectionManager databaseCollectionManager) {
        super("add_if_min (byAvePoint)", "{element}", "add a new element if its value is less than that of the smallest");
        this.collectionManager = collectionManager;
        this.databaseCollectionManager = databaseCollectionManager;
    }

    /**
     * Executes the command.
     */
    @Override
    public boolean execute(String stringArg, Object objectArg, User user) {
        try {
            if (!stringArg.isEmpty() || objectArg == null) throw new WrongFormatCommandException();
            LabRaw labRaw = (LabRaw) objectArg;
            CollectionManager listSortAve = collectionManager;
            listSortAve.getLabCollection().sort(Comparator.comparingInt(LabWork::getAveragePoint));

            if (collectionManager.getLabCollection().isEmpty() || labRaw.compareTo(listSortAve.getFirst()) < 0) {
                collectionManager.getLabCollection().add(databaseCollectionManager.insertLab(labRaw, user));
                collectionManager.sortByID();
                ResponseOutputer.append("LabWorkWasAdded");
                return true;
            } else ResponseOutputer.append("AveragePointNotSmallest");
        } catch (WrongFormatCommandException exception) {
            ResponseOutputer.append("Using");
            ResponseOutputer.appendArgs(getName() + " " + getUsage() + "'");
        } catch (DatabaseHandlingException exception) {
            ResponseOutputer.appendError("DatabaseHandlingException");
        }
        return false;
    }
}
