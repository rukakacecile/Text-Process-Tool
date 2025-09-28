package org.example.textprocessingtool;



import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.geometry.*;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.util.ArrayList;
import java.util.List;

public class RegexModule {
    private TextArea inputTextArea;
    private TextField patternField;
    private TextField replacementField;
    private TextArea resultTextArea;
    private ListView<String> matchListView;

    public VBox createRegexUI() {
        VBox mainLayout = new VBox(10);
        mainLayout.setPadding(new Insets(10));

        // Input section
        Label inputLabel = new Label("Input Text:");
        inputTextArea = new TextArea();
        inputTextArea.setPromptText("Enter your text here...");
        inputTextArea.setPrefHeight(150);

        // Pattern section
        HBox patternBox = new HBox(10);
        Label patternLabel = new Label("Regex Pattern:");
        patternField = new TextField();
        patternField.setPromptText("e.g., [a-zA-Z]+\\d{2,}");
        patternField.setPrefWidth(300);

        // Replacement section
        HBox replacementBox = new HBox(10);
        Label replacementLabel = new Label("Replacement Text:");
        replacementField = new TextField();
        replacementField.setPromptText("Text to replace matches with");

        patternBox.getChildren().addAll(patternLabel, patternField);
        replacementBox.getChildren().addAll(replacementLabel, replacementField);

        // Buttons
        HBox buttonBox = new HBox(10);
        Button searchButton = new Button("Search");
        Button replaceButton = new Button("Replace All");
        Button clearButton = new Button("Clear");

        searchButton.setOnAction(e -> searchMatches());
        replaceButton.setOnAction(e -> replaceMatches());
        clearButton.setOnAction(e -> clearAll());

        buttonBox.getChildren().addAll(searchButton, replaceButton, clearButton);

        // Results section
        Label resultLabel = new Label("Results:");
        resultTextArea = new TextArea();
        resultTextArea.setPrefHeight(150);
        resultTextArea.setEditable(false);

        // Match list
        Label matchLabel = new Label("Matches Found:");
        matchListView = new ListView<>();
        matchListView.setPrefHeight(100);

        // Add components to main layout
        mainLayout.getChildren().addAll(
                inputLabel, inputTextArea,
                patternBox, replacementBox,
                buttonBox, matchLabel, matchListView,
                resultLabel, resultTextArea
        );

        return mainLayout;
    }

    private void searchMatches() {
        String text = inputTextArea.getText();
        String patternStr = patternField.getText();

        if (text.isEmpty() || patternStr.isEmpty()) {
            showAlert("Error", "Please enter both text and pattern.");
            return;
        }

        try {
            Pattern pattern = Pattern.compile(patternStr);
            Matcher matcher = pattern.matcher(text);

            List<String> matches = new ArrayList<>();
            int matchCount = 0;

            while (matcher.find()) {
                matchCount++;
                String match = String.format("Match %d: '%s' (position %d-%d)",
                        matchCount, matcher.group(), matcher.start(), matcher.end());
                matches.add(match);
            }

            matchListView.getItems().setAll(matches);
            resultTextArea.setText(String.format("Found %d matches using pattern: %s",
                    matchCount, patternStr));

        } catch (Exception e) {
            showAlert("Regex Error", "Invalid regular expression: " + e.getMessage());
        }
    }

    private void replaceMatches() {
        String text = inputTextArea.getText();
        String patternStr = patternField.getText();
        String replacement = replacementField.getText();

        if (text.isEmpty() || patternStr.isEmpty()) {
            showAlert("Error", "Please enter text, pattern, and replacement.");
            return;
        }

        try {
            Pattern pattern = Pattern.compile(patternStr);
            Matcher matcher = pattern.matcher(text);

            String result = matcher.replaceAll(replacement);
            resultTextArea.setText(result);

            // Show what was replaced
            matcher.reset();
            List<String> replacements = new ArrayList<>();
            while (matcher.find()) {
                replacements.add(String.format("Replaced: '%s' with '%s'",
                        matcher.group(), replacement));
            }

            matchListView.getItems().setAll(replacements);

        } catch (Exception e) {
            showAlert("Regex Error", "Invalid regular expression: " + e.getMessage());
        }
    }

    private void clearAll() {
        inputTextArea.clear();
        patternField.clear();
        replacementField.clear();
        resultTextArea.clear();
        matchListView.getItems().clear();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}