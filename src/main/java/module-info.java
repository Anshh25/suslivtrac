module com.app.suslivtrac {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.app.suslivtrac to javafx.fxml;
    exports com.app.suslivtrac;
}