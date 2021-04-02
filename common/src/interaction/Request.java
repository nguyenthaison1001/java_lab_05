package interaction;

import java.io.Serializable;

public class Request implements Serializable {
    private final String commandName;
    private final String commandStringArg;
    private final Serializable commandObjectArg;

    public Request(String commandName, String commandStringArg, Serializable commandObjectArg) {
        this.commandName = commandName;
        this.commandStringArg = commandStringArg;
        this.commandObjectArg = commandObjectArg;
    }

    public Request(String commandName, String commandStringArg) {
        this(commandName, commandStringArg, null);
    }

    public Request() {
        this("", "");
    }

    public String getCommandName() {
        return commandName;
    }

    public String getCommandStringArg() {
        return commandStringArg;
    }

    public Object getCommandObjectArg() {
        return commandObjectArg;
    }

    /**
     * @return Is this request empty.
     */
    public boolean isEmpty() {
        return commandName.isEmpty() && commandStringArg.isEmpty() && commandObjectArg == null;
    }

    @Override
    public String toString() {
        return "Request[" + commandName + ", " + commandStringArg + ", " + commandObjectArg + "]";
    }
}
