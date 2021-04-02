package commands;

import data.LabWork;
import exceptions.WrongFormatCommandException;
import interaction.LabRaw;
import utility.LabCollection;
import utility.ResponseOutputer;

import java.util.Comparator;

/**
 * Command 'add_if_min'.
 */
public class AddIfMinCommand extends AbstractCommand {
    private final LabCollection labCollection;

    public AddIfMinCommand(LabCollection labCollection) {
        super("add_if_min (byAvePoint)", "{element}", "add a new element if its value is less than that of the smallest");
        this.labCollection = labCollection;
    }

    /**
     * Executes the command.
     */
    @Override
    public boolean execute(String stringArg, Object objectArg) {
        try {
            if (!stringArg.isEmpty() || objectArg == null) throw new WrongFormatCommandException();
            LabRaw labRaw = (LabRaw) objectArg;
            LabWork labToAdd = new LabWork(
                    labCollection.generateID(),
                    labRaw.getName(),
                    labRaw.getCoordinates(),
                    labRaw.getMinimalPoint(),
                    labRaw.getTunedInWorks(),
                    labRaw.getAveragePoint(),
                    labRaw.getDifficulty(),
                    labRaw.getDiscipline()
            );

            LabCollection listSortAve = labCollection;
            listSortAve.getLabCollection().sort(Comparator.comparingInt(LabWork::getAveragePoint));

        if (labCollection.getLabCollection().isEmpty() ||
                labToAdd.compareTo(listSortAve.getFirst()) < 0) {
            labCollection.getLabCollection().add(labToAdd);
            ResponseOutputer.appendln("Added successfully!");
        }
        else ResponseOutputer.appendWarning("AveragePoint is not the smallest. Add failed!");
        return true;
        } catch (WrongFormatCommandException exception) {
            ResponseOutputer.appendWarning("Using: '" + getName() + "'");
        }
        return false;
    }
}
