package jc.house.utils;

public class StringUtils {
	public static boolean strEmpty(String str) {
		if(null == str || str.trim().equals("") || str.trim().length() == 0) {
			return true;
		}
		return false;
	}
}
