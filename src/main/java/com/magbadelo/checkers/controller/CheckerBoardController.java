package com.magbadelo.checkers.controller;

import com.magbadelo.checkers.model.CheckersBoard;
import com.magbadelo.checkers.model.PieceType;
import com.magbadelo.checkers.view.CheckerBoardView;
import com.magbadelo.checkers.view.CurrentPlayerView;
import com.magbadelo.checkers.view.PieceView;
import com.magbadelo.checkers.view.TileView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.stream.IntStream;

@Component
public class CheckerBoardController {

    private CheckerBoardView checkerBoardView;
    private CheckersBoard checkersBoard;
    private PieceController pieceController;
    private CurrentPlayerView currentPlayerView;

    @Value("${checkerboard.piece.color.one}")
    private String pieceColorOne;

    @Value("${checkerboard.piece.color.two}")
    private String pieceColorTwo;

    @Value("${checkerboard.piece.radius}")
    private double pieceRadius;

    @Autowired
    public CheckerBoardController(CheckerBoardView checkerBoardView, CheckersBoard checkersBoard, PieceController pieceController, CurrentPlayerView currentPlayerView) {
        this.checkerBoardView = checkerBoardView;
        this.checkersBoard = checkersBoard;
        this.pieceController = pieceController;
        this.currentPlayerView = currentPlayerView;
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
        checkersBoard.getHumanPlayer().setPieceType(PieceType.BLACK);
        checkersBoard.getAiPlayer().setPieceType(PieceType.RED);
        checkerBoardView.getLeftVbox().getChildren().add(currentPlayerView);
        currentPlayerView.setPieceColor(checkersBoard.getCurrentPlayer().getPieceType().toString());
        System.out.println(checkersBoard.getCurrentPlayer().getPieceType());
        System.out.println(checkersBoard.generateMoves(checkersBoard.getCurrentPlayer()).size());
        setTileViewHandles();
        setPieceViews();
    }

    private void setTileViewHandles() {
        checkerBoardView.getBoard().getChildren().stream()
                .filter(node -> node instanceof TileView)
                .forEach(node -> pieceController.addDropHandling((TileView) node));
    }

    private void setPieceViews() {
        GridPane board = checkerBoardView.getBoard();
        IntStream.range(0, board.getChildren().size() - 1)
                .filter(index -> (index < 24 || index >= 40))
                .forEach(index -> {
                    TileView tileView = (TileView) board.getChildren().get(index);
                    if (!tileView.isLightTile()) {
                        String pieceColor = index < 24 ? pieceColorOne : pieceColorTwo;
                        PieceView pieceView = new PieceView(pieceColor, pieceRadius);
                        if (pieceView.getColor().equals(checkersBoard.getHumanPlayer().getPieceType().toString())) {
                            pieceController.dragButton(pieceView);
                        }
                        tileView.getChildren().add(pieceView);
                    }
                });
    }
}
