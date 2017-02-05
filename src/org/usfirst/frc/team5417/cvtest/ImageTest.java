package org.usfirst.frc.team5417.cvtest;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.opencv.core.Mat;
import org.opencv.core.Rect;
import org.usfirst.frc.team5417.cv2017.*;
import org.usfirst.frc.team5417.cv2017.opencvops.*;
import org.usfirst.frc.team5417.cvtest.matrixops.*;
import org.usfirst.frc.team5417.cvtest.opencvops.*;

public class ImageTest {

	public static void main(String[] args) throws IOException {

		// Calls Open CV library
		MatrixUtilities.LoadOpenCVLibraries();

		ImageReader imageReader = new FileImageReader("C:/temp/sampleimage.png");

		List<BooleanMatrix> horizontalTemplates = new ArrayList<BooleanMatrix>();
		// horizontalTemplates.add(new BooleanMatrix(40, 150, true));
		// horizontalTemplates.add(new BooleanMatrix(20, 150, true));
		horizontalTemplates.add(new BooleanMatrix(13, 50, true));
		horizontalTemplates.add(new BooleanMatrix(7, 50, true));

		List<BooleanMatrix> verticalTemplates = new ArrayList<BooleanMatrix>();
		// verticalTemplates.add(new BooleanMatrix(150, 40, true));
		// verticalTemplates.add(new BooleanMatrix(150, 20, true));
		verticalTemplates.add(new BooleanMatrix(50, 13, true));
		verticalTemplates.add(new BooleanMatrix(50, 7, true));

		List<BooleanMatrix> templatesToUse = horizontalTemplates;

		double[] gearLookUpTable = { 500, // 0 feet
				199, // 1 foot
				110, 77.5, 57, 45.5, 39, 33, 29, 25 // 9 feet
		};

		double[] lookUpTableToUse = gearLookUpTable;

		// Real images HSV
		// ChannelRange hueRange = new ChannelRange(130, 180);
		// ChannelRange satRange = new ChannelRange(.7, 1.0);
		// ChannelRange valRange = new ChannelRange(220, 256);

		// Test images HSV
		ChannelRange hueRange = new ChannelRange(130, 150);
		ChannelRange satRange = new ChannelRange(.5, 1.0);
		ChannelRange valRange = new ChannelRange(130, 200);

		int largestDimensionSize = 600;
		int dilateErodeKernelSize = 11;
		int removeGroupsSmallerThan = 100;
		int numberOfScaleFactors = 10;
		double minimumTemplateMatchPercentage = 0.7;

		//
		// warm up the jvm with JIT and stuff
		//
		DoMixtureOperations(imageReader, largestDimensionSize, hueRange, satRange, valRange, false,
				dilateErodeKernelSize, removeGroupsSmallerThan, numberOfScaleFactors, minimumTemplateMatchPercentage,
				templatesToUse, lookUpTableToUse);
		// DoPixelMatrixOperations(imageReader, redRange, greenRange, blueRange,
		// false, dilateErodeKernelSize,
		// removeGroupsSmallerThan, minimumTemplateScale, maximumTemplateScale,
		// minimumTemplateMatchPercentage,
		// templatesToUse);
		DoOpenCVOperations(imageReader, largestDimensionSize, hueRange, satRange, valRange, false,
				dilateErodeKernelSize, removeGroupsSmallerThan, numberOfScaleFactors, minimumTemplateMatchPercentage,
				templatesToUse);

		//
		// print results after warm up
		//

		// DoPixelMatrixOperations(imageReader, redRange, greenRange, blueRange,
		// true, dilateErodeKernelSize,
		// removeGroupsSmallerThan, minimumTemplateScale, maximumTemplateScale,
		// minimumTemplateMatchPercentage,
		// templatesToUse);
		// System.out.println();
		//
		DoOpenCVOperations(imageReader, largestDimensionSize, hueRange, satRange, valRange, true, dilateErodeKernelSize,
				removeGroupsSmallerThan, numberOfScaleFactors, minimumTemplateMatchPercentage, templatesToUse);
		System.out.println();

		DoMixtureOperations(imageReader, largestDimensionSize, hueRange, satRange, valRange, true,
				dilateErodeKernelSize, removeGroupsSmallerThan, numberOfScaleFactors, minimumTemplateMatchPercentage,
				templatesToUse, lookUpTableToUse);
		System.out.println();
	}

