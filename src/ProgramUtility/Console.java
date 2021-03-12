package ProgramUtility;

import Exceptions.ScriptRecursionException;
import Run.App;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Operates inputs/outputs in console.
 */
public class Console {
    private static CommandManager commandManager;
    private static BufferedReader reader;
    private final LabAsker labAsker;
    private final List<String> fileScriptStack = new ArrayList<>();


    public Console(CommandManager commandManager, BufferedReader reader, LabAsker labAsker) {
        Console.commandManager = commandManager;
        Console.reader = reader;
        this.labAsker = labAsker;
    }

    /**
     * Mode for catching commands from user input.
     */
    public void interactiveMode() throws IOException {
        Console.println("Welcome to app!");
        String[] userCommand;
        do {
            Console.println("\nEnter the command: ");
            Console.print(App.comPrompt);
            reader = new BufferedReader(new InputStreamReader(System.in));
            userCommand = (reader.readLine().trim() + " ").split(" ", 2);
            userCommand[1] = userCommand[1].trim();
        } while (goCommand(userCommand));
    }

    /**
     * Mode for catching commands from a script.
     * @param argument String argument.
     */
    public void scriptMode(String argument){
        String[] userCommand;
        fileScriptStack.add(argument);
        try (BufferedReader scriptReader = new BufferedReader(new FileReader(argument))) {

            BufferedReader tmpReader = labAsker.getUserReader();
            labAsker.setUserReader(scriptReader);
            labAsker.setFileMode();

            String line = scriptReader.readLine();
            do {
                userCommand = (line.trim() + " ").split(" ", 2);
                userCommand[1] = userCommand[1].trim();
                System.out.println(userCommand[0]);
                Console.println(App.comPrompt + String.join(" ", userCommand));
                if (userCommand[0].equals("execute_script")) {
                    for (String fileScript : fileScriptStack) {
                        if (userCommand[1].equals(fileScript)) throw new ScriptRecursionException();
                    }
                }
                if (goCommand(userCommand))
                    line = scriptReader.readLine();
            } while (line != null);

            labAsker.setUserReader(tmpReader);
            labAsker.setUserMode();
            Console.println("Executed script successfully!");

        } catch (FileNotFoundException exception) {
            Console.printError("File not found!");
        } catch (NoSuchElementException exception) {
            Console.printError("Script file is empty");
        } catch (IOException exception) {
            Console.printError("Interrupted I/O operations!");
        } catch (ScriptRecursionException exception) {
            Console.printError("Scripts cannot be called recursively!");
        }
    }

    /**
     * Launches the command.
     * @param userCommand Command to launch.
     * @return Status of command.
     */
    public boolean goCommand(String[] userCommand) {
        switch (userCommand[0]) {
            case "":
                return true;
            case "help":
                commandManager.help(userCommand[1]);
                return true;
            case "show":
                commandManager.show(userCommand[1]);
                return true;
            case "save":
                commandManager.save(userCommand[1]);
                return true;
            case "info":
                commandManager.info(userCommand[1]);
                return true;
            case "exit":
                commandManager.exit(userCommand[1]);
                return true;
            case "clear":
                commandManager.clear(userCommand[1]);
                return true;
            case "remove_first":
                commandManager.removeFirst(userCommand[1]);
                return true;
            case "add":
                commandManager.add(userCommand[1]);
                return true;
            case "remove_by_id":
                commandManager.removeByID(userCommand[1]);
                return true;
            case "update":
                commandManager.update(userCommand[1]);
                return true;
            case "add_if_min":
                commandManager.addIfMin(userCommand[1]);
                return true;
            case "remove_greater":
                commandManager.removeGreater(userCommand[1]);
                return true;
            case "sum_of_minimal_point":
                commandManager.sumMinimalPoint(userCommand[1]);
                return true;
            case "count_less_than_difficulty":
                commandManager.countLessThanDiff(userCommand[1]);
                return true;
            case "filter_starts_with_name":
                commandManager.filterName(userCommand[1]);
                return true;
            case "execute_script":
                commandManager.executeScript(userCommand[1]);
                scriptMode(userCommand[1]);
                return true;
            default:
                if (commandManager.noSuchCommand(userCommand[0]))
                    return true;
        } return false;
    }

    /**
     * Prints toOut.toString() to Console
     * @param toOut Object to print
     */
    public static void print(Object toOut) {
        System.out.print(toOut);
    }

    /**
     * Prints toOut.toString() + \n to Console
     * @param toOut Object to print
     */
    public static void println(Object toOut) {
        System.out.println(toOut);
    }

    /**
     * Prints formatted 2-element table to Console
     * @param element1 Left element of the row.
     * @param element2 Right element of the row.
     */
    public static void printTable(Object element1, Object element2) {
        System.out.printf("%-40s%-1s%n", element1, element2);
    }

    /**
     * Prints warning toOut.toString() + \n to Console
     * @param toOut Object to print
     */
    public static void printWarning(Object toOut) {
        System.out.println("warning: " + toOut);
    }

    /**
     * Prints error: toOut.toString() to Console
     * @param toOut Error to print
     */
    public static void printError(Object toOut) {
        System.out.println("error: " + toOut);
    }
}
