package controllers;

import client.AppClient;
import client.Client;
import controllers.tools.ObservableResourceFactory;
import data.Difficulty;
import data.LabWork;
import interaction.LabRaw;
import javafx.animation.ScaleTransition;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Shape;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.controlsfx.control.table.TableFilter;
import utility.OutputerUI;

import java.io.File;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.*;

public class MainWindowController {
    public static final String LOGIN_COMMAND_NAME = "login";
    public static final String REGISTER_COMMAND_NAME = "register";
    public static final String REFRESH_COMMAND_NAME = "refresh";
    public static final String INFO_COMMAND_NAME = "info";
    public static final String ADD_COMMAND_NAME = "add";
    public static final String UPDATE_COMMAND_NAME = "update";
    public static final String REMOVE_COMMAND_NAME = "remove_by_id";
    public static final String CLEAR_COMMAND_NAME = "clear";
    public static final String EXIT_COMMAND_NAME = "exit";
    public static final String ADD_IF_MIN_COMMAND_NAME = "add_if_min";
    public static final String REMOVE_GREATER_COMMAND_NAME = "remove_greater";
    public static final String SUM_MINIMAL_POINT_COMMAND_NAME = "sum_of_minimal_point";
    public static final String COUNT_LESS_THAN_COMMAND_NAME = "count_less_than_difficulty";
    public static final String FILTER_START_WITH_NAME_COMMAND_NAME = "filter_starts_with_name";

    private final Duration ANIMATION_DURATION = Duration.millis(1000); // time for animation

    @FXML
    private Tab tableTab;
    @FXML
    private TableView<LabWork> labWorkTable;
    @FXML
    private TableColumn<LabWork, Integer> idColumn;
    @FXML
    private TableColumn<LabWork, String> userColumn;
    @FXML
    private TableColumn<LabWork, String> nameColumn;
    @FXML
    private TableColumn<LabWork, ZonedDateTime> creationDateColumn;
    @FXML
    private TableColumn<LabWork, Integer> XColumn;
    @FXML
    private TableColumn<LabWork, Long> YColumn;
    @FXML
    private TableColumn<LabWork, Long> minimalPointColumn;
    @FXML
    private TableColumn<LabWork, Integer> tunedInWorksColumn;
    @FXML
    private TableColumn<LabWork, Integer> averagePointColumn;
    @FXML
    private TableColumn<LabWork, Difficulty> difficultyColumn;
    @FXML
    private TableColumn<LabWork, String> disciplineNameColumn;
    @FXML
    private TableColumn<LabWork, Integer> labsCountColumn;
    @FXML
    private Tab canvasTab;
    @FXML
    private AnchorPane canvasPane;
    @FXML
    private Button infoButton;
    @FXML
    private Tooltip infoButtonTooltip;
    @FXML
    private Button addButton;
    @FXML
    private Tooltip addButtonTooltip;
    @FXML
    private Button updateButton;
    @FXML
    private Tooltip updateButtonTooltip;
    @FXML
    private Button removeButton;
    @FXML
    private Tooltip removeButtonTooltip;
    @FXML
    private Button removeGreaterButton;
    @FXML
    private Tooltip removeGreaterButtonTooltip;
    @FXML
    private Button clearButton;
    @FXML
    private Tooltip clearButtonTooltip;
    @FXML
    private Button executeScriptButton;
    @FXML
    private Tooltip executeScriptButtonTooltip;
    @FXML
    private Button addIfMinButton;
    @FXML
    private Tooltip addIfMinButtonTooltip;
    @FXML
    private Button filterButton;
    @FXML
    private Tooltip filterButtonTooltip;
    @FXML
    private Button sumMinimalPointButton;
    @FXML
    private Tooltip sumMinimalPointButtonTooltip;
    @FXML
    private Label usernameLabel;
    @FXML
    private Button refreshButton;
    @FXML
    private Tooltip refreshButtonTooltip;
    @FXML
    private Button countLessThanButton;
    @FXML
    private Tooltip countLessThanButtonTooltip;
    @FXML
    private ComboBox<String> languageComboBox;

