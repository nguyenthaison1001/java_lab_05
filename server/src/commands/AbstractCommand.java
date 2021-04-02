package commands;

import java.util.Objects;

/**
 * Abstract Command class contains Object methods, name and description.
 */
public abstract class AbstractCommand implements Command{
    private final String name;
    private String usage;
    private final String description;

    public AbstractCommand(String name, String usage, String description) {
        this.name = name;
        this.usage = usage;
        this.description = description;
    }

    /**
     * @return Name of command
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * @return Usage of the command.
     */
    @Override
    public String getUsage() {
        return usage;
    }

    /**
     * @return Description of command
     */
    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return name + " (" + description + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AbstractCommand that = (AbstractCommand) o;
        return name.equals(that.name) &&
                usage.equals(that.usage) &&
                description.equals(that.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, usage, description);
    }
}
