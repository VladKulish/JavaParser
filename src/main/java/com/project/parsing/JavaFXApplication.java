package com.project.parsing;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication()
public class JavaFXApplication extends Application {
    private ConfigurableApplicationContext springContext;
    private Parent rootNode;

    public static void main(String[] args) {
        Application.launch(args);
    }
    @Override
    public void init() throws Exception {
        // Запускаем Spring Boot
        springContext = SpringApplication.run(JavaFXApplication.class);
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/main-view.fxml"));
        fxmlLoader.setControllerFactory(springContext::getBean); // Интеграция Spring
        rootNode = fxmlLoader.load();
    }
    @Override
    public void start(Stage stage) throws Exception {
        stage.setScene(new Scene(rootNode));
        stage.show();
    }

    @Override
    public void stop() {
        springContext.close();
    }
}
