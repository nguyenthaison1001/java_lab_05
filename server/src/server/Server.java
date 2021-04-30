package server;

import exceptions.ConnectionErrorException;
import utility.*;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

public class Server {
    private final int port;
    private CommandManager commandManager;
    private boolean isStopped;
    private Semaphore semaphore;
    private ForkJoinPool forkJoinPool = ForkJoinPool.commonPool();

    public Server(int port, int maxClients, CommandManager commandManager) {
        this.port = port;
        this.commandManager = commandManager;
        this.semaphore = new Semaphore(maxClients);
    }

    public void run() {
        try {
            while (!isStopped()) {
                try {
                    acquireConnection();
                    if (isStopped()) throw new ConnectionErrorException();
                    forkJoinPool.invoke(new ConnectionHandler(this, port, commandManager));
                } catch (ConnectionErrorException exception) {
                    break;
                }
            }
            forkJoinPool.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
            Outputer.println("server.Server shutdown!");
        } catch (InterruptedException e) {
            Outputer.printError("Произошла ошибка при завершении работы с уже подключенными клиентами!");
        }
    }

    /**
     * Acquire connection.
     */
    public void acquireConnection() {
        try {
            semaphore.acquire();
            AppServer.LOGGER.info("Разрешение на новое соединение получено.");
        } catch (InterruptedException exception) {
            Outputer.printError("Произошла ошибка при получении разрешения на новое соединение!");
            AppServer.LOGGER.severe("Призошла ошибка при получении разрешения на новое соединение!");
        }
    }

    /**
     * Release connection.
     */
    public void releaseConnection() {
        semaphore.release();
        AppServer.LOGGER.info("Разрыв соединения зарегистрирован.");
    }

    public synchronized void stop() {
        AppServer.LOGGER.info("Завершение работы сервера...");
        isStopped = true;
        forkJoinPool.shutdown();
        Outputer.println("Завершение работы с уже подключенными клиентами...");
        AppServer.LOGGER.info("Работа сервера завершена.");
    }

    /**
     * Checked stops of server.
     *
     * @return Status of server stop.
     */
    private synchronized boolean isStopped() {
        return isStopped;
    }
}
