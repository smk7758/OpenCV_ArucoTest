package com.github.smk7758.OpenCV_Contrib_Aruco_0;

import java.util.ArrayList;
import java.util.List;

import org.opencv.aruco.Aruco;
import org.opencv.aruco.DetectorParameters;
import org.opencv.aruco.Dictionary;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;

public class Main {

	static {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
	}

	public static void main(String[] args) {
		detectMarker();
		System.out.println("FIN");
	}

	public static void detectMarker() {
		Dictionary dictionary = Aruco.getPredefinedDictionary(Aruco.DICT_4X4_50);

		Mat inputImage = Imgcodecs.imread("F:\\users\\smk7758\\Desktop\\marker_2018-12-01_test.png");
		List<Mat> corners = new ArrayList<>();
		Mat markerIds = new Mat();
		DetectorParameters parameters = DetectorParameters.create();
		Aruco.detectMarkers(inputImage, dictionary, corners, markerIds, parameters);

		Aruco.drawDetectedMarkers(inputImage, corners, markerIds);

		Imgcodecs.imwrite("F:\\users\\smk7758\\Desktop\\marker_2018-12-01_detected.png", inputImage);
	}
}
