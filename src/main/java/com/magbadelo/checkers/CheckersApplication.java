package com.magbadelo.checkers;

import com.magbadelo.checkers.controller.CheckerBoardController;
import javafx.application.Platform;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;

import javax.annotation.PostConstruct;

@EnableScheduling
@SpringBootApplication
public class CheckersApplication extends Application {
    private ConfigurableApplicationContext springContext;
    private Parent rootNode;
    private static final Logger LOGGER = LoggerFactory.getLogger(CheckersApplication.class);

    @Autowired
    CheckerBoardController checkerBoardController;

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
        LOGGER.info("Loading Checkers application.....");
    }

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