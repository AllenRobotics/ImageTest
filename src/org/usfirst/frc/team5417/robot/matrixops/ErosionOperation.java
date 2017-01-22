package org.usfirst.frc.team5417.robot.matrixops;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.usfirst.frc.team5417.robot.MatrixUtilities;

//
//Erodes the white pixels in an image, given a kernel size
//Input: a binarized image (black and white)
//Output: a binarized image, with the white regions eroded
//
public class ErosionOperation implements MatrixOperation {

	private int regionWidth;

	public String name() { return "Erosion"; } 

	public ErosionOperation(int regionWidth) {
		this.regionWidth = regionWidth;
	}

	@Override
	public PixelMatrix doOperation(PixelMatrix m) {

		//PixelMatrix result = new PixelMatrix(m.rows(), m.cols());
		PixelMatrix result = m;

		List<Point> pointsToTurnBlack = new ArrayList<Point>();
		BooleanMatrix alreadyProcessedPixels = new BooleanMatrix(m.rows(), m.cols(), false);

		BooleanMatrix kernel = MatrixUtilities.getSquareKernel(regionWidth);

		for (int r = 0; r < m.rows(); r++) {
			for (int c = 0; c < m.cols(); c++) {

				Pixel pixel = m.get(r, c);
				//result.put(r, c, pixel);

				if (MatrixUtilities.isBlackPixel(pixel)) {
					MatrixUtilities.addAdjacentPointsIfNotProcessed(pointsToTurnBlack, alreadyProcessedPixels, r, c, m,
							kernel, MatrixUtilities.whiteColor);
				}
			}
		}

		for (Point point : pointsToTurnBlack) {
			result.put(point.getY(), point.getX(), MatrixUtilities.blackPixel);
		}

		return result;
	}

}
