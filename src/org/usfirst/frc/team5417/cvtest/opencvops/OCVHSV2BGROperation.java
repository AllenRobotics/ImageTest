package org.usfirst.frc.team5417.cvtest.opencvops;

import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;
import org.usfirst.frc.team5417.cv2017.opencvops.OpenCVOperation;

public class OCVHSV2BGROperation implements OpenCVOperation {

	@Override
	public String name() {
		return "OpenCV HSV to BGR";
	}

	@Override
	public Mat doOperation(Mat m) {
		Imgproc.cvtColor(m, m, Imgproc.COLOR_HSV2BGR);
		return m;
	}
}
