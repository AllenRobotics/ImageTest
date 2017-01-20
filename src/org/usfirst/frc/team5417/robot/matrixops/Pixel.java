package org.usfirst.frc.team5417.robot.matrixops;

import org.usfirst.frc.team5417.robot.MatrixUtilities;

//
// A pixel, either rgb or grayscale. If grayscale, just use any of the r/g/b member variables.
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
	
	public Pixel(Pixel other) {
		this.r = other.r;
		this.g = other.g;
		this.b = other.b;
		isGray = other.isGray;
	}

	public void put(int r, int g, int b) {
		this.r = r;
		this.g = g;
		this.b = b;
		isGray = false;
	}

	public void put(double[] channels) {
		if (channels.length == 3) {
			put((int)channels[0], (int)channels[1], (int)channels[2]);
		}
		else {
			putGray((int)channels[0]);
		}
	}

	public void putGray(int gray) {
		this.r = gray;
		this.g = gray;
		this.b = gray;
		isGray = true;
	}
	
	// these functions:
	//   public int hashCode()
	//   public boolean equals(Object other)
	// are so that we can use a Pixel as a key in HashSet or HashMap
	
	@Override
	public int hashCode() {
		return (int)
				(this.r ^ 3
				+ this.g ^ 13
				+ this.b ^ 29);
	}
	
	@Override
	public boolean equals(Object o) {
		if (o == null) return false;
		if (false == Pixel.class.isAssignableFrom(o.getClass())) return false;
		
		final Pixel other = (Pixel)o;
		return this.r == other.r
				&& this.g == other.g
				&& this.b == other.b;
	}

}
