package controllers;

import controllers.tools.ObservableResourceFactory;
import data.Coordinates;
import data.Difficulty;
import data.Discipline;
import data.LabWork;
import exceptions.MustBeNotEmptyException;
import exceptions.NotInDeclaredLimitsException;
import interaction.LabRaw;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import utility.OutputerUI;

public class AskWindowController {
    @FXML
    private Label nameLabel;
    @FXML
    private Label coordinatesXLabel;
    @FXML
    private Label coordinatesYLabel;
    @FXML
    private Label minimalPointLabel;
    @FXML
    private Label tunedInWorkLabel;
    @FXML
    private Label averagePointLabel;
    @FXML
    private Label difficultyLabel;
    @FXML
    private Label disciplineNameLabel;
    @FXML
    private Label labsCountLabel;
    @FXML
    private TextField nameField;
    @FXML
    private TextField coordinatesXField;
    @FXML
    private TextField coordinatesYField;
    @FXML
    private TextField minimalPointField;
    @FXML
    private TextField tunedInWorkField;
    @FXML
    private TextField averagePointField;
    @FXML
    private TextField disciplineNameField;
    @FXML
    private TextField labsCountField;
    @FXML
    private ComboBox<Difficulty> difficultyBox;
    @FXML
    private Button enterButton;

    private Stage askStage;
    private LabRaw resultLab;
    private ObservableResourceFactory resourceFactory;

    /**
     * Initialize ask window.
     */
    public void initialize() {
        difficultyBox.setItems(FXCollections.observableArrayList(Difficulty.values()));
    }

    /**
     * Enter button on action.
     */
    @FXML
    private void enterButtonOnAction() {
        try {
            resultLab = new LabRaw(
                    convertName(),
                    new Coordinates(
                            convertCoordinatesX(),
                            convertCoordinatesY()
                    ),
                    convertMinimalPoint(),
                    convertTunedInWork(),
                    convertAveragePoint(),
                    difficultyBox.getValue(),
                    new Discipline(
                            convertDisciplineName(),
                            convertLabsCount()
                    )
            );
            askStage.close();
        } catch (IllegalArgumentException exception) { /* ? */ }
    }

    /**
     * Binds interface language.
     */
    private void bindGuiLanguage() {
        nameLabel.textProperty().bind(resourceFactory.getStringBinding("NameColumn"));
        coordinatesXLabel.textProperty().bind(resourceFactory.getStringBinding("CoordinatesXColumn"));
        coordinatesYLabel.textProperty().bind(resourceFactory.getStringBinding("CoordinatesYColumn"));
        minimalPointLabel.textProperty().bind(resourceFactory.getStringBinding("MinimalPointColumn"));
        tunedInWorkLabel.textProperty().bind(resourceFactory.getStringBinding("TunedInWorksColumn"));
        averagePointLabel.textProperty().bind(resourceFactory.getStringBinding("AveragePointColumn"));
        difficultyLabel.textProperty().bind(resourceFactory.getStringBinding("DifficultyColumn"));
        disciplineNameLabel.textProperty().bind(resourceFactory.getStringBinding("DisciplineNameColumn"));
        labsCountLabel.textProperty().bind(resourceFactory.getStringBinding("LabsCountColumn"));
        enterButton.textProperty().bind(resourceFactory.getStringBinding("EnterButton"));
    }

    /**
     * Convert name.
     *
     * @return Name.
     */
    private String convertName() throws IllegalArgumentException {
        String name;
        try {
            name = nameField.getText();
            if (name.equals("")) throw new MustBeNotEmptyException();
        } catch (MustBeNotEmptyException exception) {
            OutputerUI.error("NameEmptyException");
            throw new IllegalArgumentException();
        }
        return name;
    }

    /**
     * Convert Coordinates X.
     *
     * @return X.
     */
    private int convertCoordinatesX() throws IllegalArgumentException {
        String strX;
        int x;
        try {
            strX = coordinatesXField.getText();
            x = Integer.parseInt(strX);
            if (strX.equals("")) throw new MustBeNotEmptyException();
            if (x <= LabWork.MIN_X) throw new NotInDeclaredLimitsException();
        } catch (NumberFormatException exception) {
            OutputerUI.error("CoordinatesXFormatException");
            throw new IllegalArgumentException();
        } catch (NotInDeclaredLimitsException exception) {
            OutputerUI.error("CoordinatesXLimitsException", new String[]{String.valueOf(LabWork.MIN_X)});
            throw new IllegalArgumentException();
        } catch (MustBeNotEmptyException exception) {
            OutputerUI.error("CoordinatesXEmptyException");
            throw new IllegalArgumentException();
        }
        return x;
    }

