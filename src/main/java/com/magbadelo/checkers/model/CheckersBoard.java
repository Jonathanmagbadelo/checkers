package com.magbadelo.checkers.model;

import com.magbadelo.checkers.CheckersApplication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Component
public class CheckersBoard {
    private static final Logger LOGGER = LoggerFactory.getLogger(CheckersApplication.class);
    private Player humanPlayer;
    private Player aiPlayer;
    private Player currentPlayer;

    //@Value("${checkerboard.num.rows}")
    private int numRows = 8;

    //@Value("${checkerboard.num.cols}")
    private int numCols = 8;

    private CheckersState currentCheckersState;

    public CheckersBoard() {
        this.aiPlayer = new Player(true);
        this.humanPlayer = new Player(false);
        this.currentPlayer = getHumanPlayer();
        currentCheckersState = new CheckersState(numRows, numCols, currentPlayer);
        initialise();
    }

    public Player getHumanPlayer() {
        return humanPlayer;
    }

    public Player getAiPlayer() {
        return aiPlayer;
    }

    public void switchCurrentPlayer() {
        this.currentPlayer = currentPlayer.isAIPlayer() ? getHumanPlayer() : getAiPlayer();
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public CheckersState getCurrentCheckersState() {
        return currentCheckersState;
    }

    private void initialise() {
        for (int row = 0; row < numRows; row++) {
            for (int col = 0; col < numCols; col++) {
                boolean isLightTile = (row + col) % 2 == 0;
                Piece piece = makePiece(isLightTile, row, col);
                if (piece != null) {
                    currentCheckersState.setPiece(row, col, piece);
                }
            }
        }
    }

    private Piece makePiece(boolean isLightTile, int row, int col) {
        if (row <= 2 && !isLightTile) {
            return new Piece(row, col, false, PieceType.RED);
        }

        if (row >= 5 && !isLightTile) {
            return new Piece(row, col, false, PieceType.BLACK);
        }

        return null;
    }

    public boolean isMoveValid(Move move) {
        Piece piece = currentCheckersState.getPiece(move.getSourceRow(), move.getSourceCol());
        if (!isLightTile(move.getTargetRow(), move.getTargetCol())) {

            if (Math.abs(move.getTargetCol() - move.getSourceCol()) == 1 && move.getTargetRow() - move.getSourceRow() == piece.getPieceType().getMoveDir()) {
                if (!currentCheckersState.hasPiece(move.getTargetRow(), move.getTargetCol())) {
                    return true;
                }
            }

            if (Math.abs(move.getTargetCol() - move.getSourceCol()) == 2 && move.getTargetRow() - move.getSourceRow() == piece.getPieceType().getMoveDir() * 2) {
                if (currentCheckersState.hasPiece(move.getMiddleRow(), move.getMiddleCol()) && currentCheckersState.getPiece(move.getMiddleRow(), move.getMiddleCol()).getPieceType() != piece.getPieceType()) {
                    if (!currentCheckersState.hasPiece(move.getTargetRow(), move.getTargetCol())) {
                        move.setCapturingMove();
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean isLightTile(int row, int col) {
        if (row % 2 == 0) {
            return col % 2 == 0;
        }
        return col % 2 != 0;
    }

    public List<Move> generateMoves(Player player) {
        int direction = player.getPieceType().getMoveDir();
        List<Move> everyMove = new ArrayList<>();
        for (int row = 0; row < numRows; row++) {
            for (int col = 0; col < numCols; col++) {
                if (currentCheckersState.hasPiece(row, col) && currentCheckersState.getPiece(row, col).getPieceType() == player.getPieceType()) {
                    everyMove.add(new Move(row, col, (row + direction), (col + 1)));
                    everyMove.add(new Move(row, col, (row + direction), (col - 1)));
                    everyMove.add(new Move(row, col, (row + (direction * 2)), (col + 2)));
                    everyMove.add(new Move(row, col, (row + (direction * 2)), (col - 2)));
                }
            }
        }
        List<Move> possibleMoves = everyMove.stream()
                .filter(move -> move.getTargetRow() >= 0 && move.getTargetRow() < numRows)
                .filter(move -> move.getTargetCol() >= 0 && move.getTargetCol() < numCols)
                .filter(this::isMoveValid)
                .collect(Collectors.toList());
        return possibleMoves;
    }

    public void completeMove(Move move) {
        Piece piece = currentCheckersState.getPiece(move.getSourceRow(), move.getSourceCol());
        currentCheckersState.setPiece(move.getTargetRow(), move.getTargetCol(), piece);
        currentCheckersState.setPiece(move.getSourceRow(), move.getSourceCol(), null);
        if (currentCheckersState.hasPiece(move.getMiddleRow(), move.getMiddleCol()) && currentCheckersState.getPiece(move.getMiddleRow(), move.getMiddleCol()).getPieceType() != piece.getPieceType()) {
            currentCheckersState.setPiece(move.getMiddleRow(), move.getMiddleCol(), null);
            LOGGER.info("Captured Piece at {},{}!", move.getMiddleRow(), move.getMiddleCol());
        }
        move.setCrowningMove(isCrowningMove(piece, move.getTargetRow()));
    }

    public boolean isCrowningMove(Piece piece, int targetRow) {
        if (piece.getPieceType() == PieceType.BLACK && targetRow == 0) {
            piece.setKing(true);
            return true;
        }

        if (piece.getPieceType() == PieceType.RED && targetRow == 7) {
            piece.setKing(true);
            return true;
        }

        return false;
    }

        private boolean isMoveFinished(Move move){
        List<Move> possibleMoves = generateMoves(getCurrentPlayer());
        possibleMoves = possibleMoves.stream()
                .filter(Move::isCapturingMove)
                .filter(nextMove -> nextMove.getSourceRow() == move.getTargetRow() && nextMove.getSourceCol() == move.getTargetCol())
                .collect(Collectors.toList());

        if(possibleMoves.isEmpty()){
            return true;
        }

        return false;
    }

}
