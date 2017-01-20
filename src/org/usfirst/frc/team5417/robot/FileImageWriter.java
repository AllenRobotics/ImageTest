package org.usfirst.frc.team5417.robot;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

//
// Write an image to a file
//
public class FileImageWriter implements ImageWriter {

	private String fileName;

	public FileImageWriter(String fileName) {
		this.fileName = fileName;
	}

	@Override
	public void write(Mat m) {
		// TODO Auto-generated method stub

		BufferedImage outputImage = mat2Img(m);
		File outputfile = new File(fileName);

		try {
			ImageIO.write(outputImage, "png", outputfile);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

			throw new IllegalArgumentException("File does not exist");
		}

	}

	public static BufferedImage mat2Img(Mat in) {

		BufferedImage out;

		Mat byteMat = new Mat(in.rows(), in.cols(), CvType.CV_8UC3);
		in.assignTo(byteMat, CvType.CV_8UC3);

		byte[] data = new byte[byteMat.rows() * byteMat.cols() * 3];
		byteMat.get(0, 0, data);

		int type = BufferedImage.TYPE_3BYTE_BGR;
		// the rows/cols are swapped here intentionally
		out = new BufferedImage(byteMat.cols(), byteMat.rows(), type);

		out.getRaster().setDataElements(0, 0, byteMat.cols(), byteMat.rows(), data);
		return out;
	}

}
