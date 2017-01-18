package org.usfirst.frc.team5417.robot;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

public class FileImageWriter implements ImageWriter{


	private String fileName;
	
	
	public FileImageWriter(String fileName){
		this.fileName = fileName;
	}
	
	@Override
	public void write(Mat m) {
		// TODO Auto-generated method stub
		
		BufferedImage outputImage = mat2Img(m);
		File outputfile = new File(fileName);
		
		try {
			ImageIO.write(outputImage, "jpg", outputfile);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
			throw new IllegalArgumentException("File does not exist");
		}
		
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
