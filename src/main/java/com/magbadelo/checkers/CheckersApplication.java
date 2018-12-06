package com.magbadelo.checkers;

import com.magbadelo.checkers.controller.CheckerBoardController;
import javafx.application.Platform;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import org.springframework.context.ConfigurableApplicationContext;
import javax.annotation.PostConstruct;

/**
 * The type Checkers application.
 */
@SpringBootApplication
public class CheckersApplication extends Application {
    private ConfigurableApplicationContext springContext;
    private Parent rootNode;

    /**
     * The Checker board controller.
     */
    @Autowired
    CheckerBoardController checkerBoardController;

    /**
     * Go.
     */
    @PostConstruct
    public void go() {
        Platform.runLater(() -> {
            rootNode = checkerBoardController.getCheckerBoardView().createContent();
            Stage primaryStage = new Stage();
            primaryStage.setResizable(false);

            primaryStage.setTitle("Checkers");
            Scene scene = new Scene(rootNode);
            primaryStage.setScene(scene);
            primaryStage.show();

        });
    }

    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void init() throws Exception {
        springContext = SpringApplication.run(CheckersApplication.class);
    }

    @Override
    public void start(Stage primaryStage) {
        //do nothing
    }

    @Override
    public void stop() {
        springContext.close();
    }
}