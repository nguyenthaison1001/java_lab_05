package utility;

import AppServer.AppClient;
import data.Coordinates;
import data.Difficulty;
import data.Discipline;
import exceptions.CommandUsageException;
import exceptions.IncorrectInputInScriptException;
import exceptions.NotInDeclaredLimitsException;
import exceptions.ScriptRecursionException;
import interaction.LabRaw;
import interaction.Request;
import interaction.ResponseCode;

import java.io.*;
import java.util.*;

/**
 * Operates inputs/outputs in console.
 */
public class UserHandler {

    private BufferedReader userReader;
    private final Stack<BufferedReader> readerStack = new Stack<>();
    private final Stack<File> fileScriptStack = new Stack<>();


    public UserHandler(BufferedReader userReader) {
        this.userReader = userReader;
    }

    public Request handle(ResponseCode serverResponseCode) {
        String userInput = null;
        String[] userCommand;
        ProcessingCode processingCode;
        int rewriteAttempts = 0;
        try {
            do {
                try {
                    if (isScriptMode()) {
                        userInput = userReader.readLine();
                        if (userInput == null) {
                            userReader.close();
                            userReader = readerStack.pop();
                            Outputer.println("Executing file '" + fileScriptStack.pop().getName() + "' done!");
                        }
                    }

                    // script mode
                    if (isScriptMode()) {
                            Outputer.println(AppClient.PS1 + userInput);
                    }
                    // interactive mode
                    else {
                        Outputer.println("\nEnter the command:");
                        Outputer.print(AppClient.PS1);
                        userInput = userReader.readLine();
                    }

                    assert userInput != null;
                    userCommand = (userInput.trim() + " ").split(" ", 2);
                    userCommand[1] = userCommand[1].trim();

                } catch (NoSuchElementException | IllegalStateException exception) {
                    Outputer.println();
                    Outputer.printError("An error occurred while entering the command!");
                    userCommand = new String[]{"", ""};
                    rewriteAttempts++;
                    int maxRewriteAttempts = 1;
                    if (rewriteAttempts >= maxRewriteAttempts) {
                        Outputer.printError("Number of input attempts exceeded!");
                        System.exit(0);
                    }
                }

                processingCode = processCommand(userCommand[0], userCommand[1]);

            } while (processingCode == ProcessingCode.ERROR);

            try {
                if ((serverResponseCode == ResponseCode.ERROR))
                    throw new IncorrectInputInScriptException();
                switch (processingCode) {
                    case OBJECT:
                        LabRaw labAddRaw = generateLabToAdd();
                        return new Request(userCommand[0], userCommand[1], labAddRaw);
                    case UPDATE_OBJECT:
                        LabRaw labUpdateRaw = generateLabToUpdate();
                        return new Request(userCommand[0], userCommand[1], labUpdateRaw);
                    case REMOVE_GREATER:
                        return new Request(userCommand[0], utilityRemoveGreater());
                    case SCRIPT:
                        File scriptFile = new File(userCommand[1]);
                        if (!scriptFile.exists()) throw new FileNotFoundException();
                        if (!fileScriptStack.isEmpty() && fileScriptStack.search(scriptFile) != -1)
                            throw new ScriptRecursionException();
                        readerStack.push(userReader);
                        fileScriptStack.push(scriptFile);
                        userReader = new BufferedReader(new FileReader(scriptFile));    // bat dau doc file
                        Outputer.println("Executing the script '" + scriptFile.getName() + "'...");
                        break;
                }
            } catch (ScriptRecursionException exception) {
                Outputer.printError("Scripts can't be called recursively! ");
                throw new IncorrectInputInScriptException();
            } catch (FileNotFoundException exception) {
                Outputer.printError("Script file not found!");
            }
        } catch (IncorrectInputInScriptException | IOException exception) {
            try {
                Outputer.printError("Script execution interrupted!");
                while (!readerStack.isEmpty()) {
                    userReader.close();
                    userReader = readerStack.pop();
                }
                fileScriptStack.clear();
            } catch (IOException exception1) {
                Outputer.printError("IOException occurred");
                System.exit(0);
            }
            return new Request();
        }
        return new Request(userCommand[0], userCommand[1]);
    }


//    /**
//     * Launches the command.
//     * @param userCommand Command to launch.
//     * @return Status of command.
//     */
    public ProcessingCode processCommand(String commandName, String commandArg) {
        try {
            switch (commandName) {
                case "":
                    Outputer.println("Enter 'help' for help!");
                    return ProcessingCode.ERROR;

                case "help":
                case "show":
                case "info":
                case "exit":
                case "clear":
                case "remove_first":
                case "sum_of_minimal_point":
                    if (!commandArg.isEmpty()) throw new CommandUsageException();
                    break;

                case "add":
                case "add_if_min":

                    if (!commandArg.isEmpty()) throw new CommandUsageException("{element}");
                    return ProcessingCode.OBJECT;

                case "remove_greater":
                    if (!commandArg.isEmpty()) throw new CommandUsageException("{element}");
                    return ProcessingCode.REMOVE_GREATER;

                case "remove_by_id":
                    if (commandArg.isEmpty()) throw new CommandUsageException("<ID>");
                    break;
                case "update":
                    if (commandArg.isEmpty()) throw new CommandUsageException("<ID> {element}");
                    return ProcessingCode.UPDATE_OBJECT;

                case "count_less_than_difficulty":
                    if (commandArg.isEmpty()) throw new CommandUsageException("<difficulty>");
                    break;
                case "filter_starts_with_name":
                    if (commandArg.isEmpty()) throw new CommandUsageException("<name>");
                    break;
                case "execute_script":
                    if (commandArg.isEmpty()) throw new CommandUsageException("<file_name>");
                    return ProcessingCode.SCRIPT;
                default:
                    Outputer.println("Command '" + commandName + "' not found. Enter 'help' for help.");
                    return ProcessingCode.ERROR;
            }
        } catch (CommandUsageException exception) {
            if (exception.getMessage() != null) commandName += " " + exception.getMessage();
            Outputer.println("Using: '" + commandName + "'");
            return ProcessingCode.ERROR;
        }
        return ProcessingCode.OK;
    }

