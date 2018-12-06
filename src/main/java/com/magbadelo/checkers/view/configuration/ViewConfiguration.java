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
                String fill = isLightTile ? lightColor : darkColor;
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
        hbox.setSpacing(20);
        hbox.setStyle("-fx-background-color: #975354;" +
                "-fx-border-style: solid inside;" +
                "-fx-border-width: 5;" +
                "-fx-border-color: #5A3132;");

        Button newGame = new Button("New Game");

        newGame.setPrefSize(100, 20);

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

        Button buttonProjected = new Button("Debug");
        buttonProjected.setOnMousePressed(event -> System.out.println("Debugging"));
        buttonProjected.setPrefSize(100, 20);

        hbox.getChildren().addAll(newGame, buttonProjected, rules);
        hbox.getChildren().forEach(node -> node.setStyle("-fx-background-color: #FFCC66;" +
                "-fx-border-style: solid outside;" +
                "-fx-border-width: 2;" +
                "-fx-border-color: #5A3132;"));

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
        vBox.setAlignment(Pos.CENTER);
        return vBox;
    }

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

    @Bean
    public Label logLabel() {
        Label logLabel = new Label("Game Log");
        return logLabel;
    }

    @Bean
    public ToggleGroup difficulty() {
        final ToggleGroup group = new ToggleGroup();

        RadioButton rb1 = new RadioButton("Easy");
        rb1.setToggleGroup(group);
        rb1.setSelected(true);

        RadioButton rb2 = new RadioButton("Medium");
        rb2.setToggleGroup(group);

        RadioButton rb3 = new RadioButton("Hard");
        rb3.setToggleGroup(group);

        group.getToggles().forEach(toggle -> {
            ((RadioButton) toggle).getStyleClass().add("red-radio-button");
            ((RadioButton) toggle).setStyle(".red-radio-button .dot{" +
                    "-fx-mark-highlight-color: #5A3132 ;" +
                    "-fx-mark-color: #7D4544 ;" +
                    "-fx-focus-color: #5A3132 ;}" +
                    ".red-radio-button .radio{" +
                    " -fx-border-color: #7D4544 ;" +
                    " -fx-border-width: 3 ;}");
            ((RadioButton) toggle).setAlignment(Pos.CENTER_RIGHT);
        });
        return group;
    }

}
