package com.magbadelo.checkers.model;

/**
 * The Piece class represents a checkers piece.
 */
public class Piece {
    private boolean isKing;
    private final PieceType pieceType;
    private int row;
    private int col;

    /**
     * Instantiates a new Piece.
     *
     * @param isKing    the is king
     * @param pieceType the piece type
     * @param row       the row
     * @param col       the col
     */
    public Piece(boolean isKing, PieceType pieceType, int row, int col) {
        this.isKing = isKing;
        this.pieceType = pieceType;
        this.row = row;
        this.col = col;
    }

    /**
     * Is king boolean.
     *
     * @return the boolean
     */
    public boolean isKing() {
        return isKing;
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
     * Crown.
     */
    public void crown() {
        isKing = true;
    }

    /**
     * Gets row.
     *
     * @return the row
     */
    public int getRow() {
        return row;
    }

    /**
     * Gets col.
     *
     * @return the col
     */
    public int getCol() {
        return col;
    }

    /**
     * Sets row.
     *
     * @param row the row
     */
    public void setRow(int row) {
        this.row = row;
    }

    /**
     * Sets col.
     *
     * @param col the col
     */
    public void setCol(int col) {
        this.col = col;
    }
}

