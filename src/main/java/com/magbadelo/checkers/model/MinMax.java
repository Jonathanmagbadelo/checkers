package com.magbadelo.checkers.model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class MinMax {

    private CheckersBoard checkersBoard;
    private Player maxPlayer;
    private Player minPlayer;
    private ArrayList<CheckersState> childStates = new ArrayList<>();
    private CheckersState root;

    @Autowired
    public MinMax(CheckersBoard checkersBoard){
        this.checkersBoard = checkersBoard;
        this.maxPlayer = checkersBoard.getAiPlayer();
        this.minPlayer = checkersBoard.getHumanPlayer();
    }

    public int minimax(CheckersState currentState, int depth, boolean isMaxPlayer) {
        if (depth == 0 || currentState.isGameOver()) {
            return currentState.getStateEvaluation(isMaxPlayer ? maxPlayer : minPlayer);
        }

        currentState.setChildStates(generateChildStates(currentState, isMaxPlayer));

        if (isMaxPlayer) {
            int maxEval = Integer.MIN_VALUE;
            for (CheckersState child : currentState.getChildStates()) {
                int eval = minimax(child, depth - 1, false);
                maxEval = Math.max(maxEval, eval);
            }
            return maxEval;
        } else {
            int minEval = Integer.MAX_VALUE;
            for (CheckersState child : currentState.getChildStates()) {
                int eval = minimax(child, depth - 1, true);
                minEval = Math.min(minEval, eval);
            }
            return minEval;
        }
    }

    public void generateGameTree(CheckersState checkersState){
        root = checkersState;
        root.setChildStates(generateChildStates(checkersState, true));
        for(CheckersState nextCheckerState : root.getChildStates()){
            CheckersState checkersStateCopy = new CheckersState(nextCheckerState);
            nextCheckerState.setChildStates(generateChildStates(checkersStateCopy, false));
        }
        System.out.println("Gang");
    }

    public ArrayList<CheckersState> generateChildStates(CheckersState checkersState, boolean isMaxPlayer){
        childStates.clear();

        List<Move> possibleMoves = checkersBoard.generateMoves(isMaxPlayer ? maxPlayer : minPlayer, checkersState);

        for(Move move : possibleMoves){

            if(!move.isCapturingMove()){
                CheckersState checkerStateCopy = new CheckersState(checkersState);
                checkersBoard.completeMove(move, checkerStateCopy);
                childStates.add(checkerStateCopy);
            } else {
                CheckersState checkerStateCopy = new CheckersState(checkersState);
                checkersBoard.completeMove(move, checkerStateCopy);
                move.setPossibleJumpMoves(checkersBoard.getPossibleJumpMoves(move, checkerStateCopy));
                if(!move.hasPossibleJumpMoves()){
                    childStates.add(checkerStateCopy);
                } else{
                    traverseJumpMoves(move, checkerStateCopy);
                }
            }

        }

        return childStates;
    }

    //move defo has possible jump moves
    public void traverseJumpMoves(Move move, CheckersState checkersState){
        if(!move.hasPossibleJumpMoves()){
            childStates.add(checkersState);
        } else{
            for(Move jumpMove : move.getPossibleJumpMoves()){
                CheckersState checkerStateCopy = new CheckersState(checkersState);
                checkersBoard.completeMove(jumpMove, checkerStateCopy);
                traverseJumpMoves(jumpMove, checkerStateCopy);
            }
        }
    }
}
