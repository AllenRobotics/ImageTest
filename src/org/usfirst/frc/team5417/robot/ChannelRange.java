package org.usfirst.frc.team5417.robot;

public class ChannelRange {

	private int lowerBound;
	private int upperBound;
	
	public ChannelRange(int lowerBound, int upperBound){
		this.lowerBound = lowerBound;
		this.upperBound = upperBound;
	}
	
	public int getLowerBound(){
		return lowerBound;
	}
	
	public int getUpperBound(){
		return upperBound;
	}
	
	
}
