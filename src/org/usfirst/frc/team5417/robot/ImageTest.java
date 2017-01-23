package org.usfirst.frc.team5417.robot;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.opencv.core.Mat;
import org.usfirst.frc.team5417.robot.matrixops.*;
import org.usfirst.frc.team5417.robot.opencvops.OCVDilationOperation;
import org.usfirst.frc.team5417.robot.opencvops.OCVErosionOperation;
import org.usfirst.frc.team5417.robot.opencvops.OCVFileImageReader;
import org.usfirst.frc.team5417.robot.opencvops.OCVFileImageWriter;
import org.usfirst.frc.team5417.robot.opencvops.OCVFilterColorOperation;
import org.usfirst.frc.team5417.robot.opencvops.OCVFindGroupsOperation;
import org.usfirst.frc.team5417.robot.opencvops.OCVFindGroupsWithFillOperation;
import org.usfirst.frc.team5417.robot.opencvops.OCVMatchTemplatesOperation;
import org.usfirst.frc.team5417.robot.opencvops.OCVRemoveSmallGroupsOperation;
import org.usfirst.frc.team5417.robot.opencvops.OpenCVOperation;

public class ImageTest {

	public static void main(String[] args) throws IOException {


		// Calls Open CV library
		MatrixUtilities.LoadOpenCVLibraries();

		
		// warm up the jvm with JIT and stuff
		DoMixtureOperations(false);
		DoPixelMatrixOperations(false);
		DoOpenCVOperations(false);

		// print results

		DoPixelMatrixOperations(true);
		System.out.println();

		DoOpenCVOperations(true);
		System.out.println();

		DoMixtureOperations(true);
		System.out.println();
	}

	private static void DoPixelMatrixOperations(boolean print) {
		ImageReader reader = new FileImageReader("C:/temp/sampleimage.png");
		Mat m = reader.read();

		//m = MatrixUtilities.reverseColorChannels(m);
		PixelMatrix pixelMatrix = new PixelMatrix(m);
	
		Mat originalMat = pixelMatrix.toMat();
		ImageWriter originalWriter = new FileImageWriter("C:/temp/PixelMatrix-originalImage.png");
		originalWriter.write(originalMat);
		
		List<MatrixOperation> operations = new ArrayList<MatrixOperation>();

		List<BooleanMatrix> horizontalTemplates = new ArrayList<BooleanMatrix>();
		horizontalTemplates.add(new BooleanMatrix(40, 150, true));
		horizontalTemplates.add(new BooleanMatrix(20, 150, true));

		List<BooleanMatrix> verticalTemplates = new ArrayList<BooleanMatrix>();
		verticalTemplates.add(new BooleanMatrix(150, 40, true));
		verticalTemplates.add(new BooleanMatrix(150, 20, true));

		// filter colors
		operations.add(new FilterColorOperation(
				new ChannelRange(10, 60),
				new ChannelRange(140, 210),
				new ChannelRange(40, 110)));
		// convert to gray scale
		operations.add(new GrayScaleOperation());
		// threshold above some pixel value (operates on gray scale)
		operations.add(new ThresholdAboveOperation(50));
		// dilate the white areas in the image to "heal" broken lines
		operations.add(new DilationOperation(11));
		// erode the white areas in the image (sort of undoes the dilation, but keeps "healed" lines)
		operations.add(new ErosionOperation(11));
		// find groups (operates on gray scale, outputs color)
		operations.add(new FindGroupsOperation());
		// remove all groups with too few pixels
		operations.add(new RemoveSmallGroupsOperation(100));
		// remove all groups that don't match a template
		operations.add(new MatchTemplatesOperation(verticalTemplates, 0.25, 4, 0.7));

		int operationNumber = 1;

		Stopwatch elapsedSecondsStopwatch = new Stopwatch();
		DecimalFormat decimalFormat = new DecimalFormat("0.000000");
		
		for (MatrixOperation op : operations) {
			
			pixelMatrix = DoPixelMatrixOperation(op, pixelMatrix, operationNumber++, elapsedSecondsStopwatch, decimalFormat, "PixelMatrix", print);

		}

		if (print) {
			System.out.println("All PixelMatrix operations took " + decimalFormat.format(elapsedSecondsStopwatch.getTotalSeconds()) + " seconds");
			
			Mat tempM = pixelMatrix.toMat();
			ImageWriter writer = new FileImageWriter("C:/temp/PixelMatrix-outputImage.png");
			writer.write(tempM);
		}
	}
	
