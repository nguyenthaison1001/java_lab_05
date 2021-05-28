package utility;

import controllers.tools.ObservableResourceFactory;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;

import java.text.MessageFormat;
import java.util.MissingResourceException;
import java.util.Optional;

public class OutputerUI {
    private static final String INFO_TITLE = "LabWork Collection Manager";
    private static final String ERROR_TITLE = "LabWork Collection Manager";
    private static final String CONFIRM_TITLE = "LabWork Collection Manager";


    private static ObservableResourceFactory resourceFactory;

    private static void msg(String title, String toOut, String[] arg, Alert.AlertType msgType) {
        Alert alert = new Alert(msgType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(tryResource(toOut, arg));
        alert.showAndWait();
    }

    private static String tryResource(String str, String[] args) {
        try {
            if (haveResourceFactory()) throw new NullPointerException();
            if (args == null) return resourceFactory.getResources().getString(str);
            MessageFormat messageFormat = new MessageFormat(resourceFactory.getResources().getString(str));
            return messageFormat.format(args);
        } catch (MissingResourceException | NullPointerException exception) {
            return str;
        }
    }

    public static void info(String toOut, String[] args) {
        msg(INFO_TITLE, toOut, args, Alert.AlertType.INFORMATION);
    }

    public static void info(String toOut) {
        info(toOut, null);
    }

    public static void error(String toOut, String[] args) {
        msg(ERROR_TITLE, toOut, args, Alert.AlertType.ERROR);
    }

    public static void error(String toOut) {
        error(toOut, null);
    }

    public static void errorInfoDialog(String toOut, String[] args) {
        if (toOut.startsWith("error: "))
            msg(ERROR_TITLE, toOut.substring(7), args, Alert.AlertType.ERROR);
        else msg(INFO_TITLE, toOut, args, Alert.AlertType.INFORMATION);
    }

    public static boolean confirmDialog() {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle(CONFIRM_TITLE);
        confirm.setContentText(tryResource("AreYouSure", null));
        confirm.setHeaderText(null);
        ButtonType yesButton = new ButtonType("Yes", ButtonBar.ButtonData.YES);
        ButtonType cancelButton = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        confirm.getButtonTypes().setAll(yesButton, cancelButton);
        Optional<ButtonType> option = confirm.showAndWait();
        if (option.isPresent() && option.get() == yesButton)
            return true;
        else {
            confirm.close();
            return false;
        }
    }

    public static void setResourceFactory(ObservableResourceFactory resourceFactory) {
        OutputerUI.resourceFactory = resourceFactory;
    }


    /**
     * Checking to resource factory.
     *
     * @return False if have and true if haven't
     */
    public static boolean haveResourceFactory() {
        return resourceFactory == null;
    }
}
