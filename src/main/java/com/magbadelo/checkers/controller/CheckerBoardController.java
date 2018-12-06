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

/**
 * The type Checker board controller.
 */
@Component
public class CheckerBoardController {

    private CheckerBoardView checkerBoardView;
    private CheckersBoard checkersBoard;
    private PieceController pieceController;
    private CurrentPlayerView currentPlayerView;
    private TileController tileController;

    @Value("${checkerboard.piece.fill.color.one}")
    private String pieceColorOne;

    @Value("${checkerboard.piece.fill.color.two}")
    private String pieceColorTwo;

    @Value("${checkerboard.piece.stroke.color.one}")
    private String pieceStrokeOne;

    @Value("${checkerboard.piece.stroke.color.two}")
    private String pieceStrokeTwo;

    @Value("${checkerboard.piece.radius}")
    private double pieceRadius;

    /**
     * Instantiates a new Checker board controller.
     *
     * @param checkerBoardView  the checker board view
     * @param checkersBoard     the checkers board
     * @param pieceController   the piece controller
     * @param currentPlayerView the current player view
     * @param tileController    the tile controller
     */
    @Autowired
    public CheckerBoardController(CheckerBoardView checkerBoardView, CheckersBoard checkersBoard, PieceController pieceController, CurrentPlayerView currentPlayerView, TileController tileController) {
        this.checkerBoardView = checkerBoardView;
        this.checkersBoard = checkersBoard;
        this.pieceController = pieceController;
        this.currentPlayerView = currentPlayerView;
        this.tileController = tileController;
        checkersBoard.getHumanPlayer().setPieceType(PieceType.BLACK);
        checkersBoard.getAiPlayer().setPieceType(PieceType.RED);
        checkerBoardView.getLeftVbox().getChildren().add(currentPlayerView);
        setHBoxEventHandles();
        setTileViewHandles();
    }

    /**
     * Gets checker board view.
     *
     * @return the checker board view
     */
    public CheckerBoardView getCheckerBoardView() {
        return checkerBoardView;
    }

    private void setHBoxEventHandles() {
        HBox hBox = getCheckerBoardView().getHBox();
        hBox.getChildren().get(0).setOnMousePressed(event -> initialisePieces());
        hBox.getChildren().get(1).setOnMousePressed(event -> resetGame());
    }

    private void initialisePieces() {
        currentPlayerView.setPieceColor(checkersBoard.getCurrentPlayer().getPieceType().getColor(), pieceStrokeTwo);
        setPieceViews();
        tileController.showPossibleMoveTileViews(checkersBoard.generateMoves(checkersBoard.getCurrentPlayer(), checkersBoard.getCurrentCheckersState()));
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
                        String pieceStroke = index < 24 ? pieceStrokeOne : pieceStrokeTwo;
                        PieceView pieceView = new PieceView(pieceColor, pieceStroke, pieceRadius);
                        if (pieceView.getColor().equals(checkersBoard.getHumanPlayer().getPieceType().getColor())) {
                            pieceController.dragButton(pieceView);
                        }
                        tileView.getChildren().add(pieceView);
                    }
                });
    }

    private void resetGame() {
        checkerBoardView.getBoard().getChildren().stream().filter(tile -> tile instanceof TileView).forEach(tile -> ((TileView) tile).getChildren().removeIf(piece -> piece instanceof PieceView));
        checkersBoard.reset();
        tileController.resetTileViewColors();
        pieceController.reset();
    }

}
