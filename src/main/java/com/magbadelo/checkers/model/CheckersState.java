package com.magbadelo.checkers.model;

import java.util.ArrayList;
import java.util.List;

public class CheckersState {
    private Piece[][] boardState;
    private int stateEvaluation;
    private Player currentPlayer;
    private ArrayList<CheckersState> possibleStates;
    private List<Piece> currentBlackPieces;
    private List<Piece> currentRedPieces;

    public CheckersState(int rows, int cols, Player currentPlayer) {
        this.boardState = new Piece[rows][cols];
        this.currentPlayer = currentPlayer;
        this.possibleStates = new ArrayList<>();
        this.currentBlackPieces = new ArrayList<>();
        this.currentRedPieces = new ArrayList<>();
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

    private void setStateEvaluation() {
        int blackPiecesScore = 0;
        int redPiecesScore = 0;

        for(Piece piece : currentBlackPieces){
            blackPiecesScore += piece.isKing() ? 3 : 1;
        }

        for(Piece piece : currentRedPieces){
            redPiecesScore += piece.isKing() ? 3 : 1;
        }


        if (currentPlayer.getPieceType() == PieceType.BLACK) {
            redPiecesScore = redPiecesScore * -1;
        } else {
            blackPiecesScore = blackPiecesScore * -1;
        }

        stateEvaluation = redPiecesScore + blackPiecesScore;
    }

    public int getStateEvaluation() {
        setStateEvaluation();
        return stateEvaluation;
    }

    public boolean isGameOver() {
        updateCurrentPieces();
        return currentRedPieces.size() == 0 || currentBlackPieces.size() == 0;
    }

    public void updateCurrentPieces(){
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

    public ArrayList<CheckersState> getPossibleStates() {
        return possibleStates;
    }

}
