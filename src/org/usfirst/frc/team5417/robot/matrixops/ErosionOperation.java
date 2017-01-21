package org.usfirst.frc.team5417.robot.matrixops;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.usfirst.frc.team5417.robot.MatrixUtilities;

public class ErosionOperation implements MatrixOperation{

	private int regionWidth;

	public ErosionOperation(int regionWidth) {
		this.regionWidth = regionWidth;
	}
	
	
	
	@Override
	public PixelMatrix doOperation(PixelMatrix m) {
		// TODO Auto-generated method stub
		
		PixelMatrix result = new PixelMatrix(m.rows(), m.cols());

		HashSet<Point> pointsToTurnBlack = new HashSet<>();

		Matrix kernel = MatrixUtilities.getDiamondKernel(regionWidth);
		Color whitePixel = new Color(255, 255, 255);
		
		for (int r = 0; r < m.rows(); r++) {
			for (int c = 0; c < m.cols(); c++) {

				Pixel pixel = m.get(r, c);

				if (MatrixUtilities.isBlackPixel(pixel)) {
					Point[] adjacentPoints = MatrixUtilities.getAdjacentPoints(r, c, m, kernel, whitePixel);

					for (Point point : adjacentPoints) {
						pointsToTurnBlack.add(point);
					}

				}

				result.put(r, c, pixel);
			}
		}
		
		double[] blackPixel = { 0, 0, 0 };

		for (Point point : pointsToTurnBlack) {
			result.put(point.getX(), point.getY(), blackPixel);
		}
		return result;
		
		
		
	}
	

	
	
	

}
