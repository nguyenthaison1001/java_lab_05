package commands;

import Data.LabWork;
import Exceptions.WrongFormatCommandException;
import ProgramUtility.Console;
import ProgramUtility.LabAsker;
import ProgramUtility.LabCollection;

import java.util.Comparator;

/**
 * Command 'add_if_min'.
 */
public class AddIfMinCommand extends AbstractCommand {
    private final LabCollection labCollection;
    private final LabAsker labAsker;

    public AddIfMinCommand(LabCollection labCollection, LabAsker labAsker) {
        super("add_if_min (byAvePoint) {element}", "add a new element if its value is less than that of the smallest");
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

            LabWork labToAdd = new LabWork(
                    labCollection.generateID(),
                    labAsker.askName(),
                    labAsker.askCoordinates(),
                    labAsker.askMinimalPoint(),
                    labAsker.askTunedInWorks(),
                    labAsker.askAveragePoint(),
                    labAsker.askDifficulty(),
                    labAsker.askDiscipline()
            );

            LabCollection listSortAve = labCollection;
            listSortAve.getLabCollection().sort(Comparator.comparingInt(LabWork::getAveragePoint));

        if (labCollection.getLabCollection().isEmpty() ||
                labToAdd.compareTo(listSortAve.getLabCollection().getFirst()) < 0) {
            labCollection.getLabCollection().add(labToAdd);
            Console.println("Added successfully!");
        }
        else Console.printWarning("AveragePoint is not the smallest. Add failed!");

        } catch (WrongFormatCommandException exception) {
            Console.printWarning("Using: '" + getName() + "'");
        }
    }
}
