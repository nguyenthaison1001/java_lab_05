package commands;

import data.LabWork;
import exceptions.*;
import interaction.User;
import utility.CollectionManager;
import utility.DatabaseCollectionManager;
import utility.ResponseOutputer;

/**
 * Command 'remove_by_id'.
 */
public class RemoveByIDCommand extends AbstractCommand {
    private final CollectionManager collectionManager;
    private final DatabaseCollectionManager databaseCollectionManager;

    public RemoveByIDCommand(CollectionManager collectionManager, DatabaseCollectionManager databaseCollectionManager) {
        super("remove_by_id <id>", "", "remove element from collection by ID");
        this.collectionManager = collectionManager;
        this.databaseCollectionManager = databaseCollectionManager;
    }

    /**
     * Executes the command.
     */
    @Override
    public boolean execute(String stringArg, Object objectArg, User user) {
        try {
            if (stringArg.isEmpty() || objectArg != null) throw new WrongFormatCommandException();
            if (collectionManager.getLabCollection().isEmpty()) throw new CollectionIsEmptyException();

            int id = Integer.parseInt(stringArg);
            LabWork labToRemove = collectionManager.getLabByID(id);

            if (labToRemove == null) throw new LabWorkNotFoundException();
            if (!labToRemove.getOwner().equals(user)) throw new PermissionDeniedException();
            if (!databaseCollectionManager.checkLabUserID(labToRemove.getId(), user)) throw new ManualDatabaseEditException();

            databaseCollectionManager.deleteLabByID(id);
            collectionManager.getLabCollection().remove(labToRemove);

            ResponseOutputer.append("LabWorkWasDeleted");
            return true;
        } catch (WrongFormatCommandException exception) {
            ResponseOutputer.append("Using");
            ResponseOutputer.appendArgs(getName() + " " + getUsage() + "'");
        } catch (CollectionIsEmptyException exception) {
            ResponseOutputer.appendWarning("CollectionIsEmptyException");
        } catch (LabWorkNotFoundException exception) {
            ResponseOutputer.appendError("IdOfLabWorkException");
        } catch (NumberFormatException exception) {
            ResponseOutputer.appendError("IdMustBeNumberException");
        } catch (PermissionDeniedException exception) {
            ResponseOutputer.appendError("LackRightsException");
        } catch (ManualDatabaseEditException exception) {
            ResponseOutputer.appendError("ManualDatabaseException");
        } catch (DatabaseHandlingException exception) {
            ResponseOutputer.appendError("DatabaseHandlingException");
        }
        return false;
    }
}
