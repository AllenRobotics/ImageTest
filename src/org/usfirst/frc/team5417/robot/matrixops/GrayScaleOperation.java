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
		PixelMatrix m2 = new PixelMatrix(m.rows(), m.cols());
		for (int r = 0; r < m.rows(); r++) {
			for (int c = 0; c < m.cols(); c++) {
				Pixel colorPixel = m.get(r, c);
				//
				// see https://en.wikipedia.org/wiki/Grayscale for grayscale conversion formula
				//
				Pixel grayPixel = new Pixel(colorPixel.gray());
				m2.put(r, c, grayPixel);
			}
		}
		return m2;
	}

}
