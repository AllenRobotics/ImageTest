package org.usfirst.frc.team5417.robot;

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
import org.opencv.imgproc.Imgproc;

public class MatrixUtilities {

	public static void LoadOpenCVLibraries()
	{
		System.loadLibrary(org.opencv.core.Core.NATIVE_LIBRARY_NAME );
	}
	
	public static int[] getIntPixelValues(byte[] pixel){
		
		int[] rgb = new int[3];
		
		if(pixel[0] <0){
			rgb[0] = pixel[0] + 256;
		}
		else{
			rgb[0] = pixel[0];
		}
		
		if(pixel[1] <0){
			rgb[1] = pixel[1] + 256;
		}
		else{
			rgb[1] = pixel[1];
		}
		
		if(pixel[2] <0){
			rgb[2] = pixel[2] + 256;
		}
		else{
			rgb[2] = pixel[2];
		}
		
		return rgb;
	}
	
}
