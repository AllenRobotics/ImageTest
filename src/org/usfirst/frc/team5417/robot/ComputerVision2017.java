package org.usfirst.frc.team5417.robot;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;

import org.opencv.core.Mat;
import org.usfirst.frc.team5417.robot.matrixops.BooleanMatrix;
import org.usfirst.frc.team5417.robot.matrixops.FindDistanceOperation;
import org.usfirst.frc.team5417.robot.matrixops.FindGroupsOperation;
import org.usfirst.frc.team5417.robot.matrixops.FindTargetPointOperation;
import org.usfirst.frc.team5417.robot.matrixops.MatchTemplatesOperation;
import org.usfirst.frc.team5417.robot.matrixops.MatrixOperation;
import org.usfirst.frc.team5417.robot.matrixops.Pixel;
import org.usfirst.frc.team5417.robot.matrixops.PixelMatrix;
import org.usfirst.frc.team5417.robot.matrixops.Point;
import org.usfirst.frc.team5417.robot.matrixops.PointD;
import org.usfirst.frc.team5417.robot.matrixops.RemoveSmallGroupsOperation;
import org.usfirst.frc.team5417.robot.opencvops.ImageScaleOperation;
import org.usfirst.frc.team5417.robot.opencvops.OCVDilationOperation;
import org.usfirst.frc.team5417.robot.opencvops.OCVErosionOperation;
import org.usfirst.frc.team5417.robot.opencvops.OCVFilterColorOperation;
import org.usfirst.frc.team5417.robot.opencvops.OpenCVOperation;

public class ComputerVision2017 {

	public ComputerVisionResult DoComputerVision(ImageReader reader, int largestDimensionSize, ChannelRange redRange,
			ChannelRange greenRange, ChannelRange blueRange, int dilateErodeKernelSize,
			int removeGroupsSmallerThan, double minimumTemplateScale, double maximumTemplateScale,
			double minimumTemplateMatchPercentage, List<BooleanMatrix> templatesToUse) {

		Mat m = reader.read();

		ImageScaleOperation scaleOp = new ImageScaleOperation(m, largestDimensionSize);
		m = scaleOp.resize();

		m = MatrixUtilities.reverseColorChannels(m);

		// filter colors
		OpenCVOperation filterOp = new OCVFilterColorOperation(redRange, greenRange, blueRange);

		// // dilate the white areas in the image to "heal" broken lines
		OpenCVOperation dilateOp = new OCVDilationOperation(dilateErodeKernelSize);

		// // erode the white areas in the image (sort of undoes the dilation,
		// but keeps "healed" lines)
		OpenCVOperation erodeOp = new OCVErosionOperation(dilateErodeKernelSize);

		// find groups (operates on gray scale, outputs color)
		MatrixOperation findGroupsOp = new FindGroupsOperation();

		int operationNumber = 1;
		m = filterOp.doOperation(m);
		m = dilateOp.doOperation(m);
		m = erodeOp.doOperation(m);

		// convert open cv mat to PixelMatrix
		PixelMatrix pixelMatrix = new PixelMatrix(m);

		pixelMatrix = findGroupsOp.doOperation(pixelMatrix);

		// calculate the group sizes
		HashMap<Pixel, Integer> groupSizes = MatrixUtilities.getGroupSizes(pixelMatrix);

		// remove all groups with too few pixels
		MatrixOperation removeGroupsOp = new RemoveSmallGroupsOperation(removeGroupsSmallerThan, groupSizes);

		// remove all groups that don't match a template
		MatrixOperation matchTemplatesOp = new MatchTemplatesOperation(templatesToUse, minimumTemplateScale,
				maximumTemplateScale, minimumTemplateMatchPercentage, groupSizes);

		pixelMatrix = removeGroupsOp.doOperation(pixelMatrix);
		pixelMatrix = matchTemplatesOp.doOperation(pixelMatrix);

		ComputerVisionResult cvResult = new ComputerVisionResult();
		try {
			HashMap<Pixel, Point> centersOfMass = MatrixUtilities.findCentersOfMass(pixelMatrix);
			FindDistanceOperation findDistanceOp = new FindDistanceOperation(centersOfMass);

			cvResult.distance = findDistanceOp.findDistance(pixelMatrix) * scaleOp.getInverseScaleFactor();

			FindTargetPointOperation findTargetOp = new FindTargetPointOperation(centersOfMass);
			cvResult.targetPoint = findTargetOp.findTargetPoint();
			cvResult.targetPoint = cvResult.targetPoint.adjustByScale(scaleOp.getInverseScaleFactor());

			cvResult.visionResult = pixelMatrix.toMat();

			cvResult.didSucceed = true;

		} catch (Exception ex) {

			cvResult.didSucceed = false;

			System.err.println(ex.getMessage());
		}

		return cvResult;
	}

}