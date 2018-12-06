package com.magbadelo.checkers.view;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

/**
 * The type Piece view.
 */
public class PieceView extends Circle {
    private String color;

    /**
     * Instantiates a new Piece view.
     *
     * @param fill   the fill
     * @param stroke the stroke
     * @param radius the radius
     */
    public PieceView(String fill, String stroke, double radius) {
        this.color = fill;
        setFill(Color.web(fill));
        setRadius(radius);
        setStroke(Color.web(stroke));
        setStrokeWidth(3);
    }

    /**
     * Gets color.
     *
     * @return the color
     */
    public String getColor() {
        return color;
    }

    /**
     * Crown.
     */
    public void crown() {
        setStroke(Color.web("#FFCC66"));
        setStrokeWidth(3);
    }
}
