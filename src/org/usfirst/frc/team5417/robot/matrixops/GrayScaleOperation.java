package org.usfirst.frc.team5417.robot.matrixops;

import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

//
// Convert to grayscale
// Input: a color image
// Output: a grayscale image
//
public class GrayScaleOperation implements MatrixOperation {

	@Override
	public PixelMatrix doOperation(PixelMatrix m) {

		//PixelMatrix result = new PixelMatrix(m.rows(), m.cols());
		PixelMatrix result = m;
		
		for (int r = 0; r < m.rows(); r++) {
			for (int c = 0; c < m.cols(); c++) {
				Pixel colorPixel = m.get(r, c);
				//
				// see https://en.wikipedia.org/wiki/Grayscale for grayscale conversion formula
				//
				result.put(r, c, colorPixel.gray());
			}
		}
		return result;
	}

}
