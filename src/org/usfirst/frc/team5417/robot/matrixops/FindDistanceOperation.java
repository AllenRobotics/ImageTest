package org.usfirst.frc.team5417.robot.matrixops;

import java.util.HashMap;
import java.util.List;

import org.usfirst.frc.team5417.robot.MatrixUtilities;

public class FindDistanceOperation implements MatrixOperation {

	@Override
	public String name() {
		// TODO Auto-generated method stub
		return "FindDistance";
	}

	@Override
	public PixelMatrix doOperation(PixelMatrix m) {
		// TODO Auto-generated method stub
		return null;
	}

	private class CenterOfMass {

		public int x;
		public int y;
		public int numberOfPixels;

		public CenterOfMass(int x, int y, int numberOfPixels) {
			this.x = x;
			this.y = y;
			this.numberOfPixels = numberOfPixels;
		}

	}

	public List<Point> findCentersOfMass(PixelMatrix m) {
		Pixel pixel;

		HashMap<Pixel, CenterOfMass> Masses = new HashMap<>();

		for (int y = 0; y < m.rows(); y++) {
			for (int x = 0; x < m.cols(); x++) {

				pixel = m.get(y, x);
				if (!MatrixUtilities.isBlackPixel(pixel)) {

				}

			}
		}
		return null;

	}

}
