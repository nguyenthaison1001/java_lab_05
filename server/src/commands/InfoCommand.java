package commands;

import exceptions.WrongFormatCommandException;
import utility.FileManager;
import utility.LabCollection;
import utility.ResponseOutputer;

import java.time.ZonedDateTime;

/**
 * Command 'info'.
 */
public class InfoCommand extends AbstractCommand {
    private final LabCollection labCollection;
    private final FileManager fileManager;

    public InfoCommand(LabCollection labCollection, FileManager fileManager) {
        super("info","", "display information of collection");
        this.labCollection = labCollection;
        this.fileManager = fileManager;
    }

    /**
     * Executes the command.
     */
    @Override
    public boolean execute(String stringArg, Object objectArg) {
        try {
            if (!stringArg.isEmpty() || objectArg != null) throw new WrongFormatCommandException();
//            ZonedDateTime lastSaveTime = fileManager.getLastSaveTime();
            ZonedDateTime lastInitTime = fileManager.getLastInitTime();
//            String lastSaveTimeString = (lastSaveTime == null) ? "saving hasn't happened yet" :
//                    lastSaveTime.toLocalDate() + " " + lastSaveTime.toLocalTime();
            String lastInitTimeString = (lastInitTime == null) ? "initialization hasn't happened yet" :
                    lastInitTime.toLocalDate() + " " + lastInitTime.toLocalTime();

            ResponseOutputer.appendln("Information about collection:");
            ResponseOutputer.appendln("-Type: " + labCollection.getLabCollection().getClass().getName());
            ResponseOutputer.appendln("-The number of elements: " + labCollection.getLabCollection().size());
//            Console.println("-Last saving time: " + lastSaveTimeString);
            ResponseOutputer.appendln("-Last initializing time: " + lastInitTimeString);
            return true;
        } catch (WrongFormatCommandException exception) {
            ResponseOutputer.appendln("Using: '" + getName() + "'");
        }
        return false;
    }
}
