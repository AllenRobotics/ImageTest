package org.usfirst.frc.team5417.robot;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

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
	    //Calls Open CV library
	   
	      ImageReader reader = new FileImageReader("C:/temp/sampleimage.jpg");
	      Mat originalMat = reader.read();
	      Mat m = reader.read();
	      
	      List<MatrixOperation> operations = new ArrayList<MatrixOperation>();
	      
	      operations.add(new ReversePixelChannelsOperation());
	      operations.add(
	    		  new FilterColorOperation(
	    				  new ChannelRange(30, 150),
	    				  new ChannelRange(100, 210),
	    				  new ChannelRange(40, 170))
	    		  );
	      operations.add(new ThresholdAboveOperation());
	      operations.add(new FindGroupsOperation());

	      int operationNumber = 1;
	      
	      for(MatrixOperation op : operations){
	    	  m = op.doOperation(m);
	    	  
	    	  ImageWriter operationWriter = new FileImageWriter("C:/temp/operation-step-" + operationNumber + ".png");
	    	  operationWriter.write(m);
	    	  
	    	  operationNumber++;
	      }
	       
	      ImageWriter writer = new FileImageWriter("C:/temp/outputImage.png");
	      writer.write(m);
	      
	      ImageWriter originalImageWriter = new FileImageWriter("C:/temp/OrigianlImage.png");
	      originalImageWriter.write(originalMat);
	      
//       // Mat newMat = MatrixUtilities.readMatFromFile("C:/temp/sampleimage.jpg");
//          
//        
//        
//        //
//        // TODO: filter all colors except a range from the newMat
//        //
//        
//        // here is a sample range for each color channel
//        // TODO: find a real color range
//        byte minRed = (byte)200, maxRed = (byte)255;
//        byte minGreen = (byte)200, maxGreen = (byte)255;
//        byte minBlue = (byte)200, maxBlue = (byte)255;
//        byte[] blackPixel = {(byte)0,(byte)0,(byte)0};
//        
//        for (int r = 0; r < newMat.rows(); ++r)
//        {
//        	for (int c = 0; c < newMat.cols(); ++c)
//        	{
//        		byte[] rgb = new byte[3];
//        		newMat.get(r, c, rgb);
//        		
//        		if(rgb[0] <= minRed || rgb[0] >= maxRed){
//        			rgb = blackPixel;
//        		}
//        		
//        		if(rgb[1] <= minGreen || rgb[1] >= maxGreen){
//        			rgb = blackPixel;
//        		}
//        		
//        		if(rgb[2] <= minBlue || rgb[2] >= maxBlue){
//        			rgb = blackPixel;
//        		}
//        		
//        		newMat.put(r,c,rgb);
//        		//If pixel color is out of range in any value, the pixel turns black
//        		
//        		// TODO: right here, if the rgb values for this pixel are in the range, do nothing.
//        		//       if the rgb values for this pixel are outside the range, make the pixel black
//        		
//        	}
//        }
//        
//         
//		BufferedImage outputImage = MatrixUtilities.mat2Img(newMat);
//		File outputfile = new File("C:/temp/outputImage.jpg");
//		ImageIO.write(outputImage, "jpg", outputfile);
//		//Mat
	}

}

