package jc.house.utils;

public final class StringUtils {
	public static boolean strEmpty(String str) {
		if(null == str || str.trim().equals("") || str.trim().length() == 0 || str.equalsIgnoreCase("null")) {
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

	public static String getMethodNameByFieldName(String fieldName) {
		StringBuilder sb = new StringBuilder();
		sb.append("set").append(fieldName.substring(0,1).toUpperCase()).append(fieldName.substring(1));
		return sb.toString();
	}

	public static final String[] parseHouseLables(String labels) {
		if (null == labels || strEmpty(labels)) {
			return null;
		}
		String[] result = new String[2];
		String[] labelArray = labels.trim().split(";");
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
}
