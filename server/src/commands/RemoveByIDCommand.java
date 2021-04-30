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

            ResponseOutputer.appendln("Removed successfully!");
            return true;
        } catch (WrongFormatCommandException exception) {
            ResponseOutputer.appendWarning("Using: '" + getName() + "'");
        } catch (CollectionIsEmptyException exception) {
            ResponseOutputer.appendWarning("Collection is empty!");
        } catch (LabWorkNotFoundException exception) {
            ResponseOutputer.appendError("LabWork ID not found!");
        } catch (NumberFormatException exception) {
            ResponseOutputer.appendError("ID must be a number!");
        } catch (PermissionDeniedException exception) {
            ResponseOutputer.appendError("Недостаточно прав для выполнения данной команды!");
            ResponseOutputer.appendln("Принадлежащие другим пользователям объекты доступны только для чтения.");
        } catch (ManualDatabaseEditException exception) {
            ResponseOutputer.appendError("Произошло прямое изменение базы данных!");
            ResponseOutputer.appendln("Перезапустите клиент для избежания возможных ошибок.");
        } catch (DatabaseHandlingException exception) {
            ResponseOutputer.appendError("Произошла ошибка при обращении к базе данных!");
        }
        return false;
    }
}
