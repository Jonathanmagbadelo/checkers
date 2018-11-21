package com.magbadelo.checkers.model;

public class Move {

    private int sourceRow;
    private int sourceCol;
    private int targetRow;
    private int targetCol;
    private boolean isCapturingMove;
    private boolean isCrowningMove;

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
}