	private static Mat DoOpenCVOperation(OpenCVOperation op, Mat m, int operationNumber, Stopwatch elapsedTimeStopwatch,
			DecimalFormat decimalFormat, String logPrefix, boolean print) {

		double startElapsedSeconds = elapsedTimeStopwatch.getTotalSeconds();

		elapsedTimeStopwatch.start();
		m = op.doOperation(m);
		elapsedTimeStopwatch.stop();

		double endElapsedSeconds = elapsedTimeStopwatch.getTotalSeconds();

		if (print) {
			System.out.println(logPrefix + " Operation '" + op.name() + "' took "
					+ decimalFormat.format(endElapsedSeconds - startElapsedSeconds) + " seconds");

			ImageWriter operationWriter = new FileImageWriter(
					"C:/temp/" + logPrefix + "-operation-step-" + operationNumber + ".png");
			operationWriter.write(m);
		}

		return m;
	}

	private static PixelMatrix DoPixelMatrixOperation(PixelMatrixOperation op, PixelMatrix pixelMatrix,
			int operationNumber, Stopwatch elapsedTimeStopwatch, DecimalFormat decimalFormat, String logPrefix,
			boolean print) {

		double startElapsedSeconds = elapsedTimeStopwatch.getTotalSeconds();

		elapsedTimeStopwatch.start();
		pixelMatrix = op.doOperation(pixelMatrix);
		elapsedTimeStopwatch.stop();

		double endElapsedSeconds = elapsedTimeStopwatch.getTotalSeconds();

		if (print) {
			System.out.println(logPrefix + " Operation '" + op.name() + "' took "
					+ decimalFormat.format(endElapsedSeconds - startElapsedSeconds) + " seconds");

			Mat tempM = pixelMatrix.toMat();
			ImageWriter operationWriter = new FileImageWriter(
					"C:/temp/" + logPrefix + "-operation-step-" + operationNumber + ".png");
			operationWriter.write(tempM);
		}

		return pixelMatrix;
	}

