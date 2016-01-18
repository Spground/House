package jc.house.global;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;

import com.easemob.chat.EMChat;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import jc.house.async.MThreadPool;
import jc.house.models.BaseModel;
import jc.house.models.CustomerHelper;
import jc.house.utils.StringUtils;

public class MApplication extends Application {

	/**huanxinid and name mapping **/
	public Map<String, CustomerHelper> customerHelperNameMapping = new HashMap<>();
	public static final  String SP = "JC_HOUSE";
	private SharedPreferences sp;

	@Override
	public void onCreate() {
		super.onCreate();
		//初始化环信SDK
		initHuanXinSDK();
		EMChat.getInstance().setAppInited();
		this.sp = this.getSharedPreferences(SP, MODE_PRIVATE);
	}

	/**
	 * check the application process name if process name is not qualified, then we think it is a service process and we will not init SDK
	 * @param pID
	 * @return
	 */
	private String getAppName(int pID) {
		String processName = null;
		ActivityManager am = (ActivityManager) getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
		List l = am.getRunningAppProcesses();
		Iterator i = l.iterator();
		PackageManager pm = getApplicationContext().getPackageManager();
		while (i.hasNext()) {
			ActivityManager.RunningAppProcessInfo info = (ActivityManager.RunningAppProcessInfo) (i.next());
			try {
				if (info.pid == pID) {
					CharSequence c = pm.getApplicationLabel(pm.getApplicationInfo(info.processName, PackageManager.GET_META_DATA));
					// Log.d("Process", "Id: "+ info.pid +" ProcessName: "+
					// info.processName +"  Label: "+c.toString());
					// processName = c.toString();
					processName = info.processName;
					return processName;
				}
			} catch (Exception e) {
				// Log.d("Process", "Error>> :"+ e.toString());
			}
		}
		return processName;
	}

	/**
	 * 初始化环信SDK
	 */
	private void initHuanXinSDK(){
		int pid = android.os.Process.myPid();
		String processAppName = getAppName(pid);
		// 如果app启用了远程的service，此application:onCreate会被调用2次
		// 为了防止环信SDK被初始化2次，加此判断会保证SDK被初始化1次
		// 默认的app会在以包名为默认的process name下运行，如果查到的process name不是app的process name就立即返回
		if (processAppName == null || !processAppName.equalsIgnoreCase(getApplicationContext().getPackageName())) {
			// 则此application::onCreate 是被service 调用的，直接返回
			return;
		}
		/**init huanxing SDK**/
		EMChat.getInstance().init(getApplicationContext());
	}

	public void saveJsonString(final String content, final Class<? extends BaseModel> cls) {
		if (!StringUtils.strEmpty(content)) {
			MThreadPool.getInstance().getExecutorService().submit(new Runnable() {
				@Override
				public void run() {
					SharedPreferences.Editor editor = sp.edit();
					editor.putString(cls.toString(), content);
					editor.apply();
				}
			});
		}
	}

	public String getJsonString(Class<? extends BaseModel> cls) {
		return sp.getString(cls.toString(), null);
	}

}
