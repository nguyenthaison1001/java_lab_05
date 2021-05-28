package commands;

import exceptions.DatabaseHandlingException;
import exceptions.UserAlreadyExists;
import exceptions.WrongFormatCommandException;
import interaction.User;
import utility.DatabaseUserManager;
import utility.ResponseOutputer;

public class RegisterCommand extends AbstractCommand {
    private DatabaseUserManager databaseUserManager;

    public RegisterCommand(DatabaseUserManager databaseUserManager) {
        super("register", "", "internal command");
        this.databaseUserManager = databaseUserManager;
    }

    /**
     * Executes the command.
     *
     * @return Command exit status.
     */
    @Override
    public boolean execute(String stringArg, Object objectArg, User user) {
        try {
            if (!stringArg.isEmpty() || objectArg != null) throw new WrongFormatCommandException();
            if (databaseUserManager.insertUser(user)) {
                ResponseOutputer.append("UserRegistered");
                ResponseOutputer.appendArgs(user.getUsername());
            }
            else throw new UserAlreadyExists();
            return true;
        } catch (WrongFormatCommandException exception) {
            ResponseOutputer.append("Using");
            ResponseOutputer.appendArgs(getName() + " " + getUsage() + "'");        } catch (ClassCastException exception) {
            ResponseOutputer.appendError("ClientObjectException");
        } catch (DatabaseHandlingException exception) {
            ResponseOutputer.appendError("DatabaseHandlingException");
        } catch (UserAlreadyExists exception) {
            ResponseOutputer.append("UserExistsException");
            ResponseOutputer.appendArgs(user.getUsername());
        }
        return false;
    }
}
