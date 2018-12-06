package com.magbadelo.checkers.model;

/**
 * The enum Piece type.
 */
public enum PieceType {
    /**
     * Red piece type.
     */
    RED(1), /**
     * Black piece type.
     */
    BLACK(-1);

    private final int moveDir;

    PieceType(int moveDir) {
        this.moveDir = moveDir;
    }

    /**
     * Gets move dir.
     *
     * @return the move dir
     */
    public int getMoveDir() {
        return moveDir;
    }

    @Override
    public String toString(){
        return moveDir == 1 ? "Red" : "Black";
    }

    /**
     * Get color string.
     *
     * @return the string
     */
    public String getColor(){
        return moveDir == 1 ? "ED5564" : "434A54";
    }
}
