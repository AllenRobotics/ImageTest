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
	    System.loadLibrary(org.opencv.core.Core.NATIVE_LIBRARY_NAME );
	      
		File initialFile = new File("C:/temp/sampleimage.jpg");
		InputStream imageFileStream = new FileInputStream(initialFile);
		
	      
		BufferedImage image = ImageIO.read(imageFileStream);
		byte[] pixels = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
		
		 int rowcount = image.getWidth();
         int colcount = image.getHeight();
         int type = CvType.CV_8UC3;
         Mat newMat = new Mat(rowcount,colcount,type);


		newMat.put(0, 0, pixels);
		BufferedImage outputImage = mat2Img(newMat);
		File outputfile = new File("C:/temp/outputImage.jpg");
		ImageIO.write(outputImage, "jpg", outputfile);
		//Mat
	}

	public static BufferedImage mat2Img(Mat in)
	{
		in = BGR2RGB(in);
		
	    BufferedImage out;
	    byte[] data = new byte[in.rows() * in.cols() * (int)in.elemSize()];
	    int type;
	    in.get(0, 0, data);

	    if(in.channels() == 1)
	        type = BufferedImage.TYPE_BYTE_GRAY;
	    else
	        type = BufferedImage.TYPE_3BYTE_BGR;

	    out = new BufferedImage(in.rows(), in.cols(), type);

	    out.getRaster().setDataElements(0, 0, in.rows(), in.cols(), data);
	    return out;

	}
	
	private static Mat BGR2RGB(Mat m)
	{
		Mat m2 = new Mat();
        Imgproc.cvtColor(m, m2, Imgproc.COLOR_BGR2RGB);
        return m2;
       }
}

