package com.magbadelo.checkers.view;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import org.springframework.stereotype.Component;

@Component
public class CurrentPlayerView extends VBox {

    private Circle piece;
    private String playerTurn;
    private String aiTurn;
    private String currentTurn;
    private Label currentPlayer;

    public CurrentPlayerView(){
        setStyle("-fx-background-color: #975354;" +
                "-fx-border-style: solid inside;" +
                "-fx-border-width: 5;" +
                "-fx-border-color: #7D4544;");
        setPrefSize(150, 500);
        setMaxSize(150, 500);
        this.playerTurn = "Players Turn!";
        this.aiTurn = "AI's Turn";
        this.piece = new Circle(37.5, Color.TRANSPARENT);
        this.currentTurn = playerTurn;
        this.currentPlayer = new Label("NO GAME RUNNING");
        setAlignment(Pos.CENTER);
        getChildren().addAll(currentPlayer, piece);
    }

    public void  setPieceColor(String color){
        piece.setFill(Color.web(color));
    }

    public void nextTurn(){
        currentTurn = currentTurn.equals(playerTurn) ? aiTurn : playerTurn;
        getChildren().set(0, new Label(currentTurn));
    }

}