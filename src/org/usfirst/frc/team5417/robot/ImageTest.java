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
        
        
        
        //
        // TODO: filter all colors except a range from the newMat
        //
        
        // here is a sample range for each color channel
        // TODO: find a real color range
        byte minRed = (byte)100, maxRed = (byte)150;
        byte minGreen = (byte)50, maxGreen = (byte)100;
        byte minBlue = (byte)10, maxBlue = (byte)70;
        
        for (int r = 0; r < newMat.rows(); ++r)
        {
        	for (int c = 0; c < newMat.cols(); ++c)
        	{
        		byte[] rgb = new byte[3];
        		newMat.get(r, c, rgb);
        		
        		// TODO: right here, if the rgb values for this pixel are in the range, do nothing.
        		//       if the rgb values for this pixel are outside the range, make the pixel black
        		
        	}
        }
        
         
		BufferedImage outputImage = MatrixUtilities.mat2Img(newMat);
		File outputfile = new File("C:/temp/outputImage.jpg");
		ImageIO.write(outputImage, "jpg", outputfile);
		//Mat
	}

}

