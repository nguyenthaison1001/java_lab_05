package commands;

import data.Coordinates;
import data.Difficulty;
import data.Discipline;
import data.LabWork;
import exceptions.*;
import interaction.LabRaw;
import sun.tracing.dtrace.DTraceProviderFactory;
import utility.LabCollection;
import utility.ResponseOutputer;

/**
 * Command 'update'.
 */
public class UpdateCommand extends AbstractCommand {
    private final LabCollection labCollection;

    public UpdateCommand(LabCollection labCollection) {
        super("update <id>", "{element}", "update the value of element by ID");
        this.labCollection = labCollection;
    }

    /**
     * Executes the command.
     */
    @Override
    public boolean execute(String stringArg, Object objectArg) {
        try {
            if (stringArg.isEmpty() || objectArg == null) throw new WrongFormatCommandException();
            if (labCollection.getLabCollection().isEmpty()) throw new CollectionIsEmptyException();

            Integer id = Integer.parseInt(stringArg);
            LabWork oldLab = labCollection.getLabByID(id);
            if (oldLab == null) throw new LabWorkNotFoundException();

            LabRaw labUpdate = (LabRaw) objectArg;

            String name = labUpdate.getName();
            Coordinates coordinates = labUpdate.getCoordinates();
            Long minimalPoint = labUpdate.getMinimalPoint();
            Integer tunedInWorked = labUpdate.getTunedInWorks();
            Integer averagePoint = labUpdate.getAveragePoint();
            Difficulty difficulty = labUpdate.getDifficulty();
            Discipline discipline = labUpdate.getDiscipline();

            if (name == null) name = oldLab.getName();
            if (coordinates == null) coordinates = oldLab.getCoordinates();
            if (minimalPoint == null) minimalPoint = oldLab.getMinimalPoint();
            if (tunedInWorked == null) tunedInWorked = oldLab.getTunedInWorks();
            if (averagePoint == null) averagePoint = oldLab.getAveragePoint();
            if (difficulty == null) difficulty = oldLab.getDifficulty();
            if (discipline == null) discipline = oldLab.getDiscipline();

            labCollection.getLabCollection().remove(oldLab);
            LabWork newLab = new LabWork(
                    id,
                    name,
                    coordinates,
                    minimalPoint,
                    tunedInWorked,
                    averagePoint,
                    difficulty,
                    discipline
            );
            labCollection.getLabCollection().add(newLab);

            ResponseOutputer.appendln("The old lab: \n" + oldLab);
            ResponseOutputer.appendln("The new lab: \n" + newLab);

            ResponseOutputer.appendln("Updated successfully!");

            return true;
        } catch (WrongFormatCommandException exception) {
            ResponseOutputer.appendWarning("Using: '" + getName() + "'");
        } catch (NumberFormatException exception) {
            ResponseOutputer.appendError("ID must be a number!");
        } catch (LabWorkNotFoundException exception) {
            ResponseOutputer.appendError("LabWork not found!");
        } catch (CollectionIsEmptyException exception) {
            ResponseOutputer.appendWarning("Collection is empty!");
        } catch (NullPointerException exception) {
            ResponseOutputer.appendError("Argument is null (NullPointerException)!");
        }
        return false;
    }
}
