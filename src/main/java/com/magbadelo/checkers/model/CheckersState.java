package com.magbadelo.checkers.model;

import java.util.ArrayList;

public class CheckersState {
    private Piece[][] boardState;
    private int stateEvaluation;
    private Player currentPlayer;
    private ArrayList<CheckersState> possibleStates;

    public CheckersState(int rows, int cols, Player currentPlayer) {
        this.boardState = new Piece[rows][cols];
        this.currentPlayer = currentPlayer;
        this.possibleStates = new ArrayList<>();
    }

    public Piece[][] getBoardState() {
        return boardState;
    }

    public void setPiece(int row, int col, Piece piece) {
        boardState[row][col] = piece;
    }

    public boolean hasPiece(int row, int col) {
        return boardState[row][col] != null;
    }

    public Piece getPiece(int row, int col) {
        return boardState[row][col];
    }

    public void setStateEvaluation() {
        int blackPieceCount = 0;
        int redPieceCount = 0;

        for (int row = 0; row < boardState.length; row++) {
            for (int col = 0; col < boardState.length; col++) {
                if (hasPiece(row, col)) {
                    Piece piece = getPiece(row, col);
                    if (piece.getPieceType() == PieceType.RED) {
                        redPieceCount += piece.isKing() ? 3 : 1;
                    } else {
                        blackPieceCount += piece.isKing() ? 3 : 1;
                    }
                }
            }
        }

        if (currentPlayer.getPieceType() == PieceType.BLACK) {
            redPieceCount = redPieceCount * -1;
        } else {
            blackPieceCount = blackPieceCount * -1;
        }

        stateEvaluation = redPieceCount + blackPieceCount;
    }

    public int getStateEvaluation() {
        setStateEvaluation();
        return stateEvaluation;
    }

    public boolean isGameOver() {
        return false;
    }

    public ArrayList<CheckersState> getPossibleStates() {
        return possibleStates;
    }

}
