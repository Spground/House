package jc.house.utils;

import jc.house.global.Constants;

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
		return str.substring(0, length);
	}

	public static String getMethodNameByFieldName(String fieldName) {
		StringBuilder sb = new StringBuilder();
		sb.append("set").append(fieldName.substring(0,1).toUpperCase()).append(fieldName.substring(1));
		return sb.toString();
	}

	public static final String[] parseHouseLables(String labels) {
		String temStr = labels.trim();
		while (temStr.length() > 0 && temStr.charAt(0) == ';') {
			temStr = temStr.substring(1);
		}
		if (strEmpty(temStr)) {
			return null;
		}
		String[] result = new String[2];
		String[] labelArray = temStr.split(";");
		int len = labelArray.length;
		if (len > 0) {
			result[0] = labelArray[0];
		}
		StringBuilder sb = new StringBuilder();
		for (int i = 1; i < len; i++) {
			sb.append(labelArray[i]).append("   ");
		}
		result[1] = sb.toString();
		return result;
	}

	public static final String[] parseImageUrlsThumb(String imageUrl) {
		String temStr = imageUrl.trim();
		while (temStr.length() > 0 && temStr.charAt(0) == ';') {
			temStr = temStr.substring(1);
		}
		if (strEmpty(temStr)) {
			return null;
		}
		String[] result = temStr.split(";");
		for (int i = 0; i < result.length; i++) {
			result[i] = Constants.IMAGE_URL_THUMBNAIL + result[i];
		}
		return result;
	}

	public static final String[] parseImageUrlsOrigin(String imageUrl) {
		String temStr = imageUrl.trim();
		while (temStr.length() > 0 && temStr.charAt(0) == ';') {
			temStr = temStr.substring(1);
		}
		if (strEmpty(temStr)) {
			return null;
		}
		String[] result = temStr.split(";");
		for (int i = 0; i < result.length; i++) {
			result[i] = Constants.IMAGE_URL_ORIGIN + result[i];
		}
		return result;
	}

}
