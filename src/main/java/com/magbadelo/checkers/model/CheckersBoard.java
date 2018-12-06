package com.magbadelo.checkers.model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Where most of the game logic occurs, this class takes care of validating moves, generating moves,
 * checking if a game is over, and initialising the checkerboard.
 */
@Component
public class CheckersBoard {
    private Player humanPlayer;
    private Player aiPlayer;
    private Player currentPlayer;
    private int numRows = 8;
    private int numCols = 8;
    private CheckersState currentCheckersState;

    /**
     * Instantiates a new Checkers board.
     */
    @Autowired
    public CheckersBoard() {
        this.aiPlayer = new Player(true);
        this.humanPlayer = new Player(false);
        this.currentPlayer = getHumanPlayer();
        this.currentCheckersState = new CheckersState(numRows, numCols);
        initialise();
        currentCheckersState.updateCurrentPieces();
    }

    /**
     * Gets human player.
     *
     * @return the human player
     */
    public Player getHumanPlayer() {
        return humanPlayer;
    }

    /**
     * Gets ai player.
     *
     * @return the ai player
     */
    public Player getAiPlayer() {
        return aiPlayer;
    }

    /**
     * Switch current player.
     */
    public void switchCurrentPlayer() {
        this.currentPlayer = getNextPlayer();
    }

    /**
     * Gets next player.
     *
     * @return the next player
     */
    public Player getNextPlayer() {
        return currentPlayer.isAIPlayer() ? getHumanPlayer() : getAiPlayer();
    }

    /**
     * Gets current player.
     *
     * @return the current player
     */
    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    /**
     * Gets current checkers state.
     *
     * @return the current checkers state
     */
    public CheckersState getCurrentCheckersState() {
        return currentCheckersState;
    }

    /**
     * Reset.
     */
    public void reset(){
        currentPlayer = getHumanPlayer();
        currentCheckersState = new CheckersState(numRows, numCols);
        initialise();
        currentCheckersState.updateCurrentPieces();
    }

    private void initialise() {
        for (int row = 0; row < numRows; row++) {
            for (int col = 0; col < numCols; col++) {
                boolean isLightTile = (row + col) % 2 == 0;
                Piece piece = makePiece(isLightTile, row, col);
                if (piece != null) {
                    currentCheckersState.setPiece(row, col, piece);
                }
            }
        }
    }

    private Piece makePiece(boolean isLightTile, int row, int col) {
        if (row <= 2 && !isLightTile) {
            return new Piece(false, PieceType.RED, row, col);
        }

        if (row >= 5 && !isLightTile) {
            return new Piece(false, PieceType.BLACK, row, col);
        }

        return null;
    }

    /**
     * Is move valid boolean.
     *
     * @param move          the move
     * @param checkersState the checkers state
     * @return the boolean
     */
    public boolean isMoveValid(Move move, CheckersState checkersState) {
        Piece piece = checkersState.getPiece(move.getSourceRow(), move.getSourceCol());
        if (!isLightTile(move.getTargetRow(), move.getTargetCol())) {

            if (Math.abs(move.getTargetCol() - move.getSourceCol()) == 1 && ((move.getTargetRow() - move.getSourceRow() == piece.getPieceType().getMoveDir()) || piece.isKing())) {
                if (!checkersState.hasPiece(move.getTargetRow(), move.getTargetCol())) {
                    move.setCrowningMove(isCrowningMove(piece, move.getTargetRow()));
                    return true;
                }

            }

            if (Math.abs(move.getTargetCol() - move.getSourceCol()) == 2 && ((move.getTargetRow() - move.getSourceRow() == piece.getPieceType().getMoveDir() * 2) || piece.isKing())) {
                if (checkersState.hasPiece(move.getMiddleRow(), move.getMiddleCol()) && checkersState.getPiece(move.getMiddleRow(), move.getMiddleCol()).getPieceType() != piece.getPieceType()) {
                    if (!checkersState.hasPiece(move.getTargetRow(), move.getTargetCol())) {
                        move.setCapturingMove();
                        move.setCrowningMove(isCrowningMove(piece, move.getTargetRow()));
                        return true;
                    }
                }
            }

            setInvalidMoveReason(move);

        } else {
            move.setInvalidReason("You can't move to a light tile");
        }

        return false;
    }

