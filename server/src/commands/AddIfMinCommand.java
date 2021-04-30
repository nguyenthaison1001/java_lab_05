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

            LabWork labToAdd = new LabWork(
                    labRaw.getId(),
                    labRaw.getName(),
                    labRaw.getCoordinates(),
                    labRaw.getCreationDate(),
                    labRaw.getMinimalPoint(),
                    labRaw.getTunedInWorks(),
                    labRaw.getAveragePoint(),
                    labRaw.getDifficulty(),
                    labRaw.getDiscipline(),
                    user
            );

            CollectionManager listSortAve = collectionManager;
            listSortAve.getLabCollection().sort(Comparator.comparingInt(LabWork::getAveragePoint));

            if (collectionManager.getLabCollection().isEmpty() || labToAdd.compareTo(listSortAve.getFirst()) < 0) {
                databaseCollectionManager.insertLab(labRaw, user);
                collectionManager.getLabCollection().add(labToAdd);
                ResponseOutputer.appendln("Added successfully!");
                return true;
            } else ResponseOutputer.appendln("AveragePoint is not the smallest. Add failed!");
        } catch (WrongFormatCommandException exception) {
            ResponseOutputer.appendWarning("Using: '" + getName() + "'");
        } catch (DatabaseHandlingException exception) {
            ResponseOutputer.appendError("Произошла ошибка при обращении к базе данных!");
        }
        return false;
    }
}
