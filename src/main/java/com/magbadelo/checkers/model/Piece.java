package com.magbadelo.checkers.model;

public class Piece {
    private int currentRow;
    private int currentCol;
    private boolean isKing;
    private final PieceType pieceType;

    public Piece(int currentRow, int currentCol, boolean isKing, PieceType pieceType) {
        this.currentRow = currentRow;
        this.currentCol = currentCol;
        this.isKing = isKing;
        this.pieceType = pieceType;
    }

    public boolean isKing() {
        return isKing;
    }

    public int getCurrentCol() {
        return currentCol;
    }

    public int getCurrentRow() {
        return currentRow;
    }

    public PieceType getPieceType() {
        return pieceType;
    }

    public void setCurrentCol(int currentCol) {
        this.currentCol = currentCol;
    }

    public void setCurrentRow(int currentRow) {
        this.currentRow = currentRow;
    }

    public void crown() {
        isKing = true;
    }
}

