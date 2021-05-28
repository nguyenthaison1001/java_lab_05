package commands;

import exceptions.WrongFormatCommandException;
import interaction.User;
import utility.CollectionManager;
import utility.ResponseOutputer;

/**
 * Command 'show'.
 */
public class ShowCommand extends AbstractCommand {
    private final CollectionManager collectionManager;

    public ShowCommand(CollectionManager collectionManager) {
        super("show","", "display all elements of the collection");
        this.collectionManager = collectionManager;
    }

    /**
     * Executes the command.
     */
    @Override
    public boolean execute(String stringArg, Object objectArg, User user) {
        try {
            if (!stringArg.isEmpty() || objectArg != null) throw new WrongFormatCommandException();
            collectionManager.sortByID();
            ResponseOutputer.append(collectionManager.showCollection());
            return true;
        } catch (WrongFormatCommandException exception) {
            ResponseOutputer.append("Using");
            ResponseOutputer.appendArgs(getName() + " " + getUsage() + "'");
        }
        return false;
    }
}
