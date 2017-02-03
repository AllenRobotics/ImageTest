package org.usfirst.frc.team5417.cvtest.matrixops;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.usfirst.frc.team5417.cv2017.customops.BooleanMatrix;
import org.usfirst.frc.team5417.cv2017.customops.Pixel;
import org.usfirst.frc.team5417.cv2017.customops.PixelMatrix;
import org.usfirst.frc.team5417.cv2017.customops.PixelMatrixOperation;
import org.usfirst.frc.team5417.cv2017.customops.Point;
import org.usfirst.frc.team5417.cv2017.customops.FindGroupsOperation.DisjointSetNode;
import org.usfirst.frc.team5417.cv2017.MatrixUtilities;

//
//Dilates the white pixels in an image, given a kernel size
//Input: a binarized image (black and white)
//Output: a binarized image, with the white regions dilated
//
public class DilationOperation implements PixelMatrixOperation {

	private int regionWidth;

	public String name() { return "Dilation"; } 

	public DilationOperation(int regionWidth) {
		this.regionWidth = regionWidth;
	}

	@Override
	public PixelMatrix doOperation(PixelMatrix m) {

		//PixelMatrix result = new PixelMatrix(m.rows(), m.cols());
		PixelMatrix result = m;
		
		List<Point> pointsToTurnWhite = new ArrayList<Point>();

		BooleanMatrix alreadyProcessedPixels = new BooleanMatrix(m.rows(), m.cols(), false);

		BooleanMatrix kernel = MatrixUtilities.getSquareKernel(regionWidth);

		for (int r = 0; r < m.rows(); r++) {
			for (int c = 0; c < m.cols(); c++) {

				Pixel pixel = m.get(r, c);
				//result.put(r, c, pixel);

				if (isWhitePixel(pixel)) {
					MatrixUtilities.addAdjacentPointsIfNotProcessed(pointsToTurnWhite, alreadyProcessedPixels, r, c, m,
							kernel, MatrixUtilities.blackColor);
				}
			}
		}

		for (Point point : pointsToTurnWhite) {
			result.put(point.getY(), point.getX(), MatrixUtilities.whitePixel);
		}
		
		return result;
	}

	public boolean isWhitePixel(Pixel pixel) {
		if (pixel.r == 255 && pixel.g == 255 && pixel.b == 255) {
			return true;
		} else {
			return false;
		}
	}

}
