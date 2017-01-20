package org.usfirst.frc.team5417.robot;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.opencv.core.Mat;
import org.usfirst.frc.team5417.robot.matrixops.*;

public class ImageTest {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		MatrixUtilities.LoadOpenCVLibraries();
		// Calls Open CV library

		ImageReader reader = new FileImageReader("C:/temp/sampleimage.png");
		Mat m = reader.read();

		//m = MatrixUtilities.reverseColorChannels(m);
		PixelMatrix pixelMatrix = new PixelMatrix(m);
		
		Mat originalMat = pixelMatrix.toColorMat();
		ImageWriter originalWriter = new FileImageWriter("C:/temp/originalImage.png");
		originalWriter.write(originalMat);
		
		List<MatrixOperation> operations = new ArrayList<MatrixOperation>();

		// filter colors
		operations.add(new FilterColorOperation(
				new ChannelRange(10, 60),
				new ChannelRange(140, 210),
				new ChannelRange(40, 110)));
		// convert to gray scale
		operations.add(new GrayScaleOperation());
		// threshold above some pixel value (operates on gray scale)
		operations.add(new ThresholdAboveOperation(50));
		// find groups (operates on gray scale, outputs color)
		operations.add(new FindGroupsOperation());

		int operationNumber = 1;

		Mat tempM = pixelMatrix.toColorMat();
		for (MatrixOperation op : operations) {
			pixelMatrix = op.doOperation(pixelMatrix);

			tempM = pixelMatrix.toColorMat();
			ImageWriter operationWriter = new FileImageWriter("C:/temp/operation-step-" + operationNumber + ".bmp");
			operationWriter.write(tempM);

			operationNumber++;
		}

		ImageWriter writer = new FileImageWriter("C:/temp/outputImage.png");
		writer.write(tempM);
	}

}
