package com.magbadelo.checkers.controller;

import com.magbadelo.checkers.model.CheckersBoard;
import com.magbadelo.checkers.model.CheckersState;
import com.magbadelo.checkers.model.MinMax;
import com.magbadelo.checkers.model.Move;
import com.magbadelo.checkers.view.CurrentPlayerView;
import com.magbadelo.checkers.view.PieceView;
import com.magbadelo.checkers.view.TileView;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.TextArea;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DataFormat;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import org.reactfx.util.FxTimer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.List;
import java.util.Random;

@Component
public class PieceController {
    private final DataFormat pieceViewFormat = new DataFormat("PieceView");
    private PieceView draggingPieceView;
    private TileView sourceTileView;
    private CheckersBoard checkersBoard;
    private GridPane board;
    private CurrentPlayerView currentPlayerView;
    private boolean playerFinishedMove;
    private TextArea logArea;
    private CheckersState currentCheckersState;
    private MinMax minMax;

    @Value("${checkerboard.piece.stroke.color.one}")
    private String pieceStrokeOne;

    @Value("${checkerboard.piece.stroke.color.two}")
    private String pieceStrokeTwo;

    @Autowired
    public PieceController(CheckersBoard checkersBoard, GridPane board, CurrentPlayerView currentPlayerView, TextArea logArea, MinMax minMax) {
        this.checkersBoard = checkersBoard;
        this.board = board;
        this.currentPlayerView = currentPlayerView;
        this.playerFinishedMove = false;
        this.logArea = logArea;
        this.currentCheckersState = checkersBoard.getCurrentCheckersState();
        this.minMax = minMax;
    }

    public void dragButton(PieceView pieceView) {
        pieceView.setOnDragDetected(e -> {
            if (!checkersBoard.getCurrentPlayer().isAIPlayer()) {
                Dragboard db = pieceView.startDragAndDrop(TransferMode.MOVE);
                SnapshotParameters sp = new SnapshotParameters();
                sp.setFill(Color.TRANSPARENT);
                db.setDragView(pieceView.snapshot(sp, null));
                ClipboardContent cc = new ClipboardContent();
                cc.put(pieceViewFormat, " ");
                db.setContent(cc);
                pieceView.setOpacity(0);
                draggingPieceView = pieceView;
            }
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
            if (db.hasContent(pieceViewFormat) && checkersBoard.isMoveValid(move, currentCheckersState)) {
                completePieceViewMove(move, sourceTileView, targetTileView, draggingPieceView);
                e.setDropCompleted(true);
                draggingPieceView.setOpacity(100);
                draggingPieceView = null;
            } else {
                logArea.setText(logArea.getText() + String.format("\n Move from %d,%d is invalid to %d,%d!", move.getSourceRow(), move.getSourceCol(), move.getTargetRow(), move.getTargetCol()));
                e.setDropCompleted(true);
                draggingPieceView.setOpacity(100);
                draggingPieceView = null;
            }
            FxTimer.runLater(
                    Duration.ofMillis(10), () -> {
                        if (playerFinishedMove) {
                            switchPlayer();
                            FxTimer.runLater(Duration.ofMillis(1500), this::aiMove);
                        }
                    });
        });
    }

    private void aiMove() {
        minMax.generateGameTree(currentCheckersState);
        if (checkersBoard.getCurrentPlayer().isAIPlayer()) {
            List<Move> moves = checkersBoard.generateMoves(checkersBoard.getAiPlayer(), currentCheckersState);
            Move move = moves.get(new Random().nextInt(moves.size()));

            TileView sourceTileView = getTileView(move.getSourceRow(), move.getSourceCol());
            TileView targetTileView = getTileView(move.getTargetRow(), move.getTargetCol());
            PieceView pieceView = (PieceView) sourceTileView.getChildren().get(1);

            completePieceViewMove(move, sourceTileView, targetTileView, pieceView);
            switchPlayer();
            playerFinishedMove = false;
        }
    }

    private TileView getTileView(int row, int col) {
        int index = (row * 8) + col;
        return (TileView) board.getChildren().get(index);
    }

