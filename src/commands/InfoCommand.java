package commands;

import Exceptions.WrongFormatCommandException;
import ProgramUtility.Console;
import ProgramUtility.FileManager;
import ProgramUtility.LabCollection;

import java.time.ZonedDateTime;

/**
 * Command 'info'.
 */
public class InfoCommand extends AbstractCommand {
    private final LabCollection labCollection;
    private final FileManager fileManager;

    public InfoCommand(LabCollection labCollection, FileManager fileManager) {
        super("info", "display information of collection");
        this.labCollection = labCollection;
        this.fileManager = fileManager;
    }

    /**
     * Executes the command.
     */
    @Override
    public void execute(String argument) {
        try {
            if (!argument.isEmpty()) throw new WrongFormatCommandException();
//            ZonedDateTime lastSaveTime = fileManager.getLastSaveTime();
            ZonedDateTime lastInitTime = fileManager.getLastInitTime();
//            String lastSaveTimeString = (lastSaveTime == null) ? "saving hasn't happened yet" :
//                    lastSaveTime.toLocalDate() + " " + lastSaveTime.toLocalTime();
            String lastInitTimeString = (lastInitTime == null) ? "initialization hasn't happened yet" :
                    lastInitTime.toLocalDate() + " " + lastInitTime.toLocalTime();

            Console.println("Information about collection:");
            Console.println("-Type: " + labCollection.getLabCollection().getClass().getName());
            Console.println("-The number of elements: " + labCollection.getLabCollection().size());
//            Console.println("-Last saving time: " + lastSaveTimeString);
            Console.println("-Last initializing time: " + lastInitTimeString);
        } catch (WrongFormatCommandException exception) {
            Console.printWarning("Using: '" + getName() + "'");
        }
    }
}
