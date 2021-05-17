package utility;

import interaction.Request;
import interaction.Response;
import server.Server;
import server.AppServer;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Iterator;
import java.util.concurrent.*;

public class ConnectionHandler extends RecursiveAction {
    private final Server server;
    private final int port;
    private DatagramChannel channel;
    private final CommandManager commandManager;
    private final ExecutorService processCachedThreadPool = Executors.newCachedThreadPool();
    private final ExecutorService sendCachedThreadPool = Executors.newCachedThreadPool();

    public ConnectionHandler(Server server, int port, CommandManager commandManager) {
        this.server = server;
        this.port = port;
        this.commandManager = commandManager;
    }

    /**
     * The main computation performed by this task.
     */
    @Override
    protected void compute() {
        boolean stopFlag = false;
        Con con = new Con();
        try {
            AppServer.LOGGER.info("Server starting on port " + port);

            channel = DatagramChannel.open();
            channel.socket().bind(new InetSocketAddress(port));
            channel.configureBlocking(false);

            Selector selector = Selector.open();
            SelectionKey cliKey = channel.register(selector, SelectionKey.OP_READ);
            cliKey.attach(con);

            while (channel.isOpen()) {
                try {
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
                            } catch (InterruptedException | ExecutionException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                } catch (IOException e) {
                    System.err.println("glitch, continuing... " + (e.getMessage() != null ? e.getMessage() : ""));
                }
            }
            selector.close();
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
        } finally {
            try {
                sendCachedThreadPool.shutdown();
                channel.close();
                Outputer.println("Клиент отключен от сервера.");
                AppServer.LOGGER.info("Клиент отключен от сервера.");
            } catch (IOException exception) {
                Outputer.printError("Произошла ошибка при попытке завершить соединение с клиентом!");
                AppServer.LOGGER.severe("Произошла ошибка при попытке завершить соединение с клиентом!");
            }
            if (stopFlag) server.stop();
            server.releaseConnection();
        }
    }

    private void read(SelectionKey key) throws IOException, ClassNotFoundException, ExecutionException, InterruptedException {
        DatagramChannel channel = (DatagramChannel) key.channel();
        Con con = (Con) key.attachment();

        ByteBuffer buf = ByteBuffer.allocate(2048);
        con.sa = channel.receive(buf);
        AppServer.LOGGER.info("Receiving a request...");

        byte[] arr = buf.array();
        ByteArrayInputStream bais = new ByteArrayInputStream(arr);
        ObjectInputStream ois = new ObjectInputStream(bais);

        con.request = (Request) ois.readObject();
        Future<Response> responseFuture = processCachedThreadPool.submit(new HandleRequestTask(con.request, commandManager));
        con.response = responseFuture.get();

        AppServer.LOGGER.info("Processing request " + con.request.getCommandName() + "...");

        ois.close();
    }

    private void write(SelectionKey key) {
        sendCachedThreadPool.submit(() -> {
            try {
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
            } catch (IOException exception) {
                Outputer.printError("Произошла ошибка при отправке данных на клиент!");
                AppServer.LOGGER.severe("Произошла ошибка при отправке данных на клиент!");
            }
        });
    }

    static class Con {
        Request request;
        Response response;
        SocketAddress sa;
    }
}
