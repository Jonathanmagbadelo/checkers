package com.magbadelo.checkers.controller;

import com.magbadelo.checkers.model.Move;
import com.magbadelo.checkers.view.TileView;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TileController {

    private GridPane board;

    @Autowired
    public TileController(GridPane board){
        this.board = board;
    }

    public void showPossibleMoveTileViews(List<Move> possibleMoves) {
        possibleMoves.forEach(move -> {
            TileView tileView = getTileView(move.getTargetRow(), move.getTargetCol());
            Rectangle tile = ((Rectangle) tileView.getChildren().get(0));
            tile.setFill(Color.GREENYELLOW);
            tile.setOpacity(70);
        });
    }

    public void resetTileViewColors() {
        board.getChildren().stream()
                .filter(node -> node instanceof TileView)
                .filter(node -> ((Rectangle) (((TileView) node).getChildren().get(0))).getFill().equals(Color.GREENYELLOW))
                .forEach(node -> ((Rectangle) (((TileView) node).getChildren().get(0))).setFill(Color.web("A85D5D")));
    }

    public TileView getTileView(int row, int col) {
        int index = (row * 8) + col;
        return (TileView) board.getChildren().get(index);
    }
}
