package com.magbadelo.checkers.view;

import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.layout.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class CheckerBoardView {
    private GridPane board;
    private StackPane checkerBoardContainer;
    private HBox hBox;
    private VBox leftVbox;
    private VBox rightVbox;
    private Group piecesGroup;

    @Autowired
    public CheckerBoardView(GridPane board, StackPane checkerBoardContainer, HBox hBox, @Qualifier("leftVbox") VBox leftVbox, @Qualifier("rightVbox") VBox rightVbox, Group piecesGroup) {
        this.board = board;
        this.checkerBoardContainer = checkerBoardContainer;
        this.hBox = hBox;
        this.leftVbox = leftVbox;
        this.rightVbox = rightVbox;
        this.piecesGroup = piecesGroup;
    }

    public Parent createContent() {
        BorderPane borderPane = new BorderPane();
        checkerBoardContainer.getChildren().add(board);
        borderPane.setCenter(checkerBoardContainer);
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

    public Group getPiecesGroup() {
        return piecesGroup;
    }

    public StackPane getCheckerBoardContainer() {
        return checkerBoardContainer;
    }

}
