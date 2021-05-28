package server;

import exceptions.NotInDeclaredLimitsException;
import exceptions.WrongFormatCommandException;
import utility.*;
import commands.*;

import java.util.logging.*;

/**
 * Main application class. Creates all instances and runs the program.
 * @author Nguyen Thai Son - R3135
 */
public class AppServer {
    private static final int MAX_CLIENTS = 1000;
    public static int port = 4387;
    public static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
//    private static int port;

//    private static String databaseHost;
//    private static String databaseURL ;
//    private static String databaseUsername = "s291124";
//    private static String databasePassword;

    private static String databaseHost;
    private static String databaseURL = "jdbc:postgresql://localhost:5678/studs";
    private static String databaseUsername = "postgres";
    private static String databasePassword = "postgres";

    public static void main(String[] args) {
//        if (!initialize(args)) return;

        DatabaseHandler databaseHandler = new DatabaseHandler(databaseURL, databaseUsername, databasePassword);
        DatabaseUserManager databaseUserManager = new DatabaseUserManager(databaseHandler);
        DatabaseCollectionManager databaseCollectionManager = new DatabaseCollectionManager(databaseHandler, databaseUserManager);

        CollectionManager collectionManager = new CollectionManager(databaseCollectionManager);

        CommandManager commandManager = new CommandManager(
                new HelpCommand(),
                new InfoCommand(collectionManager),
                new UpdateCommand(collectionManager, databaseCollectionManager),
                new ShowCommand(collectionManager),
                new AddCommand(collectionManager, databaseCollectionManager),
                new RemoveByIDCommand(collectionManager, databaseCollectionManager),
                new ClearCollectionCommand(collectionManager, databaseCollectionManager),
                new ExecuteScriptCommand(),
                new ExitCommand(),
                new AddIfMinCommand(collectionManager, databaseCollectionManager),
                new RemoveGreaterCommand(collectionManager, databaseCollectionManager),
                new SumMiniPointCommand(collectionManager),
                new CountLessThanDiffCommand(collectionManager),
                new FilterStartWithNameCommand(collectionManager),
                new LoginCommand(databaseUserManager),
                new RegisterCommand(databaseUserManager)
        );
        Server server = new Server(port, MAX_CLIENTS, commandManager, collectionManager);
        server.run();
        databaseHandler.closeConnection();
    }

    /**
     * Controls initialization.
     */
    private static boolean initialize(String[] args) {
        try {
            if (args.length != 3) throw new WrongFormatCommandException();
            port = Integer.parseInt(args[0]);
            if (port < 0) throw new NotInDeclaredLimitsException();
            databaseHost = args[1];
            databasePassword = args[2];
            databaseURL = "jdbc:postgresql://" + databaseHost + ":5432/studs";
//            databaseURL = "jdbc:postgresql://" + databaseHost + ":5678/studs";
            return true;
        } catch (WrongFormatCommandException exception) {
            String jarName = new java.io.File(AppServer.class.getProtectionDomain()
                    .getCodeSource()
                    .getLocation()
                    .getPath())
                    .getName();
            Outputer.println("Использование: 'java -jar " + jarName + " <port> <db_host> <db_password>'");
        } catch (NumberFormatException exception) {
            Outputer.printError("Порт должен быть представлен числом!");
            AppServer.LOGGER.severe("Порт должен быть представлен числом!");
        } catch (NotInDeclaredLimitsException exception) {
            Outputer.printError("Порт не может быть отрицательным!");
            AppServer.LOGGER.severe("Порт не может быть отрицательным!");
        }
        AppServer.LOGGER.severe("Ошибка инициализации порта запуска!");
        return false;
    }
}
