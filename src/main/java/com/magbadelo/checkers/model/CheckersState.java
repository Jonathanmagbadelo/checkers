package com.magbadelo.checkers.model;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class CheckersState {
    private Piece[][] boardState;
    private ArrayList<CheckersState> childStates;
    private List<Piece> currentBlackPieces;
    private List<Piece> currentRedPieces;

    public CheckersState(int rows, int cols) {
        this.boardState = new Piece[rows][cols];
        this.childStates = new ArrayList<>();
        this.currentBlackPieces = new ArrayList<>();
        this.currentRedPieces = new ArrayList<>();
    }

    //Copy Constructor
    public CheckersState(CheckersState checkersState){
        int length  = checkersState.getBoardState().length;
        this.boardState = new Piece[length][length];
        IntStream.range(0, length).forEach(row -> IntStream.range(0, length).forEach(col -> {
            if(checkersState.hasPiece(row, col)){
                boardState[row][col] = checkersState.getPiece(row, col);
            }
        }));
        this.childStates = new ArrayList<>();
        this.currentBlackPieces = new ArrayList<>();
        this.currentRedPieces = new ArrayList<>();
        updateCurrentPieces();
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

    public int getStateEvaluation(Player currentPlayer) {
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

        return redPiecesScore + blackPiecesScore;
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

    public ArrayList<CheckersState> getChildStates() {
        return childStates;
    }

    public void setChildStates(ArrayList<CheckersState> childStates){
        this.childStates = childStates;
    }

}
