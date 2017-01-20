package org.usfirst.frc.team5417.robot.matrixops;

//
// Matrix class handles the nastiness of 2 dimensional jagged arrays
//
public class Matrix {
	private Object[][] items;
	
	private int rowCount;
	private int colCount;

	public Matrix(int rows, int cols, Object defaultValue) {
		this.rowCount = rows;
		this.colCount = cols;

		items = new Object[rows][];
		for (int r = 0; r < rows; r++) {
			items[r] = new Object[cols];
			
			for (int c = 0; c < cols; c++) {
				items[r][c] = defaultValue;
			}
		}
	}

	public void put(int r, int c, Object item) {
		items[r][c] = item;
	}

	public Object get(int r, int c) {
		return items[r][c];
	}

	public int rows() {
		return rowCount;
	}

	public int cols() {
		return colCount;
	}
}
