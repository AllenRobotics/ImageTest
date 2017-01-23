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
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

//
// Read an image from a file
//
public class FileImageReader implements ImageReader {

	private String fileName;
	private int largestDimensionSize;

	public FileImageReader(String fileName, int largestDimensionSize) {
		this.fileName = fileName;
		this.largestDimensionSize = largestDimensionSize;
	}

	@Override
	public Mat read() {
		File initialFile = new File(fileName);

		try {
			InputStream imageFileStream = new FileInputStream(initialFile);

			BufferedImage image = ImageIO.read(imageFileStream);

			byte[] pixels = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();

			int colcount = image.getWidth();
			int rowcount = image.getHeight();
			
			// the rows/cols are swapped here intentionally
			Mat m = new Mat(rowcount, colcount, CvType.CV_8UC3);
			m.put(0, 0, pixels);

			// the rows/cols are swapped here intentionally
			Mat doubleMat = new Mat(rowcount, colcount, CvType.CV_16UC3);
			m.assignTo(doubleMat, CvType.CV_16UC3);
			
			// resize the image if necessary
			if (colcount > largestDimensionSize || rowcount > largestDimensionSize) {
				
				double scaleFactor;
				
				if (colcount > rowcount) {
					scaleFactor = (double)largestDimensionSize / colcount;
				}
				else {
					scaleFactor = (double)largestDimensionSize / rowcount;
				}
				
				colcount = (int)(colcount * scaleFactor);
				rowcount = (int)(rowcount * scaleFactor);

				Size size = new Size(colcount, rowcount);
				Mat resizedMat = new Mat(size, CvType.CV_16UC3);
				Imgproc.resize(doubleMat, resizedMat, size);
				
				return resizedMat;
			}
			else {
				return doubleMat;
			}

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
