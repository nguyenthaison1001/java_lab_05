package controllers;

import controllers.tools.ObservableResourceFactory;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class FilterWindowController {
    @FXML
    private Label nameFilterLabel;

    @FXML
    private TextField filterField;

    @FXML
    private Button enterFilterButton;

    private Stage filterStage;
    private String filterName;
    private ObservableResourceFactory resourceFactory;

    @FXML
    private void enterFilterButtonOnAction() {
        filterName = filterField.getText();
        filterStage.close();
    }

    public void clearFilter() {
         filterField.clear();
    }

    public void setFilterStage(Stage filterStage) {
        this.filterStage = filterStage;
    }

    public String getFilterName() {
        return filterName;
    }

    private void bindGuiLanguage() {
        nameFilterLabel.textProperty().bind(resourceFactory.getStringBinding("NameFilterLabel"));
        enterFilterButton.textProperty().bind(resourceFactory.getStringBinding("EnterButton"));
    }

    public void initLangs(ObservableResourceFactory resourceFactory) {
        this.resourceFactory = resourceFactory;
        bindGuiLanguage();
    }
}
