package commands;

import data.LabWork;
import exceptions.WrongFormatCommandException;
import interaction.LabRaw;
import utility.LabCollection;
import utility.ResponseOutputer;

/**
 * Command 'add'. Adds a new element to collection.
 */
public class AddCommand extends AbstractCommand {
    private final LabCollection labCollection;

    public AddCommand(LabCollection labCollection) {
        super("add", "{element}", "add new element to collection");
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
            labCollection.getLabCollection().add(new LabWork(
                    labCollection.generateID(),
                    labRaw.getName(),
                    labRaw.getCoordinates(),
                    labRaw.getMinimalPoint(),
                    labRaw.getTunedInWorks(),
                    labRaw.getAveragePoint(),
                    labRaw.getDifficulty(),
                    labRaw.getDiscipline()
            ));
            ResponseOutputer.appendln("LabWork added successfully!");
            return true;
        } catch (WrongFormatCommandException exception) {
            ResponseOutputer.appendWarning("Using: '" + getName() + "'");
        }
        return false;
    }
}
