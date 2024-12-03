package com.jovisco.um.imagescalerui.controllers;

import com.jovisco.um.imagescalerui.State;
import com.jovisco.um.imagescalerui.jfx.UiState;
import com.jovisco.um.imagescalerui.services.ImageScalerServiceAdapter;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.DirectoryChooser;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.File;
import java.net.URL;
import java.util.*;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@RequiredArgsConstructor
public class MainController implements Initializable {

    private final ImageScalerServiceAdapter service;

    @FXML public TextField txfSourceDir;
    @FXML public Button btnSourceDir;
    @FXML public CheckBox chkJpg;
    @FXML public CheckBox chkPng;
    @FXML public TextField txfTargetDir;
    @FXML public Button btnTargetDir;
    @FXML public CheckBox chkResolution2048;
    @FXML public CheckBox chkResolution1792;
    @FXML public CheckBox chkResolution1536;
    @FXML public CheckBox chkResolution1280;
    @FXML public CheckBox chkResolution1024;
    @FXML public CheckBox chkResolution768;
    @FXML public CheckBox chkResolution512;
    @FXML public CheckBox chkResolution256;
    @FXML public CheckBox chkResolution128;
    @FXML public CheckBox chkResolution64;
    @FXML public Button btnClose;
    @FXML public Button btnOK;
    @FXML public TextField txfProgress;
    @FXML public ProgressBar prbProgress;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeView();
    }

    private void initializeView() {
        txfSourceDir.textProperty().bindBidirectional(State.INSTANCE.getSourceDir());
        txfTargetDir.textProperty().bindBidirectional(State.INSTANCE.getTargetDir());
        chkJpg.selectedProperty().bindBidirectional(State.INSTANCE.getJpg());
        chkPng.selectedProperty().bindBidirectional(State.INSTANCE.getPng());
        chkResolution64.selectedProperty().bindBidirectional(State.INSTANCE.getResolution64());
        chkResolution128.selectedProperty().bindBidirectional(State.INSTANCE.getResolution128());
        chkResolution256.selectedProperty().bindBidirectional(State.INSTANCE.getResolution256());
        chkResolution512.selectedProperty().bindBidirectional(State.INSTANCE.getResolution512());
        chkResolution768.selectedProperty().bindBidirectional(State.INSTANCE.getResolution768());
        chkResolution1024.selectedProperty().bindBidirectional(State.INSTANCE.getResolution1024());
        chkResolution1280.selectedProperty().bindBidirectional(State.INSTANCE.getResolution1280());
        chkResolution1536.selectedProperty().bindBidirectional(State.INSTANCE.getResolution1536());
        chkResolution1792.selectedProperty().bindBidirectional(State.INSTANCE.getResolution1792());
        chkResolution2048.selectedProperty().bindBidirectional(State.INSTANCE.getResolution2048());
        prbProgress.progressProperty().bindBidirectional(State.INSTANCE.getProgress());

        btnOK.disableProperty().bind(
                State.INSTANCE.getAnyFormat().not()
                .or(State.INSTANCE.getAnyResolution().not())
                .or(State.INSTANCE.getSourceDir().isEmpty())
                .or(State.INSTANCE.getTargetDir().isEmpty())
        );

    }

    @FXML
    public void handleSourceDirSelection() {
        txfSourceDir.setText(chooseDirectory("Select source directory for image resizing"));
        System.out.println("State target dir: " + State.INSTANCE.getTargetDir());
    }

    @FXML
    public void handleTargetDirSelection() {
        txfTargetDir.setText(chooseDirectory("Select target directory for image resizing"));
        System.out.println("State target dir: " + State.INSTANCE.getTargetDir());
    }

    @FXML
    public void handleClose() {
         UiState.getStage().close();
    }

    @FXML
    public void handleOK() {

        try {
            // build request map
            var requestMap = buildRequestMap();
            // invoke service
            var response = service.execute(requestMap);
            showPopUp(Alert.AlertType.INFORMATION, "Success",
                    String.format("%d images found and resized into %d new images",
                            response.countOriginalImages(), response.countResizedImages()));

        } catch (Exception e) {
            showPopUp(Alert.AlertType.ERROR, "Error", e.getMessage());
        }

    }

    public void showPopUp(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        TextArea messageTextArea = new TextArea(message);
        messageTextArea.setWrapText(true);
        messageTextArea.setEditable(false);
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.setContent(messageTextArea);
//        dialogPane.getStylesheets()
//                .add(Objects.requireNonNull(
//                        getClass().getResource(ColorTheme.getCssPath(getColorTheme()))).toExternalForm());
//        dialogPane.getStylesheets()
//                .add(Objects.requireNonNull(
//                        getClass().getResource(FontSize.getCssPath(getFontSize()))).toExternalForm());
        alert.setResizable(true);
        alert.show();
    }

    private String chooseDirectory(String title) {
        try {
            var chooser = new DirectoryChooser();
            chooser.setTitle(title);
            File directory = chooser.showDialog(UiState.getStage());
            return (directory != null) ? directory.getAbsolutePath() : null;
        } catch (Exception e) {
            showPopUp(Alert.AlertType.ERROR, "Select Source Directory", e.getLocalizedMessage());
            e.printStackTrace();
        }
        return null;
    }

    private Map<String, Object> buildRequestMap() {
        Map<String, Object> request = new HashMap<>();
        request.put("sourceDir", State.INSTANCE.getSourceDir().getValue());
        request.put("targetDir", State.INSTANCE.getTargetDir().getValue());
        request.put("jpg", State.INSTANCE.getJpg().getValue());
        request.put("png", State.INSTANCE.getPng().getValue());
        request.put("resolution64", State.INSTANCE.getResolution64().getValue());
        request.put("resolution128", State.INSTANCE.getResolution128().getValue());
        request.put("resolution256", State.INSTANCE.getResolution256().getValue());
        request.put("resolution512", State.INSTANCE.getResolution512().getValue());
        request.put("resolution768", State.INSTANCE.getResolution768().getValue());
        request.put("resolution1024", State.INSTANCE.getResolution1024().getValue());
        request.put("resolution1280", State.INSTANCE.getResolution1280().getValue());
        request.put("resolution1536", State.INSTANCE.getResolution1536().getValue());
        request.put("resolution1792", State.INSTANCE.getResolution1792().getValue());
        request.put("resolution2048", State.INSTANCE.getResolution2048().getValue());
        return request;
    }
}
