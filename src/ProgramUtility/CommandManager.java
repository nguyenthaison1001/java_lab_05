package ProgramUtility;

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
        commandList.add(saveCommand);
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
     * @param argument String argument.
     */
    public void help(String argument){
        for (Command command : commandList) {
            Console.printTable(command.getName(), command.getDescription());
        }
    }

    /**
     * Executes needed command.
     * @param argument String argument.
     */
    public void show(String argument) {
        showCommand.execute(argument);
    }

    /**
     * Executes needed command.
     * @param argument String argument.
     */
    public void save(String argument) {
        saveCommand.execute(argument);
    }

    /**
     * Executes needed command.
     * @param argument String argument.
     */
    public void info(String argument) {
        infoCommand.execute(argument);
    }

    /**
     * Executes needed command.
     * @param argument String argument.
     */
    public void exit(String argument) {
        exitCommand.execute(argument);
    }

    /**
     * Executes needed command.
     * @param argument String argument.
     */
    public void clear(String argument) {
        clearCommand.execute(argument);
    }

    /**
     * Executes needed command.
     * @param argument String argument.
     */
    public void removeFirst(String argument) {
        removeFirstCommand.execute(argument);
    }

    /**
     * Executes needed command.
     * @param argument String argument.
     */
    public void add(String argument) {
        addCommand.execute(argument);
    }

    /**
     * Executes needed command.
     * @param argument String argument.
     */
    public void removeByID(String argument) {
        removeByIDCommand.execute(argument);
    }

    /**
     * Executes needed command.
     * @param argument String argument.
     */
    public void update(String argument) {
        updateCommand.execute(argument);
    }

    /**
     * Executes needed command.
     * @param argument String argument.
     */
    public void addIfMin(String argument) {
        addIfMinCommand.execute(argument);
    }

    /**
     * Executes needed command.
     * @param argument String argument.
     */
    public void removeGreater(String argument) {
        removeGreaterCommand.execute(argument);
    }

    /**
     * Executes needed command.
     * @param argument String argument.
     */
    public void sumMinimalPoint(String argument) {
        sumMinimalPointCommand.execute(argument);
    }

    /**
     * Executes needed command.
     * @param argument String argument.
     */
    public void countLessThanDiff(String argument) {
        countLessThanDiff.execute(argument);
    }

    /**
     * Executes needed command.
     * @param argument String argument.
     */
    public void filterName(String argument) {
        filterName.execute(argument);
    }

    /**
     * Executes needed command.
     * @param argument String argument.
     */
    public void executeScript(String argument) {
        executeScript.execute(argument);
    }

    /**
     * Prints that command is not found.
     * @param command String argument.
     */
    public boolean noSuchCommand(String command) {
        Console.println("Command '" + command + "' not found. Enter 'help' for help.");
        return true;
    }
}
