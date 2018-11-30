package com.magbadelo.checkers.controller;

import com.magbadelo.checkers.CheckersApplication;
import com.magbadelo.checkers.model.CheckersBoard;
import com.magbadelo.checkers.model.Move;
import com.magbadelo.checkers.view.PieceView;
import com.magbadelo.checkers.view.TileView;
import javafx.scene.SnapshotParameters;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DataFormat;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Component
public class PieceController {
    private static final Logger LOGGER = LoggerFactory.getLogger(CheckersApplication.class);
    private final DataFormat pieceViewFormat = new DataFormat("PieceView");
    private PieceView draggingPieceView;
    private TileView sourceTileView;
    private CheckersBoard checkersBoard;
    private GridPane board;

    @Autowired
    public PieceController(CheckersBoard checkersBoard, GridPane board) {
        this.checkersBoard = checkersBoard;
        this.board = board;
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

                checkersBoard.completeMove(move);
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
            aiMove();
        });
    }

    private void aiMove() {
        if (checkersBoard.getCurrentPlayer().isAIPlayer()) {
            List<Move> moves = checkersBoard.generateMoves(checkersBoard.getAiPlayer());
            Move move = moves.get(new Random().nextInt(moves.size()));
            checkersBoard.completeMove(move);

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
        sourceTileView.getChildren().remove(pieceView);
        targetTileView.getChildren().add(pieceView);

        if (move.isCapturingMove()) {
            capturePieceView(move.getMiddleRow(), move.getMiddleCol());
            move.setPossibleJumpMoves(checkersBoard.getPossibleJumpMoves(move));
            if(move.hasPossibleJumpMoves()){
                //DEAL WITH JUMP MOVES
                System.out.println("We lit");
            }
        }

        if (move.isCrowningMove()) {
            pieceView.ascend();
            LOGGER.info("{} piece at {},{} has ASCENDED", checkersBoard.getCurrentPlayer().getPieceType().toString(), move.getTargetRow(), move.getTargetCol());
        }



        checkersBoard.switchCurrentPlayer();

    }

    private void switchPlayer(){

    }
}
