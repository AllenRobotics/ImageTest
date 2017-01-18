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

public class FileImageReader implements ImageReader{

	private String fileName;
	
	
	public FileImageReader(String fileName){
		this.fileName = fileName;
	}
	
	@Override
	public Mat read() {
		File initialFile = new File(fileName);

		try {
			InputStream imageFileStream = new FileInputStream(initialFile);

			BufferedImage image = ImageIO.read(imageFileStream);

			byte[] pixels = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
			
			int rowcount = image.getWidth();
			int colcount = image.getHeight();
			int type = CvType.CV_8UC3;
			Mat newMat = new Mat(rowcount,colcount,type);
			newMat.put(0, 0, pixels);
		
			return newMat; 

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
			throw new IllegalArgumentException("File does not exist");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

			throw new IllegalArgumentException("File does not exist");
		}
	}

}
