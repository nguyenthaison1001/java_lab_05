import commands.Command;
import commands.ExitCommand;
import commands.SaveCommand;
import interaction.Request;
import interaction.Response;
import utility.*;

import java.io.*;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.logging.Level;

public class Server {
    private final int port;
    private final int soTimeout;
    private final RequestHandler requestHandler;
    private DatagramChannel channel;
    private final LabCollection labCollection;
    private final String fileName;

    public Server(int port, int soTimeout, RequestHandler requestHandler,
                  LabCollection labCollection, String fileName) {
        this.port = port;
        this.soTimeout = soTimeout;
        this.requestHandler = requestHandler;
        this.labCollection = labCollection;
        this.fileName = fileName;
    }

    public void run() {
        try {
            boolean processingStatus = true;
            while (processingStatus) {
                processingStatus = processing();
            }
            if (channel != null) channel.close();
            Outputer.println("Server shutdown!");
        } catch (IOException exception) {
            Outputer.printError("An error occurred while trying to end the connection to the client!");
            AppServer.LOGGER.warning("An error occurred while trying to end the connection to the client!");
        }
    }

    private boolean processing() {
        Con con = new Con();
        try {
            AppServer.LOGGER.info("Server starting on port " + port);

            channel = DatagramChannel.open();
            channel.socket().bind(new InetSocketAddress(port));
            channel.configureBlocking(false);

            Selector selector = Selector.open();
            SelectionKey cliKey = channel.register(selector, SelectionKey.OP_READ);
            cliKey.attach(con);

            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

            while (channel.isOpen()) {
                try {

                    if (reader.ready()) {

                        String line = reader.readLine();

                        if ("exit".equals(line)) {
                            Command exit = new ExitCommand(labCollection, fileName);
                            exit.execute("", null);
                            AppServer.LOGGER.info("Saved collection!");
                            AppServer.LOGGER.info("Server shutdown!");
                            break;
                        }
                        if ("save".equals(line)) {
                            Command save = new SaveCommand(labCollection, fileName);
                            save.execute("", null);
                            AppServer.LOGGER.info("Saved collection!");
                        }
                    }

                    if (selector.selectNow() != 0) {
                        Iterator<SelectionKey> iter = selector.selectedKeys().iterator();

                        while (iter.hasNext()) {
                            try {
                                SelectionKey key = iter.next();
                                iter.remove();

                                if (!key.isValid()) {
                                    continue;
                                }

                                if (key.isReadable()) {
                                    read(key);
                                    key.interestOps(SelectionKey.OP_WRITE);
                                }

                                if (key.isWritable()) {
                                    write(key);
                                    key.interestOps(SelectionKey.OP_READ);
                                }

                            } catch (IOException e) {
                                System.err.println("glitch, continuing... " + (e.getMessage() != null ? e.getMessage() : ""));
                            }
                        }
                    }
                } catch (IOException e) {
                  System.err.println("glitch, continuing... " + (e.getMessage() != null ? e.getMessage() : ""));
                }
            }
            selector.close();
            return false;
        } catch (ClassNotFoundException exception) {
            Outputer.printError("An error occurred while reading the received data!");
            AppServer.LOGGER.severe("Произошла ошибка при чтении полученных данных!");
        } catch (InvalidClassException | NotSerializableException exception) {
            Outputer.printError("An error occurred while sending data to the client!");
            AppServer.LOGGER.severe("Произошла ошибка при отправке данных на клиент!");
        } catch (IOException exception) {
            if (con.request == null) {
                Outputer.printError("Unexpected disconnection from the client!");
                AppServer.LOGGER.severe("Непредвиденный разрыв соединения с клиентом!");
            } else {
                Outputer.println("Клиент успешно отключен от сервера!");
                AppServer.LOGGER.info("Клиент успешно отключен от сервера!");
            }
        }
        return true;
    }

    private void read(SelectionKey key) throws IOException, ClassNotFoundException {
        DatagramChannel channel = (DatagramChannel) key.channel();
        Con con = (Con) key.attachment();

        ByteBuffer buf = ByteBuffer.allocate(2048);
        con.sa = channel.receive(buf);
        AppServer.LOGGER.info("Receiving a request...");

        byte[] arr = buf.array();
        ByteArrayInputStream bais = new ByteArrayInputStream(arr);
        ObjectInputStream ois = new ObjectInputStream(bais);

        con.request = (Request) ois.readObject();
        con.response = requestHandler.handle(con.request);
        AppServer.LOGGER.info("Processing request " + con.request.getCommandName() + "...");

        ois.close();
    }

    private void write(SelectionKey key) throws IOException {
        DatagramChannel channel = (DatagramChannel) key.channel();
        Con con = (Con) key.attachment();

        ByteArrayOutputStream baos = new ByteArrayOutputStream(1024);
        ObjectOutputStream oos = new ObjectOutputStream(new BufferedOutputStream(baos));

        oos.writeObject(con.response);
        oos.flush();

        byte[] arr = baos.toByteArray();
        ByteBuffer buf = ByteBuffer.wrap(arr);
        channel.send(buf, con.sa);
        AppServer.LOGGER.info("Sending a response...");

        oos.close();
    }

    static class Con {
        Request request;
        Response response;
        SocketAddress sa;
    }
}
