package org.example.textprocessingtool;

// DataManagementModule.java


import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.geometry.*;
import java.util.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class DataManagementModule {
    private Map<Integer, TextData> dataMap;
    private ObservableList<TextData> dataList;
    private ListView<TextData> dataListView;
    private TextField idField, contentField, categoryField;
    private int nextId = 1;

    public DataManagementModule() {
        dataMap = new HashMap<>();
        dataList = FXCollections.observableArrayList();
    }

    public VBox createDataUI() {
        VBox mainLayout = new VBox(10);
        mainLayout.setPadding(new Insets(10));

        // Input form
        Label formLabel = new Label("Add/Update Text Data:");
        GridPane formGrid = new GridPane();
        formGrid.setHgap(10);
        formGrid.setVgap(10);

        Label idLabel = new Label("ID:");
        idField = new TextField();
        idField.setEditable(false);

        Label contentLabel = new Label("Content:");
        contentField = new TextField();

        Label categoryLabel = new Label("Category:");
        categoryField = new TextField();

        formGrid.add(idLabel, 0, 0);
        formGrid.add(idField, 1, 0);
        formGrid.add(contentLabel, 0, 1);
        formGrid.add(contentField, 1, 1);
        formGrid.add(categoryLabel, 0, 2);
        formGrid.add(categoryField, 1, 2);

        // Buttons
        HBox buttonBox = new HBox(10);
        Button addButton = new Button("Add");
        Button updateButton = new Button("Update");
        Button deleteButton = new Button("Delete");
        Button clearButton = new Button("Clear Form");

        addButton.setOnAction(e -> addData());
        updateButton.setOnAction(e -> updateData());
        deleteButton.setOnAction(e -> deleteData());
        clearButton.setOnAction(e -> clearForm());

        buttonBox.getChildren().addAll(addButton, updateButton, deleteButton, clearButton);

        // Data list
        Label dataLabel = new Label("Stored Data:");
        dataListView = new ListView<>(dataList);
        dataListView.setPrefHeight(300);
        dataListView.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldVal, newVal) -> displaySelectedData(newVal));

        // Search functionality
        HBox searchBox = new HBox(10);
        Label searchLabel = new Label("Search:");
        TextField searchField = new TextField();
        searchField.setPromptText("Enter search term...");
        Button searchButton = new Button("Search");

        searchButton.setOnAction(e -> searchData(searchField.getText()));

        searchBox.getChildren().addAll(searchLabel, searchField, searchButton);

        // Statistics
        Label statsLabel = new Label("Collection Statistics:");
        TextArea statsArea = new TextArea();
        statsArea.setPrefHeight(80);
        statsArea.setEditable(false);

        Button statsButton = new Button("Show Statistics");
        statsButton.setOnAction(e -> showStatistics(statsArea));

        mainLayout.getChildren().addAll(
                formLabel, formGrid, buttonBox,
                dataLabel, dataListView,
                searchBox, statsLabel, statsButton, statsArea
        );

        return mainLayout;
    }

    private void addData() {
        String content = contentField.getText().trim();
        String category = categoryField.getText().trim();

        if (content.isEmpty()) {
            showAlert("Error", "Content cannot be empty.");
            return;
        }

        TextData newData = new TextData(nextId++, content, category);
        dataMap.put(newData.getId(), newData);
        dataList.add(newData);

        clearForm();
        showAlert("Success", "Data added successfully!");
    }

    private void updateData() {
        String idText = idField.getText();
        if (idText.isEmpty()) {
            showAlert("Error", "No item selected for update.");
            return;
        }

        int id = Integer.parseInt(idText);
        TextData data = dataMap.get(id);

        if (data != null) {
            data.setContent(contentField.getText().trim());
            data.setCategory(categoryField.getText().trim());

            // Refresh list view
            dataList.setAll(dataMap.values());
            clearForm();
            showAlert("Success", "Data updated successfully!");
        }
    }

    private void deleteData() {
        String idText = idField.getText();
        if (idText.isEmpty()) {
            showAlert("Error", "No item selected for deletion.");
            return;
        }

        int id = Integer.parseInt(idText);
        TextData data = dataMap.remove(id);

        if (data != null) {
            dataList.remove(data);
            clearForm();
            showAlert("Success", "Data deleted successfully!");
        }
    }

    private void displaySelectedData(TextData data) {
        if (data != null) {
            idField.setText(String.valueOf(data.getId()));
            contentField.setText(data.getContent());
            categoryField.setText(data.getCategory());
        }
    }

    private void searchData(String searchTerm) {
        if (searchTerm.isEmpty()) {
            dataList.setAll(dataMap.values());
            return;
        }

        List<TextData> results = new ArrayList<>();
        for (TextData data : dataMap.values()) {
            if (data.getContent().toLowerCase().contains(searchTerm.toLowerCase()) ||
                    data.getCategory().toLowerCase().contains(searchTerm.toLowerCase())) {
                results.add(data);
            }
        }

        dataList.setAll(results);
    }

    private void showStatistics(TextArea statsArea) {
        StringBuilder stats = new StringBuilder();
        stats.append("Total entries: ").append(dataMap.size()).append("\n");

        // Count by category
        Map<String, Integer> categoryCount = new HashMap<>();
        for (TextData data : dataMap.values()) {
            categoryCount.merge(data.getCategory(), 1, Integer::sum);
        }

        stats.append("Entries by category:\n");
        for (Map.Entry<String, Integer> entry : categoryCount.entrySet()) {
            stats.append("  ").append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
        }

        statsArea.setText(stats.toString());
    }

    private void clearForm() {
        idField.clear();
        contentField.clear();
        categoryField.clear();
        dataListView.getSelectionModel().clearSelection();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}