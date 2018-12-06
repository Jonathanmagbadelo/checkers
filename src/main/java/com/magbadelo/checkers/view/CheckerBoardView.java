package com.magbadelo.checkers.view;

import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class CheckerBoardView {
    private GridPane board;
    private HBox hBox;
    private VBox leftVbox;
    private VBox rightVbox;

    @Autowired
    public CheckerBoardView(GridPane board, HBox hBox, @Qualifier("leftVbox") VBox leftVbox, @Qualifier("rightVbox") VBox rightVbox, TextArea logArea, Label logLabel, ToggleGroup difficulty, ToggleButton toggleButton) {
        this.board = board;
        this.hBox = hBox;
        this.leftVbox = leftVbox;
        this.rightVbox = rightVbox;
        difficulty.getToggles().forEach(toggle -> hBox.getChildren().add((ToggleButton) toggle));
        hBox.getChildren().add(toggleButton);
        rightVbox.getChildren().addAll(logLabel, logArea);
    }

    public Parent createContent() {
        BorderPane borderPane = new BorderPane();
        borderPane.setCenter(board);
        borderPane.setTop(hBox);
        borderPane.setLeft(leftVbox);
        borderPane.setRight(rightVbox);
        return borderPane;
    }

    public GridPane getBoard() {
        return board;
    }

    public HBox getHBox() {
        return hBox;
    }

    public VBox getLeftVbox() {
        return leftVbox;
    }
}
