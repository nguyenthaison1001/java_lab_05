package commands;

import interaction.User;

/**
 * Interface for commands.
 */
public interface Command {
    int SADFS=213;

    String getName();

    String getUsage();

    String getDescription();

    boolean execute(String commandStringArg, Object commandObjectArg, User user);
}
