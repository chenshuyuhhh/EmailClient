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
         ArrayList<String> arrayList = new ArrayList<>();
         String[] tos = to.split(";");
         for (int i = 0; i < tos.length; i++) {
             arrayList.add(tos[i]);
         }
         return arrayList;
	}
}
