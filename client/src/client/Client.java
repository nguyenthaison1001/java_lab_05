package client;

import exceptions.ConnectionErrorException;
import exceptions.NotInDeclaredLimitsException;
import interaction.Request;
import interaction.Response;
import interaction.ResponseCode;
import interaction.User;
import utility.AuthHandler;
import utility.Outputer;
import utility.UserHandler;

import java.io.*;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;

public class Client {
    private String host;
    private int port;
    private int reconnectionTimeout;
    private int reconnectionAttempts;
    private int maxReconnectionAttempts;
    private DatagramChannel clientChannel;
    private UserHandler userHandler;
    private AuthHandler authHandler;
    private SocketAddress addr;
    private User user;

    public Client(String host, int port, int reconnectionTimeout, int maxReconnectionAttempts, UserHandler userHandler, AuthHandler authHandler) {
        this.host = host;
        this.port = port;
        this.reconnectionTimeout = reconnectionTimeout;
        this.maxReconnectionAttempts = maxReconnectionAttempts;
        this.userHandler = userHandler;
        this.authHandler = authHandler;
    }

    public void run() {
        try {
            while (true) {
                try {
//                    checkAddress();
                    connectToServer();
                    processUserAuthentication();
                    processRequestToServer(clientChannel, addr);
                    break;
                } catch (ConnectionErrorException exception) {
                    if (reconnectionAttempts >= maxReconnectionAttempts) {
                        Outputer.printError("Connection attempts exceeded!");
                        break;
                    }
                    try {
                        Thread.sleep(reconnectionTimeout);
                    } catch (IllegalArgumentException timeoutException) {
                        Outputer.printError("Connection timeout '" + reconnectionTimeout +
                                "' is out of range!");
                        Outputer.println("Reconnection will be done immediately.");
                    } catch (Exception timeoutException) {
                        Outputer.printError("An error occurred while trying to wait for a connection!");
                        Outputer.println("Reconnection will be done immediately.");
                    }
                }
                reconnectionAttempts++;
            }

            if (clientChannel!= null) clientChannel.close();
            Outputer.println("Client shutdown!");

//        } catch (NotInDeclaredLimitsException exception) {
//            Outputer.printError("The client can't be started!");
        } catch (IOException exception) {
            Outputer.printError("An error occurred while trying to end the connection to the server!");
        }
    }

    private void connectToServer() throws ConnectionErrorException {
        try {
            if (reconnectionAttempts >= 1) Outputer.println("Reconnecting to the server...");
            addr = new InetSocketAddress(host, port);

            clientChannel = DatagramChannel.open();
            clientChannel.connect(addr);
        } catch (IOException exception) {
            Outputer.printError("An error occurred while connecting to the server!");
            throw new ConnectionErrorException();
        }
    }

    private void checkAddress() throws NotInDeclaredLimitsException, ConnectionErrorException {
        try {
            if (port != 4387) throw new ConnectionErrorException();
            byte[] ipAddr = new byte[] { 127, 0, 0, 1 };
            InetAddress IPAddress = InetAddress.getByName(host);
            if (!IPAddress.equals(InetAddress.getByAddress(ipAddr))) throw new UnknownHostException();
        } catch (UnknownHostException exception) {
            Outputer.printError("HOST_ADDRESS's incorrect!");
            throw new NotInDeclaredLimitsException();
        } catch (ConnectionErrorException exception) {
            Outputer.printError("PORT_NUMBER's incorrect!");
            throw new ConnectionErrorException();
        }
    }

    private void processUserAuthentication() throws ConnectionErrorException {
        Con con = new Con();
        do {
            try {
                con.request = authHandler.handle();

                if (con.request.isEmpty()) continue;

                send(clientChannel, con, addr);
                receive(clientChannel, con);

                Outputer.print(con.response.getResponseBody());
            } catch (InvalidClassException | NotSerializableException exception) {
                Outputer.printError("An error occurred while sending data to the server!");
            } catch (ClassNotFoundException exception) {
                Outputer.printError("An error occurred while reading the received data!");
            } catch (IOException exception) {
                Outputer.printError("The connection to the server has been dropped!");
                reconnectionAttempts++;
                connectToServer();
            }
        } while (con.response == null || !con.response.getResponseCode().equals(ResponseCode.OK));
        // y nghia cua while: khi nao authenticating login thanh cong -> stop loop
        user = con.request.getUser();
    }

    private void processRequestToServer(DatagramChannel clientChannel, SocketAddress addr) throws ConnectionErrorException {
        Con con = new Con();
        do {
            try {
                con.request = con.response != null ? userHandler.handle(con.response.getResponseCode(), user) :
                        userHandler.handle(null, user);

                if (con.request.isEmpty()) continue;

                send(clientChannel, con, addr);
                receive(clientChannel, con);

                Outputer.print(con.response.getResponseBody());
            } catch (InvalidClassException | NotSerializableException exception) {
                Outputer.printError("An error occurred while sending data to the server!");
            } catch (ClassNotFoundException exception) {
                Outputer.printError("An error occurred while reading the received data!");
            } catch (IOException exception) {
                Outputer.printError("The connection to the server has been dropped!");
                reconnectionAttempts++;
                connectToServer();
            }
        } while (!con.request.getCommandName().equals("exit"));
    }

    private void send(DatagramChannel clientChannel, Con con, SocketAddress addr) throws IOException {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream(1024);
            ObjectOutputStream oos = new ObjectOutputStream(new BufferedOutputStream(baos));

            oos.writeObject(con.request);
            oos.flush();

            byte[] arr = baos.toByteArray();
            ByteBuffer buf = ByteBuffer.wrap(arr);

            clientChannel.send(buf, addr);
            oos.close();

        } catch (IOException e) {
            Outputer.printError("IOException from send.");
            throw new IOException();
        }
    }

    private void receive(DatagramChannel clientChannel, Con con) throws IOException, ClassNotFoundException {
        try {
            ByteBuffer buf = ByteBuffer.allocate(4096);
            buf.clear();
            clientChannel.receive(buf);

            byte[] arr = buf.array();
            ByteArrayInputStream bais = new ByteArrayInputStream(arr);
            ObjectInputStream ois = new ObjectInputStream(new BufferedInputStream(bais));

            con.response = (Response) ois.readObject();
            ois.close();

        } catch (IOException e) {
            Outputer.printError("IOException from receive.");
            throw new IOException();
        }
    }

    static class Con {
        Request request;
        Response response;
    }
}
