package org.usfirst.frc.team5417.cvtest.matrixops;

import org.opencv.core.Mat;
import org.usfirst.frc.team5417.cv2017.customops.Pixel;
import org.usfirst.frc.team5417.cv2017.customops.PixelMatrix;
import org.usfirst.frc.team5417.cv2017.customops.PixelMatrixOperation;
import org.usfirst.frc.team5417.cv2017.MatrixUtilities;

//
// Threshold above a certain value, turning the output pixels white.
// Input: a grayscale image
// Output: a binarized (black or white) image.
//
public class ThresholdAboveOperation implements PixelMatrixOperation {

	private int threshold;

	public String name() { return "Threshold Above"; } 

	public ThresholdAboveOperation(int threshold) {
		this.threshold = threshold;
	}

	@Override
	public PixelMatrix doOperation(PixelMatrix m) {

		//PixelMatrix result = new PixelMatrix(m.rows(), m.cols());
		PixelMatrix result = m;

		for (int r = 0; r < m.rows(); ++r) {
			for (int c = 0; c < m.cols(); ++c) {
				Pixel pixel = m.get(r, c);

				if (pixel.gray() >= this.threshold) {
					result.put(r, c, MatrixUtilities.whitePixel);
				} else {
					result.put(r, c, MatrixUtilities.blackPixel);
				}
			}
		}

		return result;
	}

}
