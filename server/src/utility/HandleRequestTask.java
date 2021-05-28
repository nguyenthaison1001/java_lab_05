package utility;

import interaction.Request;
import interaction.Response;
import interaction.ResponseCode;
import interaction.User;

import java.util.concurrent.Callable;

public class HandleRequestTask implements Callable<Response> {
    private final Request request;
    private final CommandManager commandManager;
    private final CollectionManager collectionManager;

    public HandleRequestTask(Request request, CommandManager commandManager, CollectionManager collectionManager) {
        this.request = request;
        this.commandManager = commandManager;
        this.collectionManager = collectionManager;
    }

    /**
     * Computes a result, or throws an exception if unable to do so.
     *
     * @return computed result
     */
    @Override
    public Response call() {
        User hashedUser = new User(
                request.getUser().getUsername(),
                PasswordHasher.hashPassword(request.getUser().getPassword())
        );

        ResponseCode responseCode = executeCommand(
                request.getCommandName(),
                request.getCommandStringArg(),
                request.getCommandObjectArg(),
                hashedUser
        );
        return new Response(responseCode, ResponseOutputer.getAndClearBuffer(), ResponseOutputer.getArgsAndClear(),
                collectionManager.getLabCollection());
    }

    public ResponseCode executeCommand(String commandName, String commandStringArg,
                                       Object commandObjectArg, User user) {
        switch (commandName) {
            case "":
                break;
            case "help":
                if (!commandManager.help(commandStringArg, commandObjectArg, user))
                    return ResponseCode.ERROR;
                break;
            case "show":
                if (!commandManager.show(commandStringArg, commandObjectArg, user))
                    return ResponseCode.ERROR;
                break;
//            case "save":
//                if (!commandManager.save(commandStringArg, commandObjectArg, user))
//                    return ResponseCode.ERROR;
//                break;
            case "info":
                if (!commandManager.info(commandStringArg, commandObjectArg, user))
                    return ResponseCode.ERROR;
                break;
            case "exit":
                if (!commandManager.exit(commandStringArg, commandObjectArg, user))
                    return ResponseCode.ERROR;
                break;
            case "clear":
                if (!commandManager.clear(commandStringArg, commandObjectArg, user))
                    return ResponseCode.ERROR;
                break;
            case "add":
                if (!commandManager.add(commandStringArg, commandObjectArg, user))
                    return ResponseCode.ERROR;
                break;
            case "remove_by_id":
                if (!commandManager.removeByID(commandStringArg, commandObjectArg, user))
                    return ResponseCode.ERROR;
                break;
            case "update":
                if (!commandManager.update(commandStringArg, commandObjectArg, user))
                    return ResponseCode.ERROR;
                break;
            case "add_if_min":
                if (!commandManager.addIfMin(commandStringArg, commandObjectArg, user))
                    return ResponseCode.ERROR;
                break;
            case "remove_greater":
                if (!commandManager.removeGreater(commandStringArg, commandObjectArg, user))
                    return ResponseCode.ERROR;
                break;
            case "sum_of_minimal_point":
                if (!commandManager.sumMinimalPoint(commandStringArg, commandObjectArg, user))
                    return ResponseCode.ERROR;
                break;
            case "count_less_than_difficulty":
                if (!commandManager.countLessThanDiff(commandStringArg, commandObjectArg, user))
                    return ResponseCode.ERROR;
                break;
            case "filter_starts_with_name":
                if (!commandManager.filterName(commandStringArg, commandObjectArg, user))
                    return ResponseCode.ERROR;
                break;
            case "execute_script":
                if (!commandManager.executeScript(commandStringArg, commandObjectArg, user))
                    return ResponseCode.ERROR;
                break;
            case "login":
                if (!commandManager.login(commandStringArg, commandObjectArg, user))
                    return ResponseCode.ERROR;
                break;
            case "register":
                if (!commandManager.register(commandStringArg, commandObjectArg, user))
                    return ResponseCode.ERROR;
                break;
            default:
                return ResponseCode.ERROR;
        }
        return ResponseCode.OK;
    }
}
