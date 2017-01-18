package org.usfirst.frc.team5417.robot;

import org.opencv.core.Mat;

public class FilterColorOperation implements MatrixOperation {

	private ChannelRange redRange;
	private ChannelRange greenRange;
	private ChannelRange blueRange;

	
	public FilterColorOperation(ChannelRange redRange, ChannelRange greenRange, ChannelRange blueRange){
		this.redRange = redRange;
		this.greenRange = greenRange;
		this.blueRange = blueRange;
	}
	
	@Override
	public Mat doOperation(Mat m) {
		// TODO Auto-generated method stub
		
		Mat newM = new Mat(m.rows(), m.cols(), m.type());
		
		
		int minRed = redRange.getLowerBound(), maxRed = redRange.getUpperBound();
		int minGreen = greenRange.getLowerBound(), maxGreen = greenRange.getUpperBound();
		int minBlue = blueRange.getLowerBound(), maxBlue = blueRange.getUpperBound();
		byte[] blackPixel = { 0, 0, 0 };
        
        for (int r = 0; r < m.rows(); ++r)
        {
        	for (int c = 0; c < m.cols(); ++c)
        	{
        		byte[] pixel = new byte[3];
        		m.get(r, c, pixel);
        		
        		int[] rgb = getIntPixelValues(pixel);
        		
        		if(rgb[0] <= minRed || rgb[0] >= maxRed){
        			pixel = blackPixel;
        		}
        		
        		if(rgb[1] <= minGreen || rgb[1] >= maxGreen){
        			pixel = blackPixel;
        		}
        		
        		if(rgb[2] <= minBlue || rgb[2] >= maxBlue){
        			pixel = blackPixel;
        		}
        		
        		newM.put(r, c, pixel);
        		//If pixel color is out of range in any value, the pixel turns black
        		
        		// TODO: right here, if the rgb values for this pixel are in the range, do nothing.
        		//       if the rgb values for this pixel are outside the range, make the pixel black
        		
        	}
        }
	
        return newM;
	        
	}

	
	public int[] getIntPixelValues(byte[] pixel){
		
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
