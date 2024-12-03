package com.jovisco.um.imagescalerui;

import javafx.application.Application;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ImageScalerUiApplication {

	public static void main(String[] args) {
		Application.launch(JavaFxApplicationSupport.class, args);
	}

}
