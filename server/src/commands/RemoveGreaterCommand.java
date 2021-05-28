package commands;

import data.LabWork;
import exceptions.*;
import interaction.User;
import utility.CollectionManager;
import utility.DatabaseCollectionManager;
import utility.ResponseOutputer;

/**
 * Command 'remove_greater'.
 */
public class RemoveGreaterCommand extends AbstractCommand {
    private final CollectionManager collectionManager;
    private final DatabaseCollectionManager databaseCollectionManager;

    public RemoveGreaterCommand(CollectionManager collectionManager, DatabaseCollectionManager databaseCollectionManager) {
        super("remove_greater (byAvePoint)","{element}", "remove all elements that are greater than the specified one");
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
            LabWork labToAssign = collectionManager.getLabByID(id);
            if (labToAssign == null) throw new LabWorkNotFoundException();

            for (LabWork labWork : collectionManager.getGreater(labToAssign)) {
                if (!labWork.getOwner().equals(user)) throw new PermissionDeniedException();
                if (!databaseCollectionManager.checkLabUserID(labWork.getId(), user)) throw new ManualDatabaseEditException();
            }

            for (LabWork labWork : collectionManager.getGreater(labToAssign)) {
                databaseCollectionManager.deleteLabByID(labWork.getId());
                collectionManager.removeFromCollection(labWork);
            }

            collectionManager.sortByID();
            ResponseOutputer.append("LabWorkWasDeleted");

            return true;
        } catch (LabWorkNotFoundException exception) {
            ResponseOutputer.appendError("LabWorkException");
        } catch (NumberFormatException exception) {
            ResponseOutputer.appendError("IdMustBeNumberException");
        } catch (WrongFormatCommandException exception) {
            ResponseOutputer.append("Using");
            ResponseOutputer.appendArgs(getName() + " " + getUsage() + "'");
        } catch (CollectionIsEmptyException exception) {
            ResponseOutputer.appendWarning("CollectionIsEmptyException");
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
