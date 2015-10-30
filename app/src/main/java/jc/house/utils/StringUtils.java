package jc.house.utils;

public class StringUtils {
	public static boolean strEmpty(String str) {
		if(null == str || str.trim().equals("") || str.trim().length() == 0) {
			return true;
		}
		return false;
	}

	public static String subStr(String str, int length) {
		if(null == str || str.length() <= length) {
			return str;
		}
		String res = str.substring(0,length);
		return null;
	}
}
