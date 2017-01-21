package org.usfirst.frc.team5417.robot.matrixops;

import java.util.HashMap;
import java.util.HashSet;

import org.usfirst.frc.team5417.robot.MatrixUtilities;

public class RemoveSmallGroupsOperation implements MatrixOperation {

	private int minimumGroupPixelCount;

	public RemoveSmallGroupsOperation(int minimumGroupPixelCount) {
		this.minimumGroupPixelCount = minimumGroupPixelCount;
	}

	@Override
	public PixelMatrix doOperation(PixelMatrix m) {
		// TODO Auto-generated method stub

		HashMap<Color, Integer> groupsToCount = new HashMap<>();

		PixelMatrix result = new PixelMatrix(m.rows(), m.cols());

		for (int r = 0; r < m.rows(); r++) {
			for (int c = 0; c < m.cols(); c++) {

				Pixel pixel = m.get(r, c);
				result.put(r, c, pixel);

				if (!MatrixUtilities.isBlackPixel(pixel)) {
					Color color = new Color(pixel);
					if (groupsToCount.containsKey(color)) {
						Integer count = groupsToCount.get(color);
						groupsToCount.put(color, count + 1);
					} else {
						groupsToCount.put(color, 1);
					}
				}
			}
		}

		HashSet<Color> colorsToRemove = new HashSet<>();

		for (Color color : groupsToCount.keySet()) {
			Integer count = groupsToCount.get(color);

			if (count < minimumGroupPixelCount) {
				colorsToRemove.add(color);
			}
		}

		Pixel blackPixel = new Pixel(0, 0, 0);

		for (int r = 0; r < m.rows(); r++) {
			for (int c = 0; c < m.cols(); c++) {
				Pixel pixel = m.get(r, c);

				if (!MatrixUtilities.isBlackPixel(pixel)) {
					Color color = new Color(pixel);

					if (colorsToRemove.contains(color)) {
						result.put(r, c, blackPixel);
					}
				}

			}

		}

		return result;
	}

}
