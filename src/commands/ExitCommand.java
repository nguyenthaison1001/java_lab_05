package commands;

import Exceptions.WrongFormatCommandException;
import ProgramUtility.Console;
import ProgramUtility.LabAsker;

/**
 * Command 'exit'.
 */
public class ExitCommand extends AbstractCommand {
    private final LabAsker labAsker;

    public ExitCommand(LabAsker labAsker) {
        super("exit", "end the program (without saving to file)");
        this.labAsker = labAsker;
    }

    /**
     * Executes the command.
     */
    @Override
    public void execute(String argument) {
        try {
            if (!argument.isEmpty()) throw new WrongFormatCommandException();
            if (labAsker.areYouSure()) {
                System.out.println("Goodbye!");
                System.exit(0);
            }
        } catch (WrongFormatCommandException exception) {
            Console.printWarning("Using: '" + getName() + "'");
        }
    }
}
