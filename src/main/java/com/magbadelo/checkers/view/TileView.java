package com.magbadelo.checkers.view;

import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class TileView extends StackPane {
    private final boolean isLightTile;
    private final int row;
    private final int col;

    public TileView(String fill, int tileSize, boolean isLightTile, int row, int col) {
        this.isLightTile = isLightTile;
        this.row = row;
        this.col = col;
        setPrefSize(tileSize, tileSize);
        getChildren().add(new Rectangle(tileSize, tileSize, Color.web(fill)));
    }

    public boolean isLightTile() {
        return isLightTile;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }
}
