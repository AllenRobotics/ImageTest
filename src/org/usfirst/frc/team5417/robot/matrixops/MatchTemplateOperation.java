package org.usfirst.frc.team5417.robot.matrixops;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import org.usfirst.frc.team5417.robot.MatrixUtilities;

//
//Match color groups against a set of templates
//Input: a color image, where each group is a unique color
//Output: a color image, where groups have been removed that match no template at no attempted scale
//
public class MatchTemplateOperation implements MatrixOperation {

	public class CenterSum {
		public BigInteger xSum = new BigInteger("0");
		public BigInteger ySum = new BigInteger("0");
		public int size = 0;

	}

	private List<Matrix> templates;
	private double minimumScale, maximumScale;
	private double minimumMatchPercentage;

	public MatchTemplateOperation(List<Matrix> templates, double minimumScale, double maximumScale, double minimumMatchPercentage) {
		this.templates = templates;
		this.minimumScale = minimumScale;
		this.maximumScale = maximumScale;
		this.minimumMatchPercentage = minimumMatchPercentage;
	}

	@Override
	public PixelMatrix doOperation(PixelMatrix m) {
		
		PixelMatrix result = new PixelMatrix(m.rows(), m.cols());
		
		// copy the input to the output. we will clear groups from the output below.
		for (int r = 0; r < m.rows(); r++) {
			for (int c = 0; c < m.cols(); c++) {
				Pixel pixel = m.get(r, c);
				result.put(r, c, pixel);
			}
		}
		
		// only generate this many scaleFactors
		int numberOfScaleFactors = 10;
		double stepSize = (maximumScale - minimumScale) / numberOfScaleFactors;
		
		List<Point> centers = findCenters(m);
		List<Double> scaleFactors = generateScaleFactors(minimumScale, maximumScale, stepSize);
		HashMap<Color, Integer> groupsToCount = MatrixUtilities.getGroupSizes(m);
		
		for (Point center : centers) {
			
			Color groupColor = getGroupColor(m, center);
			boolean doesGroupMatchAnyTemplate = false;
			
			for (Matrix template : this.templates) {
				for (Double scaleFactor : scaleFactors) {
					
					Matrix scaledTemplate = template.scale(scaleFactor);

					double matchPercentage = 0;
					if (groupColor != null) {
						matchPercentage = getMatchPercentage(m, scaledTemplate, center, groupColor, groupsToCount);
					}
					
					if (matchPercentage < minimumMatchPercentage) {
						// the match percentage is too low, so we need to get rid of this group
						doesGroupMatchAnyTemplate = false;						
					}
					else {
						// the match percentage is high enough, so we keep this group
						doesGroupMatchAnyTemplate = true;
						break;
					}
				}
				
				if (doesGroupMatchAnyTemplate) {
					break;
				}
			}
			
			if (!doesGroupMatchAnyTemplate) {
				// the match percentage is too low, so we need to get rid of this group
				removeGroup(result, groupColor);
			}
		}
		
		return result;
	}

	private void removeGroup(PixelMatrix m, Color groupColor) {

		Pixel blackPixel = new Pixel(0, 0, 0);
		
		for (int r = 0; r < m.rows(); r++) {
			for (int c = 0; c < m.cols(); c++) {

				Pixel pixel = m.get(r, c);
				
				if (pixel.r == groupColor.r() && pixel.g == groupColor.g() && pixel.b == groupColor.b()) {
					m.put(r, c, blackPixel);
				}
			}
		}
	}
	
	private List<Point> findCenters(PixelMatrix m) {

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
	
	private List<Double> generateScaleFactors(double min, double max, double step) {
		
		List<Double> scales = new ArrayList<>();
		scales.add(min);

		double current = min + step;
		
		while (current < max) {
			scales.add(current);
			current += step;
		}
		
		if (current != max) {
			scales.add(max);
		}
		
		return scales;
	}
	
	private Color getGroupColor(PixelMatrix m, Point center) {
		Pixel centerPixel = m.get(center.getY(), center.getX());

		if (!MatrixUtilities.isBlackPixel(centerPixel)) {
			return new Color(centerPixel);
		}
		else {

			// find the first non-black pixel closest to the the center.
			// search outward from the center 
			
			for (int offsetR = 1; offsetR < m.rows(); offsetR++) {
				for (int offsetC = 1; offsetC < m.cols(); offsetC++) {

					Point topLeftCorner = new Point(center.getX() - offsetC , center.getY() - offsetR);
					Point bottomRightCorner = new Point(center.getX() + offsetC, center.getY() - offsetR);

					// find the next concentric ring of pixels
					for (int r = topLeftCorner.getY(); r < bottomRightCorner.getY(); ++r) {

						// search the left vertical side
						if (MatrixUtilities.isInImage(r, topLeftCorner.getX(), m.rows(), m.cols())) {
							Pixel pixel = m.get(r, topLeftCorner.getX());
							if (!MatrixUtilities.isBlackPixel(pixel)) {
								return new Color(pixel);
							}
						}

						// search the right vertical side
						if (MatrixUtilities.isInImage(r, bottomRightCorner.getX(), m.rows(), m.cols())) {
							Pixel pixel = m.get(r, bottomRightCorner.getX());
							if (!MatrixUtilities.isBlackPixel(pixel)) {
								return new Color(pixel);
							}
						}

					}
					for (int c = topLeftCorner.getX(); c < bottomRightCorner.getX(); ++c) {
						// search the top row
						if (MatrixUtilities.isInImage(topLeftCorner.getY(), c, m.rows(), m.cols())) {
							Pixel pixel = m.get(topLeftCorner.getY(), c);
							if (!MatrixUtilities.isBlackPixel(pixel)) {
								return new Color(pixel);
							}
						}

						// search the bottom row
						if (MatrixUtilities.isInImage(bottomRightCorner.getY(), c, m.rows(), m.cols())) {
							Pixel pixel = m.get(bottomRightCorner.getY(), c);
							if (!MatrixUtilities.isBlackPixel(pixel)) {
								return new Color(pixel);
							}
						}
					}
				}
			}
		}
		
		// this can happen if there aren't any groups in the image
		return null;
	}
	
	private double getMatchPercentage(PixelMatrix m, Matrix template, Point center, Color groupColor, HashMap<Color, Integer> groupsToCount) {
		
		double templateSize = template.rows() * template.cols();

		int matchCount = 0;
		
		for (int r = 0; r < template.rows(); r++) {
			for (int c = 0; c < template.cols(); c++) {
				
				int imageR = r + center.getY() - template.rows() / 2;
				int imageC = c + center.getX() - template.cols() / 2;
				
				if (MatrixUtilities.isInImage(imageR,  imageC, m.rows(), m.cols())) {
					Pixel pixel = m.get(imageR, imageC);
					if (pixel.r == groupColor.r() && pixel.g == groupColor.g() && pixel.b == groupColor.b()) {
						matchCount++;
					}
				}
			}
		}
		
		int groupSize = groupsToCount.get(groupColor);
		return ((double)matchCount * 2.0) / (templateSize + groupSize);
	}
}
