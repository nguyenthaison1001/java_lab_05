import utility.*;
import commands.*;

import java.io.IOException;
import java.util.LinkedList;
import java.util.logging.*;

/**
 * Main application class. Creates all instances and runs the program.
 * @author Nguyen Thai Son - R3135
 */
public class AppServer {
    public static final int PORT = 4387;
    public static final int CONNECTION_TIMEOUT = 60 * 1000;
    public static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    public static void main(String[] args) {
        Handler consoleHandler;
        Handler fileHandler;
        try{
            //Creating consoleHandler and fileHandler
            consoleHandler = new ConsoleHandler();
            fileHandler  = new FileHandler("logback.log");

            //Assigning handlers to LOGGER object
            LOGGER.addHandler(consoleHandler);
            LOGGER.addHandler(fileHandler);

            //Setting levels to handlers and LOGGER
            consoleHandler.setLevel(Level.ALL);
            fileHandler.setLevel(Level.ALL);
            LOGGER.setLevel(Level.ALL);

            LOGGER.config("Configuration done.");

            //Console handler removed
            LOGGER.removeHandler(consoleHandler);

            LOGGER.log(Level.FINE, "Finer logged");
        }catch(IOException exception){
            LOGGER.log(Level.SEVERE, "Error occur in FileHandler.", exception);
        }

        LOGGER.finer("Finest example on LOGGER handler completed.");


        String fileName = "D:\\Nguyen Thai Son\\Freshman\\Programming\\LabSerious\\java_lab_5678\\testIn.xml";
//        String fileName = args[0];
        LabCollection labCollection = new LabCollection(new LinkedList<>());
        FileManager fileManager = new FileManager(fileName, labCollection);
        labCollection = fileManager.readCollection();
        CommandManager commandManager = new CommandManager(
                new HelpCommand(),
                new InfoCommand(labCollection, fileManager),
                new UpdateCommand(labCollection),
                new ShowCommand(labCollection),
                new AddCommand(labCollection),
                new RemoveByIDCommand(labCollection),
                new ClearCollectionCommand(labCollection),
                new SaveCommand(labCollection, fileName),
                new ExecuteScriptCommand(),
                new ExitCommand(labCollection, fileName),
                new RemoveFirstCommand(labCollection),
                new AddIfMinCommand(labCollection),
                new RemoveGreaterCommand(labCollection),
                new SumMiniPointCommand(labCollection),
                new CountLessThanDiffCommand(labCollection),
                new FilterStartWithNameCommand(labCollection)
        );
        RequestHandler requestHandler = new RequestHandler(commandManager);
        Server server = new Server(PORT, CONNECTION_TIMEOUT, requestHandler, labCollection, fileName);
        server.run();
    }
}
