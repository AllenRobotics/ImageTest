package org.usfirst.frc.team5417.robot;

import org.opencv.core.Mat;

public class ThresholdAboveOperation implements MatrixOperation {

	@Override
	public Mat doOperation(Mat m) {
		
		Mat result = new Mat(m.rows(), m.cols(), m.type());
		
		byte[] whitePixel = {-1, -1, -1};
		byte[] blackPixel = {0, 0, 0};
		
		for (int r = 0; r < m.rows(); ++r)
		{
			for (int c = 0; c < m.cols(); ++c)
			{
				byte[] rgb = new byte[3];
				
				m.get(r, c, rgb);
				
				if (rgb[0] > 50 || rgb[1] > 50 || rgb[2] > 50) {
					result.put(r,  c, whitePixel);
				}
				else {
					result.put(r, c, blackPixel);
				}
			}
		}
		
		return result;
	}

}
