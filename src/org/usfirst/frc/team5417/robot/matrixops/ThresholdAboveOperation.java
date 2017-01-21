package org.usfirst.frc.team5417.robot.matrixops;

import org.opencv.core.Mat;
import org.usfirst.frc.team5417.robot.MatrixUtilities;

//
// Threshold above a certain value, turning the output pixels white.
// Input: a grayscale image
// Output: a binarized (black or white) image.
//
public class ThresholdAboveOperation implements MatrixOperation {

	private int threshold;

	public ThresholdAboveOperation(int threshold) {
		this.threshold = threshold;
	}

	@Override
	public PixelMatrix doOperation(PixelMatrix m) {

		PixelMatrix result = new PixelMatrix(m.rows(), m.cols());

		double[] whitePixel = { 255 };
		double[] blackPixel = { 0 };

		for (int r = 0; r < m.rows(); ++r) {
			for (int c = 0; c < m.cols(); ++c) {
				Pixel pixel = m.get(r, c);

				if (pixel.gray() >= this.threshold) {
					result.put(r, c, whitePixel);
				} else {
					result.put(r, c, blackPixel);
				}
			}
		}

		return result;
	}

}
