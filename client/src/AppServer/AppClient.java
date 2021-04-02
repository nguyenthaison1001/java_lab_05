package AppServer;

import exceptions.NotInDeclaredLimitsException;
import exceptions.WrongFormatCommandException;
import utility.Outputer;
import utility.UserHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Scanner;

public class AppClient {
    public static final String PS1 = "$ ";
    public static final String PS2 = "> ";

    private static final int RECONNECTION_TIMEOUT = 5 * 1000;
    private static final int MAX_RECONNECTION_ATTEMPTS = 5;

    private static String host= "127.0.0.1";
//    = "127.0.0.1";
    private static int port = 4387;
//    = 4387;

    private static boolean initializeConnectionAddress(String[] hostAndPortArgs) {
        try {
            if (hostAndPortArgs.length != 2) throw new WrongFormatCommandException();
            host = hostAndPortArgs[0];
            port = Integer.parseInt(hostAndPortArgs[1]);
            if (port < 0) throw new NotInDeclaredLimitsException();
            return true;
        } catch (WrongFormatCommandException exception) {
            String jarName = new java.io.File(AppClient.class.getProtectionDomain()
                    .getCodeSource()
                    .getLocation()
                    .getPath())
                    .getName();
            Outputer.println("Using: 'java -jar " + jarName + " <host> <port>'");
        } catch (NumberFormatException exception) {
            Outputer.printError("Port must be a number!");
        } catch (NotInDeclaredLimitsException exception) {
            Outputer.printError("Port can't be negative!");
        }
        return false;
    }

    public static void main(String[] args) throws IOException {
        Outputer.println("Welcome to app!");

//      - run in IDE
//        Scanner scanner = new Scanner(System.in);
//        String host1 = scanner.nextLine();
//        String[] hostAndPort = (host1.trim() + " ").split(" ", 2);
//        hostAndPort[0] = hostAndPort[0].trim();
//        hostAndPort[1] = hostAndPort[1].trim();

//        if (!initializeConnectionAddress(hostAndPort)) return;

//        if (!initializeConnectionAddress(args)) return;

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        UserHandler userHandler = new UserHandler(reader);

//      - run in ide
//        Client client = new Client(hostAndPort[0], port, CONNECTION_TIMEOUT, userHandler);

        Client client = new Client(host, port, RECONNECTION_TIMEOUT, MAX_RECONNECTION_ATTEMPTS, userHandler);
        client.run();
        reader.close();
    }
}
