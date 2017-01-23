package org.usfirst.frc.team5417.robot.opencvops;

import java.util.HashMap;
import java.util.HashSet;

import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.usfirst.frc.team5417.robot.MatrixUtilities;
import org.usfirst.frc.team5417.robot.matrixops.Pixel;
import org.usfirst.frc.team5417.robot.matrixops.PixelMatrix;

public class OCVRemoveSmallGroupsOperation implements OpenCVOperation {

	private static double[] blackPixel = { 0, 0, 0 };

	private int minimumGroupPixelCount;
	private HashMap<Color, Integer> groupSizes;

	public String name() { return "Open CV Remove Small Groups"; } 

	public OCVRemoveSmallGroupsOperation(int minimumGroupPixelCount, HashMap<Color, Integer> groupSizes) {
		this.minimumGroupPixelCount = minimumGroupPixelCount;
		this.groupSizes = groupSizes;
	}

	@Override
	public Mat doOperation(Mat m) {

		Mat result = new Mat(m.size(), CvType.CV_32SC3);

		// find the colors to remove
		HashSet<Color> colorsToRemove = new HashSet<>();
		for (Color color : groupSizes.keySet()) {
			Integer count = groupSizes.get(color);
			if (count < minimumGroupPixelCount) {
				colorsToRemove.add(color);
			}
		}

		// declare the color outside of the loop to save expensive memory
		// allocations
		Color color = new Color(0, 0, 0);

		int[] pixel = new int[3];
		for (int y = 0; y < m.rows(); y++) {
			for (int x = 0; x < m.cols(); x++) {
				m.get(y, x, pixel);

				// if the pixel is black
				if (pixel[0] == blackPixel[0] && pixel[1] == blackPixel[1] && pixel[2] == blackPixel[2]) {
					result.put(y, x, pixel);
				}
				// else the pixel is not black, so decide whether to keep it or not
				else {
					// update the pixel value in the pixelObject
					color.put(pixel);

					// if this component is too small, then set it to black
					if (colorsToRemove.contains(color)) {
						result.put(y, x, blackPixel);
					} else {
						result.put(y, x, pixel);
					}
				}
			}

		}

		return result;
	}

}
