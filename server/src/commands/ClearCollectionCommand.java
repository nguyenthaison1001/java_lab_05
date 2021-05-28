package commands;

import data.LabWork;
import exceptions.*;
import interaction.User;
import utility.DatabaseCollectionManager;
import utility.CollectionManager;
import utility.ResponseOutputer;

/**
 * Command 'clear'.
 */
public class ClearCollectionCommand extends AbstractCommand {
    private final CollectionManager collectionManager;
    private final DatabaseCollectionManager databaseCollectionManager;

    public ClearCollectionCommand(CollectionManager collectionManager, DatabaseCollectionManager databaseCollectionManager) {
        super("clear","", "clear collection");
        this.collectionManager = collectionManager;
        this.databaseCollectionManager = databaseCollectionManager;
    }

    /**
     * Executes the command.
     */
    @Override
    public boolean execute(String stringArg, Object objectArg, User user) {
        try {
            if (!stringArg.isEmpty() || objectArg != null) throw new WrongFormatCommandException();
            for (LabWork lab : collectionManager.getLabCollection()) {
                if (!lab.getOwner().equals(user)) throw new PermissionDeniedException();
                if (!databaseCollectionManager.checkLabUserID(lab.getId(), user))
                    throw new ManualDatabaseEditException();
            }
            databaseCollectionManager.clearCollection();
            collectionManager.getLabCollection().clear();
            ResponseOutputer.append("ClearCollection");
            return true;
        } catch (WrongFormatCommandException exception) {
            ResponseOutputer.append("Using");
            ResponseOutputer.appendArgs(getName() + " " + getUsage() + "'");
        } catch (DatabaseHandlingException exception) {
            ResponseOutputer.appendError("DatabaseHandlingException");
        } catch (PermissionDeniedException exception) {
            ResponseOutputer.appendError("LackRightsException");
        } catch (ManualDatabaseEditException exception) {
            ResponseOutputer.appendError("ManualDatabaseException");
        }
        return false;
    }
}
