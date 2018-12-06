package com.magbadelo.checkers.view;

import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 * The type Checker board view.
 */
@Component
public class CheckerBoardView {
    private GridPane board;
    private HBox hBox;
    private VBox leftVbox;
    private VBox rightVbox;

    /**
     * Instantiates a new Checker board view.
     *
     * @param board        the board
     * @param hBox         the h box
     * @param leftVbox     the left vbox
     * @param rightVbox    the right vbox
     * @param logArea      the log area
     * @param logLabel     the log label
     * @param difficulty   the difficulty used to change the depth of negamax
     * @param toggleButton the toggle button
     */
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

    /**
     * Create content parent.
     *
     * @return the parent
     */
    public Parent createContent() {
        BorderPane borderPane = new BorderPane();
        borderPane.setCenter(board);
        borderPane.setTop(hBox);
        borderPane.setLeft(leftVbox);
        borderPane.setRight(rightVbox);
        return borderPane;
    }

    /**
     * Gets board.
     *
     * @return the board
     */
    public GridPane getBoard() {
        return board;
    }

    /**
     * Gets h box.
     *
     * @return the h box
     */
    public HBox getHBox() {
        return hBox;
    }

    /**
     * Gets left vbox.
     *
     * @return the left vbox
     */
    public VBox getLeftVbox() {
        return leftVbox;
    }
}
