package com.magbadelo.checkers.model;

public class Piece {
    private boolean isKing;
    private final PieceType pieceType;
    private int row;
    private int col;

    public Piece(boolean isKing, PieceType pieceType, int row, int col) {
        this.isKing = isKing;
        this.pieceType = pieceType;
        this.row = row;
        this.col = col;
    }

    public boolean isKing() {
        return isKing;
    }

    public PieceType getPieceType() {
        return pieceType;
    }

    public void crown() {
        isKing = true;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public void setCol(int col) {
        this.col = col;
    }
}

