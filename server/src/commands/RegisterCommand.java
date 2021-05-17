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
            if (databaseUserManager.insertUser(user))
                ResponseOutputer.appendln("Пользователь " + user.getUsername() + " зарегистрирован.");
            else throw new UserAlreadyExists();
            return true;
        } catch (WrongFormatCommandException exception) {
            ResponseOutputer.appendln("Использование: эммм...эээ.это внутренняя команда...");
        } catch (ClassCastException exception) {
            ResponseOutputer.appendError("Переданный клиентом объект неверен!");
        } catch (DatabaseHandlingException exception) {
            ResponseOutputer.appendError("Произошла ошибка при обращении к базе данных!");
        } catch (UserAlreadyExists exception) {
            ResponseOutputer.appendError("Пользователь " + user.getUsername() + " уже существует!");
        }
        return false;
    }
}
