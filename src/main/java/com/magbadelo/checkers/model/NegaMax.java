package com.magbadelo.checkers.model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * The type Nega max.
 */
@Component
public class NegaMax {

    private CheckersBoard checkersBoard;
    private Player maxPlayer;
    private Player minPlayer;

    /**
     * Instantiates a new Nega max.
     *
     * @param checkersBoard the checkers board
     */
    @Autowired
    public NegaMax(CheckersBoard checkersBoard) {
        this.checkersBoard = checkersBoard;
        this.maxPlayer = checkersBoard.getAiPlayer();
        this.minPlayer = checkersBoard.getHumanPlayer();
    }

    //A helper function to help generate child checkerstates from a given checkerstate, this is done in order to create the game tree required for negamax
    private ArrayList<CheckersState> generateChildStates(CheckersState checkersState, boolean isMaxPlayer) {
        //get all possible moves from a given checkerstate and player
        List<Move> possibleMoves = checkersBoard.generateMoves(isMaxPlayer ? maxPlayer : minPlayer, checkersState);
        ArrayList<CheckersState> childStates = new ArrayList<>();

        //generates the checkerstates for all the possible moves
        for (Move move : possibleMoves) {

            if (!move.isCapturingMove()) {
                CheckersState checkerStateCopy = new CheckersState(checkersState);
                checkersBoard.completeMove(move, checkerStateCopy);
                childStates.add(checkerStateCopy);
            } else {
                CheckersState checkerStateCopy = new CheckersState(checkersState);
                checkersBoard.completeMove(move, checkerStateCopy);
                move.setPossibleJumpMoves(checkersBoard.getPossibleJumpMoves(move, checkerStateCopy));
                if (!move.hasPossibleJumpMoves()) {
                    childStates.add(checkerStateCopy);
                } else {
                    traverseJumpMoves(move, checkerStateCopy, childStates);
                }
            }
        }
        return childStates;
    }

    //jump moves can happen multiple times within a single time, this function helps to create the checkertates from those jump moves recursively
    private void traverseJumpMoves(Move move, CheckersState checkersState, ArrayList<CheckersState> childStates) {
        if (!move.hasPossibleJumpMoves()) {
            childStates.add(checkersState);
        } else {
            for (Move jumpMove : move.getPossibleJumpMoves()) {
                CheckersState checkerStateCopy = new CheckersState(checkersState);
                checkersBoard.completeMove(jumpMove, checkerStateCopy);
                traverseJumpMoves(jumpMove, checkerStateCopy, childStates);
            }
        }
    }

    /**
     * Nega max double.
     *
     * @param checkersState the checkers state
     * @param depth         the depth
     * @param isAIPlayer    the is ai player
     * @param alpha         the alpha
     * @param beta          the beta
     * @return the double
     */
    public double negaMax(CheckersState checkersState, int depth, boolean isAIPlayer, double alpha, double beta) {
        if (depth == 0 || checkersState.isGameOver()) {
            return checkersState.getStateEvaluation(isAIPlayer ? maxPlayer : minPlayer);
        }
        CheckersState checkersStateCopy = new CheckersState(checkersState);
        ArrayList<CheckersState> childStates = generateChildStates(checkersStateCopy, isAIPlayer);
        double eval = Integer.MIN_VALUE;
        for (CheckersState childState : childStates) {
            CheckersState childStateCopy = new CheckersState(childState);
            eval = Math.max(eval, -negaMax(childStateCopy, depth - 1, !isAIPlayer, -beta, -alpha));
            alpha = Math.max(eval, alpha);
            if (alpha > beta)
                break;
        }
        return eval;
    }
}
