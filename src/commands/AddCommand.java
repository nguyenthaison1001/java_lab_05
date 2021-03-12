package commands;

import Data.LabWork;
import Exceptions.WrongFormatCommandException;
import ProgramUtility.Console;
import ProgramUtility.LabAsker;
import ProgramUtility.LabCollection;

/**
 * Command 'add'. Adds a new element to collection.
 */
public class AddCommand extends AbstractCommand {
    private final LabCollection labCollection;
    private final LabAsker labAsker;

    public AddCommand(LabCollection labCollection, LabAsker labAsker) {
        super("add {element}", "add new element to collection");
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

            labCollection.getLabCollection().add(new LabWork(
                    labCollection.generateID(),
                    labAsker.askName(),
                    labAsker.askCoordinates(),
                    labAsker.askMinimalPoint(),
                    labAsker.askTunedInWorks(),
                    labAsker.askAveragePoint(),
                    labAsker.askDifficulty(),
                    labAsker.askDiscipline()
            ));
            Console.println("LabWork added successfully!");
        } catch (WrongFormatCommandException exception) {
            Console.printWarning("Using: '" + getName() + "'");
        }
    }
}
