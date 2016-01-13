package jc.house.global;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageManager;

import com.easemob.chat.EMChat;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import jc.house.models.CustomerHelper;

public class MApplication extends Application {

	/**huanxinid and name mapping **/
	public Map<String, CustomerHelper> customerHelperNameMapping = new HashMap<>();

	@Override
	public void onCreate() {
		super.onCreate();
		/*
		File cacheDir = StorageUtils.getCacheDirectory(this);
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this.getApplicationContext())
				.threadPriority(Thread.NORM_PRIORITY - 2)
				.denyCacheImageMultipleSizesInMemory()
				.threadPoolSize(3)
				.memoryCacheExtraOptions(480, 800)
				.memoryCache(new LruMemoryCache(10 * 1024 * 1024))
				.diskCacheFileNameGenerator(new HashCodeFileNameGenerator())
				.diskCache(new UnlimitedDiskCache(cacheDir))
				.diskCacheFileCount(200)
				.diskCacheExtraOptions(480, 800, new BitmapProcessor() {
					@Override
					public Bitmap process(Bitmap bitmap) {
						LogUtils.debug("processBitmap", "保存在本地文件之前");
						return bitmap;
					}
				})
				.diskCacheSize(100 * 1024 * 1024)
				.tasksProcessingOrder(QueueProcessingType.LIFO)
				.writeDebugLogs()
				.build();
		ImageLoader.getInstance().init(config);
		*/
		//初始化环信SDK
		initHuanXinSDK();
		EMChat.getInstance().setAppInited();
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
