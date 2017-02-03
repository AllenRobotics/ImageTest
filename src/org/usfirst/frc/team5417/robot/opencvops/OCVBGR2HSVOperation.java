package org.usfirst.frc.team5417.robot.opencvops;

import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

public class OCVBGR2HSVOperation implements OpenCVOperation {

	@Override
	public String name() {
		return "OpenCV BGR to HSV";
	}

	@Override
	public Mat doOperation(Mat m) {
		Imgproc.cvtColor(m, m, Imgproc.COLOR_BGR2HSV);
		return m;
	}
	
}
