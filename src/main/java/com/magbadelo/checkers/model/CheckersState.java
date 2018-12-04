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
                boardState[row][col] = new Piece(checkersState.getPiece(row, col).isKing(), checkersState.getPiece(row, col).getPieceType());
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

    public int getStateEvaluation(Player currentPlayer){
        if(!isEndGame()){
            return normalStateEvalutation(currentPlayer);
        }

        return endGameStateEvaluation(currentPlayer);

    }

    private int normalStateEvalutation(Player currentPlayer){
        int score = 0;
        for(int row = 0; row < 8; row++){
            for(int col = 0; col < 8; col++){
                if(hasPiece(row, col) && getPiece(row, col).getPieceType().equals(currentPlayer.getPieceType())){
                    if(getPiece(row, col).isKing()){
                        score += 10;
                    }else{
                        if(row > 3){
                            score += 7;
                        } else{
                            score +=5;
                        }
                    }
                }
            }
        }
        return score;
    }

    //TODO
    private int endGameStateEvaluation(Player currentPlayer){
        return 0;
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

    public boolean isEndGame(){
        updateCurrentPieces();
        long blackKingCount = currentBlackPieces.stream().filter(Piece::isKing).count();
        long redKingCount = currentRedPieces.stream().filter(Piece::isKing).count();
        return blackKingCount == currentBlackPieces.size() && redKingCount == currentRedPieces.size();
    }

}
