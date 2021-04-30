package interaction;

import java.io.Serializable;

public class Request implements Serializable {
    private final String commandName;
    private final String commandStringArg;
    private final Serializable commandObjectArg;
    private User user;

    public Request(String commandName, String commandStringArg, Serializable commandObjectArg, User user) {
        this.commandName = commandName;
        this.commandStringArg = commandStringArg;
        this.commandObjectArg = commandObjectArg;
        this.user = user;
    }

    public Request(String commandName, String commandStringArg, User user) {
        this(commandName, commandStringArg, null, user);
    }

    public Request(User user) {
        this("", "", user);
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

    public User getUser() {
        return user;
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
