package frc.robot;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.HashMap;

import org.opencv.core.*;
import org.opencv.core.Core.*;
import org.opencv.features2d.FeatureDetector;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.*;
import org.opencv.objdetect.*;

/**
* GripPipeline class.
*
* <p>An OpenCV pipeline generated by GRIP.
*
* @author GRIP
*/
public class GripPipeline {

	//Outputs
	private Mat rgbThreshold0Output = new Mat();
	private ArrayList<MatOfPoint> findContoursOutput = new ArrayList<MatOfPoint>();
	private ArrayList<MatOfPoint> filterContoursOutput = new ArrayList<MatOfPoint>();
	private Mat rgbThreshold1Output = new Mat();
	private ArrayList<Line> findLinesOutput = new ArrayList<Line>();

	static {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
	}

	/**
	 * This is the primary method that runs the entire pipeline and updates the outputs.
	 */
	public void process(Mat source0) {
		// Step RGB_Threshold0:
		Mat rgbThreshold0Input = source0;
		double[] rgbThreshold0Red = {0.0, 75.12626262626263};
		double[] rgbThreshold0Green = {105.48561151079136, 255.0};
		double[] rgbThreshold0Blue = {0.26143917230968805, 141.23737373737373};
		rgbThreshold(rgbThreshold0Input, rgbThreshold0Red, rgbThreshold0Green, rgbThreshold0Blue, rgbThreshold0Output);

		// Step Find_Contours0:
		Mat findContoursInput = rgbThreshold0Output;
		boolean findContoursExternalOnly = true;
		findContours(findContoursInput, findContoursExternalOnly, findContoursOutput);

		// Step Filter_Contours0:
		ArrayList<MatOfPoint> filterContoursContours = findContoursOutput;
		double filterContoursMinArea = 50.0;
		double filterContoursMinPerimeter = 0.0;
		double filterContoursMinWidth = 0.0;
		double filterContoursMaxWidth = 1000.0;
		double filterContoursMinHeight = 0.0;
		double filterContoursMaxHeight = 1000.0;
		double[] filterContoursSolidity = {0, 100};
		double filterContoursMaxVertices = 100.0;
		double filterContoursMinVertices = 0.0;
		double filterContoursMinRatio = 0.0;
		double filterContoursMaxRatio = 1000.0;
		filterContours(filterContoursContours, filterContoursMinArea, filterContoursMinPerimeter, filterContoursMinWidth, filterContoursMaxWidth, filterContoursMinHeight, filterContoursMaxHeight, filterContoursSolidity, filterContoursMaxVertices, filterContoursMinVertices, filterContoursMinRatio, filterContoursMaxRatio, filterContoursOutput);

		// // Step RGB_Threshold1:
		// Mat rgbThreshold1Input = source1;
		// double[] rgbThreshold1Red = {0.0, 0.0};
		// double[] rgbThreshold1Green = {13.758992805755396, 255.0};
		// double[] rgbThreshold1Blue = {0.0, 124.74820143884891};
		// rgbThreshold(rgbThreshold1Input, rgbThreshold1Red, rgbThreshold1Green, rgbThreshold1Blue, rgbThreshold1Output);

		// // Step Find_Lines0:
		// Mat findLinesInput = rgbThreshold1Output;
		// findLines(findLinesInput, findLinesOutput);

	}

	/**
	 * This method is a generated getter for the output of a RGB_Threshold.
	 * @return Mat output from RGB_Threshold.
	 */
	public Mat rgbThreshold0Output() {
		return rgbThreshold0Output;
	}

	/**
	 * This method is a generated getter for the output of a Find_Contours.
	 * @return ArrayList<MatOfPoint> output from Find_Contours.
	 */
	public ArrayList<MatOfPoint> findContoursOutput() {
		return findContoursOutput;
	}

	/**
	 * This method is a generated getter for the output of a Filter_Contours.
	 * @return ArrayList<MatOfPoint> output from Filter_Contours.
	 */
	public ArrayList<MatOfPoint> filterContoursOutput() {
		return filterContoursOutput;
	}

	/**
	 * This method is a generated getter for the output of a RGB_Threshold.
	 * @return Mat output from RGB_Threshold.
	 */
	public Mat rgbThreshold1Output() {
		return rgbThreshold1Output;
	}

	/**
	 * This method is a generated getter for the output of a Find_Lines.
	 * @return ArrayList<Line> output from Find_Lines.
	 */
	public ArrayList<Line> findLinesOutput() {
		return findLinesOutput;
	}