    /**
     * Convert Coordinates Y.
     *
     * @return Y.
     */
    private long convertCoordinatesY() throws IllegalArgumentException {
        String strY;
        long y;
        try {
            strY = coordinatesYField.getText();
            y = Long.parseLong(strY);
            if (strY.equals("")) throw new MustBeNotEmptyException();
        } catch (NumberFormatException exception) {
            OutputerUI.error("CoordinatesYFormatException");
            throw new IllegalArgumentException();
        } catch (MustBeNotEmptyException exception) {
            OutputerUI.error("CoordinateYEmptyException");
            throw new IllegalArgumentException();
        }
        return y;
    }

    private long convertMinimalPoint() throws IllegalArgumentException {
        String strMinimalPoint;
        long minimalPont;
        try {
            strMinimalPoint = minimalPointField.getText();
            minimalPont = Long.parseLong(strMinimalPoint);
            if (minimalPont <= 0) throw new NotInDeclaredLimitsException();
        } catch (NumberFormatException exception) {
            OutputerUI.error("MinimalPointFormatException");
            throw new IllegalArgumentException();
        } catch (NotInDeclaredLimitsException exception) {
            OutputerUI.error("MinimalPointLimitsException", new String[]{"0"});
            throw new IllegalArgumentException();
        }
        return minimalPont;
    }

    private int convertTunedInWork() throws IllegalStateException {
        String strTuned;
        int tuned;
        try {
            strTuned = tunedInWorkField.getText();
            tuned = Integer.parseInt(strTuned);
        } catch (NumberFormatException exception) {
            OutputerUI.error("TunedInWorkFormatException");
            throw new IllegalArgumentException();
        }
        return tuned;
    }

    private int convertAveragePoint() throws IllegalArgumentException {
        String strAveragePoint;
        int averagePoint;
        try {
            strAveragePoint = averagePointField.getText();
            averagePoint = Integer.parseInt(strAveragePoint);
            if (averagePoint <= 0) throw new NotInDeclaredLimitsException();
        } catch (NumberFormatException exception) {
            OutputerUI.error("AveragePointFormatException");
            throw new IllegalArgumentException();
        } catch (NotInDeclaredLimitsException exception) {
            OutputerUI.error("AveragePointLimitsException", new String[]{"0"});
            throw new IllegalArgumentException();
        }
        return averagePoint;
    }

    private String convertDisciplineName() throws IllegalStateException {
        String disciplineName;
        try {
            disciplineName = disciplineNameField.getText();
            if (disciplineName.equals("")) throw new MustBeNotEmptyException();
        } catch (MustBeNotEmptyException exception) {
            OutputerUI.error("DisciplineNameEmptyException");
            throw new IllegalArgumentException();
        }
        return disciplineName;
    }

    private int convertLabsCount() throws IllegalArgumentException {
        String strLabsCount;
        int LabsCount;
        try {
            strLabsCount = labsCountField.getText();
            LabsCount = Integer.parseInt(strLabsCount);
        } catch (NumberFormatException exception) {
            OutputerUI.error("LabsCountFormatException");
            throw new IllegalArgumentException();
        }
        return LabsCount;
    }

    /**
     * Set Lab.
     *
     * @param lab Lab to set.
     */
    public void setLab(LabWork lab) {
        nameField.setText(lab.getName());
        coordinatesXField.setText(lab.getCoordinates().getX() + "");
        coordinatesYField.setText(lab.getCoordinates().getY() + "");
        minimalPointField.setText(lab.getMinimalPoint() + "");
        tunedInWorkField.setText(lab.getTunedInWorks() + "");
        averagePointField.setText(lab.getAveragePoint() + "");
        difficultyBox.setValue(lab.getDifficulty());
        disciplineNameField.setText(lab.getDiscipline().getName());
        labsCountField.setText(lab.getDiscipline().getLabsCount() + "");
    }

    /**
     * Clear Lab.
     */
    public void clearLab() {
        nameField.clear();
        coordinatesXField.clear();
        coordinatesYField.clear();
        minimalPointField.clear();
        tunedInWorkField.clear();
        averagePointField.clear();
        difficultyBox.setValue(Difficulty.NORMAL);
        disciplineNameField.clear();
        labsCountField.clear();
    }

    /**
     * Get and clear Lab.
     *
     * @return Lab to return.
     */
    public LabRaw getAndClear() {
        LabRaw labToReturn = resultLab;
        resultLab = null;
        return labToReturn;
    }

    public void setAskStage(Stage askStage) {
        this.askStage = askStage;
    }

    /**
     * Init langs.
     *
     * @param resourceFactory Resource factory to set.
     */
    public void initLangs(ObservableResourceFactory resourceFactory) {
        this.resourceFactory = resourceFactory;
        bindGuiLanguage();
    }



}
