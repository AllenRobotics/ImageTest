package org.usfirst.frc.team5417.robot.matrixops;

import org.usfirst.frc.team5417.robot.MatrixUtilities;

//
// A pixel, either rgb or grayscale. If grayscale, just use any of the r/g/b member variables.
//
// NOTE: Pixels are immutable. Immutability is an important characteristic
//       since a Pixel can be used in a hash-based data structure. 
//
public class Pixel {

	private int _r;
	private int _g;
	private int _b;
	
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
		this._r = other._r;
		this._g = other._g;
		this._b = other._b;
		isGray = other.isGray;
	}

	public int gray() {
		if (isGray) {
			return _r;
		}
		else {
			//
			// see https://en.wikipedia.org/wiki/Grayscale for grayscale conversion formula
			//
			return (int)(0.299 * _r + 0.587 * _g + 0.114 * _b);
		}
	}
	
	public int r() {
		return _r;
	}
	public int g() {
		return _g;
	}
	public int b() {
		return _b;
	}
	
	public Pixel addToR(int value) {
		return new Pixel(_r + value, _g, _b);
	}
	public Pixel addToG(int value) {
		return new Pixel(_r, _g + value, _b);
	}
	public Pixel addToB(int value) {
		return new Pixel(_r, _g, _b + value);
	}
	
	// please leave this private
	private void put(int r, int g, int b) {
		this._r = r;
		this._g = g;
		this._b = b;
		isGray = false;
	}

	// please leave this private
	private void put(double[] channels) {
		if (channels.length == 3) {
			put((int)channels[0], (int)channels[1], (int)channels[2]);
		}
		else {
			putGray((int)channels[0]);
		}
	}

	// please leave this private
	private void putGray(int gray) {
		this._r = gray;
		this._g = gray;
		this._b = gray;
		isGray = true;
	}
	
	// these functions:
	//   public int hashCode()
	//   public boolean equals(Object other)
	// are so that we can use a Pixel as a key in HashSet or HashMap
	
	@Override
	public int hashCode() {
		return (int)
				(this._r ^ 3
				+ this._g ^ 13
				+ this._b ^ 29);
	}
	
	@Override
	public boolean equals(Object o) {
		if (o == null) return false;
		if (false == Pixel.class.isAssignableFrom(o.getClass())) return false;
		
		final Pixel other = (Pixel)o;
		return this._r == other._r
				&& this._g == other._g
				&& this._b == other._b;
	}

}
