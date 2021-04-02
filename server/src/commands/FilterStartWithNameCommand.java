package commands;

import data.LabWork;
import exceptions.CollectionIsEmptyException;
import exceptions.WrongFormatCommandException;
import utility.LabCollection;
import utility.ResponseOutputer;

import java.util.LinkedList;

public class FilterStartWithNameCommand extends AbstractCommand {
    private final LabCollection labCollection;

    /**
     * Command 'filter_starts_with_name'.
     */
    public FilterStartWithNameCommand(LabCollection labCollection) {
        super("filter_starts_with_name name", "", "display elements whose name begins with a given substring");
        this.labCollection = labCollection;
    }

    /**
     * Executes the command.
     */
    @Override
    public boolean execute(String stringArg, Object objectArg) {
        try {
            if (stringArg.isEmpty() || objectArg != null) throw new WrongFormatCommandException();
            if (labCollection.getLabCollection().isEmpty()) throw new CollectionIsEmptyException();

            String filter = labCollection.filterStartWithName(stringArg);
            if (!filter.isEmpty()) ResponseOutputer.appendln(filter);
            else ResponseOutputer.appendln("No such LabWork with this name found!");
            return true;
        } catch (WrongFormatCommandException exception) {
            ResponseOutputer.appendWarning("Using: '" + getName() + "'");
        } catch (CollectionIsEmptyException exception) {
            ResponseOutputer.appendWarning("Collection is empty!");
        }
        return false;
    }
}
