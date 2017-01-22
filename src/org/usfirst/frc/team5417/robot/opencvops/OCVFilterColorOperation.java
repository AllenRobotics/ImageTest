package org.usfirst.frc.team5417.robot.opencvops;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.usfirst.frc.team5417.robot.ChannelRange;

public class OCVFilterColorOperation implements OpenCVOperation {

	private ChannelRange redRange;
	private ChannelRange greenRange;
	private ChannelRange blueRange;

	public String name() { return "Open CV Filter Colors"; } 

	public OCVFilterColorOperation(ChannelRange redRange, ChannelRange greenRange, ChannelRange blueRange) {
		this.redRange = redRange;
		this.greenRange = greenRange;
		this.blueRange = blueRange;
	}

	
	@Override
	public Mat doOperation(Mat m) {
		
		Mat result = new Mat();
		
		Scalar lowerBound = new Scalar(redRange.getLowerBound(), greenRange.getLowerBound(), blueRange.getLowerBound());
		Scalar upperBound = new Scalar(redRange.getUpperBound(), greenRange.getUpperBound(), blueRange.getUpperBound());
		
		Core.inRange(m, lowerBound, upperBound, result);
		
		return result;
	}

}
