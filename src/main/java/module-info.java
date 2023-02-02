module com.example.chess {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.chess to javafx.fxml;
    exports com.example.chess;
    exports com.example.chess.pieces;
    opens com.example.chess.pieces to javafx.fxml;
    exports com.example.chess.logic;
    opens com.example.chess.logic to javafx.fxml;
}