	private static void DoOpenCVOperations(boolean print) {
		ImageReader reader = new OCVFileImageReader("C:/temp/sampleimage.png");
		Mat m = reader.read();

		ImageWriter originalWriter = new OCVFileImageWriter("C:/temp/OpenCV-originalImage.png");
		originalWriter.write(m);

		m = MatrixUtilities.reverseColorChannels(m);
	
		List<OpenCVOperation> operations = new ArrayList<OpenCVOperation>();

		List<BooleanMatrix> horizontalTemplates = new ArrayList<BooleanMatrix>();
		horizontalTemplates.add(new BooleanMatrix(80, 300, true));
		horizontalTemplates.add(new BooleanMatrix(40, 300, true));

		List<BooleanMatrix> verticalTemplates = new ArrayList<BooleanMatrix>();
		verticalTemplates.add(new BooleanMatrix(300, 80, true));
		verticalTemplates.add(new BooleanMatrix(300, 40, true));

		// filter colors
		operations.add(new OCVFilterColorOperation(
				new ChannelRange(10, 60),
				new ChannelRange(140, 210),
				new ChannelRange(40, 110)));
//		// dilate the white areas in the image to "heal" broken lines
		operations.add(new OCVDilationOperation(11));
//		// erode the white areas in the image (sort of undoes the dilation, but keeps "healed" lines)
		operations.add(new OCVErosionOperation(11));

		// find groups (operates on gray scale, outputs color)
		//
		// only use one of these methods. just choose the fastest one
//		operations.add(new OCVFindGroupsWithFillOperation());
		operations.add(new OCVFindGroupsOperation());
		
//		// remove all groups with too few pixels
		operations.add(new OCVRemoveSmallGroupsOperation(100));
//		// remove all groups that don't match a template
//		operations.add(new OCVMatchTemplatesOperation(horizontalTemplates, 0.25, 4, 0.7));
		operations.add(new OCVMatchTemplatesOperation(verticalTemplates, 0.25, 4, 0.7));

		int operationNumber = 1;

		Stopwatch elapsedSecondsStopwatch = new Stopwatch();
		DecimalFormat decimalFormat = new DecimalFormat("0.000000");
		
		for (OpenCVOperation op : operations) {
			
			m = DoOpenCVOperation(op, m, operationNumber++, elapsedSecondsStopwatch, decimalFormat, "OpenCV", print);

		}

		if (print) {
			System.out.println("All OpenCV operations took " + decimalFormat.format(elapsedSecondsStopwatch.getTotalSeconds()) + " seconds");
			
			ImageWriter writer = new OCVFileImageWriter("C:/temp/OpenCV-outputImage.png");
			writer.write(m);
		}
	}
	
	private static Mat DoOpenCVOperation(OpenCVOperation op, Mat m, int operationNumber, Stopwatch elapsedTimeStopwatch, DecimalFormat decimalFormat, String logPrefix, boolean print) {

		double startElapsedSeconds = elapsedTimeStopwatch.getTotalSeconds();
		
		elapsedTimeStopwatch.start();
		m = op.doOperation(m);
		elapsedTimeStopwatch.stop();
		
		double endElapsedSeconds = elapsedTimeStopwatch.getTotalSeconds();

		if (print) {
			System.out.println(logPrefix + " Operation '" + op.name() + "' took " + decimalFormat.format(endElapsedSeconds - startElapsedSeconds) + " seconds");
			
			ImageWriter operationWriter = new OCVFileImageWriter("C:/temp/" + logPrefix + "-operation-step-" + operationNumber + ".bmp");
			operationWriter.write(m);
		}
		
		return m;
	}
	
	private static PixelMatrix DoPixelMatrixOperation(MatrixOperation op, PixelMatrix pixelMatrix, int operationNumber, Stopwatch elapsedTimeStopwatch, DecimalFormat decimalFormat, String logPrefix, boolean print) {
		
		double startElapsedSeconds = elapsedTimeStopwatch.getTotalSeconds();
		
		elapsedTimeStopwatch.start();
		pixelMatrix = op.doOperation(pixelMatrix);
		elapsedTimeStopwatch.stop();
		
		double endElapsedSeconds = elapsedTimeStopwatch.getTotalSeconds();
		
		if (print) {
			System.out.println(logPrefix + " Operation '" + op.name() + "' took " + decimalFormat.format(endElapsedSeconds - startElapsedSeconds) + " seconds");
	
			Mat tempM = pixelMatrix.toMat();
			ImageWriter operationWriter = new FileImageWriter("C:/temp/" + logPrefix + "-operation-step-" + operationNumber + ".bmp");
			operationWriter.write(tempM);
		}
		
		return pixelMatrix;
	}

