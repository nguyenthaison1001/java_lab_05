package commands;

import data.LabWork;
import exceptions.CollectionIsEmptyException;
import exceptions.LabWorkNotFoundException;
import exceptions.WrongFormatCommandException;
import interaction.LabRaw;
import interaction.Response;
import utility.LabCollection;
import utility.ResponseOutputer;

/**
 * Command 'remove_greater'.
 */
public class RemoveGreaterCommand extends AbstractCommand {
    private final LabCollection labCollection;

    public RemoveGreaterCommand(LabCollection labCollection) {
        super("remove_greater (byAvePoint)","{element}", "remove all elements that are greater than the specified one");
        this.labCollection = labCollection;
    }

    /**
     * Executes the command.
     */
    @Override
    public boolean execute(String stringArg, Object objectArg) {
        try {
            if (stringArg.isEmpty() || objectArg != null) throw new WrongFormatCommandException();
            if (labCollection.getLabCollection().isEmpty()) throw new CollectionIsEmptyException();

            if (stringArg.equals("no")) {
                ResponseOutputer.appendln("Remove greater failed!");
                return true;
            }

            int id = Integer.parseInt(stringArg);
            LabWork labToAssign = labCollection.getLabByID(id);
            if (labToAssign == null) {
                throw new LabWorkNotFoundException();
            } else {
                labCollection.getLabCollection().removeIf(labWork -> labWork.compareTo(labToAssign) > 0);
                ResponseOutputer.appendln("Removed greater successfully!");
            }
            return true;
        } catch (LabWorkNotFoundException exception) {
            ResponseOutputer.appendError("LabWork not found!");
        } catch (NumberFormatException exception) {
            ResponseOutputer.appendError("ID must be a number!");
        } catch (WrongFormatCommandException exception) {
            ResponseOutputer.appendWarning("Using: '" + getName() + "'");
        } catch (CollectionIsEmptyException exception) {
            ResponseOutputer.appendWarning("Collection is empty!");
        }
        return false;
    }
}
