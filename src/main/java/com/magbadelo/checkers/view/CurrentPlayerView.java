package com.magbadelo.checkers.view;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import org.springframework.stereotype.Component;

/**
 * The Current player view.
 */
@Component
public class CurrentPlayerView extends VBox {
    private Circle piece;
    private String playerTurn;
    private String aiTurn;
    private String currentTurn;

    /**
     * Instantiates a new Current player view.
     */
    public CurrentPlayerView(){
        this.playerTurn = "Players Turn!";
        this.aiTurn = "AI's Turn";
        this.piece = new Circle(37.5, Color.TRANSPARENT);
        this.currentTurn = playerTurn;

        setStyle("-fx-background-color: #975354;" +
                "-fx-border-style: solid inside;" +
                "-fx-border-width: 5;" +
                "-fx-border-color: #7D4544;");
        setPrefSize(150, 500);
        setMaxSize(150, 500);
        setAlignment(Pos.CENTER);
        setSpacing(75);

        getChildren().addAll(piece, new Label(playerTurn));
    }

    /**
     * Set piece color.
     *
     * @param color  the color
     * @param stroke the stroke
     */
    public void setPieceColor(String color, String stroke){
        piece.setFill(Color.web(color));
        piece.setStroke(Color.web(stroke));
        piece.setStrokeWidth(3);
    }

    /**
     * Displays the name of the next player.
     */
    public void nextTurn(){
        currentTurn = currentTurn.equals(playerTurn) ? aiTurn : playerTurn;
        getChildren().set(1, new Label(currentTurn));
    }

    /**
     * Presents a game over message to the user.
     *
     * @param endGameMessage the end game message
     */
    public void gameOver(String endGameMessage){
        getChildren().set(1, new Label(endGameMessage));
    }

}
