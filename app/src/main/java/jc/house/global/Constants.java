package jc.house.global;

import jc.house.R;

//wujie
public final class Constants {
	public static final boolean DEBUG = true;
	public static final String MESSAGE_ATTR_IS_VOICE_CALL = "is_voice_call";

	public static class ACCOUNT{
		public static final String Account = "wujie";
		public static final String Pwd = "wujie";
	}
	//http://192.168.31.195/jchouse/web/index.php?r=feedback/feedbacks
	private static final String SERVER_ROOT = "http://192.168.9.72/house/";
//    private static final String SERVER_ROOT = "http://192.168.31.195/jchouse/";
	public static final String SERVER_URL = SERVER_ROOT + "web/index.php?r=";
	public static final String IMAGE_URL = SERVER_ROOT + "images/";
	public static final String FEEDBACK_URL = SERVER_URL + "feedback/feedbacks";
//	public static final int CODE_SUCCESS = 1;
//	public static final int CODE_FAILED = 0;
//	public static final int CODE_NO_DATA = 2;

	public static final int[] resActivity = {R.drawable.temp_activity_a, R.drawable.temp_activity_b,
			R.drawable.temp_activity_c, R.drawable.temp_activity_d, R.drawable.temp_activity_e};
	public static final int[] resHouse = {R.drawable.temp_house_a, R.drawable.temp_house_b,
			R.drawable.temp_house_c, R.drawable.temp_house_d};

	/**包名的信息**/
	public static class PackageInfo {
		public final static String modelPackage = "jc.house.models";
	}
}
