package com.magbadelo.checkers.model;

import java.util.List;

public class Move {

    private int sourceRow;
    private int sourceCol;
    private int targetRow;
    private int targetCol;
    private boolean isCapturingMove;
    private boolean isCrowningMove;
    private List<Move> possibleJumpMoves;
    private String invalidReason;

    public Move(int sourceRow, int sourceCol, int targetRow, int targetCol){
        this.sourceRow = sourceRow;
        this.sourceCol = sourceCol;
        this.targetRow = targetRow;
        this.targetCol = targetCol;
        this.isCapturingMove = false;
        this.isCrowningMove = false;
    }

    public int getSourceRow() {
        return sourceRow;
    }

    public int getSourceCol() {
        return sourceCol;
    }

    public int getTargetRow() {
        return targetRow;
    }

    public int getTargetCol() {
        return targetCol;
    }

    public int getMiddleRow() {
        return (getSourceRow() + getTargetRow())/2;
    }

    public int getMiddleCol() {
        return (getSourceCol() + getTargetCol())/2;
    }

    public boolean isCapturingMove() {
        return isCapturingMove;
    }

    public boolean isCrowningMove() {
        return isCrowningMove;
    }

    public void setCapturingMove(){
        isCapturingMove = true;
    }

    public void setCrowningMove(boolean crowningMove) {
        isCrowningMove = crowningMove;
    }

    public void setPossibleJumpMoves(List<Move> possibleJumpMoves) {
        this.possibleJumpMoves = possibleJumpMoves;
    }

    public List<Move> getPossibleJumpMoves() {
        return possibleJumpMoves;
    }

    public boolean hasPossibleJumpMoves() {
        return possibleJumpMoves != null;
    }

    public String getInvalidReason() {
        return invalidReason;
    }

    public void setInvalidReason(String invalidReason) {
        this.invalidReason = invalidReason;
    }
}
