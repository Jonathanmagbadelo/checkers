package com.magbadelo.checkers.controller;

import com.magbadelo.checkers.model.Move;
import com.magbadelo.checkers.view.TileView;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * The type Tile controller.
 */
@Component
public class TileController {

    private GridPane board;

    /**
     * Instantiates a new Tile controller.
     *
     * @param board the board
     */
    @Autowired
    public TileController(GridPane board){
        this.board = board;
    }

    /**
     * Show possible move tile views.
     *
     * @param possibleMoves the possible moves
     */
    public void showPossibleMoveTileViews(List<Move> possibleMoves) {
        possibleMoves.forEach(move -> {
            TileView tileView = getTileView(move.getTargetRow(), move.getTargetCol());
            Rectangle tile = ((Rectangle) tileView.getChildren().get(0));
            tile.setFill(Color.GREENYELLOW);
            tile.setOpacity(70);
        });
    }

    /**
     * Reset tile view colors.
     */
    public void resetTileViewColors() {
        board.getChildren().stream()
                .filter(node -> node instanceof TileView)
                .filter(node -> ((Rectangle) (((TileView) node).getChildren().get(0))).getFill().equals(Color.GREENYELLOW))
                .forEach(node -> ((Rectangle) (((TileView) node).getChildren().get(0))).setFill(Color.web("A85D5D")));
    }

    /**
     * Gets tile view.
     *
     * @param row the row
     * @param col the col
     * @return the tile view
     */
    public TileView getTileView(int row, int col) {
        int index = (row * 8) + col;
        return (TileView) board.getChildren().get(index);
    }
}
