package com.xiaoqf.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONObject;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.bitmap.BitmapDisplayConfig;
import com.lidroid.xutils.bitmap.callback.BitmapLoadCallBack;
import com.lidroid.xutils.bitmap.callback.BitmapLoadFrom;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.os.StatFs;
import android.text.TextUtils;
import android.text.format.Time;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;

import com.xiaoqf.app.R;
import com.xiaoqf.common.Consts;
import com.xiaoqf.common.XiaoQApplication;
import com.xiaoqf.service.UpdateManager;

/**
 * 
 * @ClassName: CommonUtil
 * @Description: 公共工具
 * @version: 1.0
 * @Create: 2015-5-5 14:26
 */
public class CommonUtil {

	private static final int Align_Num = 4;
	private static long lastClickTime;

	/**
	 * @Title: isFastDoubleClick
	 * @Description: 是否多次重复点击
	 * @return
	 * @date 2015-5-5 14:26
	 */
	public static boolean isFastDoubleClick() {
		long currentTime = System.currentTimeMillis();
		long diffTime = currentTime - lastClickTime;
		if (0 < diffTime && diffTime < 1000) {
			return true;
		}
		lastClickTime = currentTime;
		return false;
	}

	/**
	 * 
	 * @Title: checkNetWork
	 * @Description: 判断是否有网络访问
	 * @return
	 * @date 2015-5-5 14:27
	 */
	public static boolean checkNetWork(Context context) {
		if (context != null) {
			ConnectivityManager mConnectivityManager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo mNetworkInfo = mConnectivityManager
					.getActiveNetworkInfo();
			if (mNetworkInfo != null) {
				return mNetworkInfo.isAvailable();
			}
		}
		return false;
	}

