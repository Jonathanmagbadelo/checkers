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

    @Autowired
    public MinMax(CheckersBoard checkersBoard){
        this.checkersBoard = checkersBoard;
        this.maxPlayer = checkersBoard.getAiPlayer();
        this.minPlayer = checkersBoard.getHumanPlayer();
    }

    public int minimax(CheckersState currentState, int depth, boolean isMaxPlayer) {
        if (depth == 0 || currentState.isGameOver()) {
            return currentState.getStateEvaluation();
        }

        currentState.setChildStates(generateChildStates(currentState));

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

    public void generateGameTree(){

    }

    public ArrayList<CheckersState> generateChildStates(CheckersState checkersState, boolean isMaxPlayer){
        List<Move> possibleMoves = checkersBoard.generateMoves(isMaxPlayer ? maxPlayer : minPlayer, checkersState);



        return null;
    }
}
