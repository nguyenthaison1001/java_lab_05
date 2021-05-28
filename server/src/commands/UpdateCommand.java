package commands;

import data.Coordinates;
import data.Difficulty;
import data.Discipline;
import data.LabWork;
import exceptions.*;
import interaction.LabRaw;
import interaction.User;
import utility.CollectionManager;
import utility.DatabaseCollectionManager;
import utility.ResponseOutputer;

import java.time.ZonedDateTime;

/**
 * Command 'update'.
 */
public class UpdateCommand extends AbstractCommand {
    private final CollectionManager collectionManager;
    private DatabaseCollectionManager databaseCollectionManager;

    public UpdateCommand(CollectionManager collectionManager, DatabaseCollectionManager databaseCollectionManager) {
        super("update <id>", "{element}", "update the value of element by ID");
        this.collectionManager = collectionManager;
        this.databaseCollectionManager = databaseCollectionManager;
    }

    /**
     * Executes the command.
     */
    @Override
    public boolean execute(String stringArg, Object objectArg, User user) {
        try {
            if (stringArg.isEmpty() || objectArg == null) throw new WrongFormatCommandException();
            if (collectionManager.getLabCollection().isEmpty()) throw new CollectionIsEmptyException();

            int id = Integer.parseInt(stringArg);
            if (id <= 0) throw new NumberFormatException();
            LabWork oldLab = collectionManager.getLabByID(id);

            if (oldLab == null) throw new LabWorkNotFoundException();
            if (!oldLab.getOwner().equals(user)) throw new PermissionDeniedException();
            if (!databaseCollectionManager.checkLabUserID(oldLab.getId(), user))
                throw new ManualDatabaseEditException();

            LabRaw labRaw = (LabRaw) objectArg;
            databaseCollectionManager.updateLabByID(id, labRaw);

            String name = labRaw.getName();
            Coordinates coordinates = labRaw.getCoordinates();
            ZonedDateTime creationDate = oldLab.getCreationDate();
            Long minimalPoint = labRaw.getMinimalPoint();
            Integer tunedInWorked = labRaw.getTunedInWorks();
            Integer averagePoint = labRaw.getAveragePoint();
            Difficulty difficulty = labRaw.getDifficulty();
            Discipline discipline = labRaw.getDiscipline();

            if (name == null) name = oldLab.getName();
            if (coordinates == null) coordinates = oldLab.getCoordinates();
            if (minimalPoint == null) minimalPoint = oldLab.getMinimalPoint();
            if (tunedInWorked == null) tunedInWorked = oldLab.getTunedInWorks();
            if (averagePoint == null) averagePoint = oldLab.getAveragePoint();
            if (difficulty == null) difficulty = oldLab.getDifficulty();
            if (discipline == null) discipline = oldLab.getDiscipline();

            collectionManager.getLabCollection().remove(oldLab);
            LabWork newLab = new LabWork(
                    id,
                    name,
                    coordinates,
                    creationDate,
                    minimalPoint,
                    tunedInWorked,
                    averagePoint,
                    difficulty,
                    discipline,
                    user
            );
            collectionManager.getLabCollection().add(newLab);
            collectionManager.sortByID();
            ResponseOutputer.append("LabWorkUpdated");
            return true;
        } catch (WrongFormatCommandException exception) {
            ResponseOutputer.append("Using");
            ResponseOutputer.appendArgs(getName() + " " + getUsage() + "'");
        } catch (NumberFormatException exception) {
            ResponseOutputer.appendError("IdMustBeNumberException");
        } catch (LabWorkNotFoundException exception) {
            ResponseOutputer.appendError("LabWorkException");
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
