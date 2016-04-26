package jc.house.global;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import com.easemob.EMConnectionListener;
import com.easemob.EMError;
import com.easemob.chat.EMChat;
import com.easemob.chat.EMChatManager;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import jc.house.models.CustomerHelper;
import jc.house.models.HouseHelpers;
import jc.house.utils.LogUtils;
import jc.house.utils.StringUtils;

public class MApplication extends Application implements Application.ActivityLifecycleCallbacks, EMConnectionListener {

	public final String TAG = "MApplication";
	/**huanxinid and name mapping **/
	public Map<String, CustomerHelper> customerHelperNameMapping = new HashMap<>();
	public List<HouseHelpers> houseHelpersList;
	public final static String CONNECTION_CONFLICT = "jc.house.CONNECTION_CONFLICT";
	public boolean isEmployeeLogin = false;
	public Set<WeakReference<Activity>> loadedActivities = new HashSet<>();
	public static boolean isLowerVersion = false;

	@Override
	public void onCreate() {
		super.onCreate();
		initHuanXinSDK();
		EMChatManager.getInstance().addConnectionListener(this);
		EMChat.getInstance().setAppInited();
		this.registerActivityLifecycleCallbacks(this);
		initPicasso();
		String version = Build.VERSION.RELEASE;
		isLowerVersion = checkVersion(version);
		this.houseHelpersList = new ArrayList<>();
		HouseHelpers helpers = new HouseHelpers();
		helpers.setId(1);
		helpers.setName("A");
		List<CustomerHelper> list = new ArrayList<>();
		CustomerHelper item = new CustomerHelper();
		item.setId(1);
		item.setName("楠楠");
		item.setHxID("nannan");
		item.setPicUrl("1457399788.jpg");
		list.add(item);
		CustomerHelper item2 = new CustomerHelper();
		item2.setId(1);
		item2.setName("陈洋");
		item2.setHxID("cy");
		item2.setPicUrl("1457483566.jpg");
		list.add(item2);
		helpers.setHelpers(list);
		houseHelpersList.add(helpers);

		HouseHelpers helpers2 = new HouseHelpers();
		helpers2.setId(2);
		helpers2.setName("B");
		List<CustomerHelper> list2 = new ArrayList<>();
		CustomerHelper item21 = new CustomerHelper();
		item21.setId(1);
		item21.setName("王晓宇");
		item21.setHxID("wxy");
		item21.setPicUrl("1457399788.jpg");
		list2.add(item21);
		CustomerHelper item22 = new CustomerHelper();
		item22.setId(1);
		item22.setName("李苗");
		item22.setHxID("ly");
		item22.setPicUrl("1458367788.jpg");
		list2.add(item22);
		helpers2.setHelpers(list2);
		houseHelpersList.add(helpers2);
	}

	private boolean checkVersion(String version) {
		if (!StringUtils.strEmpty(version)) {
			float r = Float.valueOf(StringUtils.subStr(version, 1));
			if (r < 5.0) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 初始化Picasso
	 */
	private void initPicasso() {
		MPicasso.initPicasso(getApplicationContext());
	}

	@Override
	public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
		loadedActivities.add(new WeakReference<>(activity));
	}

	@Override
	public void onActivityStarted(Activity activity) {

	}

	@Override
	public void onActivityResumed(Activity activity) {
	}

	@Override
	public void onActivityPaused(Activity activity) {

	}

	@Override
	public void onActivityStopped(Activity activity) {

	}

	@Override
	public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

	}

	@Override
	public void onActivityDestroyed(Activity activity) {
	}

	/**
	 * 当环信账号登录成功的时候
	 */
	@Override
	public void onConnected() {
		isEmployeeLogin = true;
		LogUtils.debug(TAG, ">> connected to server");
	}

	/**
	 * 当环信账号被迫下线的时候
	 * @param error
	 */
	@Override
	public void onDisconnected(final int error) {
		isEmployeeLogin = false;
		LogUtils.debug(TAG, ">> error code is " + error);
		//被迫下线，账号在另一处设备登录
		if (error == EMError.CONNECTION_CONFLICT) {
			Intent intent = new Intent();
			intent.setAction(MApplication.CONNECTION_CONFLICT);
			sendBroadcast(intent);
		}
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

}
