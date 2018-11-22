package com.magbadelo.checkers.view;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class TileView extends Rectangle {

    private final int tileSize;
    private boolean isLightTile;

    public TileView(String fill, int tileSize, boolean isLightTile) {
        setWidth(tileSize);
        setHeight(tileSize);
        setFill(Color.web(fill));
        this.tileSize = tileSize;
        this.isLightTile = isLightTile;
    }

    public double getCentreX(){
        return getLayoutX() + (tileSize/2.0);
    }

    public double getCentreY(){
        return getLayoutY() + (tileSize/2.0);
    }

    public boolean isLightTile() {
        return isLightTile;
    }
}
