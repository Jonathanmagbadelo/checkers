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
        Piece[][] initialState = checkersBoard.getCurrentCheckersState().getBoardState();
        int index = 0;
        Group pieceGroup = checkerBoardView.getPiecesGroup();

        for (int row = 0; row < initialState.length; row++) {
            for (int col = 0; col < initialState.length; col++) {
                if (checkersBoard.getCurrentCheckersState().hasPiece(row, col)) {
                    TileView tileView = (TileView) checkerBoardView.getBoard().getChildren().get((row * initialState.length) + col);
                    PieceView pieceView = (PieceView) pieceGroup.getChildren().get(index);
                    pieceView.move(tileView.getCentreX(), tileView.getCentreY());
                    pieceView.setOrigin(tileView.getCentreX(), tileView.getCentreY());
                    index++;
                }
            }
        }
        checkersBoard.getHumanPlayer().setPieceType(PieceType.RED);
        checkersBoard.getAiPlayer().setPieceType(PieceType.BLACK);
        System.out.println(checkersBoard.generateMoves(checkersBoard.getCurrentPlayer()).size());
        setPieceViewEventHandles();
        checkerBoardView.getCheckerBoardContainer().getChildren().add(pieceGroup);
    }

    private void setPieceViewEventHandles() {
        checkerBoardView.getPiecesGroup().getChildren().forEach(child -> {
            PieceView pieceView = (PieceView) child;
            if (checkersBoard.getHumanPlayer().getPieceType().toString().equals(pieceView.getColor())){
                pieceController.setPieceViewEventHandles(pieceView);
            }
        });
    }
}