    private void capturePieceView(int row, int col) {
        getTileView(row, col).getChildren().remove(1);
    }

    private void completePieceViewMove(Move move, TileView sourceTileView, TileView targetTileView, PieceView pieceView) {
        checkersBoard.completeMove(move, currentCheckersState);
        sourceTileView.getChildren().remove(pieceView);
        targetTileView.getChildren().add(pieceView);
        logArea.setText(logArea.getText() + String.format("\n %s move from %d,%d to %d,%d!", checkersBoard.getCurrentPlayer().getPieceType().toString(), move.getSourceRow(), move.getSourceCol(), move.getTargetRow(), move.getTargetCol()));
        if (move.isCapturingMove()) {
            capturePieceView(move.getMiddleRow(), move.getMiddleCol());
            logArea.setText(logArea.getText() + String.format("\n Captured %s piece at %d,%d!", checkersBoard.getNextPlayer().getPieceType().toString(), move.getMiddleRow(), move.getMiddleCol()));
            move.setPossibleJumpMoves(checkersBoard.getPossibleJumpMoves(move, currentCheckersState));
            if (move.hasPossibleJumpMoves()) {
                if (checkersBoard.getCurrentPlayer().isAIPlayer()) {
                    //automaticaly do ai moves
                    //issue here stop recursion
                    Move nextMove = move.getPossibleJumpMoves().get(new Random().nextInt(move.getPossibleJumpMoves().size()));
                    resetTileViewColors();
                    if (!move.isCrowningMove()) {
                        showPossibleJumpMoveTileViews(move.getPossibleJumpMoves());
                        FxTimer.runLater(Duration.ofMillis(1500), () -> completePieceViewMove(nextMove, getTileView(nextMove.getSourceRow(), nextMove.getSourceCol()), getTileView(nextMove.getTargetRow(), nextMove.getTargetCol()), pieceView));
                    }
                } else {
                    //human does what it whats
                    if (!move.isCrowningMove()) {
                        logArea.setText(logArea.getText() + "\n Player MUST capture!");
                        resetTileViewColors();
                        showPossibleJumpMoveTileViews(move.getPossibleJumpMoves());
                        playerFinishedMove = false;
                    }
                }
                System.out.println("We lit");
            } else if (!checkersBoard.getCurrentPlayer().isAIPlayer()) {
                playerFinishedMove = true;
            }
        }

        if (move.isCrowningMove()) {
            pieceView.crown();
            logArea.setText(logArea.getText() + String.format("\n %s piece at %d,%d is now a king", checkersBoard.getCurrentPlayer().getPieceType().toString(), move.getTargetRow(), move.getTargetCol()));
        }

        if (!move.hasPossibleJumpMoves()) {
            resetTileViewColors();
            if (!checkersBoard.getCurrentPlayer().isAIPlayer()) {
                playerFinishedMove = true;
            }
        }
    }

    private void switchPlayer() {
        if (currentCheckersState.isGameOver()) {
            currentPlayerView.gameOver();
        } else {
            checkersBoard.switchCurrentPlayer();
            String strokeColor = checkersBoard.getCurrentPlayer().getPieceType().toString().equals("Red") ? pieceStrokeOne : pieceStrokeTwo;
            currentPlayerView.setPieceColor(checkersBoard.getCurrentPlayer().getPieceType().getColor(), strokeColor);
            currentPlayerView.nextTurn();
        }

    }

    public void showPossibleJumpMoveTileViews(List<Move> possibleJumpMoves) {
        possibleJumpMoves.forEach(move -> {
            TileView tileView = getTileView(move.getTargetRow(), move.getTargetCol());
            Rectangle tile = ((Rectangle) tileView.getChildren().get(0));
            tile.setFill(Color.GREENYELLOW);
            tile.setOpacity(70);
        });
    }

    public void resetTileViewColors() {
        board.getChildren().stream()
                .filter(node -> node instanceof TileView)
                .filter(node -> ((Rectangle) (((TileView) node).getChildren().get(0))).getFill().equals(Color.GREENYELLOW))
                .forEach(node -> ((Rectangle) (((TileView) node).getChildren().get(0))).setFill(Color.web("A85D5D")));
    }
}
