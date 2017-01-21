package org.usfirst.frc.team5417.robot.matrixops;

import org.opencv.core.CvType;
import org.opencv.core.Mat;

//
// PixelMatrix wraps the nastiness of dealing with 2 dimensional jagged arrays.
// It also wraps the nastiness of dealing with opencv Mat's.
//
public class PixelMatrix {

	private Pixel[][] pixels;
	private int rowCount;
	private int colCount;

	public PixelMatrix(int rows, int cols) {
		this.rowCount = rows;
		this.colCount = cols;

		pixels = new Pixel[rows][];
		for (int r = 0; r < rows; r++) {
			pixels[r] = new Pixel[cols];

			for (int c = 0; c < cols; c++) {
				pixels[r][c] = new Pixel(0, 0, 0);
			}
		}
	}

	public PixelMatrix(Mat m) {
		this(m.rows(), m.cols());
		
		for (int r = 0; r < m.rows(); r++) {
			for (int c = 0; c < m.cols(); c++) {
				double[] pixel = m.get(r, c);
				// m.get(...) returns the pixel in bgr order, but we want rgb
				reverseChannels(pixel);
				this.put(r, c, pixel);
			}
		}
	}
	
	private void reverseChannels(double[] pixel) {
		// reverse the array
		for(int i = 0; i < pixel.length / 2; i++) {
			int headIndex = i;
			int tailIndex = pixel.length - i - 1;
			
			// swap
		    double temp = pixel[headIndex];
		    pixel[headIndex] = pixel[tailIndex];
		    pixel[tailIndex] = temp;
		}
	}

	public void put(int r, int c, double[] channels) {
		this.get(r, c).put(channels);
	}
	
	public void put(int r, int c, double gray) {
		this.get(r, c).put(gray);
	}

	public void put(int r, int c, Pixel pixel) {
		this.get(r, c).put(pixel);
	}

	public Pixel get(int r, int c) {
		return pixels[r][c];
	}

	public int rows() {
		return rowCount;
	}

	public int cols() {
		return colCount;
	}

	public Mat toMat() {
		Mat m = new Mat(rows(), cols(), CvType.CV_32SC3);

		for (int r = 0; r < rows(); r++) {
			for (int c = 0; c < cols(); c++) {
				Pixel pixel = this.get(r, c);

				double[] bgr = { pixel.r, pixel.g, pixel.b };
				m.put(r, c, bgr);
			}
		}

		return m;
	}
}
