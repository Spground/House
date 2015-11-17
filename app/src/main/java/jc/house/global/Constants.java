package jc.house.global;
//wujie
public final class Constants {
	public static final boolean DEBUG = true;
	public static final String MESSAGE_ATTR_IS_VOICE_CALL = "is_voice_call";
	public static class ACCOUNT{
		public static final String Account = "wujie";
		public static final String Pwd = "wujie";
	}
	private static final String SERVER_ROOT = "http://192.168.9.72/house/";
	public static final String SERVER_URL = SERVER_ROOT + "web/index.php?r=";
	public static final String IMAGE_URL = SERVER_ROOT + "images/";
	public static final int CODE_SUCCESS = 1;
	public static final int CODE_FAILED = 0;
	public static final int CODE_NO_DATA = 2;
}
