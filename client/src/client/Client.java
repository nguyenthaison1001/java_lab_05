package client;

import controllers.MainWindowController;
import data.LabWork;
import exceptions.ConnectionErrorException;
import exceptions.NotInDeclaredLimitsException;
import interaction.Request;
import interaction.Response;
import interaction.ResponseCode;
import interaction.User;
import utility.Outputer;
import utility.OutputerUI;
import utility.ScriptHandler;

import java.io.*;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.util.Arrays;
import java.util.LinkedList;

public class Client {
    private final String host;
    private final int port;
    private DatagramChannel clientChannel;
    private SocketAddress addr;
    private User user;
    private boolean isConnected;

    public Client(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void run() {
        try {
            connectToServer();
        } catch (NotInDeclaredLimitsException exception) {
            Outputer.printError("ClientException");
            System.exit(0);
        } catch (ConnectionErrorException exception) { /* ? */ }
    }

    public void stop() {
        try {
            processRequestToServer(MainWindowController.EXIT_COMMAND_NAME, "", null);
            clientChannel.close();
            Outputer.println("EndWorkOfClient");
        } catch (IOException | NullPointerException exception) {
            Outputer.printError("EndWorkOfClientException");
            if (clientChannel == null) Outputer.printError("EndRunningWorkOfClientException");
        }
    }

    private void connectToServer() throws ConnectionErrorException, NotInDeclaredLimitsException {
        try {
            Outputer.println("ConnectionToServer");
            addr = new InetSocketAddress(host, port);
            clientChannel = DatagramChannel.open();
            clientChannel.connect(addr);
            isConnected = true;
            Outputer.println("ConnectionToServerComplete");
        } catch (IllegalArgumentException exception) {
            Outputer.printError("ServerAddressException");
            isConnected = false;
            throw new NotInDeclaredLimitsException();
        } catch (IOException exception) {
            Outputer.printError("ConnectionToServerException");
            isConnected = false;
            throw new ConnectionErrorException();
        }
    }

    public Boolean processUserAuthentication(String username, String password, boolean register) {
        Con con = new Con();
        String command;
        try {
            command = register ? MainWindowController.REGISTER_COMMAND_NAME : MainWindowController.LOGIN_COMMAND_NAME;
            con.request = new Request(command, "", new User(username, password));
            send(con);
            receive(con);
            if (register || con.response.getResponseCode() == ResponseCode.ERROR)
                OutputerUI.errorInfoDialog(con.response.getResponseBody(), con.response.getResponseBodyArgs());
        } catch (InvalidClassException | NotSerializableException exception) {
            OutputerUI.error("DataSendingException");
        } catch (ClassNotFoundException exception) {
            OutputerUI.error("DataReadingException");
        } catch (IOException exception) {
            OutputerUI.error("EndConnectionToServerException");
            try {
                connectToServer();
                OutputerUI.info("ConnectionToServerComplete");
            } catch (ConnectionErrorException | NotInDeclaredLimitsException reconnectionException) {
                OutputerUI.info("TryAuthLater");
            }
        }
        if (con.response != null && con.response.getResponseCode().equals(ResponseCode.OK)) {
            user = con.request.getUser();
            return true;
        }
        return false;
    }

    public LinkedList<LabWork> processRequestToServer(String commandName, String commandStringArg,
                                                      Serializable commandObjectArg) {
        Con con = new Con();
        try {
            con.request = new Request(commandName, commandStringArg, commandObjectArg, user);
            send(con);
            receive(con);
            System.out.println("(" + con.response.getResponseBody()+")");
            System.out.println(Arrays.toString(con.response.getResponseBodyArgs()));
            if (!con.response.getResponseBody().isEmpty())
                OutputerUI.errorInfoDialog(con.response.getResponseBody(), con.response.getResponseBodyArgs());
        } catch (InvalidClassException | NotSerializableException exception) {
            OutputerUI.error("DataSendingException");
        } catch (ClassNotFoundException exception) {
            OutputerUI.error("DataReadingException");
        } catch (IOException exception) {
            OutputerUI.error("EndConnectionToServerException");
            try {
                connectToServer();
                OutputerUI.info("ConnectionToServerComplete");
            } catch (ConnectionErrorException | NotInDeclaredLimitsException reconnectionException) {
                OutputerUI.info("TryCommandLater");
            }
        }
        return con.response == null ? null : con.response.getLabCollection();
    }

    public Boolean processScriptToServer(File scriptFile) {
        Con con = new Con();
        ScriptHandler scriptHandler = new ScriptHandler(scriptFile);
        do {
            try {
                con.request = con.response != null ? scriptHandler.handle(con.response.getResponseCode(), user) :
                        scriptHandler.handle(null, user);
                if (con.request == null) return false;
                if (con.request.isEmpty()) continue;
                send(con);
                receive(con);
                if (!con.response.getResponseBody().isEmpty())
                    OutputerUI.errorInfoDialog(con.response.getResponseBody(), con.response.getResponseBodyArgs());
            } catch (InvalidClassException | NotSerializableException exception) {
                OutputerUI.error("DataSendingException");
            } catch (ClassNotFoundException exception) {
                OutputerUI.error("DataReadingException");
            } catch (IOException exception) {
                OutputerUI.error("EndConnectionToServerException");
                try {
                    connectToServer();
                    OutputerUI.info("ConnectionToServerComplete");
                } catch (ConnectionErrorException | NotInDeclaredLimitsException reconnectionException) {
                    OutputerUI.info("TryCommandLater");
                }
            }
        } while (!con.request.getCommandName().equals("exit"));
        return true;
    }

    private void send(Con con) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream(1024);
        ObjectOutputStream oos = new ObjectOutputStream(new BufferedOutputStream(baos));
        oos.writeObject(con.request);
        oos.flush();
        byte[] arrSend = baos.toByteArray();
        ByteBuffer bufSend = ByteBuffer.wrap(arrSend);
        clientChannel.send(bufSend, addr);
        oos.close();
    }

    private void receive(Con con) throws IOException, ClassNotFoundException {
        ByteBuffer bufReceive = ByteBuffer.allocate(8192);
        bufReceive.clear();
        clientChannel.receive(bufReceive);
        byte[] arrReceive = bufReceive.array();
        ByteArrayInputStream bais = new ByteArrayInputStream(arrReceive);
        ObjectInputStream ois = new ObjectInputStream(new BufferedInputStream(bais));
        con.response = (Response) ois.readObject();
        ois.close();
    }

    static class Con {
        Request request;
        Response response;
    }

    public boolean isConnected() {
        return isConnected;
    }

    public String getUsername() {
        return user == null ? null : user.getUsername();
    }
}
