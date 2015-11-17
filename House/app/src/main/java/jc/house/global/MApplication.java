package jc.house.global;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageManager;
import com.easemob.chat.EMChat;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.UsingFreqLimitedMemoryCache;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.utils.StorageUtils;

import java.io.File;

import java.util.Iterator;
import java.util.List;

public class MApplication extends Application {

	@Override
	public void onCreate() {
		super.onCreate();
		File cacheDir = StorageUtils.getOwnCacheDirectory(getApplicationContext(), "jcimageloader/cache");
		ImageLoaderConfiguration.Builder config = new ImageLoaderConfiguration.Builder(this);
		config.threadPriority(Thread.NORM_PRIORITY - 2)
				.denyCacheImageMultipleSizesInMemory()
				.threadPoolSize(3)
				.memoryCacheExtraOptions(480, 800)
				.memoryCache(new UsingFreqLimitedMemoryCache(3 * 1024 * 1024))
				.diskCacheFileNameGenerator(new Md5FileNameGenerator())
				.diskCache(new UnlimitedDiskCache(cacheDir))
				.diskCacheFileCount(200)
//				.diskCacheExtraOptions(480, 800, new BitmapProcessor() {
//					@Override
//					public Bitmap process(Bitmap bitmap) {
//						return bitmap;
//					}
//				})
				.diskCacheSize(50 * 1024 * 1024)
				.tasksProcessingOrder(QueueProcessingType.LIFO)
				.writeDebugLogs();
		ImageLoader.getInstance().init(config.build());
		//初始化环信SDK
		initHuanXinSDK();
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