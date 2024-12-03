package com.jovisco.um.imagescalerui;

import com.jovisco.um.imagescalerui.jfx.UiState;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import com.jovisco.um.imagescalerui.JavaFxApplicationSupport.StageReadyEvent;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class StageInitializer implements ApplicationListener<StageReadyEvent> {

    private final ApplicationContext context;

    @Override
    public void onApplicationEvent(StageReadyEvent event) {
        Stage stage = event.getStage();
        UiState.setStage(stage);
        try {
            var fxmlLoader = new FXMLLoader(new ClassPathResource("views/main.fxml").getURL());
            fxmlLoader.setControllerFactory(context::getBean);
            Parent parent = fxmlLoader.load();
            var scene = new Scene(parent);
            UiState.setScene(scene);
            stage.setScene(scene);
            // load stylesheets according to application config
            scene.getStylesheets().add(new ClassPathResource("styles/dark.css").getURL().toExternalForm());
            scene.getStylesheets().add(new ClassPathResource("styles/smallfont.css").getURL().toExternalForm());
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
