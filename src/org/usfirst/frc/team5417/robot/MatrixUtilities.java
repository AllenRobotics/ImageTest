package org.usfirst.frc.team5417.robot;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;
import org.usfirst.frc.team5417.robot.matrixops.Color;
import org.usfirst.frc.team5417.robot.matrixops.Matrix;
import org.usfirst.frc.team5417.robot.matrixops.Pixel;
import org.usfirst.frc.team5417.robot.matrixops.PixelMatrix;
import org.usfirst.frc.team5417.robot.matrixops.Point;

public class MatrixUtilities {

	public static void LoadOpenCVLibraries() {
		System.loadLibrary(org.opencv.core.Core.NATIVE_LIBRARY_NAME);
	}

	public static Matrix getDiamondKernel(int regionWidth) {

		Matrix diamond = new Matrix(regionWidth, regionWidth, 0);

		// build a diamond. first calculate width of diamond for this row in "w"
		for (int w = 1, r = 0; r <= diamond.rows() / 2; r++, w += 2) {
			for (int leftRightOffset = 0; leftRightOffset < w / 2 + 1; leftRightOffset++) {
				int r1 = r;
				int r2 = diamond.rows() - r - 1;
				int c1 = diamond.cols() / 2 - leftRightOffset;
				int c2 = diamond.cols() / 2 + leftRightOffset;

				diamond.put(r1, c1, 1);
				diamond.put(r1, c2, 1);
				diamond.put(r2, c1, 1);
				diamond.put(r2, c2, 1);
			}
		}

		return diamond;
	}

	public static Matrix getSquareKernel(int regionWidth) {

		Matrix square = new Matrix(regionWidth, regionWidth, 1);

		return square;
	}

	public static boolean isInImage(int r, int c, int rows, int cols) {
		return r >= 0 && c >= 0 && r < rows && c < cols;
	}
	
	public static Matrix getRectangleKernel(int regionWidth, int regionHeight) {

		Matrix square = new Matrix(regionWidth, regionHeight, 1);

		return square;
	}

	

	public static Point[] getAdjacentPoints(int r, int c, PixelMatrix image, Matrix kernel, Color onlyMatchColor) {

		int startR = r - kernel.rows() / 2;
		int startC = c - kernel.cols() / 2;

		int endR = r + kernel.rows() / 2;
		int endC = c + kernel.cols() / 2;

		List<Point> adjacentPoints = new ArrayList<Point>();

		for (int RR = startR; RR <= endR; RR++) {
			for (int CC = startC; CC <= endC; CC++) {
				if (isInImage(RR, CC, image.rows(), image.cols())) {
					if (kernel.get(RR - startR, CC - startC).equals(1)) {

						Pixel pixel = image.get(RR, CC);

						if (onlyMatchColor == null ||
								(
								onlyMatchColor.r() == pixel.r && onlyMatchColor.g() == pixel.g
								&& onlyMatchColor.b() == pixel.b
								)
							) {
							Point point = new Point(RR, CC);

							// pixel is set
							adjacentPoints.add(point);
						}
					}
				}
			}
		}

		Point[] result = new Point[adjacentPoints.size()];
		result = adjacentPoints.toArray(result);
		return result;
	}
	
	public static boolean isBlackPixel(Pixel pixel) {
		if (pixel.r == 0 && pixel.g == 0 && pixel.b == 0) {
			return true;
		} else {
			return false;
		}
	}
	
	

}
