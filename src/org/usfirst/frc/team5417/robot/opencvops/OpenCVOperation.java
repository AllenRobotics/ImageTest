package org.usfirst.frc.team5417.robot.opencvops;

import org.opencv.core.Mat;

public interface OpenCVOperation {

	public String name(); 
	public Mat doOperation(Mat m);
	
}
