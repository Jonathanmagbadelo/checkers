package com.magbadelo.checkers.view;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class PieceView extends Circle {
    private String color;

    public PieceView(String fill, double radius) {
        this.color = fill;
        setFill(Color.web(fill));
        setRadius(radius);
    }

    public String getColor() {
        return color;
    }

    public void ascend() {
        setStroke(Color.web("#FFCC66"));
        setStrokeWidth(3);
    }
}