    private void setInvalidMoveReason(Move move) {
        if (move.getTargetRow() - move.getSourceRow() > -1) {
            move.setInvalidReason("Pawns can only move forward");
        } else if (currentCheckersState.hasPiece(move.getTargetRow(), move.getTargetCol())) {
            move.setInvalidReason(String.format("There is already a piece at %d,%d", move.getTargetRow(), move.getTargetCol()));
        } else if(move.getTargetRow() - move.getSourceRow() < -1) {
            move.setInvalidReason("Can't move more than one tile, \n unless its a capture move!");
        }
    }

    private boolean isLightTile(int row, int col) {
        if (row % 2 == 0) {
            return col % 2 == 0;
        }
        return col % 2 != 0;
    }

    /**
     * Generate moves list.
     *
     * @param player        the player
     * @param checkersState the checkers state
     * @return the list
     */
//successor function
    public List<Move> generateMoves(Player player, CheckersState checkersState) {
        int direction = player.getPieceType().getMoveDir();
        List<Move> everyMove = new ArrayList<>();
        for (int row = 0; row < numRows; row++) {
            for (int col = 0; col < numCols; col++) {
                if (checkersState.hasPiece(row, col) && checkersState.getPiece(row, col).getPieceType() == player.getPieceType()) {
                    everyMove.add(new Move(row, col, (row + direction), (col + 1)));
                    everyMove.add(new Move(row, col, (row + direction), (col - 1)));
                    everyMove.add(new Move(row, col, (row + (direction * 2)), (col + 2)));
                    everyMove.add(new Move(row, col, (row + (direction * 2)), (col - 2)));
                    if (checkersState.getPiece(row, col).isKing()) {
                        //Kings can move in backwards
                        everyMove.add(new Move(row, col, (row - direction), (col + 1)));
                        everyMove.add(new Move(row, col, (row - direction), (col - 1)));
                        everyMove.add(new Move(row, col, (row - (direction * 2)), (col + 2)));
                        everyMove.add(new Move(row, col, (row - (direction * 2)), (col - 2)));
                    }
                }
            }
        }
        List<Move> possibleMoves = everyMove.stream()
                .filter(move -> move.getTargetRow() >= 0 && move.getTargetRow() < numRows)
                .filter(move -> move.getTargetCol() >= 0 && move.getTargetCol() < numCols)
                .filter(move -> isMoveValid(move, checkersState))
                .collect(Collectors.toList());
        return possibleMoves;
    }

    /**
     * Complete move.
     *
     * @param move          the move
     * @param checkersState the checkers state
     */
    public void completeMove(Move move, CheckersState checkersState) {
        Piece piece = checkersState.getPiece(move.getSourceRow(), move.getSourceCol());
        checkersState.setPiece(move.getTargetRow(), move.getTargetCol(), piece);
        checkersState.setPiece(move.getSourceRow(), move.getSourceCol(), null);
        if (checkersState.hasPiece(move.getMiddleRow(), move.getMiddleCol()) && checkersState.getPiece(move.getMiddleRow(), move.getMiddleCol()).getPieceType() != piece.getPieceType()) {
            checkersState.setPiece(move.getMiddleRow(), move.getMiddleCol(), null);
        }
        if (move.isCrowningMove()) {
            piece.crown();
        }
        checkersState.updateCurrentPieces();
    }

    /**
     * Is crowning move boolean.
     *
     * @param piece     the piece
     * @param targetRow the target row
     * @return the boolean
     */
    public boolean isCrowningMove(Piece piece, int targetRow) {
        if (!piece.isKing()) {
            if (piece.getPieceType() == PieceType.BLACK && targetRow == 0) {
                return true;
            }

            if (piece.getPieceType() == PieceType.RED && targetRow == 7) {
                return true;
            }
        }
        return false;
    }

    /**
     * Gets possible jump moves.
     *
     * @param move          the move
     * @param checkersState the checkers state
     * @return the possible jump moves
     */
    public List<Move> getPossibleJumpMoves(Move move, CheckersState checkersState) {
        List<Move> possibleMoves = generateMoves(getCurrentPlayer(), checkersState);
        possibleMoves = possibleMoves.stream()
                .filter(Move::isCapturingMove)
                .filter(nextMove -> nextMove.getSourceRow() == move.getTargetRow() && nextMove.getSourceCol() == move.getTargetCol())
                .collect(Collectors.toList());

        if (possibleMoves.isEmpty()) {
            return null;
        }

        return possibleMoves;
    }

    /**
     * Is game over boolean.
     *
     * @return the boolean
     */
    public boolean isGameOver() {
        //checks if pieces are gone
        List<Move> possibleMoves = generateMoves(getNextPlayer(), getCurrentCheckersState());
        return getCurrentCheckersState().isGameOver() || Objects.isNull(possibleMoves);
    }

}
