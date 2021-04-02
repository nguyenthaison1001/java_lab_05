package commands;

/**
 * Interface for commands.
 */
public interface Command {
    String getName();

    String getUsage();

    String getDescription();

    boolean execute(String commandStringArg, Object commandObjectArg);
}
