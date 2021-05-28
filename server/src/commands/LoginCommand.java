package commands;

import exceptions.DatabaseHandlingException;
import exceptions.UserIsNotFoundException;
import exceptions.WrongFormatCommandException;
import interaction.User;
import utility.DatabaseUserManager;
import utility.ResponseOutputer;

public class LoginCommand extends AbstractCommand {
    private DatabaseUserManager databaseUserManager;

    public LoginCommand(DatabaseUserManager databaseUserManager) {
        super("Login", "", "internal command");
        this.databaseUserManager = databaseUserManager;
    }

    @Override
    public boolean execute(String stringArg, Object objectArg, User user) {
        try {
            if (!stringArg.isEmpty() || objectArg != null) throw new WrongFormatCommandException();
            if (databaseUserManager.checkUserByUsernameAndPassword(user)){
                ResponseOutputer.append("UserAuthorized");
                ResponseOutputer.appendArgs(user.getUsername());
            }
            else throw new UserIsNotFoundException();
            return true;
        } catch (WrongFormatCommandException exception) {
            ResponseOutputer.append("Using");
            ResponseOutputer.appendArgs(getName() + " " + getUsage() + "'");
        } catch (ClassCastException exception) {
            ResponseOutputer.appendError("ClientObjectException");
        } catch (DatabaseHandlingException exception) {
            ResponseOutputer.appendError("DatabaseHandlingException");
        } catch (UserIsNotFoundException exception) {
            ResponseOutputer.appendError("InvalidUserException");
        }
        return false;
    }
}
