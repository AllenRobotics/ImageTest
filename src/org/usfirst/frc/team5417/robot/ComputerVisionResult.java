package org.usfirst.frc.team5417.robot;

import org.opencv.core.Mat;
import org.usfirst.frc.team5417.robot.matrixops.PointD;

public class ComputerVisionResult {

	public boolean didSucceed = false;
	
	public double distance = -1;
	public PointD targetPoint = null;
	public Mat visionResult = null;
	
}
