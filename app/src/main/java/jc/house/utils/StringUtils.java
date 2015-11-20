package jc.house.utils;

public final class StringUtils {
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
		return str.substring(0,length);
	}
}
