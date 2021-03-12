package commands;

/**
 * Interface for commands.
 */
public interface Command {
    String getDescription();
    String getName();
    void execute(String argument) ;
}
