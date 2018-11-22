package com.magbadelo.checkers.controller;

import com.magbadelo.checkers.CheckersApplication;
import com.magbadelo.checkers.model.CheckersBoard;
import com.magbadelo.checkers.model.Move;
import com.magbadelo.checkers.view.PieceView;
import com.magbadelo.checkers.view.TileView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Random;

@Component
public class PieceController {
    private static final Logger LOGGER = LoggerFactory.getLogger(CheckersApplication.class);

    private CheckersBoard checkersBoard;

    private int TILE_SIZE = 100;
    private int NUMROWS = 8;
    private GridPane board;

    @Autowired
    public PieceController(CheckersBoard checkersBoard, GridPane board) {
        this.checkersBoard = checkersBoard;
        this.board = board;
    }

    public void setPieceViewEventHandles(PieceView pieceView) {
        pieceView.setOnMousePressed(event -> setMousePos(event, pieceView));
        pieceView.setOnMouseReleased(event -> tryMove(event, pieceView));
        pieceView.setOnMouseDragged(event -> drag(event, pieceView));
    }


    private void drag(MouseEvent event, PieceView pieceView) {
        pieceView.move(event.getSceneX() - pieceView.getMouseX() + pieceView.getOriginX(), event.getSceneY() - pieceView.getMouseY() + pieceView.getOriginY());
    }

    private void setMousePos(MouseEvent event, PieceView pieceView) {
        pieceView.toFront();
        pieceView.setMousePoint(event.getSceneX(), event.getSceneY());
        //pieceView.setOrigin(pieceView.getCenterX(), pieceView.getCenterY());
    }

    private void tryMove(MouseEvent event, PieceView pieceView) {
        int sourceRow = (int) Math.floor(pieceView.getLayoutY() / TILE_SIZE);
        int sourceCol = (int) Math.floor(pieceView.getLayoutX() / TILE_SIZE);
        int targetRow = (int) Math.floor(pieceView.getLocalToParentTransform().getTy() / TILE_SIZE);
        int targetCol = (int) Math.floor(pieceView.getLocalToParentTransform().getTx() / TILE_SIZE);
        Move move = new Move(sourceRow, sourceCol, targetRow, targetCol);
        if (checkersBoard.isMoveValid(move)) {
            LOGGER.info("Move from {},{} is valid to {},{}!", sourceRow, sourceCol, targetRow, targetCol);
            checkersBoard.completeMove(move);
            completePieceViewMove(move, pieceView);
            checkersBoard.switchCurrentPlayer();
        } else {
            LOGGER.info("Move from {},{} is invalid to {},{}!", sourceRow, sourceCol, targetRow, targetCol);
            pieceView.abortMove();
        }
        //aiMove();
    }

    private void completePieceViewMove(Move move, PieceView movedPiece) {
//        TileView targetTileView = (TileView) board.getChildren().get(getProspectTile(move.getTargetRow(), move.getTargetCol()));
//        movedPiece.setOrigin(targetTileView.getCentreX(), targetTileView.getCentreY());
//        movedPiece.move(targetTileView.getCentreX(), targetTileView.getCentreY());
//        board.getChildren().set(getProspectTile(move.getTargetRow(), move.getTargetCol()), movedPiece);
//        board.getChildren().remove(movedPiece);
        ObservableList<Node> workingCollection = FXCollections.observableArrayList(board.getChildren());
        Collections.swap(workingCollection, getProspectTile(move.getSourceRow(), move.getSourceCol()), getProspectTile(move.getTargetRow(), move.getTargetCol()));
        board.getChildren().setAll(workingCollection);
        double newX = ((((move.getTargetRow()+1)*TILE_SIZE)-TILE_SIZE/2.0));
        double newY = ((((move.getTargetCol()+1)*TILE_SIZE)-TILE_SIZE/2.0));
        GridPane.setHalignment(movedPiece, HPos.CENTER);
        GridPane.setValignment(movedPiece, VPos.CENTER);
        if (move.isCapturingMove()) {
            PieceView capturedPieceView = (PieceView) board.getChildren().get(getProspectTile(move.getMiddleRow(), move.getMiddleCol()));
            board.getChildren().remove(capturedPieceView);
        }
        if(move.isCrowningMove()){
            movedPiece.ascend();
        }
    }
//
    private int getProspectTile(int row, int col) {
        return ((row * NUMROWS) + col);
    }
//
//    private PieceView getPieceView(int row, int col) {
//        TileView tileView = (TileView) board.getChildren().get(getProspectTile(row, col));
//        //return (PieceView) pieceGroup.getChildren().filtered(node -> {
//            PieceView pieceView = (PieceView) node;
//            return pieceView.getOriginX() == tileView.getCentreX() && pieceView.getOriginY() == tileView.getCentreY();
//        }).get(0);
//    }

//    public void aiMove() {
//        if (checkersBoard.getCurrentPlayer().isAIPlayer()) {
//            List<Move> moves = checkersBoard.generateMoves(checkersBoard.getAiPlayer());
//            Move move = moves.get(new Random().nextInt(moves.size()));
//            checkersBoard.completeMove(move);
//            PieceView pieceView = getPieceView(move.getSourceRow(), move.getSourceCol());
//            completePieceViewMove(move, pieceView);
//            checkersBoard.switchCurrentPlayer();
//        }
//    }

}
