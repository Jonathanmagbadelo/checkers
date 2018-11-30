package com.magbadelo.checkers.controller;

import com.magbadelo.checkers.CheckersApplication;
import com.magbadelo.checkers.model.CheckersBoard;
import com.magbadelo.checkers.model.Move;
import com.magbadelo.checkers.view.CurrentPlayerView;
import com.magbadelo.checkers.view.PieceView;
import com.magbadelo.checkers.view.TileView;
import javafx.scene.SnapshotParameters;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DataFormat;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import org.reactfx.util.FxTimer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.List;
import java.util.Random;

@Component
public class PieceController {
    private static final Logger LOGGER = LoggerFactory.getLogger(CheckersApplication.class);
    private final DataFormat pieceViewFormat = new DataFormat("PieceView");
    private PieceView draggingPieceView;
    private TileView sourceTileView;
    private CheckersBoard checkersBoard;
    private GridPane board;
    private CurrentPlayerView currentPlayerView;

    @Autowired
    public PieceController(CheckersBoard checkersBoard, GridPane board, CurrentPlayerView currentPlayerView) {
        this.checkersBoard = checkersBoard;
        this.board = board;
        this.currentPlayerView = currentPlayerView;
    }

    public void dragButton(PieceView pieceView) {
        pieceView.setOnDragDetected(e -> {
            Dragboard db = pieceView.startDragAndDrop(TransferMode.MOVE);
            SnapshotParameters sp = new SnapshotParameters();
            sp.setFill(Color.TRANSPARENT);
            db.setDragView(pieceView.snapshot(sp, null));
            ClipboardContent cc = new ClipboardContent();
            cc.put(pieceViewFormat, " ");
            db.setContent(cc);
            pieceView.setOpacity(0);
            draggingPieceView = pieceView;
        });
    }

    public void addDropHandling(TileView targetTileView) {
        targetTileView.setOnDragOver(e -> {
            Dragboard db = e.getDragboard();
            if (db.hasContent(pieceViewFormat) && draggingPieceView != null) {
                e.acceptTransferModes(TransferMode.MOVE);
            }
        });

        targetTileView.setOnDragDropped(e -> {
            Dragboard db = e.getDragboard();
            sourceTileView = (TileView) (draggingPieceView.getParent());
            Move move = new Move(sourceTileView.getRow(), sourceTileView.getCol(), targetTileView.getRow(), targetTileView.getCol());
            if (db.hasContent(pieceViewFormat) && checkersBoard.isMoveValid(move)) {
                LOGGER.info("Move from {},{} is valid to {},{}!", move.getSourceRow(), move.getSourceCol(), move.getTargetRow(), move.getTargetCol());

                completePieceViewMove(move, sourceTileView, targetTileView, draggingPieceView);

                e.setDropCompleted(true);
                draggingPieceView.setOpacity(100);
                draggingPieceView = null;
            } else {
                LOGGER.info("Move from {},{} is invalid to {},{}!", move.getSourceRow(), move.getSourceCol(), move.getTargetRow(), move.getTargetCol());
                e.setDropCompleted(true);
                draggingPieceView.setOpacity(100);
                draggingPieceView = null;
            }
            FxTimer.runLater(
                    Duration.ofMillis(1500),
                    this::aiMove);
        });
    }

    private void aiMove() {
        if (checkersBoard.getCurrentPlayer().isAIPlayer()) {
            List<Move> moves = checkersBoard.generateMoves(checkersBoard.getAiPlayer());
            Move move = moves.get(new Random().nextInt(moves.size()));

            TileView sourceTileView = getTileView(move.getSourceRow(), move.getSourceCol());
            TileView targetTileView = getTileView(move.getTargetRow(), move.getTargetCol());
            PieceView pieceView = (PieceView) sourceTileView.getChildren().get(1);

            completePieceViewMove(move, sourceTileView, targetTileView, pieceView);
        }
    }

    private TileView getTileView(int row, int col) {
        int index = (row * 8) + col;
        return (TileView) board.getChildren().get(index);
    }

    private void capturePieceView(int row, int col) {
        getTileView(row, col).getChildren().remove(1);
    }

    private void completePieceViewMove(Move move, TileView sourceTileView, TileView targetTileView, PieceView pieceView){
        checkersBoard.completeMove(move);
        sourceTileView.getChildren().remove(pieceView);
        targetTileView.getChildren().add(pieceView);

        if (move.isCapturingMove()) {
            capturePieceView(move.getMiddleRow(), move.getMiddleCol());
            move.setPossibleJumpMoves(checkersBoard.getPossibleJumpMoves(move));
            if(move.hasPossibleJumpMoves()){
                if(checkersBoard.getCurrentPlayer().isAIPlayer()){
                    //automaticaly do ai moves
                    //issue here stop recursion
                    Move nextMove = move.getPossibleJumpMoves().get(new Random().nextInt(move.getPossibleJumpMoves().size()));
                    completePieceViewMove(nextMove, getTileView(nextMove.getSourceRow(), nextMove.getSourceCol()), getTileView(nextMove.getTargetRow(), nextMove.getTargetCol()), pieceView);
                } else{
                    //human does what it whats
                }
                System.out.println("We lit");
            }
        }

        if (move.isCrowningMove()) {
            pieceView.ascend();
            LOGGER.info("{} piece at {},{} has ASCENDED", checkersBoard.getCurrentPlayer().getPieceType().toString(), move.getTargetRow(), move.getTargetCol());
        }

        switchPlayer();

    }

    private void switchPlayer(){
        checkersBoard.switchCurrentPlayer();
        currentPlayerView.setPieceColor(checkersBoard.getCurrentPlayer().getPieceType().toString());
        currentPlayerView.nextTurn();
    }
}
