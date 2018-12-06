package com.magbadelo.checkers.model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class CheckersBoard {
    private Player humanPlayer;
    private Player aiPlayer;
    private Player currentPlayer;

    @Value("${checkerboard.num.rows}")
    private int numRows = 8;

    @Value("${checkerboard.num.cols}")
    private int numCols = 8;

    private CheckersState currentCheckersState;

    @Autowired
    public CheckersBoard() {
        this.aiPlayer = new Player(true);
        this.humanPlayer = new Player(false);
        this.currentPlayer = getHumanPlayer();
        this.currentCheckersState = new CheckersState(numRows, numCols);
        initialise();
        currentCheckersState.updateCurrentPieces();
    }

    public Player getHumanPlayer() {
        return humanPlayer;
    }

    public Player getAiPlayer() {
        return aiPlayer;
    }

    public void switchCurrentPlayer() {
        this.currentPlayer = getNextPlayer();
    }

    public Player getNextPlayer() {
        return currentPlayer.isAIPlayer() ? getHumanPlayer() : getAiPlayer();
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public CheckersState getCurrentCheckersState() {
        return currentCheckersState;
    }

    public void reset(){
        currentPlayer = getHumanPlayer();
        this.currentCheckersState = new CheckersState(numRows, numCols);
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

    public boolean isGameOver() {
        //checks if pieces are gone
        List<Move> possibleMoves = generateMoves(getNextPlayer(), getCurrentCheckersState());
        return getCurrentCheckersState().isGameOver() || Objects.isNull(possibleMoves);
    }

}
