package org.usfirst.frc.team5417.robot.matrixops;

import org.usfirst.frc.team5417.robot.MatrixUtilities;

//
// A pixel, either rgb or grayscale. If grayscale, just use any of the r/g/b member variables.
//
// NOTE: Pixels are mutable. That means they should not be used
//       with hash-based data structure. They have been left mutable
//       for speed. It can be expensive to create a lot of tiny objects
//       in memory.
//
public class Pixel {

	public int r;
	public int g;
	public int b;
	
	private boolean isGray = false;

	public Pixel(int r, int g, int b) {
		put(r, g, b);
	}

	public Pixel(int gray) {
		putGray(gray);
	}

	public Pixel(double[] channels) {
		put(channels);
	}
	
	public Pixel(Pixel other) {
		this.r = other.r;
		this.g = other.g;
		this.b = other.b;
		this.isGray = other.isGray;
	}

	public int gray() {
		if (isGray) {
			return r;
		}
		else {
			//
			// see https://en.wikipedia.org/wiki/Grayscale for grayscale conversion formula
			//
			return (int)(0.299 * r + 0.587 * g + 0.114 * b);
		}
	}
	
	public void put(int r, int g, int b) {
		this.r = r;
		this.g = g;
		this.b = b;
		this.isGray = false;
	}

	public void put(double[] channels) {
		if (channels.length == 3) {
			put((int)channels[0], (int)channels[1], (int)channels[2]);
		}
		else {
			putGray((int)channels[0]);
		}
	}

	public void put(double gray) {
		putGray((int)gray);
	}
	
	public void put(Pixel other) {
		this.r = other.r;
		this.g = other.g;
		this.b = other.b;
		this.isGray = other.isGray;
	}

	// please leave this private
	private void putGray(int gray) {
		this.r = gray;
		this.g = gray;
		this.b = gray;
		this.isGray = true;
	}
	
}
