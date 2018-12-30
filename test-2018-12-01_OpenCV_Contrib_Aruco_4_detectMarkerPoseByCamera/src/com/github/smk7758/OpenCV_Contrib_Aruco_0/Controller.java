package com.github.smk7758.OpenCV_Contrib_Aruco_0;

import org.opencv.aruco.Aruco;

import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;

public class Controller {
	MarkerDetectorService markerDetectorService = null;
	AnimationTimer imageAnimation = null;

	@FXML
	ImageView imageView;

	@FXML
	public void initialize() {
		markerDetectorService = new MarkerDetectorService(Aruco.getPredefinedDictionary(Aruco.DICT_4X4_50));

		// imageView.setPreserveRatio(true);

		imageAnimation = new AnimationTimer() {
			@Override
			public void handle(long now) {
				imageView.setImage(markerDetectorService.getLastValue());
			}
		};
	}

	@FXML
	public void onStartButton() {
		Main.printDebug("startButton");
		imageAnimation.start();
		if (!markerDetectorService.isRunning()) markerDetectorService.start();
		// markerDetector.start();

		// imageView.fitWidthProperty().bind(markerDetectorService.getLastValue().widthProperty().subtract(20));
	}

	@FXML
	public void onStopButton() {
		Main.printDebug("stopButton");
		imageAnimation.stop();
		markerDetectorService.cancel();

		markerDetectorService.reset();
		imageView.setImage(null);
	}

	@FXML
	public void onPauseButton() {
		Main.printDebug("pauseButton");
		imageAnimation.stop();
	}
}
