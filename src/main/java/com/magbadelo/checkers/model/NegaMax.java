package com.magbadelo.checkers.model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class NegaMax {

    private CheckersBoard checkersBoard;
    private Player maxPlayer;
    private Player minPlayer;

    @Autowired
    public NegaMax(CheckersBoard checkersBoard) {
        this.checkersBoard = checkersBoard;
        this.maxPlayer = checkersBoard.getAiPlayer();
        this.minPlayer = checkersBoard.getHumanPlayer();
    }

    public ArrayList<CheckersState> generateChildStates(CheckersState checkersState, boolean isMaxPlayer) {
        List<Move> possibleMoves = checkersBoard.generateMoves(isMaxPlayer ? maxPlayer : minPlayer, checkersState);
        ArrayList<CheckersState> childStates = new ArrayList<>();

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

    public void traverseJumpMoves(Move move, CheckersState checkersState, ArrayList<CheckersState> childStates) {
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

    public int negaMax(CheckersState checkersState, int depth, boolean isMaxPlayer, int alpha, int beta) {
        if (depth == 0 || checkersState.isGameOver()) {
            return checkersState.getStateEvaluation(isMaxPlayer ? maxPlayer : minPlayer);
        }
        CheckersState checkersStateCopy = new CheckersState(checkersState);
        ArrayList<CheckersState> childStates = generateChildStates(checkersStateCopy, isMaxPlayer);
        for (CheckersState childState : childStates) {
            CheckersState childStateCopy = new CheckersState(childState);
            int eval = -negaMax(childStateCopy, depth - 1, !isMaxPlayer, -beta, -alpha);
            if (eval >= beta) {
                return eval;
            }
            if (eval > alpha) {
                alpha = eval;
            }
        }
        return alpha;
    }
}