	// private static void DoPixelMatrixOperations(ImageReader reader,
	// ChannelRange redRange, ChannelRange greenRange,
	// ChannelRange blueRange, boolean print, int dilateErodeKernelSize, int
	// removeGroupsSmallerThan,
	// double minimumTemplateScale, double maximumTemplateScale, double
	// minimumTemplateMatchPercentage,
	// List<BooleanMatrix> templatesToUse) {
	//
	// String logPrefix = "PixelMatrix";
	//
	// Mat m = reader.read();
	// m = MatrixUtilities.reverseColorChannels(m);
	//
	// if (print) {
	// Mat tempM = new PixelMatrix(m).toMat();
	// ImageWriter writer = new FileImageWriter("C:/temp/" + logPrefix +
	// "-operation-step-0.png");
	// writer.write(tempM);
	// }
	//
	// PixelMatrix pixelMatrix = new PixelMatrix(m);
	//
	// List<MatrixOperation> operations = new ArrayList<MatrixOperation>();
	//
	// // filter colors
	// MatrixOperation filterOp = new FilterColorOperation(redRange, greenRange,
	// blueRange);
	//
	// // convert to gray scale
	// MatrixOperation grayScaleOp = new GrayScaleOperation();
	//
	// // threshold above some pixel value (operates on gray scale)
	// MatrixOperation thresholdOp = new ThresholdAboveOperation(50);
	//
	// // dilate the white areas in the image to "heal" broken lines
	// MatrixOperation dilateOp = new DilationOperation(dilateErodeKernelSize);
	//
	// // erode the white areas in the image (sort of undoes the dilation, but
	// // keeps "healed" lines)
	// MatrixOperation erodeOp = new ErosionOperation(dilateErodeKernelSize);
	//
	// // find groups (operates on gray scale, outputs color)
	// MatrixOperation findGroupsOp = new FindGroupsOperation();
	//
	// DecimalFormat decimalFormat = new DecimalFormat("0.000000");
	// Stopwatch elapsedSecondsStopwatch = new Stopwatch();
	//
	// int operationNumber = 1;
	// pixelMatrix = DoPixelMatrixOperation(filterOp, pixelMatrix,
	// operationNumber++, elapsedSecondsStopwatch,
	// decimalFormat, logPrefix, print);
	// pixelMatrix = DoPixelMatrixOperation(grayScaleOp, pixelMatrix,
	// operationNumber++, elapsedSecondsStopwatch,
	// decimalFormat, logPrefix, print);
	// pixelMatrix = DoPixelMatrixOperation(thresholdOp, pixelMatrix,
	// operationNumber++, elapsedSecondsStopwatch,
	// decimalFormat, logPrefix, print);
	// pixelMatrix = DoPixelMatrixOperation(dilateOp, pixelMatrix,
	// operationNumber++, elapsedSecondsStopwatch,
	// decimalFormat, logPrefix, print);
	// pixelMatrix = DoPixelMatrixOperation(erodeOp, pixelMatrix,
	// operationNumber++, elapsedSecondsStopwatch,
	// decimalFormat, logPrefix, print);
	// pixelMatrix = DoPixelMatrixOperation(findGroupsOp, pixelMatrix,
	// operationNumber++, elapsedSecondsStopwatch,
	// decimalFormat, logPrefix, print);
	//
	// double startElapsedSeconds = elapsedSecondsStopwatch.getTotalSeconds();
	// elapsedSecondsStopwatch.start();
	//
	// // calculate the group sizes
	// HashMap<Pixel, Integer> groupSizes =
	// MatrixUtilities.getGroupSizes(pixelMatrix);
	//
	// elapsedSecondsStopwatch.stop();
	// double endElapsedSeconds = elapsedSecondsStopwatch.getTotalSeconds();
	//
	// if (print)
	// System.out.println(
	// "getGroupSizes took " + decimalFormat.format(endElapsedSeconds -
	// startElapsedSeconds) + " seconds");
	//
	// // remove all groups with too few pixels
	// MatrixOperation removeGroupsOp = new
	// RemoveSmallGroupsOperation(removeGroupsSmallerThan, groupSizes);
	//
	// // remove all groups that don't match a template
	// MatrixOperation matchTemplatesOp = new
	// MatchTemplatesOperation(templatesToUse, minimumTemplateScale,
	// maximumTemplateScale, minimumTemplateMatchPercentage, groupSizes);
	//
	// pixelMatrix = DoPixelMatrixOperation(removeGroupsOp, pixelMatrix,
	// operationNumber++, elapsedSecondsStopwatch,
	// decimalFormat, logPrefix, print);
	// pixelMatrix = DoPixelMatrixOperation(matchTemplatesOp, pixelMatrix,
	// operationNumber++, elapsedSecondsStopwatch,
	// decimalFormat, logPrefix, print);
	//
	// if (print) {
	// System.out.println("All " + logPrefix + " operations took "
	// + decimalFormat.format(elapsedSecondsStopwatch.getTotalSeconds()) + "
	// seconds");
	//
	// Mat tempM = pixelMatrix.toMat();
	// ImageWriter writer = new FileImageWriter("C:/temp/" + logPrefix +
	// "-outputImage.png");
	// writer.write(tempM);
	// }
	//
	// if (print) {
	// double distance = 0;
	// try {
	// FindDistanceOperation findDistanceOp = new FindDistanceOperation();
	// distance = findDistanceOp.findDistance(pixelMatrix);
	// System.out.println("distance equals " + distance);
	// } catch (Exception ex) {
	// System.err.println(ex.getMessage());
	// }
	// }
	// }
	//
	private static void DoOpenCVOperations(ImageReader reader, int largestDimensionSize, ChannelRange c1Range,
			ChannelRange c2Range, ChannelRange c3Range, boolean print, int dilateErodeKernelSize,
			int removeGroupsSmallerThan, int numberOfScaleFactors, double minimumTemplateMatchPercentage,
			List<BooleanMatrix> templatesToUse) {

		String logPrefix = "OpenCV";

		Mat m = reader.read();

		ImageScaleOperation scaleOp = new ImageScaleOperation(m, largestDimensionSize);
		m = scaleOp.resize();

		// m = MatrixUtilities.reverseColorChannels(m);

		OCVBGR2HSVOperation bgr2hsvOp = new OCVBGR2HSVOperation();
		m = bgr2hsvOp.doOperation(m);

		if (print) {
			Mat tempM = new PixelMatrix(m).toMat();
			ImageWriter writer = new FileImageWriter("C:/temp/" + logPrefix + "-operation-step-0.png");
			writer.write(tempM);
		}

		// filter colors
		OpenCVOperation filterOp = new OCVFilterColorOperation(c1Range, c2Range, c3Range);

		// dilate the white areas in the image to "heal" broken lines
		OpenCVOperation dilateOp = new OCVDilationOperation(dilateErodeKernelSize);

		// erode the white areas in the image (sort of undoes the dilation, but
		// keeps "healed" lines)
		OpenCVOperation erodeOp = new OCVErosionOperation(dilateErodeKernelSize);

		// find groups (operates on gray scale, outputs color)
		//
		// only use one of these methods. just choose the fastest one
		OCVFindGroupsWithFillOperation findGroupsOp = new OCVFindGroupsWithFillOperation();
		// OCVFindGroupsOperation findGroupsOp = new OCVFindGroupsOperation();

		DecimalFormat decimalFormat = new DecimalFormat("0.000000");
		Stopwatch elapsedSecondsStopwatch = new Stopwatch();

		int operationNumber = 1;
		m = DoOpenCVOperation(filterOp, m, operationNumber++, elapsedSecondsStopwatch, decimalFormat, logPrefix, print);
		m = DoOpenCVOperation(dilateOp, m, operationNumber++, elapsedSecondsStopwatch, decimalFormat, logPrefix, print);
		m = DoOpenCVOperation(erodeOp, m, operationNumber++, elapsedSecondsStopwatch, decimalFormat, logPrefix, print);
		m = DoOpenCVOperation(findGroupsOp, m, operationNumber++, elapsedSecondsStopwatch, decimalFormat, logPrefix,
				print);

		List<Color> groupColors = findGroupsOp.getOutputColors();

		// remove all groups with too few pixels
		OCVMatchTemplatesAndRemoveSmallGroupsOperation matchAndRemoveOp = new OCVMatchTemplatesAndRemoveSmallGroupsOperation(
				removeGroupsSmallerThan, templatesToUse, numberOfScaleFactors, minimumTemplateMatchPercentage,
				groupColors);

		m = DoOpenCVOperation(matchAndRemoveOp, m, operationNumber++, elapsedSecondsStopwatch, decimalFormat, logPrefix,
				print);

		if (print) {
			System.out.println("All " + logPrefix + " operations took "
					+ decimalFormat.format(elapsedSecondsStopwatch.getTotalSeconds()) + " seconds");

			ImageWriter writer = new FileImageWriter("C:/temp/" + logPrefix + "-outputImage.png");
			writer.write(m);
		}

		if (print) {
			double distance = 0;
			try {
				// PixelMatrix pixelMatrix = new PixelMatrix(m);
				//
				// FindDistanceOperation findDistanceOp = new
				// FindDistanceOperation();
				// distance = findDistanceOp.findDistance(pixelMatrix);
				// System.out.println("distance equals " + distance);
			} catch (Exception ex) {
				System.err.println(ex.getMessage());
			}
		}
	}

