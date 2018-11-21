package com.magbadelo.checkers.view;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class PieceView extends Circle {
    private double originX, originY;
    private double mouseX, mouseY;
    private String color;

    public PieceView(String fill, double radius) {
        this.color = fill;
        setFill(Color.web(fill));
        setRadius(radius);
    }

    public String getColor() {
        return color;
    }

    public void move(double x, double y) {
        setCenterX(x);
        setCenterY(y);
    }

    public void setOrigin(double originX, double originY) {
        this.originX = originX;
        this.originY = originY;
    }

    public void abortMove() {
        move(originX, originY);
    }

    public void setMousePoint(double mouseX, double mouseY) {
        this.mouseX = mouseX;
        this.mouseY = mouseY;
    }

    public double getMouseX() {
        return mouseX;
    }

    public double getMouseY() {
        return mouseY;
    }

    public double getOriginX(){
        return originX;
    }

    public double getOriginY(){
        return originY;
    }

    public void ascend(){
        setStroke(Color.web("#FFCC66"));
        setStrokeWidth(3);
    }
}
