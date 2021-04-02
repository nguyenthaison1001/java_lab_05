package utility;

import interaction.Request;
import interaction.Response;
import interaction.ResponseCode;

public class RequestHandler {
    private final CommandManager commandManager;

    public RequestHandler(CommandManager commandManager) {
        this.commandManager = commandManager;
    }

    public Response handle(Request request) {
        ResponseCode responseCode = executeCommand(
                request.getCommandName(),
                request.getCommandStringArg(),
                request.getCommandObjectArg()
        );
        return new Response(responseCode, ResponseOutputer.getAndClearBuffer());
    }

    public ResponseCode executeCommand(String commandName, String commandStringArg,
                                       Object commandObjectArg) {
        switch (commandName) {
            case "":
                break;
            case "help":
                if (!commandManager.help(commandStringArg, commandObjectArg))
                    return ResponseCode.ERROR;
                break;
            case "show":
                if (!commandManager.show(commandStringArg, commandObjectArg))
                    return ResponseCode.ERROR;
                break;
            case "save":
                if (!commandManager.save(commandStringArg, commandObjectArg))
                    return ResponseCode.ERROR;
                break;
            case "info":
                if (!commandManager.info(commandStringArg, commandObjectArg))
                    return ResponseCode.ERROR;
                break;
            case "exit":
                if (!commandManager.exit(commandStringArg, commandObjectArg))
                    return ResponseCode.ERROR;
                break;
            case "clear":
                if (!commandManager.clear(commandStringArg, commandObjectArg))
                    return ResponseCode.ERROR;
                break;
            case "remove_first":
                if (!commandManager.removeFirst(commandStringArg, commandObjectArg))
                    return ResponseCode.ERROR;
                break;
            case "add":
                if (!commandManager.add(commandStringArg, commandObjectArg))
                    return ResponseCode.ERROR;
                break;
            case "remove_by_id":
                if (!commandManager.removeByID(commandStringArg, commandObjectArg))
                    return ResponseCode.ERROR;
                break;
            case "update":
                if (!commandManager.update(commandStringArg, commandObjectArg))
                    return ResponseCode.ERROR;
                break;
            case "add_if_min":
                if (!commandManager.addIfMin(commandStringArg, commandObjectArg))
                    return ResponseCode.ERROR;
                break;
            case "remove_greater":
                if (!commandManager.removeGreater(commandStringArg, commandObjectArg))
                    return ResponseCode.ERROR;
                break;
            case "sum_of_minimal_point":
                if (!commandManager.sumMinimalPoint(commandStringArg, commandObjectArg))
                    return ResponseCode.ERROR;
                break;
            case "count_less_than_difficulty":
                if (!commandManager.countLessThanDiff(commandStringArg, commandObjectArg))
                    return ResponseCode.ERROR;
                break;
            case "filter_starts_with_name":
                if (!commandManager.filterName(commandStringArg, commandObjectArg))
                    return ResponseCode.ERROR;
                break;
            case "execute_script":
                if (!commandManager.executeScript(commandStringArg, commandObjectArg))
                    return ResponseCode.ERROR;
                break;
            default:
                return ResponseCode.ERROR;
        }
        return ResponseCode.OK;
    }
}
