package com.magbadelo.checkers.model;

public class Piece {
    private boolean isKing;
    private final PieceType pieceType;

    public Piece(boolean isKing, PieceType pieceType) {
        this.isKing = isKing;
        this.pieceType = pieceType;
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
}

