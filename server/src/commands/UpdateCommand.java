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

            System.out.println(newLab);

//            ResponseOutputer.appendln("The old lab: \n" + oldLab);
//            ResponseOutputer.appendln("The new lab: \n" + newLab);

            ResponseOutputer.appendln("Updated successfully!");

            return true;
        } catch (WrongFormatCommandException exception) {
            ResponseOutputer.appendWarning("Using: '" + getName() + "'");
        } catch (NumberFormatException exception) {
            ResponseOutputer.appendError("ID must be a number > 0!");
        } catch (LabWorkNotFoundException exception) {
            ResponseOutputer.appendError("LabWork not found!");
        } catch (CollectionIsEmptyException exception) {
            ResponseOutputer.appendWarning("Collection is empty!");
        } catch (NullPointerException exception) {
            ResponseOutputer.appendError("Argument is null (NullPointerException)!");
        } catch (PermissionDeniedException exception) {
            ResponseOutputer.appendError("Insufficient rights to execute this command!");
            ResponseOutputer.appendln("Objects owned by other users are read-only.");
        } catch (ManualDatabaseEditException exception) {
            ResponseOutputer.appendError("There was a direct database change!!");
            ResponseOutputer.appendln("Restart the client to avoid possible errors.");
        } catch (DatabaseHandlingException exception) {
            ResponseOutputer.appendError("Произошла ошибка при обращении к базе данных!");
        }
        return false;
    }
}
