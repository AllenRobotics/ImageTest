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
		
		Mat originalMat = pixelMatrix.toMat();
		ImageWriter originalWriter = new FileImageWriter("C:/temp/originalImage.png");
		originalWriter.write(originalMat);
		
		List<MatrixOperation> operations = new ArrayList<MatrixOperation>();

		List<Matrix> horizontalTemplates = new ArrayList<Matrix>();
		horizontalTemplates.add(new Matrix(40, 150, 1));
		horizontalTemplates.add(new Matrix(20, 150, 1));

		List<Matrix> verticalTemplates = new ArrayList<Matrix>();
		verticalTemplates.add(new Matrix(150, 40, 1));
		verticalTemplates.add(new Matrix(150, 20, 1));

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
		operations.add(new MatchTemplateOperation(verticalTemplates, 0.25, 4, 0.7));

		int operationNumber = 1;

		Mat tempM = pixelMatrix.toMat();
		for (MatrixOperation op : operations) {
			pixelMatrix = op.doOperation(pixelMatrix);

			tempM = pixelMatrix.toMat();
			ImageWriter operationWriter = new FileImageWriter("C:/temp/operation-step-" + operationNumber + ".bmp");
			operationWriter.write(tempM);

			operationNumber++;
		}

		tempM = pixelMatrix.toMat();
		ImageWriter writer = new FileImageWriter("C:/temp/outputImage.png");
		writer.write(tempM);
	}

}
