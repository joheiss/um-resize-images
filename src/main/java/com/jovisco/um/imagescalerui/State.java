package com.jovisco.um.imagescalerui;

import javafx.beans.property.*;
import lombok.Getter;

@Getter
public enum State {

    INSTANCE;

    private StringProperty sourceDir = new SimpleStringProperty();
    private StringProperty targetDir = new SimpleStringProperty();
    private BooleanProperty jpg = new SimpleBooleanProperty(true);
    private BooleanProperty png = new SimpleBooleanProperty();
    private BooleanProperty resolution64 = new SimpleBooleanProperty();
    private BooleanProperty resolution128 = new SimpleBooleanProperty(true);
    private BooleanProperty resolution256 = new SimpleBooleanProperty(true);
    private BooleanProperty resolution512 = new SimpleBooleanProperty(true);
    private BooleanProperty resolution768 = new SimpleBooleanProperty(true);
    private BooleanProperty resolution1024 = new SimpleBooleanProperty(true);
    private BooleanProperty resolution1280 = new SimpleBooleanProperty(true);
    private BooleanProperty resolution1536 = new SimpleBooleanProperty(true);
    private BooleanProperty resolution1792 = new SimpleBooleanProperty(true);
    private BooleanProperty resolution2048 = new SimpleBooleanProperty(true);
    private FloatProperty progress = new SimpleFloatProperty(0.0f);

    private BooleanProperty anyFormat = new SimpleBooleanProperty();
    private BooleanProperty anyResolution = new SimpleBooleanProperty();

   State() {
        anyFormat.bind(jpg.or(png));
        anyResolution.bind(
                resolution64.or(resolution128).or(resolution256).or(resolution512)
                .or(resolution768).or(resolution1024).or(resolution1280).or(resolution1536)
                .or(resolution1792).or(resolution2048));
    }

    public BooleanProperty isValidInput() {
        System.out.println(anyFormat.toString());
        System.out.println(anyResolution.toString());
        System.out.println(sourceDir.isNotEmpty().toString());
        System.out.println(targetDir.isNotEmpty().toString());
        return new SimpleBooleanProperty(
                anyFormat.getValue() &&
                anyResolution.getValue() &&
                sourceDir.isNotEmpty().getValue() &&
                targetDir.isNotEmpty().getValue()
        );
    }
}
