package org.usfirst.frc.team5417.robot.opencvops;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;
import org.usfirst.frc.team5417.robot.ImageWriter;

//
// Write an image to a file
//
public class OCVFileImageWriter implements ImageWriter {

	private String fileName;

	public OCVFileImageWriter(String fileName) {
		this.fileName = fileName;
	}

	@Override
	public void write(Mat m) {

		Highgui.imwrite(fileName, m);
		
	}

}
