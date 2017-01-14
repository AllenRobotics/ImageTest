package org.usfirst.frc.team5417.robot;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

public class ImageTest {

//	public class TakePicture {
//		
//		//open camera
//		Webcam webcam = Webcam.getDefault();
//		webcam.open();
//		
//		//get image
//		Buffered image image = webcam.getImage();
//	}
	
	     
	
	public static void main(String[] args) throws IOException
	{
		// TODO Auto-generated method stub
	    MatrixUtilities.LoadOpenCVLibraries();
	   
	      
         Mat newMat = MatrixUtilities.readMatFromFile("C:/temp/sampleimage.jpg");
         
         
		BufferedImage outputImage = MatrixUtilities.mat2Img(newMat);
		File outputfile = new File("C:/temp/outputImage.jpg");
		ImageIO.write(outputImage, "jpg", outputfile);
		//Mat
	}

}

