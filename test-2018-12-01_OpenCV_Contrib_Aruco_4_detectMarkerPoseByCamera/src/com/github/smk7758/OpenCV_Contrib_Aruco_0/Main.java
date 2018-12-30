package com.github.smk7758.OpenCV_Contrib_Aruco_0;

import java.io.IOException;

import org.opencv.core.Core;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
	public static boolean debugMode = true;

	static {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
	}

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		try {
			Scene scene = new Scene(FXMLLoader.load(getClass().getResource("window.fxml")));
			// FXMLLoader loader = new FXMLLoader(getClass().getResource("MemoWriter_0.0.1.fxml"));
			// Scene scene = new Scene(loader.load());
			// controller = (Controller) loader.getController();
			// Set ico
			// primaryStage.getIcons().add(new Image((getClass().getResource("OnlyDownloader_x16.ico").toString())));
			// primaryStage.getIcons().add(new Image((getClass().getResource("OnlyDownloader_x32.ico").toString())));
			// Set Title
			primaryStage.setTitle("test");
			// // Set Window
			primaryStage.setResizable(false);
			// Set Scene
			primaryStage.setScene(scene);
			primaryStage.show();
			// primaryStage.onShownProperty();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void printDebug(String text) {
		if (Main.debugMode) System.out.println(text);
	}
}
