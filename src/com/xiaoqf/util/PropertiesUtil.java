package com.xiaoqf.util;

import java.util.Properties;

import android.content.Context;

/**
 * @ClassName: PropertiesUtil
 * @Description: 系统配置文件工具类
 * @version: 1.0
 */
public class PropertiesUtil {

	public static Properties getProperties(Context context) {
		Properties properties = new Properties();
		try {
			properties.load(context.getAssets().open("system.properties"));
			return properties;
		} catch (Exception e) {
			LogUtil.logError(e);
		}
		return null;
	}
}
