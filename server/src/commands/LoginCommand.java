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
            if (databaseUserManager.checkUserByUsernameAndPassword(user))
                ResponseOutputer.appendln("Пользователь " + user.getUsername() + " авторизован.");
            else throw new UserIsNotFoundException();
            return true;
        } catch (WrongFormatCommandException exception) {
            ResponseOutputer.appendln("Использование: эммм...эээ.это внутренняя команда...");
        } catch (ClassCastException exception) {
            ResponseOutputer.appendError("Переданный клиентом объект неверен!");
        } catch (DatabaseHandlingException exception) {
            ResponseOutputer.appendError("Произошла ошибка при обращении к базе данных!");
        } catch (UserIsNotFoundException exception) {
            ResponseOutputer.appendError("Неправильные имя пользователя или пароль!");
        }
        return false;
    }
}
