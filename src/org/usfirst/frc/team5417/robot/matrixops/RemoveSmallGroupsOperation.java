package org.usfirst.frc.team5417.robot.matrixops;

import java.util.HashMap;
import java.util.HashSet;

import org.usfirst.frc.team5417.robot.MatrixUtilities;

//
//Removes small groups from the image
//Input: a color image, where each group is a unique color
//Output: a color image, where small groups of color have been removed
//
public class RemoveSmallGroupsOperation implements MatrixOperation {

	private int minimumGroupPixelCount;

	public String name() { return "Remove Small Groups"; } 

	public RemoveSmallGroupsOperation(int minimumGroupPixelCount) {
		this.minimumGroupPixelCount = minimumGroupPixelCount;
	}

	@Override
	public PixelMatrix doOperation(PixelMatrix m) {

//		PixelMatrix result = new PixelMatrix(m.rows(), m.cols());
//		for (int r = 0; r < m.rows(); r++) {
//			for (int c = 0; c < m.cols(); c++) {
//
//				Pixel pixel = m.get(r, c);
//				result.put(r, c, pixel);
//
//			}
//		}

		PixelMatrix result = m;


		HashMap<Pixel, Integer> groupsToCount = MatrixUtilities.getGroupSizes(m);
		HashSet<Pixel> colorsToRemove = new HashSet<>();

		for (Pixel color : groupsToCount.keySet()) {
			Integer count = groupsToCount.get(color);

			if (count < minimumGroupPixelCount) {
				// NOTE: we MUST make a new Pixel here because we mutate the pixels
				// below as we go, thus invalidating any of them that have been added
				// to the colorsToRemove HashSet
				colorsToRemove.add(new Pixel(color.location.getX(), color.location.getY(), color));
			}
		}

		for (int r = 0; r < m.rows(); r++) {
			for (int c = 0; c < m.cols(); c++) {
				Pixel pixel = m.get(r, c);

				if (!MatrixUtilities.isBlackPixel(pixel)) {
					if (colorsToRemove.contains(pixel)) {
						result.put(r, c, MatrixUtilities.blackPixel);
					}
				}

			}

		}

		return result;
	}

}
