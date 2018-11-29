package com.magbadelo.checkers.view;

import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class TileView extends StackPane {

    private boolean isLightTile;
    private int row;
    private int col;

    public TileView(String fill, int tileSize, boolean isLightTile, int row, int col) {
        setPrefSize(tileSize, tileSize);
        Rectangle rectangle = new Rectangle(tileSize, tileSize, Color.web(fill));
        getChildren().add(rectangle);
        this.isLightTile = isLightTile;
        this.row = row;
        this.col = col;
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
