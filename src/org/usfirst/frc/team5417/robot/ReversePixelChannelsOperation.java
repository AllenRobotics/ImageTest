package org.usfirst.frc.team5417.robot;

import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

public class ReversePixelChannelsOperation implements MatrixOperation{

	@Override
	public Mat doOperation(Mat m) {
		// TODO Auto-generated method stub
		Mat m2 = new Mat();
        Imgproc.cvtColor(m, m2, Imgproc.COLOR_BGR2RGB);
        return m2;
	}
	

}
