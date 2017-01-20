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
		File initialFile = new File(fileName);

		try {
			InputStream imageFileStream = new FileInputStream(initialFile);

			BufferedImage image = ImageIO.read(imageFileStream);

			byte[] pixels = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();

			int rowcount = image.getWidth();
			int colcount = image.getHeight();
			
			// the rows/cols are swapped here intentionally
			Mat m = new Mat(colcount, rowcount, CvType.CV_8UC3);
			m.put(0, 0, pixels);

			// the rows/cols are swapped here intentionally
			Mat doubleMat = new Mat(colcount, rowcount, CvType.CV_32SC3);
			m.assignTo(doubleMat, CvType.CV_32SC3);
			
			return doubleMat;

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
