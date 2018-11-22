package com.magbadelo.checkers.controller;

import com.magbadelo.checkers.model.CheckersBoard;
import com.magbadelo.checkers.model.Piece;
import com.magbadelo.checkers.model.PieceType;
import com.magbadelo.checkers.view.CheckerBoardView;
import com.magbadelo.checkers.view.PieceView;
import com.magbadelo.checkers.view.TileView;
import javafx.scene.Group;
import javafx.scene.layout.HBox;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CheckerBoardController {

    private CheckerBoardView checkerBoardView;
    private CheckersBoard checkersBoard;
    private PieceController pieceController;

    @Autowired
    public CheckerBoardController(CheckerBoardView checkerBoardView, CheckersBoard checkersBoard, PieceController pieceController) {
        this.checkerBoardView = checkerBoardView;
        this.checkersBoard = checkersBoard;
        this.pieceController = pieceController;
        setHBoxEventHandles();
    }

    public CheckerBoardView getCheckerBoardView() {
        return checkerBoardView;
    }


    private void setHBoxEventHandles() {
        HBox hBox = getCheckerBoardView().getHBox();
        hBox.getChildren().get(0).setOnMousePressed(event -> {
            System.out.println("Working");
            initialisePieces();
        });
    }

    private void initialisePieces() {
        checkersBoard.getHumanPlayer().setPieceType(PieceType.RED);
        checkersBoard.getAiPlayer().setPieceType(PieceType.BLACK);
        System.out.println(checkersBoard.generateMoves(checkersBoard.getCurrentPlayer()).size());
        setPieceViewEventHandles();
    }

    private void setPieceViewEventHandles() {
        checkerBoardView.getBoard().getChildren().stream()
                .filter(node -> node instanceof PieceView)
                .filter(node -> checkersBoard.getHumanPlayer().getPieceType().toString().equals(((PieceView) node).getColor()))
                .forEach(node -> pieceController.setPieceViewEventHandles((PieceView) node));
    }
}
