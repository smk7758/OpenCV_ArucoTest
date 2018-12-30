package com.github.smk7758.OpenCV_Contrib_Aruco_0;

import org.opencv.aruco.Aruco;
import org.opencv.aruco.Dictionary;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;

public class Main {

	static {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
	}

	public static void main(String[] args) {
		createMarker();
	}

	public static void createMarker() {
		Dictionary dictionary = Aruco.getPredefinedDictionary(Aruco.DICT_4X4_50);

		final int markerID = 0;
		final int sidePixels = 200;
		Mat markerImage = new Mat();
		Aruco.drawMarker(dictionary, markerID, sidePixels, markerImage);

		Imgcodecs.imwrite("F:\\users\\smk7758\\Desktop\\marker_2018-12-01.png", markerImage);
	}
}
