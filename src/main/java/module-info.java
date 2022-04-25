module queues.queues {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;


    opens tema2 to javafx.fxml;
    exports tema2;
}