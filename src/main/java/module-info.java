module com.example.flappyghost {
    requires javafx.controls;
    requires javafx.fxml;
            
                            
    opens com.example.flappyghost to javafx.fxml;
    exports com.example.flappyghost;
}