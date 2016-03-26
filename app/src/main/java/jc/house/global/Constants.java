package jc.house.global;

import jc.house.R;

public final class Constants {
	public static final boolean PRODUCING = false;
	public static final boolean DEBUG = false;
	public static final String MESSAGE_ATTR_IS_VOICE_CALL = "is_voice_call";
	public static final String MESSAGE_ATTR_IS_HOUSE = "is_house";
	public static final String DEFAULT_PWD = "123456";

	/**shared preference name**/
	public static class PREFERENCESNAME {
		public static final String RegisterInfo = "registerinfo";
	}

	private static final String SERVER_ROOT = "http://218.24.163.140:808/house/";
	public static final String SERVER_URL = SERVER_ROOT + "web/index.php?r=";
	public static final String IMAGE_URL_THUMBNAIL = SERVER_ROOT + "images_mobile/";
	public static final String IMAGE_URL_ORIGIN = SERVER_ROOT + "images/";
	public static final String FEEDBACK_URL = SERVER_URL + "feedback/feedbacks";
	public static final String ACTIVITY_URL = SERVER_URL + "activity/activities";
	public static final String ACTIVITY_SHOW_URL = SERVER_URL + "activity/mobile&id=";
	public static final String NEWS_URL = SERVER_URL + "news/news";
	public static final String NEWS_MOBILE_URL = SERVER_URL + "news/mobile&id=";
	public static final String SLIDE_URL = Constants.SERVER_URL + "slideshow/slides";
	public static final String SLIDE_MOBILE_URL = SERVER_URL + "slideshow/mobile&id=";
	public static final String HOUSE_URL = SERVER_URL + "house/houses";
	public static final String CUSTOMER_HELPER_NAME_URL = SERVER_URL + "helper/helpers";

	public static final int[] resActivity = {R.drawable.temp_activity_a, R.drawable.temp_activity_b,
			R.drawable.temp_activity_c, R.drawable.temp_activity_d, R.drawable.temp_activity_e};
	public static final int[] resHouse = {R.drawable.temp_house_a, R.drawable.temp_house_b,
			R.drawable.temp_house_c, R.drawable.temp_house_d};

	public static class APPINFO {
		public static boolean USER_VERSION = false;
	}
}

