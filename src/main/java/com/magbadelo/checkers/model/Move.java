package com.magbadelo.checkers.model;

import java.util.List;

/**
 * The type Move.
 */
public class Move {

    private int sourceRow;
    private int sourceCol;
    private int targetRow;
    private int targetCol;
    private boolean isCapturingMove;
    private boolean isCrowningMove;
    private List<Move> possibleJumpMoves;
    private String invalidReason;

    /**
     * Instantiates a new Move.
     *
     * @param sourceRow the source row
     * @param sourceCol the source col
     * @param targetRow the target row
     * @param targetCol the target col
     */
    public Move(int sourceRow, int sourceCol, int targetRow, int targetCol){
        this.sourceRow = sourceRow;
        this.sourceCol = sourceCol;
        this.targetRow = targetRow;
        this.targetCol = targetCol;
        this.isCapturingMove = false;
        this.isCrowningMove = false;
    }

    /**
     * Gets source row.
     *
     * @return the source row
     */
    public int getSourceRow() {
        return sourceRow;
    }

    /**
     * Gets source col.
     *
     * @return the source col
     */
    public int getSourceCol() {
        return sourceCol;
    }

    /**
     * Gets target row.
     *
     * @return the target row
     */
    public int getTargetRow() {
        return targetRow;
    }

    /**
     * Gets target col.
     *
     * @return the target col
     */
    public int getTargetCol() {
        return targetCol;
    }

    /**
     * Gets middle row.
     *
     * @return the middle row
     */
    public int getMiddleRow() {
        return (getSourceRow() + getTargetRow())/2;
    }

    /**
     * Gets middle col.
     *
     * @return the middle col
     */
    public int getMiddleCol() {
        return (getSourceCol() + getTargetCol())/2;
    }

    /**
     * Is capturing move boolean.
     *
     * @return the boolean
     */
    public boolean isCapturingMove() {
        return isCapturingMove;
    }

    /**
     * Is crowning move boolean.
     *
     * @return the boolean
     */
    public boolean isCrowningMove() {
        return isCrowningMove;
    }

    /**
     * Set capturing move.
     */
    public void setCapturingMove(){
        isCapturingMove = true;
    }

    /**
     * Sets crowning move.
     *
     * @param crowningMove the crowning move
     */
    public void setCrowningMove(boolean crowningMove) {
        isCrowningMove = crowningMove;
    }

    /**
     * Sets possible jump moves.
     *
     * @param possibleJumpMoves the possible jump moves
     */
    public void setPossibleJumpMoves(List<Move> possibleJumpMoves) {
        this.possibleJumpMoves = possibleJumpMoves;
    }

    /**
     * Gets possible jump moves.
     *
     * @return the possible jump moves
     */
    public List<Move> getPossibleJumpMoves() {
        return possibleJumpMoves;
    }

    /**
     * Has possible jump moves boolean.
     *
     * @return the boolean
     */
    public boolean hasPossibleJumpMoves() {
        return possibleJumpMoves != null;
    }

    /**
     * Gets invalid reason.
     *
     * @return the invalid reason
     */
    public String getInvalidReason() {
        return invalidReason;
    }

    /**
     * Sets invalid reason.
     *
     * @param invalidReason the invalid reason
     */
    public void setInvalidReason(String invalidReason) {
        this.invalidReason = invalidReason;
    }
}
