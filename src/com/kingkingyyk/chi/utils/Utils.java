package com.kingkingyyk.chi.utils;

public class Utils {

	private static String [] ByteUnits = {"bytes","KB","MB","GB"};
	private static int [] BytesDivisor = {1,1024,1024,1024};
	
	public static String formatBytes(long bytes) {
		int index=0;
		while (bytes>=BytesDivisor[index+1]) {
			bytes/=BytesDivisor[index+1];
			index++;
		}
		return bytes+ByteUnits[index];
	}
}
