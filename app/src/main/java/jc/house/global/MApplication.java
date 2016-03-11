package jc.house.global;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.easemob.EMConnectionListener;
import com.easemob.EMError;
import com.easemob.chat.EMChat;
import com.easemob.chat.EMChatManager;
import com.squareup.picasso.Picasso;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import jc.house.async.MThreadPool;
import jc.house.models.BaseModel;
import jc.house.models.CustomerHelper;
import jc.house.utils.LogUtils;
import jc.house.utils.StringUtils;

public class MApplication extends Application implements Application.ActivityLifecycleCallbacks {

	public final String TAG = "MApplication";
	/**huanxinid and name mapping **/
	public Map<String, CustomerHelper> customerHelperNameMapping = new HashMap<>();
	public final static String CONNECTION_CONFLICT = "jc.house.CONNECTION_CONFLICT";
	public boolean isEmployeeLogin = false;

	public Set<WeakReference<Activity>> loadedActivities = new HashSet<>();
	@Override
	public void onCreate() {
		super.onCreate();
		//初始化环信SDK
		initHuanXinSDK();
		EMChat.getInstance().setAppInited();
		//监听是否已经连接到聊天服务器了
		EMChatManager.getInstance().addConnectionListener(new EMConnectionListener() {
			@Override
			public void onConnected() {
				isEmployeeLogin = true;
				LogUtils.debug(TAG, ">> connected to server");
			}

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
		});
		this.registerActivityLifecycleCallbacks(this);
		if(Constants.DEBUG)
			Picasso.with(this).setIndicatorsEnabled(true);
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
