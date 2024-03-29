package com.magbadelo.checkers.controller;

import com.magbadelo.checkers.model.CheckersBoard;
import com.magbadelo.checkers.model.CheckersState;
import com.magbadelo.checkers.model.NegaMax;
import com.magbadelo.checkers.model.Move;
import com.magbadelo.checkers.view.CurrentPlayerView;
import com.magbadelo.checkers.view.PieceView;
import com.magbadelo.checkers.view.TileView;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.TextArea;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DataFormat;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.paint.Color;
import org.reactfx.util.FxTimer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * The Piece controller.
 */
@Component
public class PieceController {
    private final DataFormat pieceViewFormat = new DataFormat("PieceView");
    private PieceView draggingPieceView;
    private TileView sourceTileView;
    private CheckersBoard checkersBoard;
    private CurrentPlayerView currentPlayerView;
    private TileController tileController;
    private boolean playerFinishedMove;
    private TextArea logArea;
    private CheckersState currentCheckersState;
    private NegaMax negaMax;
    private int depth;
    private boolean showHints;

    @Value("${checkerboard.piece.stroke.color.one}")
    private String pieceStrokeOne;

    @Value("${checkerboard.piece.stroke.color.two}")
    private String pieceStrokeTwo;

    /**
     * Instantiates a new Piece controller.
     *
     * @param checkersBoard     the checkers board
     * @param currentPlayerView the current player view
     * @param logArea           the log area
     * @param minMax            the min max
     * @param tileController    the tile controller
     * @param toggleButton      the toggle button
     * @param difficulty        the difficulty
     */
    @Autowired
    public PieceController(CheckersBoard checkersBoard, CurrentPlayerView currentPlayerView, TextArea logArea, NegaMax minMax, TileController tileController, ToggleButton toggleButton, ToggleGroup difficulty) {
        this.checkersBoard = checkersBoard;
        this.currentPlayerView = currentPlayerView;
        this.tileController = tileController;
        this.playerFinishedMove = false;
        this.logArea = logArea;
        this.currentCheckersState = checkersBoard.getCurrentCheckersState();
        this.negaMax = minMax;
        this.showHints = false;
        this.depth = 1;
        toggleButton.setOnMousePressed(event -> showHints = !showHints);
        difficultyListener(difficulty);
    }

    //Sets the difficulty by listening to the three different user toggles which map to a depth number for negamax
    private void difficultyListener(ToggleGroup difficulty) {
        difficulty.selectedToggleProperty().addListener((ov, old_toggle, new_toggle) -> {
            if (difficulty.getSelectedToggle() != null) {
                ToggleButton button = (ToggleButton) difficulty.getSelectedToggle();
                switch (button.getText()) {
                    case "Easy":
                        depth = 1;
                        break;
                    case "Medium":
                        depth = 2;
                        break;
                    case "Hard":
                        depth = 4;
                        break;
                }
            }
        });
    }

    /**
     * Drag button.
     *
     * @param pieceView the piece view
     */
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

