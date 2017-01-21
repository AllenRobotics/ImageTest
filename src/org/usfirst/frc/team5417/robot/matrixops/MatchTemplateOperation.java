package org.usfirst.frc.team5417.robot.matrixops;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.usfirst.frc.team5417.robot.MatrixUtilities;

public class MatchTemplateOperation implements MatrixOperation {

	public class CenterSum {
		public BigInteger xSum = new BigInteger("0");
		public BigInteger ySum = new BigInteger("0");
		public int size = 0;

	}

	private List<Matrix> templates;
	private double minimumScale, maximumScale;

	public MatchTemplateOperation(List<Matrix> templates, double minimumScale, double maximumScale) {
		this.templates = templates;
		this.minimumScale = minimumScale;
		this.maximumScale = maximumScale;
	}

	@Override
	public PixelMatrix doOperation(PixelMatrix m) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<Point> findCenters(PixelMatrix m) {

		HashMap<Color, CenterSum> groupsCenter = new HashMap<>();

		for (int r = 0; r < m.rows(); r++) {
			for (int c = 0; c < m.cols(); c++) {

				Pixel pixel = m.get(r, c);

				if (!MatrixUtilities.isBlackPixel(pixel)) {
					Color color = new Color(pixel);

					if (groupsCenter.containsKey(color)) {
						CenterSum sum = groupsCenter.get(color);
						sum.xSum = sum.xSum.add(new BigInteger(new Integer(c).toString()));
						sum.ySum = sum.ySum.add(new BigInteger(new Integer(r).toString()));
						sum.size++;
					} else {
						CenterSum sum = new CenterSum();
						sum.xSum = sum.xSum.add(new BigInteger(new Integer(c).toString()));
						sum.ySum = sum.ySum.add(new BigInteger(new Integer(r).toString()));
						sum.size++;
						groupsCenter.put(color, sum);
					}
				}
			}
		}

		List<Point> result = new ArrayList<Point>();

		for (Color color : groupsCenter.keySet()) {

			CenterSum sum = groupsCenter.get(color);
			int centerX = sum.xSum.divide(new BigInteger((new Integer(sum.size).toString()))).intValue();
			int centerY = sum.ySum.divide(new BigInteger((new Integer(sum.size).toString()))).intValue();
			Point center = new Point(centerX, centerY);
			result.add(center);
		}
		return result;

	}
}
