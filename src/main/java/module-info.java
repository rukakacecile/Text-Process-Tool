module org.example.textprocessingtool {
    requires javafx.controls;
    requires javafx.fxml;


    opens org.example.textprocessingtool to javafx.fxml;
    exports org.example.textprocessingtool;
}