    private Client client;
    private Stage askStage;
    private Stage filterStage;
    private Stage primaryStage;
    private FileChooser fileChooser;
    private AskWindowController askWindowController;
    private FilterWindowController filterWindowController;
    private Map<String, Color> userColorMap;
    private Map<Shape, Integer> shapeMap;
    private Map<Integer, Text> textMap;
    private Shape prevClicked;
    private Color prevColor;
    private Random randomGenerator;
    private ObservableResourceFactory resourceFactory;
    private Map<String, Locale> localeMap;

    /**
     * Initialize main window.
     */
    public void initialize() {
        initializeTable();
        fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File("."));
        userColorMap = new HashMap<>();
        shapeMap = new HashMap<>();
        textMap = new HashMap<>();
        long RANDOM_SEED = 1000L;
        randomGenerator = new Random(RANDOM_SEED);
        localeMap = new HashMap<>();
        localeMap.put("Русский", new Locale("ru", "RU"));
        localeMap.put("Deutsche", new Locale("de", "DE"));
        localeMap.put("Ελληνικά ", new Locale("el", "GR"));
        localeMap.put("Español", new Locale("es", "ES"));
        languageComboBox.setItems(FXCollections.observableArrayList(localeMap.keySet()));
    }

    /**
     * Initialize table.
     */
    private void initializeTable() {
        idColumn.setCellValueFactory(cellData ->
                new ReadOnlyObjectWrapper<>(cellData.getValue().getId()));
        userColumn.setCellValueFactory(cellData ->
                new ReadOnlyObjectWrapper<>(cellData.getValue().getOwner().getUsername()));
        nameColumn.setCellValueFactory(cellData ->
                new ReadOnlyObjectWrapper<>(cellData.getValue().getName()));
        creationDateColumn.setCellValueFactory(cellData ->
                new ReadOnlyObjectWrapper<>(cellData.getValue().getCreationDate()));
        XColumn.setCellValueFactory(cellData ->
                new ReadOnlyObjectWrapper<>(cellData.getValue().getCoordinates().getX()));
        YColumn.setCellValueFactory(cellData ->
                new ReadOnlyObjectWrapper<>(cellData.getValue().getCoordinates().getY()));
        minimalPointColumn.setCellValueFactory(cellData ->
                new ReadOnlyObjectWrapper<>(cellData.getValue().getMinimalPoint()));
        tunedInWorksColumn.setCellValueFactory(cellData ->
                new ReadOnlyObjectWrapper<>(cellData.getValue().getTunedInWorks()));
        averagePointColumn.setCellValueFactory(cellData ->
                new ReadOnlyObjectWrapper<>(cellData.getValue().getAveragePoint()));
        difficultyColumn.setCellValueFactory(cellData ->
                new ReadOnlyObjectWrapper<>(cellData.getValue().getDifficulty()));
        disciplineNameColumn.setCellValueFactory(cellData ->
                new ReadOnlyObjectWrapper<>(cellData.getValue().getDiscipline().getName()));
        labsCountColumn.setCellValueFactory(cellData ->
                new ReadOnlyObjectWrapper<>(cellData.getValue().getDiscipline().getLabsCount()));
    }

    /**
     * Bind gui language.
     */
    private void bindGuiLanguage() {
        resourceFactory.setResources(ResourceBundle.getBundle
                (AppClient.BUNDLE, localeMap.get(languageComboBox.getSelectionModel().getSelectedItem())));

        idColumn.textProperty().bind(resourceFactory.getStringBinding("IDColumn"));
        userColumn.textProperty().bind(resourceFactory.getStringBinding("UserColumn"));
        nameColumn.textProperty().bind(resourceFactory.getStringBinding("NameColumn"));
        creationDateColumn.textProperty().bind(resourceFactory.getStringBinding("CreationDateColumn"));
        XColumn.textProperty().bind(resourceFactory.getStringBinding("CoordinatesXColumn"));
        YColumn.textProperty().bind(resourceFactory.getStringBinding("CoordinatesYColumn"));
        minimalPointColumn.textProperty().bind(resourceFactory.getStringBinding("MinimalPointColumn"));
        tunedInWorksColumn.textProperty().bind(resourceFactory.getStringBinding("TunedInWorksColumn"));
        averagePointColumn.textProperty().bind(resourceFactory.getStringBinding("AveragePointColumn"));
        difficultyColumn.textProperty().bind(resourceFactory.getStringBinding("DifficultyColumn"));
        disciplineNameColumn.textProperty().bind(resourceFactory.getStringBinding("DisciplineNameColumn"));
        labsCountColumn.textProperty().bind(resourceFactory.getStringBinding("LabsCountColumn"));

        tableTab.textProperty().bind(resourceFactory.getStringBinding("TableTab"));
        canvasTab.textProperty().bind(resourceFactory.getStringBinding("CanvasTab"));

        infoButton.textProperty().bind(resourceFactory.getStringBinding("InfoButton"));
        addButton.textProperty().bind(resourceFactory.getStringBinding("AddButton"));
        updateButton.textProperty().bind(resourceFactory.getStringBinding("UpdateButton"));
        removeButton.textProperty().bind(resourceFactory.getStringBinding("RemoveButton"));
        removeGreaterButton.textProperty().bind(resourceFactory.getStringBinding("RemoveGreaterButton"));
        clearButton.textProperty().bind(resourceFactory.getStringBinding("ClearButton"));
        executeScriptButton.textProperty().bind(resourceFactory.getStringBinding("ExecuteScriptButton"));
        addIfMinButton.textProperty().bind(resourceFactory.getStringBinding("AddIfMinButton"));
        filterButton.textProperty().bind(resourceFactory.getStringBinding("FilterStartWithNameButton"));
        sumMinimalPointButton.textProperty().bind(resourceFactory.getStringBinding("SumMinimalPointButton"));
        countLessThanButton.textProperty().bind(resourceFactory.getStringBinding("CountLessThan"));

        refreshButton.textProperty().bind(resourceFactory.getStringBinding("RefreshButton"));

        infoButtonTooltip.textProperty().bind(resourceFactory.getStringBinding("InfoButtonTooltip"));
        addButtonTooltip.textProperty().bind(resourceFactory.getStringBinding("AddButtonTooltip"));
        updateButtonTooltip.textProperty().bind(resourceFactory.getStringBinding("UpdateButtonTooltip"));
        removeButtonTooltip.textProperty().bind(resourceFactory.getStringBinding("RemoveButtonTooltip"));
        removeGreaterButtonTooltip.textProperty().bind(resourceFactory.getStringBinding("RemoveGreaterButtonTooltip"));
        clearButtonTooltip.textProperty().bind(resourceFactory.getStringBinding("ClearButtonTooltip"));
        executeScriptButtonTooltip.textProperty().bind(resourceFactory.getStringBinding("ExecuteScriptButtonTooltip"));
        addIfMinButtonTooltip.textProperty().bind(resourceFactory.getStringBinding("AddIfMinButtonTooltip"));
        filterButtonTooltip.textProperty().bind(resourceFactory.getStringBinding("FilterButtonTooltip"));
        sumMinimalPointButtonTooltip.textProperty().bind(resourceFactory.getStringBinding("SumMinimalPointButtonTooltip"));
        refreshButtonTooltip.textProperty().bind(resourceFactory.getStringBinding("RefreshButtonTooltip"));
        countLessThanButtonTooltip.textProperty().bind(resourceFactory.getStringBinding("CountLessThanButtonTooltip"));
    }

    @FXML
    public void addButtonOnAction() {
        askWindowController.clearLab();
        askStage.showAndWait();
        LabRaw labRaw = askWindowController.getAndClear();
        if (labRaw != null) requestAction(ADD_COMMAND_NAME, "", labRaw);
    }

    @FXML
    public void addIfMinButtonOnAction() {
        askWindowController.clearLab();
        askStage.showAndWait();
        LabRaw labRaw = askWindowController.getAndClear();
        if (labRaw != null) requestAction(ADD_IF_MIN_COMMAND_NAME, "", labRaw);
    }

    @FXML
    public void filterButtonOnAction() {
        filterWindowController.clearFilter();
        filterStage.showAndWait();
        String filterStr = filterWindowController.getFilterName();
        if (!filterStr.isEmpty())
            requestAction(FILTER_START_WITH_NAME_COMMAND_NAME, filterStr, null);
    }

    @FXML
    public void clearButtonOnAction() {
        if (OutputerUI.confirmDialog())
            requestAction(CLEAR_COMMAND_NAME);
        else OutputerUI.info("CommandCancelled");
    }

    @FXML
    public void executeScriptButtonOnAction() {
        File selectedFile = fileChooser.showOpenDialog(primaryStage);
        if (selectedFile == null) return;
        if (client.processScriptToServer(selectedFile)) Platform.exit();
        else refreshButtonOnAction();
    }

    @FXML
    public void infoButtonOnAction() {
        requestAction(INFO_COMMAND_NAME);
    }

    @FXML
    public void refreshButtonOnAction() {
        requestAction(REFRESH_COMMAND_NAME);
    }

    @FXML
    public void removeButtonOnAction() {
        if (!labWorkTable.getSelectionModel().isEmpty()) {
            if (OutputerUI.confirmDialog())
                requestAction(REMOVE_COMMAND_NAME,
                        labWorkTable.getSelectionModel().getSelectedItem().getId().toString(), null);
            else OutputerUI.info("CommandCancelled");
        }
        else OutputerUI.error("RemoveButtonSelectionException");
    }

    @FXML
    public void removeGreaterButtonOnAction() {
        if (!labWorkTable.getSelectionModel().isEmpty()) {
            if (OutputerUI.confirmDialog())
                requestAction(REMOVE_GREATER_COMMAND_NAME,
                        labWorkTable.getSelectionModel().getSelectedItem().getId().toString(), null);
            else OutputerUI.info("CommandCancelled");
        }
        else OutputerUI.error("RemoveGreaterButtonSelectionException");
    }

    @FXML
    public void sumMinimalPointOnAction() {
        requestAction(SUM_MINIMAL_POINT_COMMAND_NAME);
    }

    @FXML
    public void countLessThanOnAction() {
        if (!labWorkTable.getSelectionModel().isEmpty())
            requestAction(COUNT_LESS_THAN_COMMAND_NAME,
                    labWorkTable.getSelectionModel().getSelectedItem().getDifficulty().toString(), null);
        else OutputerUI.error("CountLessThanButtonSelectionException");
    }

    @FXML
    public void updateButtonOnAction() {
        if (!labWorkTable.getSelectionModel().isEmpty()) {
            int id = labWorkTable.getSelectionModel().getSelectedItem().getId();
            askWindowController.setLab(labWorkTable.getSelectionModel().getSelectedItem());
            askStage.showAndWait();
            LabRaw labRaw = askWindowController.getAndClear();
            if (labRaw != null) requestAction(UPDATE_COMMAND_NAME, id + "", labRaw);
        } else OutputerUI.error("UpdateButtonSelectionException");
    }

    /**
     * Request action.
     */
    private void requestAction(String commandName, String commandStringArgument, Serializable commandObjectArgument) {
        LinkedList<LabWork> responseLabs =
                client.processRequestToServer(commandName, commandStringArgument, commandObjectArgument);
        System.out.println(responseLabs);
        if (responseLabs != null) {
            ObservableList<LabWork> labsList = FXCollections.observableArrayList(responseLabs);
            labWorkTable.setItems(labsList);
//            TableFilter.forTableView(labWorkTable).apply();
            labWorkTable.getSelectionModel().clearSelection();
            refreshCanvas();
        }
    }

    /**
     * Binds request action.
     */
    private void requestAction(String commandName) {
        requestAction(commandName, "", null);
    }

    private void refreshCanvas() {
        shapeMap.keySet().forEach(s -> canvasPane.getChildren().remove(s));
        shapeMap.clear();
        textMap.values().forEach(s -> canvasPane.getChildren().remove(s));
        textMap.clear();
        for (LabWork labWork : labWorkTable.getItems()) {
            if (!userColorMap.containsKey(labWork.getOwner().getUsername())) // neu k phai owner
                userColorMap.put(labWork.getOwner().getUsername(),
                        Color.color(randomGenerator.nextDouble(), randomGenerator.nextDouble(), randomGenerator.nextDouble()));

            double MAX_SIZE = 250;
            double size = Math.min(labWork.getAveragePoint(), MAX_SIZE);

            // shape
            Shape circleObject = new Circle(size, userColorMap.get(labWork.getOwner().getUsername()));
            circleObject.setOnMouseClicked(this::shapeOnMouseClicked);
            circleObject.translateXProperty().bind(canvasPane.widthProperty().divide(2).add(labWork.getCoordinates().getX()));
            circleObject.translateYProperty().bind(canvasPane.heightProperty().divide(2).subtract(labWork.getCoordinates().getY()));

            Text textObject = new Text(labWork.getId().toString());
            textObject.setOnMouseClicked(circleObject::fireEvent);
            textObject.setFont(Font.font(size/3)); //size chu = size circle / a number. chia lon -> chu nho
            textObject.setFill(userColorMap.get(labWork.getOwner().getUsername()).darker());
            textObject.translateXProperty().bind(circleObject.translateXProperty().subtract(textObject.getLayoutBounds().getWidth() / 2));
            textObject.translateYProperty().bind(circleObject.translateYProperty().add(textObject.getLayoutBounds().getHeight() / 4));

            canvasPane.getChildren().add(circleObject);
            canvasPane.getChildren().add(textObject);
            shapeMap.put(circleObject, labWork.getId());
            textMap.put(labWork.getId(), textObject);

            ScaleTransition circleAnimation = new ScaleTransition(ANIMATION_DURATION, circleObject);
            ScaleTransition textAnimation = new ScaleTransition(ANIMATION_DURATION, textObject);
            circleAnimation.setFromX(0);
            circleAnimation.setToX(1);
            circleAnimation.setFromY(0);
            circleAnimation.setToY(1);
            textAnimation.setFromX(0);
            textAnimation.setToX(1);
            textAnimation.setFromY(0);
            textAnimation.setToY(1);
            circleAnimation.play();
            textAnimation.play();
        }
    }

    private void shapeOnMouseClicked(MouseEvent event) {
        Shape shape = (Shape) event.getSource();
        long id = shapeMap.get(shape);
        for (LabWork lab : labWorkTable.getItems()) {
            if (lab.getId() == id) {
                labWorkTable.getSelectionModel().select(lab);
                if (event.getClickCount() == 2) OutputerUI.info(lab.toString());
                break;
            }
        }
        if (prevClicked != null) {
            prevClicked.setFill(prevColor);
        }
        prevClicked = shape;
        prevColor = (Color) shape.getFill();
        shape.setFill(prevColor.brighter());
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public void setUsername(String username) {
        usernameLabel.setText(username);
    }

    public void setAskStage(Stage askStage) {
        this.askStage = askStage;
    }

    public void setFilterStage(Stage filterStage) {
        this.filterStage = filterStage;
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public void setAskWindowController(AskWindowController askWindowController) {
        this.askWindowController = askWindowController;
    }

    public void setFilterWindowController(FilterWindowController filterWindowController) {
        this.filterWindowController = filterWindowController;
    }

    public void initLangs(ObservableResourceFactory resourceFactory) {
        this.resourceFactory = resourceFactory;
        for (String localeName : localeMap.keySet()) {
            if (localeMap.get(localeName).equals(resourceFactory.getResources().getLocale()))
                languageComboBox.getSelectionModel().select(localeName);
        }
        if (languageComboBox.getSelectionModel().getSelectedItem().isEmpty())
            languageComboBox.getSelectionModel().selectFirst();
        languageComboBox.setOnAction((event) ->
                resourceFactory.setResources(ResourceBundle.getBundle
                        (AppClient.BUNDLE, localeMap.get(languageComboBox.getValue()))));
        bindGuiLanguage();
    }
}
