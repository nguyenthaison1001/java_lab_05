package utility;

import client.AppClient;
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
import interaction.User;

import java.io.*;
import java.util.*;

/**
 * Operates inputs/outputs in console.
 */
public class ScriptHandler {

    private Scanner userScanner;
    private final Stack<Scanner> scannerStack = new Stack<>();
    private final Stack<File> scriptStack = new Stack<>();

    public ScriptHandler(File scriptFile) {
        try {
            userScanner = new Scanner(scriptFile);
            scannerStack.add(userScanner);
            scriptStack.add(scriptFile);
        } catch (Exception exception) { /* ? */ }
    }

    public Request handle(ResponseCode serverResponseCode, User user) {
        String userInput = null;
        String[] userCommand;
        ProcessingCode processingCode;
        int rewriteAttempts = 0;
        try {
            do {
                try {
                    if (serverResponseCode == ResponseCode.ERROR || serverResponseCode == ResponseCode.SERVER_EXIT)
                        throw new IncorrectInputInScriptException();
                    while (!scannerStack.isEmpty() && !userScanner.hasNextLine()) {
                        userScanner.close();
                        userScanner = scannerStack.pop();
                        if (!scannerStack.isEmpty()) scriptStack.pop();
                        else return null;
                    }
                    userInput = userScanner.nextLine();
                    if (!userInput.isEmpty()) {
                        Outputer.print(AppClient.PS1);
                        Outputer.println(userInput);
                    }
                    userCommand = (userInput.trim() + " ").split(" ", 2);
                    userCommand[1] = userCommand[1].trim();

                } catch (NoSuchElementException | IllegalStateException exception) {
                    Outputer.println();
                    Outputer.printError("CommandErrorException");
                    userCommand = new String[]{"", ""};
                    rewriteAttempts++;
                    int maxRewriteAttempts = 1;
                    if (rewriteAttempts >= maxRewriteAttempts) {
                        Outputer.printError("RewriteAttemptsException");
                        System.exit(0);
                    }
                }

                processingCode = processCommand(userCommand[0], userCommand[1]);

            } while (userCommand[0].isEmpty());

            try {
                if (isScriptMode() && (/*serverResponseCode == ResponseCode.ERROR ||*/ processingCode == ProcessingCode.ERROR))
                    throw new IncorrectInputInScriptException();
                switch (processingCode) {
                    case OBJECT:
                        LabRaw labAddRaw = generateLabToAdd();
                        return new Request(userCommand[0], userCommand[1], labAddRaw, user);
                    case UPDATE_OBJECT:
                        LabRaw labUpdateRaw = generateLabToUpdate();
                        return new Request(userCommand[0], userCommand[1], labUpdateRaw, user);
                    case REMOVE_GREATER:
                        return new Request(userCommand[0], utilityRemoveGreater(), user);
                    case SCRIPT:
                        File scriptFile = new File(userCommand[1]);
                        if (!scriptFile.exists()) throw new FileNotFoundException();
                        if (!scriptStack.isEmpty() && scriptStack.search(scriptFile) != -1)
                            throw new ScriptRecursionException();
                        scannerStack.push(userScanner);
                        scriptStack.push(scriptFile);
                        userScanner = new Scanner(scriptFile);    // bat dau doc file
                        Outputer.println("ScriptRunning" + scriptFile.getName());
                        break;
                }
            } catch (ScriptRecursionException exception) {
                Outputer.printError("ScriptRecursionException");
                throw new IncorrectInputInScriptException();
            } catch (FileNotFoundException exception) {
                Outputer.printError("ScriptFileNotFoundException");
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        } catch (IncorrectInputInScriptException exception) {
            Outputer.printError("IncorrectInputInScriptException");
            while (!scannerStack.isEmpty()) {
                userScanner.close();
                userScanner = scannerStack.pop();
            }
            scriptStack.clear();
            return null;
        }
        return new Request(userCommand[0], userCommand[1], null, user);
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
        return !scannerStack.isEmpty();
    }

    private LabRaw generateLabToAdd() throws IncorrectInputInScriptException {
        LabAsker labAsker = new LabAsker(userScanner);
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

    private LabRaw generateLabToUpdate() throws IncorrectInputInScriptException {
        LabAsker labAsker = new LabAsker(userScanner);

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
                Scanner scanner = labAsker.getUserReader();
                int option = Integer.parseInt(scanner.nextLine().trim());
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
            }
        } while (!done);
        return new LabRaw(name, coordinates, minimalPoint,
                tunedInWorked, averagePoint, difficulty, discipline);
    }

    private String utilityRemoveGreater() throws IOException {
        Outputer.println("Enter ID of the LabWork you want to assign: ");
        Outputer.print(AppClient.PS2);
        String id = new BufferedReader(new InputStreamReader(System.in)).readLine();

        LabAsker labAsker = new LabAsker(userScanner);
        if (labAsker.areYouSure())
            return id;
        else return "no";
    }
}
