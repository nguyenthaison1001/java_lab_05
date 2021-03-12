package commands;

import Data.LabWork;
import Exceptions.CollectionIsEmptyException;
import Exceptions.LabWorkNotFoundException;
import Exceptions.WrongFormatCommandException;
import ProgramUtility.Console;
import ProgramUtility.LabAsker;
import ProgramUtility.LabCollection;
import Run.App;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Command 'remove_greater'.
 */
public class RemoveGreaterCommand extends AbstractCommand {
    private final LabCollection labCollection;
    private final LabAsker labAsker;

    public RemoveGreaterCommand(LabCollection labCollection, LabAsker labAsker) {
        super("remove_greater (byAvePoint) {element}", "remove all elements that are greater than the specified one");
        this.labCollection = labCollection;
        this.labAsker = labAsker;
    }

    /**
     * Executes the command.
     */
    @Override
    public void execute(String argument) {
        try {
            if (!argument.isEmpty()) throw new WrongFormatCommandException();
            if (labCollection.getLabCollection().isEmpty()) throw new CollectionIsEmptyException();

            while (true) {
                try {
                    Console.println("Enter ID of LabWork you want to assign: ");
                    Console.print(App.comPrompt);
                    int id = Integer.parseInt(new BufferedReader(new InputStreamReader(System.in)).readLine());
                    LabWork labToAssign = labCollection.getLabByID(id);
                    if (labToAssign == null) {
                        throw new LabWorkNotFoundException();
                    } else {
                        Console.println("This is LabWork you want to assign: \n" + labToAssign);
                        if (labAsker.areYouSure()) {
                            labCollection.getLabCollection().
                                    removeIf(labWork -> labWork.compareTo(labToAssign) > 0);
                            Console.println("Removed greater successfully!");
                        }
                        else {
                            Console.println("Remove greater failed!");
                        }
                        break;
                    }
                } catch (LabWorkNotFoundException exception) {
                    Console.printError("LabWork not found!");
                } catch (NumberFormatException exception) {
                    Console.printError("ID must be a number!");
                }
            }
        } catch (WrongFormatCommandException exception) {
            Console.printWarning("Using: '" + getName() + "'");
        } catch (CollectionIsEmptyException exception) {
            Console.printWarning("Collection is empty!");
        } catch (IOException exception) {
            Console.printError("Interrupted I/O operations!");
        }
    }
}
