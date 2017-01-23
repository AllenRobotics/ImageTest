package org.usfirst.frc.team5417.robot;


import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;

//
// Write an image to a file
//
public class FileImageWriter implements ImageWriter {

	private String fileName;

	public FileImageWriter(String fileName) {
		this.fileName = fileName;
	}

	@Override
	public void write(Mat m) {

		Highgui.imwrite(fileName, m);

	}

}
