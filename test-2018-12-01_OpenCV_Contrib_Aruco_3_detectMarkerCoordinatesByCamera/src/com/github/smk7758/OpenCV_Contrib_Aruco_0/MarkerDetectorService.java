package com.github.smk7758.OpenCV_Contrib_Aruco_0;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;

import org.opencv.aruco.Aruco;
import org.opencv.aruco.Dictionary;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.imgproc.Moments;
import org.opencv.videoio.VideoCapture;

import javafx.concurrent.ScheduledService;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.scene.image.Image;

public class MarkerDetectorService extends ScheduledService<Image> {
	final Dictionary dictionary;
	// Mat inputImage;
	// List<Mat> corners;
	// Mat markerIds;

	static final int provisionalCameraNumber = 0;

	VideoCapture vc = null;
	int cameraNumber = 0;

	public MarkerDetectorService(Dictionary dictionary, int cameraNumber) {
		this.dictionary = dictionary;
		this.cameraNumber = cameraNumber;
		initialize();

		this.setOnCancelled(new EventHandler<WorkerStateEvent>() {
			@Override
			public void handle(WorkerStateEvent event) {
				vc.release();
			}
		});
	}

	public MarkerDetectorService(Dictionary dictionary) {
		this(dictionary, provisionalCameraNumber);
	}

	public void initialize() {
		vc = new VideoCapture();
		vc.open(cameraNumber);
	}

	@Override
	public void reset() {
		super.reset();

		initialize();
	}

	@Override
	public boolean cancel() {
		Main.printDebug("Service is stopped.");
		return super.cancel();
	}

	@Override
	protected Task<Image> createTask() {
		return new Task<Image>() {
			@Override
			protected Image call() throws Exception {
				if (!vc.isOpened()) {
					System.err.println("VC is not opened.");
					this.cancel();
					return null;
				}

				Mat inputImage = new Mat();

				if (!vc.read(inputImage) || inputImage == null) {
					System.err.println("Cannot load camera image.");
					this.cancel();
					return null;
				}

				List<Mat> corners = new ArrayList<>();
				Mat markerIds = new Mat();
				// DetectorParameters parameters = DetectorParameters.create();
				Aruco.detectMarkers(inputImage, dictionary, corners, markerIds);

				// if (corners.size() < 1) {
				if (corners.isEmpty()) return convertMatToImage(inputImage);

				Aruco.drawDetectedMarkers(inputImage, corners, markerIds);

				System.out.println("Mat output!---");

				System.out.println(markerIds.dump());
				// markerIds.get(i, 0);
				for (int i = 0; i < markerIds.rows(); i++) {
					System.out.println(markerIds.get(i, 0)[0]);
				}

				for (Mat mat : corners) {
					System.out.println(mat.dump());
					List<Point> cornerPoints = new ArrayList<>();
					for (int row = 0; row < mat.height(); row++) {
						for (int col = 0; col < mat.width(); col++) {
							final Point point = new Point(mat.get(row, col));
							cornerPoints.add(point);

							Imgproc.circle(inputImage, point, 5, new Scalar(100, 100, 0), -1);
						}
					}
					Imgproc.circle(inputImage, new Point(getCenter(cornerPoints)), 5, new Scalar(0, 0, 255), -1);
				}
				return convertMatToImage(inputImage);
			}
		};
	}

	public double[] getCenter(List<Point> points) {
		final MatOfPoint points_ = new MatOfPoint();
		points_.fromList(points);
		return getCenter(points_);
	}

	public double[] getCenter(MatOfPoint points) {
		// 重心を取得(投げてるver)
		Moments moments = Imgproc.moments(points);
		double[] center = { moments.get_m10() / moments.get_m00(), moments.get_m01() / moments.get_m00() };
		return center;
	}

	private Image convertMatToImage(Mat inputImage) {
		MatOfByte byte_mat = new MatOfByte();
		Imgcodecs.imencode(".bmp", inputImage, byte_mat);

		return new Image(new ByteArrayInputStream(byte_mat.toArray()));
	}
}
