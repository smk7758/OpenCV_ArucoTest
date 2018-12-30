package com.github.smk7758.OpenCV_Contrib_Aruco_0;

import org.opencv.aruco.Aruco;
import org.opencv.aruco.Dictionary;
import org.opencv.aruco.GridBoard;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;

public class Main {

	static {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
	}

	public static void main(String[] args) {
		createMarker();
	}

	public static void createMarker() {
		// 2894 4093
		Dictionary dictionary = Aruco.getPredefinedDictionary(Aruco.DICT_4X4_50);

		final int markersX = 5, markersY = 6, markerLength = 400, markerSeparation = 40;
		GridBoard gridBoard = GridBoard.create(markersX, markersY, markerLength, markerSeparation, dictionary);

		final int margins = 100;
		Size imageSize = new Size();
		imageSize.width = markersX * (markerLength + markerSeparation) - markerSeparation + 2 * margins;
		imageSize.height = markersY * (markerLength + markerSeparation) - markerSeparation + 2 * margins;
		Mat boardImage = new Mat();
		gridBoard.draw(imageSize, boardImage);

		Imgcodecs.imwrite("F:\\users\\smk7758\\Desktop\\board_2018-12-02.png", boardImage);
	}
}
