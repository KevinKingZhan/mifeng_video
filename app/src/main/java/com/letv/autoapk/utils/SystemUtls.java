package com.letv.autoapk.utils;

import java.io.BufferedReader;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.Locale;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Environment;
import android.os.StatFs;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.letv.autoapk.R;
import com.letv.autoapk.common.utils.Logger;

public class SystemUtls {
	private static final String TAG = null;
	private static SystemUtls util;

	public static SystemUtls getInstance() {
		if (util == null) {
			util = new SystemUtls();
		}
		return util;
	}

	/**
	 * 将yyyy-mm-dd格式的字符串转换为Long秒
	 * 
	 * @param DateString
	 *            yyyy-mm-dd格式
	 * @param format
	 *            什么格式，如yyyy-mm-dd
	 * @return
	 * @throws ParseException
	 */
	public static Long Strdate2Long(String DateString, String format) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		Date cd = (Date) sdf.parse(DateString);
		long time = cd.getTime();
		Long birthday = time / 1000;
		return birthday;
	}

	public static String getIMEI(Context context) {
		if (context == null) {
			return "";
		}
		try {
			String deviceId = ((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
			if (null == deviceId || deviceId.length() <= 0) {
				return "";
			} else {
				return deviceId.replace(" ", "");
			}
		} catch (Exception e) {
			Logger.log(e);
			return "";
		}
	}

	public static int getAppVersionCode(Context context) {
		int versionCode = -1;
		try {
			PackageManager packageManager = context.getPackageManager();
			PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
			versionCode = packageInfo.versionCode;

		} catch (Exception e) {
			Logger.log(e);
		}
		return versionCode;
	}

	public static String getAppVersionName(Context context) {
		String versionName = "1.0";
		try {
			PackageManager packageManager = context.getPackageManager();
			PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
			versionName = packageInfo.versionName;

		} catch (Exception e) {
			Logger.log(e);
		}
		return versionName;
	}

	public static String getApplicationName(Context context) {
		PackageManager packageManager = null;
		ApplicationInfo applicationInfo = null;
		try {
			packageManager = context.getPackageManager();
			applicationInfo = packageManager.getApplicationInfo(context.getPackageName(), 0);
		} catch (PackageManager.NameNotFoundException e) {
			applicationInfo = null;
		}
		String applicationName = (String) packageManager.getApplicationLabel(applicationInfo);
		return applicationName;
	}

	/**
	 * 获取经纬度
	 * 
	 * @param context
	 * @return
	 */
	public static Location getLocationInfo(Context context) {
		// double latitude = 0.0;
		// double longitude = 0.0;
		LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
		if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
			Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
			// Logger.e("Map", "GPS_PROVIDER : " + location);
			if (location != null) {
				Logger.e("Map", "GPS_PROVIDER : Lat: " + location.getLatitude() + " Lng: " + location.getLongitude());
				return location;
			}
		}

		LocationListener locationListener = new LocationListener() {

			// Provider的状态在可用、暂时不可用和无服务三个状态直接切换时触发此函数
			@Override
			public void onStatusChanged(String provider, int status, Bundle extras) {

			}

			// Provider被enable时触发此函数，比如GPS被打开
			@Override
			public void onProviderEnabled(String provider) {

			}

			// Provider被disable时触发此函数，比如GPS被关闭
			@Override
			public void onProviderDisabled(String provider) {

			}

			// 当坐标改变时触发此函数，如果Provider传进相同的坐标，它就不会被触发
			@Override
			public void onLocationChanged(Location location) {
				if (location != null) {
					// Logger.e("Map", "Location changed : Lat: " +
					// location.getLatitude() + " Lng: " +
					// location.getLongitude());
				}
			}
		};
		locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 0, locationListener);
		Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
		// Log.e("Logger", "NETWORK_PROVIDER : " + location);
		if (location != null) {
			// latitude = location.getLatitude(); // 经度
			// longitude = location.getLongitude(); // 纬度
			// Log.e("Logger", "NETWORK_PROVIDER : Lat: " +
			// location.getLatitude() + " Lng: " + location.getLongitude());
			return location;
		}
		return null;
	}

	public static String getPrintSize(long size) { // 如果字节数少于1024，则直接以B为单位，否则先除于1024，后3位因太少无意义z
		if (size < 1024) {
			return String.valueOf(size) + "B";
		} else {
			size = size / 1024;
		}
		// 如果原字节数除于1024之后，少于1024，则可以直接以KB作为单位
		// 因为还没有到达要使用另一个单位的时候
		// 接下去以此类推
		if (size < 1024) {
			return String.valueOf(size) + "KB";
		} else {
			size = size * 10 / 1024;// *10是为了保留一位小数，如a =
									// 3.5*10，那么a的整数位就是a/10,小数位就是a%10
		}
		if (size < 10240) {
			return String.valueOf((size / 10)) + "." + String.valueOf((size % 10)) + "MB";
		} else {
			// 否则如果要以GB为单位的，先除于1024再作同样的处理
			size = size * 10 / 1024;
			return String.valueOf((size / 10)) + "." + String.valueOf((size % 10)) + "GB";
		}

	}

	/**
	 * 获得SD卡总大小
	 * 
	 * @return
	 */
	public static long getSDTotalSize() {
		File path = Environment.getExternalStorageDirectory();
		StatFs stat = new StatFs(path.getPath());
		long blockSize = stat.getBlockSize();
		long totalBlocks = stat.getBlockCount();
		return blockSize * totalBlocks;
	}

	/**
	 * 获得sd卡剩余容量，即可用大小
	 * 
	 * @return
	 */
	public static long getSDAvailableSize() {
		File path = Environment.getExternalStorageDirectory();
		StatFs stat = new StatFs(path.getPath());
		long blockSize = stat.getBlockSize();
		long availableBlocks = stat.getAvailableBlocks();
		return blockSize * availableBlocks;
	}

	public static String getSDCardInfo() {
		String result = "无法检测到SD卡";
		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
			result = "可用空间:" + getSDAvailableSize() / 1024 / 1024 / 1024 + "GB/共"
					+ getSDTotalSize() / 1024 / 1024 / 1024 + "GB";
		}
		return result;
	}

	public static boolean getSDCardUsable() {
		boolean result = false;
		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
			result = true;
		}
		return result;
	}

	public static String getDownloadPath() {
		File sdDir = null;
		String downloadPath = "";
		boolean sdCardExist = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED); // 判断sd卡是否存在
		if (sdCardExist) {
			sdDir = Environment.getExternalStorageDirectory();// 获取跟目录
			downloadPath = sdDir + File.separator + "autoApk";
			File downloadPathFile = new File(downloadPath);
			if (!downloadPathFile.exists()) {
				downloadPathFile.mkdirs();
			}
		}
		return downloadPath;
	}

	/**
	 * 获取网络类型
	 * 
	 * @param context
	 * @return
	 */
	public static String getNetType(Context context) {
		if (context == null) {
			return null;
		}
		ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo ni = cm.getActiveNetworkInfo();

		return ni.getTypeName();
	}

	/**
	 * 运营商编码
	 * 
	 * @param context
	 * @return
	 */
	public static String getNetworkOperator(Context context) {
		return ((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE)).getNetworkOperator();
	}

	/**
	 * 设备ip地址
	 */
	public static String getIpAddress() {
		try {
			for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
				NetworkInterface intf = en.nextElement();
				for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
					InetAddress inetAddress = enumIpAddr.nextElement();
					if (!inetAddress.isLoopbackAddress() && !inetAddress.isLinkLocalAddress()) {
						return inetAddress.getHostAddress().toString();
					}
				}
			}
		} catch (SocketException ex) {
			Logger.log(ex);
		}

		return null;
	}

	/**
	 * 获取设备品牌
	 * 
	 * @return
	 */
	public static String getDeviceBrand() {
		return android.os.Build.BRAND;
	}

	/**
	 * 获取设备型号
	 * 
	 * @return
	 */
	public static String getDeviceModel() {
		return android.os.Build.MODEL;
	}

	/**
	 * 操心系统
	 * 
	 * @return
	 */
	public static String getOsVersion() {
		return android.os.Build.VERSION.RELEASE;
	}

	/**
	 * 移动国家码
	 */
	public static String getCountryCode(Context context) {
		TelephonyManager tel = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		String simOperator = tel.getSimOperator();
		String mcc = simOperator.substring(0, 3);
		return mcc;
	}

	/**
	 * 
	 */
	public static String getContryCode(Context ctx) {
		String CountryID = "";
		String countryCode = "";

		TelephonyManager manager = (TelephonyManager) ctx.getSystemService(Context.TELEPHONY_SERVICE);
		// getNetworkCountryIso
		CountryID = manager.getSimCountryIso().toUpperCase();
		String[] rl = ctx.getResources().getStringArray(R.array.CountryCodes);
		for (int i = 0; i < rl.length; i++) {
			String[] g = rl[i].split(",");
			if (g[1].trim().equals(CountryID.trim())) {
				countryCode = g[0];
				break;
			}
		}
		if (TextUtils.isEmpty(countryCode)) {
			return "86";
		}
		return countryCode;
	}

	/**
	 * 根据busybox获取本地Mac
	 * 
	 * @return
	 */
	public static String getLocalMacAddress() {
		String result = "";
		String Mac = "";
		result = callCmd("busybox ifconfig", "HWaddr");

		// 如果返回的result == null，则说明网络不可取
		if (result == null) {
			return "网络出错，请检查网络";
		}

		// 对该行数据进行解析
		// 例如：eth0 Link encap:Ethernet HWaddr 00:16:E8:3E:DF:67
		if (result.length() > 0 && result.contains("HWaddr") == true) {
			Mac = result.substring(result.indexOf("HWaddr") + 6, result.length() - 1);

			/*
			 * if(Mac.length()>1){ Mac = Mac.replaceAll(" ", ""); result = "";
			 * String[] tmp = Mac.split(":"); for(int i = 0;i<tmp.length;++i){
			 * result +=tmp[i]; } }
			 */
			result = Mac;
		}
		return result;
	}

	private static String callCmd(String cmd, String filter) {
		String result = "";
		String line = "";
		try {
			Process proc = Runtime.getRuntime().exec(cmd);
			InputStreamReader is = new InputStreamReader(proc.getInputStream());
			BufferedReader br = new BufferedReader(is);

			// 执行命令cmd，只取结果中含有filter的这一行
			while ((line = br.readLine()) != null && line.contains(filter) == false) {
				// result += line;
				Log.i("test", "line: " + line);
			}

			result = line;
			Log.i("test", "result: " + result);
		} catch (Exception e) {
			Logger.log(e);
		}
		return result;
	}

	/**
	 * 获取mac地址2
	 * 
	 * @return
	 */
	public static String getMacAddress() {
		String macSerial = null;
		String str = "";
		try {
			Process pp = Runtime.getRuntime().exec("cat /sys/class/net/wlan0/address ");
			InputStreamReader ir = new InputStreamReader(pp.getInputStream());
			LineNumberReader input = new LineNumberReader(ir);

			for (; null != str;) {
				str = input.readLine();
				if (str != null) {
					macSerial = str.trim();// 去空格
					break;
				}
			}
		} catch (IOException ex) {
			// 赋予默认值
			Logger.log(ex);
		}
		return macSerial;
	}

	/**
	 * 获取android id
	 * 
	 * @param context
	 * @return
	 */
	public static String getAndroidId(Context context) {
		String ANDROID_ID = Settings.System.getString(context.getContentResolver(), Settings.System.ANDROID_ID);
		return ANDROID_ID;
	}

	/**
	 * 判断网络环境是否可用
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isNetworkConnected(Context context) {
		if (context != null) {
			ConnectivityManager mConnectivityManager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
			if (mNetworkInfo != null) {
				return mNetworkInfo.isAvailable();
			}
		}
		return false;
	}

	/**
	 * 隐藏键盘
	 * 
	 * @param context
	 */
	public static void hideInputMethod(Context context, View view) {
		InputMethodManager inputMethodManager = (InputMethodManager) context
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		if (inputMethodManager.isActive()) {
			inputMethodManager.hideSoftInputFromWindow(view.getApplicationWindowToken(), 0);
		}
	}

	/**
	 * 获取渠道号
	 */
	public static String getChannelName(Context ctx) {
		return getAppMetaData(ctx, "UMENG_CHANNEL");
	}

	/**
	 * application中指定的meta-data
	 * 
	 * @param ctx
	 * @param key
	 * @return
	 */
	public static String getAppMetaData(Context ctx, String key) {
		if (ctx == null || TextUtils.isEmpty(key)) {
			return null;
		}
		String resultData = null;
		try {
			PackageManager packageManager = ctx.getPackageManager();
			if (packageManager != null) {
				ApplicationInfo applicationInfo = packageManager.getApplicationInfo(ctx.getPackageName(),
						PackageManager.GET_META_DATA);
				if (applicationInfo != null) {
					if (applicationInfo.metaData != null) {
						resultData = applicationInfo.metaData.getString(key);
					}
				}

			}
		} catch (PackageManager.NameNotFoundException e) {
			Logger.log(e);
		}

		return resultData;
	}

	/**
	 * 获取语言
	 */
	public static String getLanguage(Context context) {
		String lan = Locale.getDefault().getLanguage();
		return lan;
	}

	public static String getDiskCacheDir(Context context) {
		String cachePath = null;
		if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
				|| !Environment.isExternalStorageRemovable()) {
			cachePath = context.getExternalCacheDir().getPath();
		} else {
			cachePath = context.getCacheDir().getPath();
		}
		return cachePath;
	}

}
