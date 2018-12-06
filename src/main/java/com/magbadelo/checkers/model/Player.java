package com.magbadelo.checkers.model;

/**
 * The type Player.
 */
public class Player {
    private final boolean isAIPlayer;
    private PieceType pieceType;

    /**
     * Instantiates a new Player.
     *
     * @param isAIPlayer the is ai player
     */
    public Player(boolean isAIPlayer) {
        this.isAIPlayer = isAIPlayer;
    }

    /**
     * Is ai player boolean.
     *
     * @return the boolean
     */
    public boolean isAIPlayer() {
        return isAIPlayer;
    }

    /**
     * Gets piece type.
     *
     * @return the piece type
     */
    public PieceType getPieceType() {
        return pieceType;
    }

    /**
     * Sets piece type.
     *
     * @param pieceType the piece type
     */
    public void setPieceType(PieceType pieceType) {
        this.pieceType = pieceType;
    }
}
