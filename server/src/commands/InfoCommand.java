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

            ResponseOutputer.append("CollectionInfo");
            ResponseOutputer.appendArgs(
                    collectionManager.getLabCollection().getClass().getName(),
                    String.valueOf(collectionManager.getLabCollection().size()),
                    lastInitTimeString);
            return true;
        } catch (WrongFormatCommandException exception) {
            ResponseOutputer.append("Using");
            ResponseOutputer.appendArgs(getName() + " " + getUsage() + "'");
        }
        return false;
    }
}
