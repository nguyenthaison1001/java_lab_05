package Run;

import ProgramUtility.*;
import commands.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;

/**
 * Main application class. Creates all instances and runs the program.
 * @author Nguyen Thai Son - R3135
 */
public class App {
    public static final String comPrompt = "> ";
    public static void main(String[] args) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
            String fileName = "D:\\Nguyen Thai Son\\Freshman\\Programming\\LabSerious\\Lab_05\\src\\testIn.xml";
//            String fileName = args[0];

            LabAsker labAsker = new LabAsker(reader);
            LabCollection labCollection = new LabCollection(new LinkedList<>());
            FileManager fileManager = new FileManager(fileName, labCollection);
            labCollection = fileManager.readCollection();
            CommandManager commandManager = new CommandManager(
                    new HelpCommand(),
                    new InfoCommand(labCollection, fileManager),
                    new UpdateCommand(labCollection, labAsker),
                    new ShowCommand(labCollection),
                    new AddCommand(labCollection, labAsker),
                    new RemoveByIDCommand(labCollection, labAsker),
                    new ClearCollectionCommand(labCollection, labAsker),
                    new SaveCommand(labCollection, fileName),
                    new ExecuteScriptCommand(),
                    new ExitCommand(labAsker),
                    new RemoveFirstCommand(labCollection, labAsker),
                    new AddIfMinCommand(labCollection, labAsker),
                    new RemoveGreaterCommand(labCollection, labAsker),
                    new SumMiniPointCommand(labCollection),
                    new CountLessThanDiffCommand(labCollection),
                    new FilterStartWithNameCommand(labCollection)
            );
            Console console = new Console(commandManager, reader, labAsker);
            console.interactiveMode();
        } catch (IOException exception) {
            Console.printError("File not found!");
        }
    }
}
