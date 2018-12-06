package com.magbadelo.checkers.view;

import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * The type Tile view.
 */
public class TileView extends StackPane {
    private final boolean isLightTile;
    private final int row;
    private final int col;

    /**
     * Instantiates a new Tile view.
     *
     * @param fill        the fill
     * @param tileSize    the tile size
     * @param isLightTile the is light tile
     * @param row         the row
     * @param col         the col
     */
    public TileView(String fill, int tileSize, boolean isLightTile, int row, int col) {
        this.isLightTile = isLightTile;
        this.row = row;
        this.col = col;
        setPrefSize(tileSize, tileSize);
        getChildren().add(new Rectangle(tileSize, tileSize, Color.web(fill)));
    }

    /**
     * Is light tile boolean.
     *
     * @return the boolean
     */
    public boolean isLightTile() {
        return isLightTile;
    }

    /**
     * Gets row.
     *
     * @return the row
     */
    public int getRow() {
        return row;
    }

    /**
     * Gets col.
     *
     * @return the col
     */
    public int getCol() {
        return col;
    }
}
