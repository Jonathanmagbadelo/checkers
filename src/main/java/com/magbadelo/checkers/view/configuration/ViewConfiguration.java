package com.magbadelo.checkers.view.configuration;

import com.magbadelo.checkers.view.PieceView;
import com.magbadelo.checkers.view.TileView;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.stream.IntStream;

@Configuration
public class ViewConfiguration {

    @Value("${checkerboard.tile.size}")
    private int tileSize;

    @Value("${checkerboard.num.rows}")
    private int numRows;

    @Value("${checkerboard.num.cols}")
    private int numCols;

    @Value("${checkerboard.tile.lightColor}")
    private String lightColor;

    @Value("${checkerboard.tile.darkColor}")
    private String darkColor;

    @Value("${checkerboard.piece.color.one}")
    private String pieceColorOne;

    @Value("${checkerboard.piece.color.two}")
    private String pieceColorTwo;

    @Value("${checkerboard.piece.radius}")
    private double pieceRadius;

    @Bean
    public GridPane gridPane() {
        GridPane gridPane = new GridPane();
        gridPane.setMinSize(tileSize * numRows, tileSize * numCols);
        gridPane.setMinSize(tileSize * numRows, tileSize * numCols);
        for (int row = 0; row < numRows; row++) {
            for (int col = 0; col < numCols; col++) {
                boolean isLightTile = (row + col) % 2 == 0;
                String color = isLightTile ? lightColor : darkColor;
                TileView tileView = new TileView(color, tileSize, isLightTile);
                gridPane.add(tileView, col, row);
            }
        }
        return gridPane;
    }

    @Bean
    public StackPane stackPane() {
        StackPane stackPane = new StackPane();
        stackPane.setMinSize(tileSize * numRows, tileSize * numCols);
        return stackPane;
    }

    @Bean
    public HBox topBox() {
        HBox hbox = new HBox();
        hbox.setPadding(new Insets(15, 12, 15, 12));
        hbox.setSpacing(10);
        hbox.setStyle("-fx-background-color: #975354;" +
                "-fx-border-style: solid inside;" +
                "-fx-border-width: 5;" +
                "-fx-border-color: #5A3132;");

        Button buttonCurrent = new Button("New Game");

        buttonCurrent.setPrefSize(100, 20);
        buttonCurrent.setStyle("-fx-background-color: #FFCC66;");

        Button buttonProjected = new Button("Debug");
        buttonProjected.setOnMousePressed(event -> System.out.println("Debugging"));
        buttonProjected.setPrefSize(100, 20);
        buttonProjected.setStyle("-fx-background-color: #FFCC66;");

        hbox.getChildren().addAll(buttonCurrent, buttonProjected);

        return hbox;

    }

    @Bean
    @Qualifier("LeftVbox")
    public VBox leftVbox() {
        VBox vBox = new VBox();
        vBox.setStyle("-fx-background-color: #975354;" +
                "-fx-border-style: solid inside;" +
                "-fx-border-width: 5;" +
                "-fx-border-color: #7D4544;");
        vBox.setMinWidth(200);
        return vBox;
    }

    @Bean
    @Qualifier("rightVbox")
    public VBox rightVbox() {
        TextArea logArea = new TextArea();
        VBox vBox = new VBox();
        vBox.setStyle("-fx-background-color: #975354;" +
                "-fx-border-style: solid inside;" +
                "-fx-border-width: 5;" +
                "-fx-border-color: #7D4544;");
        vBox.setMinWidth(300);
        vBox.setPadding(new Insets(15, 12, 15, 12));
        vBox.setSpacing(10);
        logArea.setPrefSize(200, 750);
        vBox.getChildren().add(logArea);
        return vBox;
    }

    @Bean
    public Group pieceGroup() {
        Group pieceGroup = new Group();
        IntStream.range(0, 24).forEach(num ->
                pieceGroup.getChildren().add(num < 12 ? new PieceView(pieceColorOne, pieceRadius) : new PieceView(pieceColorTwo, pieceRadius))
        );
        pieceGroup.setAutoSizeChildren(false);
        return pieceGroup;
    }

}
