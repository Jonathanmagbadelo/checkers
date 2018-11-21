package com.magbadelo.checkers.model;

public class Player {
    private final boolean isAIPlayer;
    private PieceType pieceType;

    public Player(boolean isAIPlayer) {
        this.isAIPlayer = isAIPlayer;
    }

    public boolean isAIPlayer() {
        return isAIPlayer;
    }

    public PieceType getPieceType() {
        return pieceType;
    }

    public void setPieceType(PieceType pieceType) {
        this.pieceType = pieceType;
    }
}
