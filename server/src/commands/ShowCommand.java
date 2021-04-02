package commands;

import exceptions.WrongFormatCommandException;
import utility.LabCollection;
import utility.ResponseOutputer;

/**
 * Command 'show'.
 */
public class ShowCommand extends AbstractCommand {
    private final LabCollection labCollection;

    public ShowCommand(LabCollection labCollection) {
        super("show","", "display all elements of the collection");
        this.labCollection = labCollection;
    }

    /**
     * Executes the command.
     */
    @Override
    public boolean execute(String stringArg, Object objectArg) {
        try {
            if (!stringArg.isEmpty() || objectArg != null) throw new WrongFormatCommandException();
//            ResponseOutputer.appendln(labCollection.showCollection());
            labCollection.sortByName();
            ResponseOutputer.appendln(labCollection.showCollection());
            return true;
        } catch (WrongFormatCommandException exception) {
            ResponseOutputer.appendWarning("Using: '" + getName() + "'");
        }
        return false;
    }
}
