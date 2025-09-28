package org.example.textprocessingtool;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;

public class TextProcessingTool extends Application {

    private RegexModule regexModule;
    private DataManagementModule dataModule;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Text Processing Tool");

        // Initialize modules
        regexModule = new RegexModule();
        dataModule = new DataManagementModule();

        // Create tab pane for different modules
        TabPane tabPane = new TabPane();

        // Regex Tab
        Tab regexTab = new Tab("Regular Expressions");
        regexTab.setContent(regexModule.createRegexUI());
        regexTab.setClosable(false);


        Tab dataTab = new Tab("Data Management");
        dataTab.setContent(dataModule.createDataUI());
        dataTab.setClosable(false);

        tabPane.getTabs().addAll(regexTab, dataTab);

        Scene scene = new Scene(tabPane, 900, 700);


        try {
            scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
        } catch (NullPointerException e) {
            System.out.println("CSS file not found. Application will run with default styling.");
        }

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}