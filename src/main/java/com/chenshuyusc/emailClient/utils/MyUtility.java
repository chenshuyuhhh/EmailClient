package com.chenshuyusc.emailClient.utils;

import javax.swing.*;
import javax.swing.plaf.FontUIResource;
import java.awt.*;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Vector;

/**
 * 工具包类
 */
public class MyUtility {
	 public static void initGlobalFont(Font font) {
		    FontUIResource fontRes = new FontUIResource(font);
		    for (Enumeration<Object> keys = UIManager.getDefaults().keys();
		         keys.hasMoreElements(); ) {
		      Object key = keys.nextElement();
		      Object value = UIManager.get(key);
		      if (value instanceof FontUIResource) {
		        UIManager.put(key, fontRes);
		      }
		    }
	 }
	 
	 public static List<String> getListFromString(String to) {
			List<String> stringList = new ArrayList<String>();
			String[] tos = to.split(";");
			stringList.addAll(stringList);
			return stringList;
	}
}