    private boolean isScriptMode() {
        return !readerStack.isEmpty();
    }

    private LabRaw generateLabToAdd() {
        LabAsker labAsker = new LabAsker(userReader);
        if (isScriptMode()) labAsker.setFileMode();
        return new LabRaw(
                labAsker.askName(),
                labAsker.askCoordinates(),
                labAsker.askMinimalPoint(),
                labAsker.askTunedInWorks(),
                labAsker.askAveragePoint(),
                labAsker.askDifficulty(),
                labAsker.askDiscipline()
        );
    }

    private LabRaw generateLabToUpdate() {
        LabAsker labAsker = new LabAsker(userReader);
        if (isScriptMode()) labAsker.setFileMode();

        String name = null;
        Coordinates coordinates = null;
        Long minimalPoint = null;
        Integer tunedInWorked = null;
        Integer averagePoint = null;
        Difficulty difficulty = null;
        Discipline discipline = null;

        boolean done = false;
        do {
            try {
                Outputer.println("Choose a number (0-7) of the item which you want to update:");
                Outputer.println("\t(0) Name\n\t(1) Coordinates\n\t(2) MinimalPoint\n\t(3) TunedInWorks" +
                        "\n\t(4) AveragePoint\n\t(5) Difficulty\n\t(6) Discipline\n\t(7) Done!");
                Outputer.print(AppClient.PS2);
                BufferedReader reader = labAsker.getUserReader();
                int option = Integer.parseInt(reader.readLine().trim());
                switch (option) {
                    case (0): name = labAsker.askName();
                        Outputer.println("The name is updated!");
                        break;
                    case (1): coordinates = labAsker.askCoordinates();
                        Outputer.println("The coordinates are updated!");
                        break;
                    case (2): minimalPoint = labAsker.askMinimalPoint();
                        Outputer.println("The minimalPoint is updated!");
                        break;
                    case (3): tunedInWorked = labAsker.askTunedInWorks();
                        Outputer.println("The tunedInWorked is updated!");
                        break;
                    case (4): averagePoint = labAsker.askAveragePoint();
                        Outputer.println("The averagePoint is updated!");
                        break;
                    case (5): difficulty = labAsker.askDifficulty();
                        Outputer.println("The difficulty is updated!");
                        break;
                    case (6): discipline = labAsker.askDiscipline();
                        Outputer.println("The discipline is updated!");
                        break;
                    case (7): done = true;
                        break;
                    default: throw new NotInDeclaredLimitsException();
                }
            } catch (NumberFormatException exception) {
                Outputer.printError("Require a number!");
            } catch (NotInDeclaredLimitsException exception){
                Outputer.printError("Number must be in range [0-7]");
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        } while (!done);
        return new LabRaw(name, coordinates, minimalPoint,
                tunedInWorked, averagePoint, difficulty, discipline);
    }

    private String utilityRemoveGreater() throws IOException {
        Outputer.println("Enter ID of the LabWork you want to assign: ");
        Outputer.print(AppClient.PS2);
        String id = new BufferedReader(new InputStreamReader(System.in)).readLine();

        LabAsker labAsker = new LabAsker(userReader);
        if (labAsker.areYouSure())
            return id;
        else return "no";
    }
}