    /**
     * Add drop handling.
     *
     * @param targetTileView the target tile view
     */
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
            List<Move> possibleJumpMoves = checkersBoard.generateMoves(checkersBoard.getCurrentPlayer(), currentCheckersState).stream().filter(Move::isCapturingMove).collect(Collectors.toList());
            if (db.hasContent(pieceViewFormat) && checkersBoard.isMoveValid(move, currentCheckersState)) {
                if (!move.isCapturingMove() && !possibleJumpMoves.isEmpty()) {
                    logArea.setText(logArea.getText() + "\n Player MUST capture!");
                    tileController.showPossibleMoveTileViews(possibleJumpMoves);
                } else {
                    completePieceViewMove(move, sourceTileView, targetTileView, draggingPieceView);
                }
            } else {
                logArea.setText(logArea.getText() + "\n" + move.getInvalidReason());
            }
            e.setDropCompleted(true);
            draggingPieceView.setOpacity(100);
            draggingPieceView = null;
            FxTimer.runLater(
                    Duration.ofMillis(10), () -> {
                        if (playerFinishedMove) {
                            switchPlayer();
                            FxTimer.runLater(Duration.ofMillis(1500), this::aiMove);
                        }
                    });
        });
    }

        /**
     * Negamax
     *
         * This method uses the negamax class to get a move choice for the AI player, during the search
         * it keeps track of what the best score and move is.
     * @param possibleMoves the different possible moves from a given state
     */
    private Move getNegaMaxMove(List<Move> possibleMoves) {
        //set alpha beta values
        double alpha = Integer.MIN_VALUE;
        double beta = Integer.MAX_VALUE;
        Move bestMove = possibleMoves.get(0);
        List<Move> capturingMoves = possibleMoves.stream().filter(Move::isCapturingMove).collect(Collectors.toList());
        //if their are possible capturing moves we only care about their sub branches in the game tree
        if (!capturingMoves.isEmpty()) {
            possibleMoves = capturingMoves;
        }
        for (Move possibleMove : possibleMoves) {
            CheckersState checkersState = new CheckersState(currentCheckersState);
            double eval = negaMax.negaMax(checkersState, depth, true, alpha, beta);
            if (eval >= alpha) {
                alpha = eval;
                bestMove = possibleMove;
            }
        }
        return bestMove;
    }

    private void aiMove() {
        if (checkersBoard.getCurrentPlayer().isAIPlayer()) {
            List<Move> moves = checkersBoard.generateMoves(checkersBoard.getAiPlayer(), currentCheckersState);
            Move move = getNegaMaxMove(moves);

            TileView sourceTileView = tileController.getTileView(move.getSourceRow(), move.getSourceCol());
            TileView targetTileView = tileController.getTileView(move.getTargetRow(), move.getTargetCol());
            PieceView pieceView = (PieceView) sourceTileView.getChildren().get(1);

            completePieceViewMove(move, sourceTileView, targetTileView, pieceView);
            switchPlayer();
            playerFinishedMove = false;
        }
    }

    private void capturePieceView(int row, int col) {
        tileController.getTileView(row, col).getChildren().remove(1);
    }

    //This class is only called with a valid move object as it updates the game view
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
                    //issue here recursion stops after two jumps :(
                    Move nextMove = move.getPossibleJumpMoves().get(new Random().nextInt(move.getPossibleJumpMoves().size()));
                    tileController.resetTileViewColors();
                    if (!move.isCrowningMove()) {
                        tileController.showPossibleMoveTileViews(move.getPossibleJumpMoves());
                        FxTimer.runLater(Duration.ofMillis(1500), () -> completePieceViewMove(nextMove, tileController.getTileView(nextMove.getSourceRow(), nextMove.getSourceCol()), tileController.getTileView(nextMove.getTargetRow(), nextMove.getTargetCol()), pieceView));
                    }
                } else {
                    //players must jump when they have the opportunity to
                    if (!move.isCrowningMove()) {
                        logArea.setText(logArea.getText() + "\n Player MUST capture!");
                        tileController.resetTileViewColors();
                        tileController.showPossibleMoveTileViews(move.getPossibleJumpMoves());
                        playerFinishedMove = false;
                    }
                }
            } else if (!checkersBoard.getCurrentPlayer().isAIPlayer()) {
                playerFinishedMove = true;
            }
        }

        //if the move was a crowning move, update the pieceView to become a king
        if (move.isCrowningMove()) {
            pieceView.crown();
            logArea.setText(logArea.getText() + String.format("\n %s piece at %d,%d is now a king", checkersBoard.getCurrentPlayer().getPieceType().toString(), move.getTargetRow(), move.getTargetCol()));
        }

        //if the player has possible jump moves, then their turn is not finished
        if (!move.hasPossibleJumpMoves()) {
            tileController.resetTileViewColors();
            if (!checkersBoard.getCurrentPlayer().isAIPlayer()) {
                playerFinishedMove = true;
            }
        }

        if (!checkersBoard.getCurrentPlayer().isAIPlayer()) {
            tileController.resetTileViewColors();
        }
    }

    private void switchPlayer() {
        if (checkersBoard.isGameOver()) {
            currentPlayerView.gameOver("TBD");
        } else {
            checkersBoard.switchCurrentPlayer();
            String strokeColor = checkersBoard.getCurrentPlayer().getPieceType().toString().equals("Red") ? pieceStrokeOne : pieceStrokeTwo;
            currentPlayerView.setPieceColor(checkersBoard.getCurrentPlayer().getPieceType().getColor(), strokeColor);
            currentPlayerView.nextTurn();
            if (!checkersBoard.getCurrentPlayer().isAIPlayer() && showHints) {
                List<Move> possibleMoves = checkersBoard.generateMoves(checkersBoard.getCurrentPlayer(), currentCheckersState);
                List<Move> possibleJumpMoves = checkersBoard.generateMoves(checkersBoard.getCurrentPlayer(), currentCheckersState).stream().filter(Move::isCapturingMove).collect(Collectors.toList());
                tileController.showPossibleMoveTileViews(possibleJumpMoves.isEmpty() ? possibleMoves : possibleJumpMoves);
            }
        }
    }

    /**
     * Reset the game
     */
    public void reset() {
        playerFinishedMove = false;
        sourceTileView = null;
        currentCheckersState = checkersBoard.getCurrentCheckersState();
        logArea.clear();
    }

}
