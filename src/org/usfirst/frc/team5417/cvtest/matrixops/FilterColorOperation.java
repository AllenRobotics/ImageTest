package org.usfirst.frc.team5417.cvtest.matrixops;

import org.opencv.core.Mat;
import org.usfirst.frc.team5417.cv2017.ChannelRange;
import org.usfirst.frc.team5417.cv2017.customops.Pixel;
import org.usfirst.frc.team5417.cv2017.customops.PixelMatrix;
import org.usfirst.frc.team5417.cv2017.customops.PixelMatrixOperation;
import org.usfirst.frc.team5417.cv2017.MatrixUtilities;

//
// Filters colors using the ranges passed to the constructor
// Input: a color image
// Output: a color image, with all colors outside the ChannelRanges set to black
//
public class FilterColorOperation implements PixelMatrixOperation {

	private ChannelRange c1Range;
	private ChannelRange c2Range;
	private ChannelRange c3Range;

	public String name() { return "Filter Color"; } 

	public FilterColorOperation(ChannelRange c1Range, ChannelRange c2Range, ChannelRange c3Range) {
		this.c1Range = c1Range;
		this.c2Range = c2Range;
		this.c3Range = c3Range;
	}

	@Override
	public PixelMatrix doOperation(PixelMatrix m) {

		//PixelMatrix result = new PixelMatrix(m.rows(), m.cols());
		PixelMatrix result = m;

		double minRed = c1Range.getLowerBound(), maxRed = c1Range.getUpperBound();
		double minGreen = c2Range.getLowerBound(), maxGreen = c2Range.getUpperBound();
		double minBlue = c3Range.getLowerBound(), maxBlue = c3Range.getUpperBound();

		for (int r = 0; r < m.rows(); ++r) {
			for (int c = 0; c < m.cols(); ++c) {
				Pixel pixel = m.get(r, c);

				// If pixel color is out of range in any value,
				// the pixel turns black
				if (pixel.r < minRed || pixel.r > maxRed) {
					result.put(r, c, MatrixUtilities.blackPixel);
				} else if (pixel.g < minGreen || pixel.g > maxGreen) {
					result.put(r, c, MatrixUtilities.blackPixel);
				} else if (pixel.b < minBlue || pixel.b > maxBlue) {
					result.put(r, c, MatrixUtilities.blackPixel);
				} else {
					// if the rgb values for this pixel are in the
					// range, pass it through
					result.put(r, c, pixel);
				}
			}
		}

		return result;

	}
}
