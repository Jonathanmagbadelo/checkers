package com.magbadelo.checkers.model;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

/**
 * The Checkers state class represents the checkerboard at a given point in time.
 */
public class CheckersState {
    private Piece[][] boardState;
    private List<Piece> currentBlackPieces;
    private List<Piece> currentRedPieces;

    /**
     * Instantiates a new Checkers state.
     *
     * @param rows the rows
     * @param cols the cols
     */
    public CheckersState(int rows, int cols) {
        this.boardState = new Piece[rows][cols];
        this.currentBlackPieces = new ArrayList<>();
        this.currentRedPieces = new ArrayList<>();
    }

    /**
     * Instantiates a new Checkers state.
     *
     * @param checkersState the checkers state
     */
//Copy Constructor
    public CheckersState(CheckersState checkersState) {
        int length = checkersState.getBoardState().length;
        this.boardState = new Piece[length][length];
        IntStream.range(0, length).forEach(row -> IntStream.range(0, length).forEach(col -> {
            if (checkersState.hasPiece(row, col)) {
                boardState[row][col] = new Piece(checkersState.getPiece(row, col).isKing(),
                        checkersState.getPiece(row, col).getPieceType(),
                        checkersState.getPiece(row, col).getRow(),
                        checkersState.getPiece(row, col).getCol());
            }
        }));
        this.currentBlackPieces = new ArrayList<>();
        this.currentRedPieces = new ArrayList<>();
        updateCurrentPieces();
    }

    private Piece[][] getBoardState() {
        return boardState;
    }

    /**
     * Sets piece.
     *
     * @param row   the row
     * @param col   the col
     * @param piece the piece
     */
    public void setPiece(int row, int col, Piece piece) {
        boardState[row][col] = piece;
        if (piece != null) {
            piece.setRow(row);
            piece.setCol(col);
        }

    }

    /**
     * Has piece boolean.
     *
     * @param row the row
     * @param col the col
     * @return the boolean
     */
    public boolean hasPiece(int row, int col) {
        return boardState[row][col] != null;
    }

    /**
     * Gets piece.
     *
     * @param row the row
     * @param col the col
     * @return the piece
     */
    public Piece getPiece(int row, int col) {
        return boardState[row][col];
    }

    /**
     * Gets state evaluation.
     *
     * @param currentPlayer the current player
     * @return the state evaluation
     */
    public double getStateEvaluation(Player currentPlayer) {
        if (!isEndGame()) {
            return normalStateEvaluation(currentPlayer);
        }

        return endGameStateEvaluation(currentPlayer);

    }

    private double normalStateEvaluation(Player currentPlayer) {
        int score = 0;
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                if (hasPiece(row, col) && getPiece(row, col).getPieceType().equals(currentPlayer.getPieceType())) {
                    if (getPiece(row, col).isKing()) {
                        score += 10;
                    } else {
                        if (row > 3) {
                            score += 7;
                        } else {
                            score += 5;
                        }
                    }
                }
            }
        }
        return (double) score;
    }

    private double endGameStateEvaluation(Player currentPlayer) {
        List<Piece> currentPlayerPieces = currentPlayer.getPieceType().toString().equals("Red") ? currentRedPieces : currentBlackPieces;
        List<Piece> opponentPieces = currentPlayerPieces == currentRedPieces ? currentBlackPieces : currentRedPieces;
        double totalDistance = 0;
        for (Piece piece : currentPlayerPieces) {
            if (piece.isKing()) {
                for (Piece opponentPiece : opponentPieces) {
                    totalDistance += Math.hypot(piece.getRow() - opponentPiece.getRow(), piece.getCol() - opponentPiece.getCol());
                }
            }
        }
        return currentPlayerPieces.size() > opponentPieces.size() ?  (1000.0 * (1.0 / totalDistance)) :  totalDistance;
    }

    /**
     * Is game over boolean.
     *
     * @return the boolean
     */
    public boolean isGameOver() {
        updateCurrentPieces();
        return currentRedPieces.size() == 0 || currentBlackPieces.size() == 0;
    }

    /**
     * Update current pieces.
     */
    public void updateCurrentPieces() {
        currentRedPieces.clear();
        currentBlackPieces.clear();
        for (int row = 0; row < boardState.length; row++) {
            for (int col = 0; col < boardState.length; col++) {
                if (hasPiece(row, col)) {
                    Piece piece = getPiece(row, col);
                    if (piece.getPieceType() == PieceType.RED) {
                        currentRedPieces.add(piece);
                    } else {
                        currentBlackPieces.add(piece);
                    }
                }
            }
        }
    }

    private boolean isEndGame() {
        updateCurrentPieces();
        return currentBlackPieces.size() + currentRedPieces.size() <= 15;
    }
}
