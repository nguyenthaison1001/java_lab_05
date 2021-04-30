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

            if (stringArg.equals("no")) {
                ResponseOutputer.appendln("Remove greater failed!");
                return true;
            }

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

            ResponseOutputer.appendln("Removed greater successfully!");

            return true;
        } catch (LabWorkNotFoundException exception) {
            ResponseOutputer.appendError("LabWork not found!");
        } catch (NumberFormatException exception) {
            ResponseOutputer.appendError("ID must be a number!");
        } catch (WrongFormatCommandException exception) {
            ResponseOutputer.appendWarning("Using: '" + getName() + "'");
        } catch (CollectionIsEmptyException exception) {
            ResponseOutputer.appendWarning("Collection is empty!");
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