	/**
	 * Sets the values of pixels in a binary image to their distance to the nearest black pixel.
	 * @param input The image on which to perform the Distance Transform.
	 * @param type The Transform.
	 * @param maskSize the size of the mask.
	 * @param output The image in which to store the output.
	 */
	private void findContours(Mat input, boolean externalOnly,
		List<MatOfPoint> contours) {
		Mat hierarchy = new Mat();
		contours.clear();
		int mode;
		if (externalOnly) {
			mode = Imgproc.RETR_EXTERNAL;
		}
		else {
			mode = Imgproc.RETR_LIST;
		}
		int method = Imgproc.CHAIN_APPROX_SIMPLE;
		Imgproc.findContours(input, contours, hierarchy, mode, method);
	}


	/**
	 * Filters out contours that do not meet certain criteria.
	 * @param inputContours is the input list of contours
	 * @param output is the the output list of contours
	 * @param minArea is the minimum area of a contour that will be kept
	 * @param minPerimeter is the minimum perimeter of a contour that will be kept
	 * @param minWidth minimum width of a contour
	 * @param maxWidth maximum width
	 * @param minHeight minimum height
	 * @param maxHeight maximimum height
	 * @param Solidity the minimum and maximum solidity of a contour
	 * @param minVertexCount minimum vertex Count of the contours
	 * @param maxVertexCount maximum vertex Count
	 * @param minRatio minimum ratio of width to height
	 * @param maxRatio maximum ratio of width to height
	 */
	private void filterContours(List<MatOfPoint> inputContours, double minArea,
		double minPerimeter, double minWidth, double maxWidth, double minHeight, double
		maxHeight, double[] solidity, double maxVertexCount, double minVertexCount, double
		minRatio, double maxRatio, List<MatOfPoint> output) {
		final MatOfInt hull = new MatOfInt();
		output.clear();
		//operation
		for (int i = 0; i < inputContours.size(); i++) {
			final MatOfPoint contour = inputContours.get(i);
			final Rect bb = Imgproc.boundingRect(contour);
			if (bb.width < minWidth || bb.width > maxWidth) continue;
			if (bb.height < minHeight || bb.height > maxHeight) continue;
			final double area = Imgproc.contourArea(contour);
			if (area < minArea) continue;
			if (Imgproc.arcLength(new MatOfPoint2f(contour.toArray()), true) < minPerimeter) continue;
			Imgproc.convexHull(contour, hull);
			MatOfPoint mopHull = new MatOfPoint();
			mopHull.create((int) hull.size().height, 1, CvType.CV_32SC2);
			for (int j = 0; j < hull.size().height; j++) {
				int index = (int)hull.get(j, 0)[0];
				double[] point = new double[] { contour.get(index, 0)[0], contour.get(index, 0)[1]};
				mopHull.put(j, 0, point);
			}
			final double solid = 100 * area / Imgproc.contourArea(mopHull);
			if (solid < solidity[0] || solid > solidity[1]) continue;
			if (contour.rows() < minVertexCount || contour.rows() > maxVertexCount)	continue;
			final double ratio = bb.width / (double)bb.height;
			if (ratio < minRatio || ratio > maxRatio) continue;
			output.add(contour);
		}
	}

	/**
	 * Segment an image based on color ranges.
	 * @param input The image on which to perform the RGB threshold.
	 * @param red The min and max red.
	 * @param green The min and max green.
	 * @param blue The min and max blue.
	 * @param output The image in which to store the output.
	 */
	private void rgbThreshold(Mat input, double[] red, double[] green, double[] blue,
		Mat out) {
		Imgproc.cvtColor(input, out, Imgproc.COLOR_BGR2RGB);
		Core.inRange(out, new Scalar(red[0], green[0], blue[0]),
			new Scalar(red[1], green[1], blue[1]), out);
	}

	public static class Line {
		public final double x1, y1, x2, y2;
		public Line(double x1, double y1, double x2, double y2) {
			this.x1 = x1;
			this.y1 = y1;
			this.x2 = x2;
			this.y2 = y2;
		}
		public double lengthSquared() {
			return Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2);
		}
		public double length() {
			return Math.sqrt(lengthSquared());
		}
		public double angle() {
			return Math.toDegrees(Math.atan2(y2 - y1, x2 - x1));
		}
	}
	/**
	 * Finds all line segments in an image.
	 * @param input The image on which to perform the find lines.
	 * @param lineList The output where the lines are stored.
	 */
	private void findLines(Mat input, ArrayList<Line> lineList) {
		final LineSegmentDetector lsd = Imgproc.createLineSegmentDetector();
		final Mat lines = new Mat();
		lineList.clear();
		if (input.channels() == 1) {
			lsd.detect(input, lines);
		} else {
			final Mat tmp = new Mat();
			Imgproc.cvtColor(input, tmp, Imgproc.COLOR_BGR2GRAY);
			lsd.detect(tmp, lines);
		}
		if (!lines.empty()) {
			for (int i = 0; i < lines.rows(); i++) {
				lineList.add(new Line(lines.get(i, 0)[0], lines.get(i, 0)[1],
					lines.get(i, 0)[2], lines.get(i, 0)[3]));
			}
		}
	}




}