	private static void DoMixtureOperations(ImageReader reader, int largestDimensionSize, ChannelRange c1Range,
			ChannelRange c2Range, ChannelRange c3Range, boolean print, int dilateErodeKernelSize,
			int removeGroupsSmallerThan, int numberOfScaleFactors, double minimumTemplateMatchPercentage,
			List<BooleanMatrix> templatesToUse, double[] lookUpTableToUse) {

		String logPrefix = "Mixture";

		Mat m = reader.read();

		ImageScaleOperation scaleOp = new ImageScaleOperation(m, largestDimensionSize);
		m = scaleOp.resize();

		// m = MatrixUtilities.reverseColorChannels(m);

		OCVBGR2HSVOperation bgr2hsvOp = new OCVBGR2HSVOperation();
		m = bgr2hsvOp.doOperation(m);

		if (print) {

			// Mat m2 = new Mat(m, new Rect(new org.opencv.core.Point(0, 0),
			// m.size()));
			// m2 = hsv2bgrOp.doOperation(m2);
			// m2 = MatrixUtilities.reverseColorChannels(m2);
			//
			// Mat tempM = new PixelMatrix(m2).toMat();
			ImageWriter writer = new FileImageWriter("C:/temp/" + logPrefix + "-operation-step-0.png");
			writer.write(m);
		}

		// filter colors
		OpenCVOperation filterOp = new OCVFilterColorOperation(c1Range, c2Range, c3Range);

		// // dilate the white areas in the image to "heal" broken lines
		OpenCVOperation dilateOp = new OCVDilationOperation(dilateErodeKernelSize);

		// // erode the white areas in the image (sort of undoes the dilation,
		// but keeps "healed" lines)
		OpenCVOperation erodeOp = new OCVErosionOperation(dilateErodeKernelSize);

		// find groups (operates on gray scale, outputs color)
		PixelMatrixOperation findGroupsOp = new FindGroupsOperation();

		DecimalFormat decimalFormat = new DecimalFormat("0.000000");
		Stopwatch elapsedSecondsStopwatch = new Stopwatch();

		int operationNumber = 1;
		m = DoOpenCVOperation(filterOp, m, operationNumber++, elapsedSecondsStopwatch, decimalFormat, logPrefix, print);
		m = DoOpenCVOperation(dilateOp, m, operationNumber++, elapsedSecondsStopwatch, decimalFormat, logPrefix, print);
		m = DoOpenCVOperation(erodeOp, m, operationNumber++, elapsedSecondsStopwatch, decimalFormat, logPrefix, print);

		double startElapsedSeconds = elapsedSecondsStopwatch.getTotalSeconds();
		elapsedSecondsStopwatch.start();

		// convert open cv mat to PixelMatrix
		PixelMatrix pixelMatrix = new PixelMatrix(m);

		elapsedSecondsStopwatch.stop();
		double endElapsedSeconds = elapsedSecondsStopwatch.getTotalSeconds();

		if (print)
			System.out.println("Convert from Mat to PixelMatrix took "
					+ decimalFormat.format(endElapsedSeconds - startElapsedSeconds) + " seconds");

		pixelMatrix = DoPixelMatrixOperation(findGroupsOp, pixelMatrix, operationNumber++, elapsedSecondsStopwatch,
				decimalFormat, logPrefix, print);

		startElapsedSeconds = elapsedSecondsStopwatch.getTotalSeconds();
		elapsedSecondsStopwatch.start();

		// calculate the group sizes
		HashMap<Pixel, Integer> groupSizes = MatrixUtilities.getGroupSizes(pixelMatrix);

		elapsedSecondsStopwatch.stop();
		endElapsedSeconds = elapsedSecondsStopwatch.getTotalSeconds();

		if (print)
			System.out.println(
					"getGroupSizes took " + decimalFormat.format(endElapsedSeconds - startElapsedSeconds) + " seconds");

		// remove all groups with too few pixels
		PixelMatrixOperation removeGroupsOp = new RemoveSmallGroupsOperation(removeGroupsSmallerThan, groupSizes);

		// remove all groups that don't match a template
		PixelMatrixOperation matchTemplatesOp = new MatchTemplatesOperation(templatesToUse, numberOfScaleFactors,
				minimumTemplateMatchPercentage, groupSizes);

		pixelMatrix = DoPixelMatrixOperation(removeGroupsOp, pixelMatrix, operationNumber++, elapsedSecondsStopwatch,
				decimalFormat, logPrefix, print);
		pixelMatrix = DoPixelMatrixOperation(matchTemplatesOp, pixelMatrix, operationNumber++, elapsedSecondsStopwatch,
				decimalFormat, logPrefix, print);

		if (print) {
			double distance = 0;
			try {
				// HashMap<Pixel, Point> centersOfMass =
				// MatrixUtilities.findCentersOfMass(pixelMatrix);
				// FindDistanceOperation findDistanceOp = new
				// FindDistanceOperation(centersOfMass, lookUpTableToUse);
				//
				// elapsedSecondsStopwatch.start();
				// distance = findDistanceOp.findDistanceInPixels() *
				// scaleOp.getInverseScaleFactor();
				// distance = findDistanceOp.findDistanceInFeet(distance);
				// elapsedSecondsStopwatch.stop();
				//
				// System.out.println("distance equals " + distance);
				//
				// FindTargetPointOperation findTargetOp = new
				// FindTargetPointOperation(centersOfMass);
				//
				// elapsedSecondsStopwatch.start();
				// PointD targetPoint = findTargetOp.findTargetPoint();
				// targetPoint =
				// targetPoint.adjustByScale(scaleOp.getInverseScaleFactor());
				// elapsedSecondsStopwatch.stop();
				//
				// System.out.println("Target Point = (" + targetPoint.getX() +
				// ", " + targetPoint.getY() + ")");

			} catch (Exception ex) {
				System.err.println(ex.getMessage());
			}
		}

		if (print)
			System.out.println("All " + logPrefix + " operations took "
					+ decimalFormat.format(elapsedSecondsStopwatch.getTotalSeconds()) + " seconds");

		if (print) {
			m = pixelMatrix.toMat();
			ImageWriter writer = new FileImageWriter("C:/temp/" + logPrefix + "-outputImage.png");
			writer.write(m);
		}

	}
}
