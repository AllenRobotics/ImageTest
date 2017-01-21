package org.usfirst.frc.team5417.robot.matrixops;

//
// Color is immutable. This is important so that it can be safely
// used in hash-based data structures.
//
public class Color {
	
	private int _r;
	private int _g;
	private int _b;
	
	public Color(int r, int g, int b) {
		this._r = r;
		this._g = g;
		this._b = b;
	}
	
	// It could be convenient to construct a Color from a Pixel 
	public Color(Pixel pixel) {
		this._r = pixel.r;
		this._g = pixel.g;
		this._b = pixel.b;
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
		if (false == Color.class.isAssignableFrom(o.getClass())) return false;
		
		final Color other = (Color)o;
		return this._r == other._r
				&& this._g == other._g
				&& this._b == other._b;
	}

}
