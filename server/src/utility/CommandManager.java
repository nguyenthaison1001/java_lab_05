package utility;

import commands.Command;
import interaction.User;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Operates the commands.
 */
public class CommandManager {
    private List<Command> commandList = new ArrayList<>();
    private Command helpCommand;
    private Command infoCommand;
    private Command showCommand;
    private Command exitCommand;
    private Command clearCommand;
    private Command addCommand;
    private Command removeByIDCommand;
    private Command updateCommand;
    private Command addIfMinCommand;
    private Command removeGreaterCommand;
    private Command sumMinimalPointCommand;
    private Command filterName;
    private Command countLessThanDiff;
    private Command executeScript;
    private Command loginCommand;
    private Command registerCommand;

    private ReadWriteLock collectionLocker = new ReentrantReadWriteLock();

    public CommandManager(Command helpCommand, Command infoCommand, Command updateCommand,
                          Command showCommand,
                          Command addCommand, Command removeByIDCommand, Command clearCommand,
                          Command executeScript, Command exitCommand,
                          Command addIfMinCommand, Command removeGreaterCommand, Command sumMinimalPointCommand,
                          Command countLessThanDiff, Command filterName, Command loginCommand, Command registerCommand) {
        this.helpCommand = helpCommand;
        this.infoCommand = infoCommand;
        this.showCommand = showCommand;
        this.clearCommand = clearCommand;
        this.exitCommand = exitCommand;
        this.addCommand = addCommand;
        this.removeByIDCommand = removeByIDCommand;
        this.updateCommand = updateCommand;
        this.addIfMinCommand = addIfMinCommand;
        this.removeGreaterCommand = removeGreaterCommand;
        this.sumMinimalPointCommand = sumMinimalPointCommand;
        this.filterName = filterName;
        this.countLessThanDiff = countLessThanDiff;
        this.executeScript = executeScript;
        this.loginCommand = loginCommand;
        this.registerCommand = registerCommand;

        commandList.add(helpCommand);
        commandList.add(infoCommand);
        commandList.add(showCommand);
        commandList.add(addCommand);
        commandList.add(updateCommand);
        commandList.add(removeByIDCommand);
        commandList.add(clearCommand);
        commandList.add(executeScript);
        commandList.add(exitCommand);
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
    public boolean help(String stringArg, Object objectArg, User user){
        if (helpCommand.execute(stringArg, objectArg, user)) {
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
    public boolean show(String stringArg, Object objectArg, User user) {
        collectionLocker.readLock().lock();
        try {
            return showCommand.execute(stringArg, objectArg, user);
        } finally {
            collectionLocker.readLock().unlock();
        }    }


    /**
     * Executes needed command.
     * @param stringArg String argument.
     */
    public boolean info(String stringArg, Object objectArg, User user) {
        collectionLocker.readLock().lock();
        try {
            return infoCommand.execute(stringArg, objectArg, user);
        } finally {
            collectionLocker.readLock().unlock();
        }
    }

    /**
     * Executes needed command.
     * @param stringArg String argument.
     */
    public boolean exit(String stringArg, Object objectArg, User user) {
        return exitCommand.execute(stringArg, objectArg, user);
    }

    /**
     * Executes needed command.
     * @param stringArg String argument.
     */
    public boolean clear(String stringArg, Object objectArg, User user) {
        collectionLocker.writeLock().lock();
        try {
            return clearCommand.execute(stringArg, objectArg, user);
        } finally {
            collectionLocker.writeLock().unlock();
        }
    }

    /**
     * Executes needed command.
     * @param stringArg String argument.
     */
    public boolean add(String stringArg, Object objectArg, User user) {
        collectionLocker.writeLock().lock();
        try {
            return addCommand.execute(stringArg, objectArg, user);
        } finally {
            collectionLocker.writeLock().unlock();
        }
    }

    /**
     * Executes needed command.
     * @param stringArg String argument.
     */
    public boolean removeByID(String stringArg, Object objectArg, User user) {
        collectionLocker.writeLock().lock();
        try {
            return removeByIDCommand.execute(stringArg, objectArg, user);
        } finally {
            collectionLocker.writeLock().unlock();
        }
    }

    /**
     * Executes needed command.
     * @param stringArg String argument.
     */
    public boolean update(String stringArg, Object objectArg, User user) {
        collectionLocker.writeLock().lock();
        try {
            return updateCommand.execute(stringArg, objectArg, user);
        } finally {
            collectionLocker.writeLock().unlock();
        }
    }

    /**
     * Executes needed command.
     * @param stringArg String argument.
     */
    public boolean addIfMin(String stringArg, Object objectArg, User user) {
        collectionLocker.writeLock().lock();
        try {
            return addIfMinCommand.execute(stringArg, objectArg, user);
        } finally {
            collectionLocker.writeLock().unlock();
        }
    }

    /**
     * Executes needed command.
     * @param stringArg String argument.
     */
    public boolean removeGreater(String stringArg, Object objectArg, User user) {
        collectionLocker.writeLock().lock();
        try {
            return removeGreaterCommand.execute(stringArg, objectArg, user);
        } finally {
            collectionLocker.writeLock().unlock();
        }
    }

    /**
     * Executes needed command.
     * @param stringArg String argument.
     */
    public boolean sumMinimalPoint(String stringArg, Object objectArg, User user) {
        collectionLocker.readLock().lock();
        try {
            return sumMinimalPointCommand.execute(stringArg, objectArg, user);
        } finally {
            collectionLocker.readLock().unlock();
        }
    }

    /**
     * Executes needed command.
     * @param stringArg String argument.
     */
    public boolean countLessThanDiff(String stringArg, Object objectArg, User user) {
        collectionLocker.readLock().lock();
        try {
            return countLessThanDiff.execute(stringArg, objectArg, user);
        } finally {
            collectionLocker.readLock().unlock();
        }
    }

    /**
     * Executes needed command.
     * @param stringArg String argument.
     */
    public boolean filterName(String stringArg, Object objectArg, User user) {
        collectionLocker.readLock().lock();
        try {
            return filterName.execute(stringArg, objectArg, user);
        } finally {
            collectionLocker.readLock().unlock();
        }
    }

    /**
     * Executes needed command.
     * @param stringArg String argument.
     */
    public boolean executeScript(String stringArg, Object objectArg, User user) {
        return executeScript.execute(stringArg, objectArg, user);
    }

    /**
     * Executes needed command.
     *
     * @param stringArg Its string argument.
     * @param objectArg Its object argument.
     * @param user           User object.
     * @return Command exit status.
     */
    public boolean login(String stringArg, Object objectArg, User user) {
        return loginCommand.execute(stringArg, objectArg, user);
    }

    /**
     * Executes needed command.
     *
     * @param stringArg Its string argument.
     * @param objectArg Its object argument.
     * @param user           User object.
     * @return Command exit status.
     */
    public boolean register(String stringArg, Object objectArg, User user) {
        return registerCommand.execute(stringArg, objectArg, user);
    }
}
