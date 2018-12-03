package com.magbadelo.checkers.model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MinMax {

    private CheckersBoard checkersBoard;

    @Autowired
    public MinMax(CheckersBoard checkersBoard){
        this.checkersBoard = checkersBoard;
    }

    public int minimax(CheckersState currentState, int depth, boolean isMaxPlayer) {
        if (depth == 0 || currentState.isGameOver()) {
            return currentState.getStateEvaluation();
        }

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

    public void generateChildStates(CheckersState checkersState){

    }
}
