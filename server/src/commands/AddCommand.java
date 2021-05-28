package commands;

import exceptions.DatabaseHandlingException;
import exceptions.WrongFormatCommandException;
import interaction.LabRaw;
import interaction.User;
import utility.DatabaseCollectionManager;
import utility.CollectionManager;
import utility.ResponseOutputer;

/**
 * Command 'add'. Adds a new element to collection.
 */
public class AddCommand extends AbstractCommand {
    private final CollectionManager collectionManager;
    private final DatabaseCollectionManager databaseCollectionManager;

    public AddCommand(CollectionManager collectionManager, DatabaseCollectionManager databaseCollectionManager) {
        super("add", "{element}", "add new element to collection");
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
            collectionManager.getLabCollection().add(databaseCollectionManager.insertLab(labRaw, user));
            ResponseOutputer.append("LabWorkWasAdded");
            return true;
        } catch (WrongFormatCommandException exception) {
            ResponseOutputer.append("Using");
            ResponseOutputer.appendArgs(getName() + " " + getUsage() + "'");
        } catch (DatabaseHandlingException exception) {
            ResponseOutputer.appendError("DatabaseHandlingException");
        }
        return false;
    }
}
