package com.magbadelo.checkers.view.configuration;

import com.magbadelo.checkers.view.TileView;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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



    @Bean
    public GridPane gridPane() {
        GridPane gridPane = new GridPane();
        gridPane.setPrefSize(tileSize * numRows, tileSize * numCols);
        for (int row = 0; row < numRows; row++) {
            for (int col = 0; col < numCols; col++) {
                boolean isLightTile = (row + col) % 2 == 0;
                String fill =  isLightTile ? lightColor : darkColor;
                gridPane.add(new TileView(fill, tileSize, isLightTile, row, col), col, row);
            }
        }
        gridPane.setGridLinesVisible(true);
        return gridPane;
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
}
