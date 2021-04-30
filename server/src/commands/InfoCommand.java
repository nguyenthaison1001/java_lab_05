package commands;

import exceptions.WrongFormatCommandException;
import interaction.User;
import utility.CollectionManager;
import utility.ResponseOutputer;

import java.time.ZonedDateTime;

/**
 * Command 'info'.
 */
public class InfoCommand extends AbstractCommand {
    private final CollectionManager collectionManager;

    public InfoCommand(CollectionManager collectionManager) {
        super("info","", "display information of collection");
        this.collectionManager = collectionManager;
    }

    /**
     * Executes the command.
     */
    @Override
    public boolean execute(String stringArg, Object objectArg, User user) {
        try {
            if (!stringArg.isEmpty() || objectArg != null) throw new WrongFormatCommandException();
            ZonedDateTime lastInitTime = collectionManager.getLastInitTime();
            String lastInitTimeString = (lastInitTime == null) ? "initialization hasn't happened yet" :
                    lastInitTime.toLocalDate() + " " + lastInitTime.toLocalTime();

            ResponseOutputer.appendln("Information about collection:");
            ResponseOutputer.appendln("-Type: " + collectionManager.getLabCollection().getClass().getName());
            ResponseOutputer.appendln("-The number of elements: " + collectionManager.getLabCollection().size());
            ResponseOutputer.appendln("-Last initializing time: " + lastInitTimeString);
            return true;
        } catch (WrongFormatCommandException exception) {
            ResponseOutputer.appendln("Using: '" + getName() + "'");
        }
        return false;
    }
}
