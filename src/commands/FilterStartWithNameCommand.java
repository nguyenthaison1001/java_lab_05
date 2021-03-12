package commands;

import Data.LabWork;
import Exceptions.CollectionIsEmptyException;
import Exceptions.WrongFormatCommandException;
import ProgramUtility.Console;
import ProgramUtility.LabCollection;

import java.util.LinkedList;

public class FilterStartWithNameCommand extends AbstractCommand {
    private final LabCollection labCollection;

    /**
     * Command 'filter_starts_with_name'.
     */
    public FilterStartWithNameCommand(LabCollection labCollection) {
        super("filter_starts_with_name name", "display elements whose name begins with a given substring");
        this.labCollection = labCollection;
    }

    /**
     * Executes the command.
     */
    @Override
    public void execute(String argument) {
        try {
            if (argument.isEmpty()) throw new WrongFormatCommandException();
            if (labCollection.getLabCollection().isEmpty()) throw new CollectionIsEmptyException();

            LinkedList<LabWork> colFilter = new LinkedList<>();
            for (LabWork labWork : labCollection.getLabCollection()) {
                if (labWork.getName().startsWith(argument))
                    colFilter.add(labWork);
            }

            if (!colFilter.isEmpty())
                System.out.println(colFilter);
            else Console.println("No such LabWork found!");

        } catch (WrongFormatCommandException exception) {
            Console.printWarning("Using: '" + getName() + "'");
        } catch (CollectionIsEmptyException exception) {
            Console.printWarning("Collection is empty!");
        }
    }
}
