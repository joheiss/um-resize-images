package com.jovisco.um.imagescalerui.jfx;

import javafx.application.HostServices;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lombok.Getter;

public enum UiState {

    INSTANCE;

    @Getter
    private static Scene scene;
    @Getter
    private static Stage stage;
    private static String title;
    private static HostServices hostServices;

    public static void setScene(final Scene scene) {
        UiState.scene = scene;
    }

    public static void setStage(final Stage stage) {
        UiState.stage = stage;
    }

    public static void setTitle(final String title) {
        UiState.title = title;
    }


    public static void setHostServices(HostServices hostServices) {
        UiState.hostServices = hostServices;
    }
}
