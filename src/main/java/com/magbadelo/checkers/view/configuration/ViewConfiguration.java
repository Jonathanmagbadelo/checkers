package com.magbadelo.checkers.view.configuration;

import com.google.common.io.Resources;
import com.magbadelo.checkers.view.TileView;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;

/**
 * The type View configuration.
 */
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

    /**
     * Grid pane grid pane.
     *
     * @return the grid pane
     */
    @Bean
    public GridPane gridPane() {
        GridPane gridPane = new GridPane();
        gridPane.setPrefSize(tileSize * numRows, tileSize * numCols);
        for (int row = 0; row < numRows; row++) {
            for (int col = 0; col < numCols; col++) {
                boolean isLightTile = (row + col) % 2 == 0;
                String fill = isLightTile ? lightColor : darkColor;
                gridPane.add(new TileView(fill, tileSize, isLightTile, row, col), col, row);
            }
        }
        gridPane.setGridLinesVisible(true);
        return gridPane;
    }

    /**
     * Top box h box.
     *
     * @return the h box
     */
    @Bean
    public HBox topBox() {
        HBox hbox = new HBox();
        hbox.setPadding(new Insets(15, 12, 15, 12));
        hbox.setSpacing(20);
        hbox.setStyle("-fx-background-color: #975354;" +
                "-fx-border-style: solid inside;" +
                "-fx-border-width: 5;" +
                "-fx-border-color: #5A3132;");

        Button start = new Button("Start");

        start.setPrefSize(100, 20);

        Button rules = new Button("Rules");

        rules.setPrefSize(100, 20);
        rules.setOnMousePressed(event -> {
            Alert rulePopUpBox = new Alert(Alert.AlertType.INFORMATION);
            rulePopUpBox.setTitle("Rules");
            rulePopUpBox.setHeaderText("Checker Game Rules");
            URL checkerRulesUrl = Resources.getResource("checker-rules.txt");
            try {
                String checkerRules = Resources.toString(checkerRulesUrl, Charset.defaultCharset());
                rulePopUpBox.setContentText(checkerRules);
            } catch (IOException e) {
                e.printStackTrace();
                rulePopUpBox.setContentText("no rules set");
            }


            rulePopUpBox.showAndWait();
        });

        Button reset = new Button("Reset");
        reset.setPrefSize(100, 20);

        hbox.getChildren().addAll(start, reset, rules);
        hbox.getChildren().forEach(node -> node.setStyle("-fx-background-color: #FFCC66;" +
                "-fx-border-style: solid outside;" +
                "-fx-border-width: 2;" +
                "-fx-border-color: #5A3132;"));

        return hbox;
    }

    /**
     * Left vbox v box.
     *
     * @return the v box
     */
    @Bean
    @Qualifier("LeftVbox")
    public VBox leftVbox() {
        VBox vBox = new VBox();
        vBox.setStyle("-fx-background-color: #975354;" +
                "-fx-border-style: solid inside;" +
                "-fx-border-width: 5;" +
                "-fx-border-color: #7D4544;");
        vBox.setMinWidth(200);
        vBox.setAlignment(Pos.CENTER);
        return vBox;
    }

    /**
     * Right vbox v box.
     *
     * @return the v box
     */
    @Bean
    @Qualifier("rightVbox")
    public VBox rightVbox() {

        VBox vBox = new VBox();
        vBox.setStyle("-fx-background-color: #975354;" +
                "-fx-border-style: solid inside;" +
                "-fx-border-width: 5;" +
                "-fx-border-color: #7D4544;");
        vBox.setMinWidth(300);
        vBox.setPadding(new Insets(15, 12, 15, 12));
        vBox.setSpacing(30);
        vBox.setAlignment(Pos.CENTER);
        return vBox;
    }

    /**
     * Log area text area.
     *
     * @return the text area
     */
    @Bean
    public TextArea logArea() {
        TextArea logArea = new TextArea();
        logArea.setPrefSize(200, 650);
        logArea.setStyle(".text-area .scroll-pane { " +
                "    -fx-background-color: yellow;" +
                ".text-area .scroll-pane .viewport{" +
                "    -fx-background-color: transparent;" +
                ".text-area .scroll-pane .content{" +
                "    -fx-background-color: transparent;}");
        logArea.setEditable(false);
        return logArea;
    }

    /**
     * Log label label.
     *
     * @return the label
     */
    @Bean
    public Label logLabel() {
        Label logLabel = new Label("Game Log");
        return logLabel;
    }

    /**
     * Difficulty toggle group.
     *
     * @return the toggle group
     */
    @Bean
    public ToggleGroup difficulty() {
        final ToggleGroup group = new ToggleGroup();

        ToggleButton rb1 = new ToggleButton("Easy");
        rb1.setToggleGroup(group);
        rb1.setSelected(true);

        ToggleButton rb2 = new ToggleButton("Medium");
        rb2.setToggleGroup(group);

        ToggleButton rb3 = new ToggleButton("Hard");
        rb3.setToggleGroup(group);

        return group;
    }

    /**
     * Toggle switch toggle button.
     *
     * @return the toggle button
     */
    @Bean
    public ToggleButton toggleSwitch() {
        ToggleButton toggleButton = new ToggleButton("Show Hints?");
        toggleButton.setSelected(false);
        return toggleButton;
    }

}
