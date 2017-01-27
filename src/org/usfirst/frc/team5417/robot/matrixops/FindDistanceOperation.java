package org.usfirst.frc.team5417.robot.matrixops;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.usfirst.frc.team5417.robot.MatrixUtilities;

public class FindDistanceOperation {

	public String name() {
		// TODO Auto-generated method stub
		return "FindDistance";
	}

	public double findDistance(PixelMatrix m) throws Exception {
		// TODO Auto-generated method stub
		List<Point> centersOfMass = findCentersOfMass(m);
		if(centersOfMass.size()==2){
			Point groupCenter1 = centersOfMass.get(0);
			Point groupCenter2 = centersOfMass.get(1);
			
			double xDifference = (groupCenter2.getX() - groupCenter1.getX());
			double yDifference = (groupCenter2.getY() - groupCenter1.getY());

			double distance = Math.sqrt((xDifference * xDifference) + (yDifference * yDifference));
			return distance;
			
		} else{
			throw new Exception("did not find exactly 2 groups");
			
		}
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

		HashMap<Pixel, CenterOfMass> masses = new HashMap<>();

		for (int y = 0; y < m.rows(); y++) {
			for (int x = 0; x < m.cols(); x++) {

				Pixel pixel = m.get(y, x);
				if (!MatrixUtilities.isBlackPixel(pixel)) {
					if (masses.containsKey(pixel)) {
						// We need to update the group
						CenterOfMass mass = masses.get(pixel);
						mass.x = mass.x + x;
						mass.y = mass.y + y;
						mass.numberOfPixels++;
					} else {
						// We need to add the group with the HashMap
						CenterOfMass mass = new CenterOfMass(x, y, 1);
						masses.put(pixel, mass);

					}
				}

			}
		}
		List<Point> findResults = new ArrayList<>();

		for (Pixel pixel : masses.keySet()) {
			CenterOfMass mass = masses.get(pixel);
			mass.x = mass.x / mass.numberOfPixels;
			mass.y = mass.y / mass.numberOfPixels;

			findResults.add(new Point(mass.x, mass.y));

		}
		return findResults;

	}

}
