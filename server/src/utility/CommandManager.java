package utility;

import commands.Command;

import java.util.ArrayList;
import java.util.List;

/**
 * Operates the commands.
 */
public class CommandManager {
    private List<Command> commandList = new ArrayList<>();
    private Command helpCommand;
    private Command infoCommand;
    private Command showCommand;
    private Command saveCommand;
    private Command exitCommand;
    private Command clearCommand;
    private Command removeFirstCommand;
    private Command addCommand;
    private Command removeByIDCommand;
    private Command updateCommand;
    private Command addIfMinCommand;
    private Command removeGreaterCommand;
    private Command sumMinimalPointCommand;
    private Command filterName;
    private Command countLessThanDiff;
    private Command executeScript;

    public CommandManager(Command helpCommand, Command infoCommand, Command updateCommand,
                          Command showCommand,
                          Command addCommand, Command removeByIDCommand, Command clearCommand,
                          Command saveCommand, Command executeScript, Command exitCommand, Command removeFirstCommand,
                          Command addIfMinCommand, Command removeGreaterCommand, Command sumMinimalPointCommand,
                          Command countLessThanDiff, Command filterName) {
        this.helpCommand = helpCommand;
        this.infoCommand = infoCommand;
        this.showCommand = showCommand;
        this.clearCommand = clearCommand;
        this.saveCommand = saveCommand;
        this.exitCommand = exitCommand;
        this.removeFirstCommand = removeFirstCommand;
        this.addCommand = addCommand;
        this.removeByIDCommand = removeByIDCommand;
        this.updateCommand = updateCommand;
        this.addIfMinCommand = addIfMinCommand;
        this.removeGreaterCommand = removeGreaterCommand;
        this.sumMinimalPointCommand = sumMinimalPointCommand;
        this.filterName = filterName;
        this.countLessThanDiff = countLessThanDiff;
        this.executeScript = executeScript;

        commandList.add(helpCommand);
        commandList.add(infoCommand);
        commandList.add(showCommand);
        commandList.add(addCommand);
        commandList.add(updateCommand);
        commandList.add(removeByIDCommand);
        commandList.add(clearCommand);
//        commandList.add(saveCommand);
        commandList.add(executeScript);
        commandList.add(exitCommand);
        commandList.add(removeFirstCommand);
        commandList.add(addIfMinCommand);
        commandList.add(removeGreaterCommand);
        commandList.add(sumMinimalPointCommand);
        commandList.add(countLessThanDiff);
        commandList.add(filterName);
    }

    /**
     * Prints info about the all commands.
     * @param stringArg String argument.
     */
    public boolean help(String stringArg, Object objectArg){
        if (helpCommand.execute(stringArg, objectArg)) {
            for (Command command : commandList) {
                ResponseOutputer.appendTable(command.getName() + " " + command.getUsage(), command.getDescription());
            }
            return true;
        } else return false;
    }

    /**
     * Executes needed command.
     * @param stringArg String argument.
     */
    public boolean show(String stringArg, Object objectArg) {
        return showCommand.execute(stringArg, objectArg);
    }

    /**
     * Executes needed command.
     * @param stringArg String argument.
     */
    public boolean save(String stringArg, Object objectArg) {
        return saveCommand.execute(stringArg, objectArg);
    }

    /**
     * Executes needed command.
     * @param stringArg String argument.
     */
    public boolean info(String stringArg, Object objectArg) {
        return infoCommand.execute(stringArg, objectArg);
    }

    /**
     * Executes needed command.
     * @param stringArg String argument.
     */
    public boolean exit(String stringArg, Object objectArg) {
        return exitCommand.execute(stringArg, objectArg);
    }

    /**
     * Executes needed command.
     * @param stringArg String argument.
     */
    public boolean clear(String stringArg, Object objectArg) {
        return clearCommand.execute(stringArg, objectArg);
    }

    /**
     * Executes needed command.
     * @param stringArg String argument.
     */
    public boolean removeFirst(String stringArg, Object objectArg) {
        return removeFirstCommand.execute(stringArg, objectArg);
    }

    /**
     * Executes needed command.
     * @param stringArg String argument.
     */
    public boolean add(String stringArg, Object objectArg) {
        return addCommand.execute(stringArg, objectArg);
    }

    /**
     * Executes needed command.
     * @param stringArg String argument.
     */
    public boolean removeByID(String stringArg, Object objectArg) {
        return removeByIDCommand.execute(stringArg, objectArg);
    }

    /**
     * Executes needed command.
     * @param stringArg String argument.
     */
    public boolean update(String stringArg, Object objectArg) {
        return updateCommand.execute(stringArg, objectArg);
    }

    /**
     * Executes needed command.
     * @param stringArg String argument.
     */
    public boolean addIfMin(String stringArg, Object objectArg) {
        return addIfMinCommand.execute(stringArg, objectArg);
    }

    /**
     * Executes needed command.
     * @param stringArg String argument.
     */
    public boolean removeGreater(String stringArg, Object objectArg) {
        return removeGreaterCommand.execute(stringArg, objectArg);
    }

    /**
     * Executes needed command.
     * @param stringArg String argument.
     */
    public boolean sumMinimalPoint(String stringArg, Object objectArg) {
        return sumMinimalPointCommand.execute(stringArg, objectArg);
    }

    /**
     * Executes needed command.
     * @param stringArg String argument.
     */
    public boolean countLessThanDiff(String stringArg, Object objectArg) {
        return countLessThanDiff.execute(stringArg, objectArg);
    }

    /**
     * Executes needed command.
     * @param stringArg String argument.
     */
    public boolean filterName(String stringArg, Object objectArg) {
        return filterName.execute(stringArg, objectArg);
    }

    /**
     * Executes needed command.
     * @param stringArg String argument.
     */
    public boolean executeScript(String stringArg, Object objectArg) {
        return executeScript.execute(stringArg, objectArg);
    }
}