	private static void DoMixtureOperations(boolean print) {
		ImageReader reader = new OCVFileImageReader("C:/temp/sampleimage.png");
		Mat m = reader.read();

		ImageWriter originalWriter = new OCVFileImageWriter("C:/temp/Mixture-originalImage.png");
		originalWriter.write(m);

		m = MatrixUtilities.reverseColorChannels(m);
	

		List<BooleanMatrix> horizontalTemplates = new ArrayList<BooleanMatrix>();
		horizontalTemplates.add(new BooleanMatrix(80, 300, true));
		horizontalTemplates.add(new BooleanMatrix(40, 300, true));

		List<BooleanMatrix> verticalTemplates = new ArrayList<BooleanMatrix>();
		verticalTemplates.add(new BooleanMatrix(300, 80, true));
		verticalTemplates.add(new BooleanMatrix(300, 40, true));

		
		// filter colors
		OpenCVOperation filterOp = new OCVFilterColorOperation(
				new ChannelRange(10, 60),
				new ChannelRange(140, 210),
				new ChannelRange(40, 110));

		
//		// dilate the white areas in the image to "heal" broken lines
		OpenCVOperation dilateOp = new OCVDilationOperation(11);

		
//		// erode the white areas in the image (sort of undoes the dilation, but keeps "healed" lines)
		OpenCVOperation erodeOp = new OCVErosionOperation(11);

		
		// find groups (operates on gray scale, outputs color)
		MatrixOperation findGroupsOp = new FindGroupsOperation();

				
		// remove all groups with too few pixels
		MatrixOperation removeGroupsOp = new RemoveSmallGroupsOperation(100);

		
		// remove all groups that don't match a template
		MatrixOperation matchTemplatesOp = new MatchTemplatesOperation(verticalTemplates, 0.25, 4, 0.7);


		DecimalFormat decimalFormat = new DecimalFormat("0.000000");
		Stopwatch elapsedSecondsStopwatch = new Stopwatch();
		
		int operationNumber = 1;
		m = DoOpenCVOperation(filterOp, m, operationNumber++, elapsedSecondsStopwatch, decimalFormat, "Mixture", print);
		m = DoOpenCVOperation(dilateOp, m, operationNumber++, elapsedSecondsStopwatch, decimalFormat, "Mixture", print);
		m = DoOpenCVOperation(erodeOp, m, operationNumber++, elapsedSecondsStopwatch, decimalFormat, "Mixture", print);


		double startElapsedSeconds = elapsedSecondsStopwatch.getTotalSeconds();
		elapsedSecondsStopwatch.start();
		
		// convert open cv mat to PixelMatrix
		//m = MatrixUtilities.reverseColorChannels(m);
		PixelMatrix pixelMatrix = new PixelMatrix(m);
		
		elapsedSecondsStopwatch.stop();
		double endElapsedSeconds = elapsedSecondsStopwatch.getTotalSeconds();
		
		if (print)
			System.out.println("Convert from Mat to PixelMatrix took " + decimalFormat.format(endElapsedSeconds - startElapsedSeconds) + " seconds");

		pixelMatrix = DoPixelMatrixOperation(findGroupsOp, pixelMatrix, operationNumber++, elapsedSecondsStopwatch, decimalFormat, "Mixture", print);
		pixelMatrix = DoPixelMatrixOperation(removeGroupsOp, pixelMatrix, operationNumber++, elapsedSecondsStopwatch, decimalFormat, "Mixture", print);
		pixelMatrix = DoPixelMatrixOperation(matchTemplatesOp, pixelMatrix, operationNumber++, elapsedSecondsStopwatch, decimalFormat, "Mixture", print);

		if (print)
			System.out.println("All Mixture operations took " + decimalFormat.format(elapsedSecondsStopwatch.getTotalSeconds()) + " seconds");
		
		if (print) {
			m = pixelMatrix.toMat();
			ImageWriter writer = new FileImageWriter("C:/temp/Mixture-outputImage.png");
			writer.write(m);
		}
	}
}
