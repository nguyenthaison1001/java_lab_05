package commands;

import exceptions.CollectionIsEmptyException;
import exceptions.WrongFormatCommandException;
import interaction.User;
import utility.CollectionManager;
import utility.ResponseOutputer;

public class FilterStartWithNameCommand extends AbstractCommand {
    private final CollectionManager collectionManager;

    /**
     * Command 'filter_starts_with_name'.
     */
    public FilterStartWithNameCommand(CollectionManager collectionManager) {
        super("filter_starts_with_name name", "", "display elements whose name begins with a given substring");
        this.collectionManager = collectionManager;
    }

    /**
     * Executes the command.
     */
    @Override
    public boolean execute(String stringArg, Object objectArg, User user) {
        try {
            if (stringArg.isEmpty() || objectArg != null) throw new WrongFormatCommandException();
            if (collectionManager.getLabCollection().isEmpty()) throw new CollectionIsEmptyException();

            String filter = collectionManager.filterStartWithName(stringArg);
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