	/**
	 * 
	 * @Title: returnBitMap
	 * @Description: 从网络获取图片(2.3版本以后，改为ImageUtil中图片处理方式)
	 * @param url
	 * @return
	 * @date 2015-5-5 14:27
	 */
	public static Bitmap returnBitMap(String url) {
		URL myFileUrl = null;
		Bitmap bitmap = null;
		try {
			myFileUrl = new URL(url);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		try {
			HttpURLConnection conn = (HttpURLConnection) myFileUrl
					.openConnection();
			conn.setDoInput(true);
			conn.connect();
			InputStream is = conn.getInputStream();
			bitmap = BitmapFactory.decodeStream(is);
			is.close();
		} catch (IOException e) {
			LogUtil.logError(e);
		}
		return bitmap;
	}

	/**
	 * @Title: getTotalPage
	 * @Description: 获取所有页数
	 * @param count
	 * @param pageSize
	 * @return
	 * @throws Exception
	 * @date 2015-5-5 14:27
	 */
	public static int getTotalPage(int count, int pageSize) throws Exception {
		return ((count - 1) / pageSize + 1);
	}

	/**
	 * 
	 * @Title: getAppVersionName
	 * @Description: 获取版本号
	 * @return
	 * @date 2015-5-5 14:28
	 */
	public static String getAppVersionName(Activity activity) {
		try {
			String pkName = activity.getPackageName();
			String versionName = activity.getPackageManager().getPackageInfo(
					pkName, 0).versionName;
			return versionName;
		} catch (Exception e) {
			LogUtil.logError(e);
		}
		return "";
	}

	/**
	 * 
	 * @Title: getServerVersionName
	 * @Description: 获取服务端版本号
	 * @param v
	 * @date 2015-5-5 14:13
	 */
	public static void getServerVersionName(final Activity activity,
			final String TAG) {
		final UpdateManager mUpdateManager = new UpdateManager(activity);

		HttpUtils http = new HttpUtils();
		RequestParams params = new RequestParams();
		params.addQueryStringParameter("deviceType", "android");

		http.send(HttpRequest.HttpMethod.GET, Consts.SERVER_URL_CHECK_VERSION,
				params, new RequestCallBack<String>() {
					String versionName;// 版本号
					String serverVersionName;
					//int isForce;// 是否强制更新

					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {
						try {
							// 获取json数据，动态加载。
							JSONObject jsonObject = new JSONObject(
									responseInfo.result);
							if ("0".equals(jsonObject.getString("status"))) {

								serverVersionName = jsonObject.getString("version");
								//serverVersionName ="1.2";
								//XiaoQApplication.apkDownloadUrl = jsonObject.getString("url");
								XiaoQApplication.apkDownloadUrl = "http://xiaoqf.com/app/xiaoqf.apk";
								//http://apk.hiapk.com/appdown/cn.nubia.neoshare?webparams=sviptodoc291cmNlPTkz
								versionName = getAppVersionName(activity);

								if (CommonUtil.isUpdate(versionName,
										serverVersionName)) {
									mUpdateManager
											.checkUpdateInfo(true,serverVersionName);
								} else {
									/*if ("view.ConfigActivity"
											.equalsIgnoreCase(activity
													.getLocalClassName())) {
										Toast.makeText(activity, "恭喜！已经是最新版本了",
												Toast.LENGTH_SHORT).show();
									}*/
								}
							} else {
								LogUtil.log(TAG, "获取版本号失败");
							}

						} catch (Exception e) {
							LogUtil.log(TAG, "获取版本号失败");
						}
					}

					@Override
					public void onFailure(HttpException error, String msg) {
						LogUtil.log(TAG, "获取版本号失败");
					}
				});
	}

	/**
	 * @Title: isUpdate
	 * @Description: 手机安装版本和服务端版本是否需要更新
	 * @param String
	 *            localVersionName
	 * @param String
	 *            serverVersionName
	 * @return boolean 是否需要更新。true-是
	 * @author tongdu
	 * @date 2015-5-5 14:28
	 */
	private static boolean isUpdate(String localVersionName,
			String serverVersionName) {
		// 异常状态不更新
		if (localVersionName == null || TextUtils.isEmpty(serverVersionName)) {
			return false;
		}

		return numberAlign(serverVersionName, Align_Num).compareToIgnoreCase(
				numberAlign(localVersionName, Align_Num)) > 0 ? true : false;
	}

	/**
	 * 数字补齐——按指定长度数字串补齐
	 * 
	 * @param num
	 *            原始数字字符串
	 * @param align
	 *            补齐的位数
	 * @return 添0补齐后的字符串
	 */
	private static String numberAlign(String num, int align) {
		int max = 0;
		// 按指定模式在字符串查找
		String pattern1 = "(\\d+)";
		String pre = "";

		// 创建 Pattern 对象
		Pattern r1 = Pattern.compile(pattern1);

		// 现在创建 matcher 对象
		Matcher m1 = r1.matcher(num);

		// 遍历查找最长数字
		while (m1.find()) {
			int length = m1.group(1).length();
			if (length > max) {
				max = length;
			}
		}

		// 补齐的位数小于最长的数字
		if (align < max) {
			align = max;
		}

		// 替换文字长度
		for (int i = 0; i < align; i++) {
			pre += "0";
		}
		String replacement1 = pre + "$1";

		num = m1.replaceAll(replacement1);

		String pattern2 = "0*(\\d{" + align + "})";
		String replacement2 = "$1";

		// 创建 Pattern 对象
		Pattern r2 = Pattern.compile(pattern2);

		// 现在创建 matcher 对象
		Matcher m2 = r2.matcher(num);
		num = m2.replaceAll(replacement2);

		return num;
	}

	private static long getEnvironmentSize() {
		File localFile = Environment.getDataDirectory();
		long l1;

		if (localFile == null)
			l1 = 0L;

		String str = localFile.getPath();
		StatFs localStatFs = new StatFs(str);
		long l2 = localStatFs.getBlockSize();
		l1 = localStatFs.getBlockCount() * l2;

		return l1;
	}

	/**
	 * 跳转到指定界面
	 * 
	 * @param srcActivity
	 * @param dstActivityClz
	 *            要打开的界面
	 */
	public static void openActivity(
		Activity srcActivity, Class<? extends Activity> dstActivityClz) {
		Intent intent = new Intent();
		intent.setClass(srcActivity, dstActivityClz);
		srcActivity.startActivity(intent);
//		srcActivity.finish();
	}

	/**
	 * 将数字字符串按指定长度分节，如用于格式化价格字符串
	 * @param price
	 * @param divideLen
	 * @return
	 */
	public static String formatStr(String price, int divideLen) {
		String format = price;
		if (!price.matches("^[-+]?\\b[0-9]+(\\.[0-9]{1,2})?\\b$")) {
			return price;
		}
		int len = price.indexOf('.');
		int intLen = (len > 0) ? len : price.length();
		int mod = intLen%divideLen; 
		if ( mod != 0) {
			format = price.substring(0, intLen).replaceFirst("(\\A\\d{"+ mod +"})", "$1,");
		}
		format = format.replaceAll("(\\d{"+ divideLen +"})", "$1,");
		format = format.substring(0, format.length()-1);
		return format + price.substring(intLen);
	}
	
	/**
	 * 获取屏幕的宽度
	 * @param act
	 * @return
	 */
	public static int getScreenWidth(Activity act) {  
		DisplayMetrics  dm = new DisplayMetrics();      
		act.getWindowManager().getDefaultDisplay().getMetrics(dm);      
		return dm.widthPixels;
	}  
	
	/**
	 * 获取屏幕的高度
	 * @param act
	 * @return
	 */
	public static int getScreenHeight(Activity act) {  
		DisplayMetrics  dm = new DisplayMetrics();      
		act.getWindowManager().getDefaultDisplay().getMetrics(dm);      
		return dm.heightPixels;
	}
	
	
	public static String getPackageName(Activity act) {
		PackageInfo info;
		String packageNames = "";
		try {  
		    info = act.getPackageManager().getPackageInfo(act.getPackageName(), 0);  
		   /* // 当前应用的版本名称  
		    String versionName = info.versionName;  
		    // 当前版本的版本号  
		    int versionCode = info.versionCode;  */
		    // 当前版本的包名  
		    packageNames = info.packageName;  
		} catch (NameNotFoundException e) {  
		    e.printStackTrace();  
		}
		return packageNames;
	}
	
	/*
     * 打开设置网络界面
     * */
    public static void setNetworkMethod(final Context context){
        //提示对话框
        AlertDialog.Builder builder=new Builder(context);
        builder.setTitle("网络设置提示").setMessage("网络连接不可用,是否进行设置?").setPositiveButton("设置", new DialogInterface.OnClickListener() {
            
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
                Intent intent=null;
                //判断手机系统的版本  即API大于10 就是3.0或以上版本 
                if(android.os.Build.VERSION.SDK_INT>10){
                    intent = new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS);
                }else{
                    intent = new Intent();
                    ComponentName component = new ComponentName("com.android.settings","com.android.settings.WirelessSettings");
                    intent.setComponent(component);
                    intent.setAction("android.intent.action.VIEW");
                }
                context.startActivity(intent);
            }
        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
                dialog.dismiss();
            }
        }).show();
    }
    
    /**
     * 获取本机IP地址
     * @return
     */
    public static String getLocalIpAddress() {
    	String regexIPv4 = "(?:[0-9]{1,3}\\.){3}[0-9]{1,3}";
        try {  
            for (Enumeration<NetworkInterface> en = NetworkInterface  
                    .getNetworkInterfaces(); en.hasMoreElements();) {   
                NetworkInterface intf = en.nextElement();  
                for (Enumeration<InetAddress> enumIpAddr = intf  
                        .getInetAddresses(); enumIpAddr.hasMoreElements();) {  
                    InetAddress inetAddress = enumIpAddr.nextElement();  
                    if (!inetAddress.isLoopbackAddress() &&
                    	inetAddress.getHostAddress().toString().matches(regexIPv4)) {  
                        return inetAddress.getHostAddress().toString();  
                    }  
                }  
            }  
        } catch (SocketException ex) {  
        	LogUtil.log("WifiPreference IpAddress", ex.toString());  
        }  
        return null;  
    }
    
    @SuppressLint("SimpleDateFormat")
	public static String getCurrentMonth() {
		Date date = new java.util.Date(System.currentTimeMillis());
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
    	return sdf.format(date);
    }
    
    @SuppressLint("NewApi") public static String getImageSavePath(Context context)  {
    	String cachePath = null;  
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())  
                || !Environment.isExternalStorageRemovable()) {  
            cachePath = context.getExternalCacheDir().getPath();  
        } else {  
            cachePath = context.getCacheDir().getPath();  
        }  
        return cachePath; 
    }
    
    public static void loadImageFromUrl(Context context,final ImageView iv,String url) {
    	BitmapUtils utils = new BitmapUtils(context);
    	utils.configDefaultLoadingImage(R.drawable.icon_default);
    	utils.configDefaultLoadFailedImage(R.drawable.icon_default);
    	utils.configThreadPoolSize(3);
    	utils.display(iv,url,new BitmapLoadCallBack<View>() {

			@Override
			public void onLoadCompleted(View v, String arg1, Bitmap bitmap,
					BitmapDisplayConfig arg3, BitmapLoadFrom arg4) {
				// TODO Auto-generated method stub
				iv.setImageBitmap(bitmap);
			}

			@Override
			public void onLoadFailed(View v, String arg1, Drawable arg2) {
				// TODO Auto-generated method stub
				
			}
		});
    	//utils.configDefaultBitmapMaxSize(720, 1280);
    	
    }
    
    /**
     * 从短信中获取验证码
     * @param sms
     * @return
     */
    public static String getVerificationCode(String sms) {
    	String regexVerCode = "(?<!\\d)\\d{4,10}(?!\\d)";
    	
    	if (TextUtils.isEmpty(sms)) {
			return null;
		}
    	
    	Pattern p = Pattern.compile(regexVerCode);
    	Matcher matcher = p.matcher(sms);
    	if (matcher.find()) {
			return matcher.group();
		}
    	
    	return null;
    }
}
