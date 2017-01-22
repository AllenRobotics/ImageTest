package org.usfirst.frc.team5417.robot.matrixops;

import org.opencv.core.Mat;
import org.usfirst.frc.team5417.robot.MatrixUtilities;

//
// Filters colors using the ranges passed to the constructor
// Input: a color image
// Output: a color image, with all colors outside the ChannelRanges set to black
//
public class FilterColorOperation implements MatrixOperation {

	private ChannelRange redRange;
	private ChannelRange greenRange;
	private ChannelRange blueRange;

	public FilterColorOperation(ChannelRange redRange, ChannelRange greenRange, ChannelRange blueRange) {
		this.redRange = redRange;
		this.greenRange = greenRange;
		this.blueRange = blueRange;
	}

	@Override
	public PixelMatrix doOperation(PixelMatrix m) {

		//PixelMatrix result = new PixelMatrix(m.rows(), m.cols());
		PixelMatrix result = m;

		int minRed = redRange.getLowerBound(), maxRed = redRange.getUpperBound();
		int minGreen = greenRange.getLowerBound(), maxGreen = greenRange.getUpperBound();
		int minBlue = blueRange.getLowerBound(), maxBlue = blueRange.getUpperBound();

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
