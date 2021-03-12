package commands;

import Data.Coordinates;
import Data.Difficulty;
import Data.Discipline;
import Data.LabWork;
import Exceptions.*;
import ProgramUtility.Console;
import ProgramUtility.LabAsker;
import ProgramUtility.LabCollection;
import Run.App;

import java.io.BufferedReader;
import java.io.IOException;

/**
 * Command 'update'.
 */
public class UpdateCommand extends AbstractCommand {
    private final LabCollection labCollection;
    private final LabAsker labAsker;

    public UpdateCommand(LabCollection labCollection, LabAsker labAsker) {
        super("update <id> {element}", "update the value of element by ID");
        this.labCollection = labCollection;
        this.labAsker = labAsker;
    }

    /**
     * Executes the command.
     */
    @Override
    public void execute(String argument) {
        try {
            if (argument.isEmpty()) throw new WrongFormatCommandException();
            if (labCollection.getLabCollection().isEmpty()) throw new CollectionIsEmptyException();

            Integer id = Integer.parseInt(argument);
            LabWork oldLab = labCollection.getLabByID(id);
            if (oldLab == null) throw new LabWorkNotFoundException();
            Console.println(oldLab);

            String name = oldLab.getName();
            Coordinates coordinates = oldLab.getCoordinates();
            long minimalPoint = oldLab.getMinimalPoint();
            int tunedInWorked = oldLab.getTunedInWorks();
            int averagePoint = oldLab.getAveragePoint();
            Difficulty difficulty = oldLab.getDifficulty();
            Discipline discipline = oldLab.getDiscipline();

            boolean done = false;
            do {
                try {
                    Console.println("Choose a number (0-7) of item which you want to update:");
                    Console.println("\tName(0)\n\tCoordinates(1)\n\tMinimalPoint(2)\n\tTunedInWorks(3)" +
                            "\n\tAveragePoint(4)\n\tDifficulty(5)\n\tDiscipline(6)\n\t!Done(7)");
                    Console.print(App.comPrompt);
                    BufferedReader reader = labAsker.getUserReader();
                    int num = Integer.parseInt(reader.readLine().trim());
                    Console.println(num);
                    switch (num) {
                        case (0): name = labAsker.askName(); Console.println("Name updated!"); break;
                        case (1): coordinates = labAsker.askCoordinates(); Console.println("Coordinates updated!"); break;
                        case (2): minimalPoint = labAsker.askMinimalPoint(); Console.println("MinimalPoint updated!"); break;
                        case (3): tunedInWorked = labAsker.askTunedInWorks(); Console.println("TunedInWorked updated!"); break;
                        case (4): averagePoint = labAsker.askAveragePoint(); Console.println("averagePoint updated!"); break;
                        case (5): difficulty = labAsker.askDifficulty(); Console.println("Difficulty updated!"); break;
                        case (6): discipline = labAsker.askDiscipline(); Console.println("Discipline updated!"); break;
                        case (7): done = true; break;
                        default: throw new NotInDeclaredLimitsException();
                    }
                } catch (NumberFormatException exception) {
                Console.printError("Require a number!");
                } catch (NotInDeclaredLimitsException exception){
                Console.printError("Number must be in range [0-7]");
                }
            } while (!done);

            labCollection.getLabCollection().remove(oldLab);
            labCollection.getLabCollection().add(new LabWork(
                    id,
                    name,
                    coordinates,
                    minimalPoint,
                    tunedInWorked,
                    averagePoint,
                    difficulty,
                    discipline
            ));
            labCollection.sortByID();
            Console.println("Updated successfully!");
        } catch (WrongFormatCommandException exception) {
            Console.printWarning("Using: '" + getName() + "'");
        } catch (NumberFormatException exception) {
            Console.printError("ID must be a number!");
        } catch (LabWorkNotFoundException exception) {
            Console.printError("LabWork not found!");
        } catch (CollectionIsEmptyException exception) {
            Console.printWarning("Collection is empty!");
        } catch (IOException exception) {
            Console.printError("Interrupted I/O operations!");
        } catch (NullPointerException exception) {
            Console.printError("Argument is null (NullPointerException)!");
        }
    }
}
