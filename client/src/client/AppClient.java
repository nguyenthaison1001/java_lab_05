package client;

import controllers.AskWindowController;
import controllers.FilterWindowController;
import controllers.LoginWindowController;
import controllers.MainWindowController;
import controllers.tools.ObservableResourceFactory;
import exceptions.NotInDeclaredLimitsException;
import exceptions.WrongFormatCommandException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import utility.Outputer;
import utility.OutputerUI;

import java.io.IOException;
import java.util.ResourceBundle;
import java.util.Scanner;

public class AppClient extends Application {
    public static final String PS1 = "$ ";
    public static final String PS2 = "> ";
    public static final String BUNDLE = "bundles.myGUI";

    private static final String APP_TITLE = "LabWork Collection Manager";

    private static String host = "127.0.0.1";
//    "127.0.0.1", "se.ifmo.ru"
    private static int port = 4387;
//    4387
    private static Scanner userScanner;
    private static Client client;
    private static ObservableResourceFactory resourceFactory;
    private Stage primaryStage;

    public static void main(String[] args) {
        resourceFactory = new ObservableResourceFactory();
        resourceFactory.setResources(ResourceBundle.getBundle(BUNDLE));
        OutputerUI.setResourceFactory(resourceFactory);
        Outputer.setResourceFactory(resourceFactory);

//        if (initialize(args)) launch(args);
//        else System.exit(0);

        launch(args);
    }

    private static boolean initialize(String[] hostAndPortArgs) {
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

    @Override
    public void init() {
        userScanner = new Scanner(System.in);
        client = new Client(host, port);
        client.run();
    }

    @Override
    public void stop() {
        client.stop();
        userScanner.close();
    }

    @Override
    public void start(Stage stage) {
        try {
            this.primaryStage = stage; // what for??

            FXMLLoader loginWindowLoader = new FXMLLoader();
            loginWindowLoader.setLocation(getClass().getResource("/view/LoginWindow.fxml"));
            Parent loginWindowRootNode = loginWindowLoader.load();
            Scene loginWindowScene = new Scene(loginWindowRootNode);
            LoginWindowController loginWindowController = loginWindowLoader.getController();
            loginWindowController.setApp(this);
            loginWindowController.setClient(client);
            loginWindowController.initLangs(resourceFactory);

            primaryStage.setTitle(APP_TITLE);
            primaryStage.setScene(loginWindowScene);
            primaryStage.setResizable(false);
            primaryStage.show();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    public void setMainWindow() {
        try {
            FXMLLoader mainWindowLoader = new FXMLLoader();
            mainWindowLoader.setLocation(getClass().getResource("/view/MainWindow.fxml"));
            Parent mainWindowRootNode = mainWindowLoader.load();
            Scene mainWindowScene = new Scene(mainWindowRootNode);
            MainWindowController mainWindowController = mainWindowLoader.getController();
            mainWindowController.initLangs(resourceFactory);

            FXMLLoader askWindowLoader = new FXMLLoader();
            askWindowLoader.setLocation(getClass().getResource("/view/AskWindow.fxml"));
            Parent askWindowRootNode = askWindowLoader.load();
            Scene askWindowScene = new Scene(askWindowRootNode);
            Stage askStage = new Stage();
            askStage.setTitle(APP_TITLE);
            askStage.setScene(askWindowScene);
            askStage.setResizable(false);
            askStage.initModality(Modality.WINDOW_MODAL);
            askStage.initOwner(primaryStage);
            AskWindowController askWindowController = askWindowLoader.getController();
            askWindowController.setAskStage(askStage);
            askWindowController.initLangs(resourceFactory);

            FXMLLoader filterWindowLoader = new FXMLLoader();
            filterWindowLoader.setLocation(getClass().getResource("/view/FilterWindow.fxml"));
            Parent filterWindowRootNode = filterWindowLoader.load();
            Scene filterWindowScene = new Scene(filterWindowRootNode);
            Stage filterStage = new Stage();
            filterStage.setTitle(APP_TITLE);
            filterStage.setScene(filterWindowScene);
            filterStage.setResizable(false);
            filterStage.initModality(Modality.WINDOW_MODAL);
            filterStage.initOwner(primaryStage);
            FilterWindowController filterWindowController = filterWindowLoader.getController();
            filterWindowController.setFilterStage(filterStage);
            filterWindowController.initLangs(resourceFactory);

            mainWindowController.setClient(client);
            mainWindowController.setUsername(client.getUsername());
            mainWindowController.setAskStage(askStage);
            mainWindowController.setFilterStage(filterStage);
            mainWindowController.setPrimaryStage(primaryStage);
            mainWindowController.setAskWindowController(askWindowController);
            mainWindowController.setFilterWindowController(filterWindowController);
            mainWindowController.refreshButtonOnAction();

            primaryStage.setScene(mainWindowScene);
            primaryStage.setMinWidth(mainWindowScene.getWidth());
            primaryStage.setMinHeight(mainWindowScene.getHeight());
            primaryStage.setResizable(true);

        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

}
