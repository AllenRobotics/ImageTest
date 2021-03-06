package org.usfirst.frc.team5417.cvtest;


import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;
import org.usfirst.frc.team5417.cv2017.ImageReader;

//
// Read an image from a file
//
public class FileImageReader implements ImageReader {

	private String fileName;

	public FileImageReader(String fileName) {
		this.fileName = fileName;
	}

	@Override
	public Mat read() {

		Mat m = Highgui.imread(fileName);

		int colcount = m.cols();
		int rowcount = m.rows();
		
		Mat threeChannel = m;
		
		Mat doubleMat = new Mat(rowcount, colcount, CvType.CV_32F);
		threeChannel.assignTo(doubleMat, CvType.CV_32F);

		return doubleMat;
	}

}
