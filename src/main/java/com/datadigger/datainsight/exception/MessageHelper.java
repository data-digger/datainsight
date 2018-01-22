package com.datadigger.datainsight.exception;

import java.util.*;

import org.apache.log4j.Logger;

public class MessageHelper {
	private static final Logger log = Logger.getLogger(MessageHelper.class);
	private static Map<String, MessageHelper> helperMap = new HashMap<String, MessageHelper>();

	public static MessageHelper getInstance(Class<? extends Enum> clz) {
		String name = clz.getName();
		MessageHelper helper = helperMap.get(name);
		if (helper == null) {
			helper = new MessageHelper(clz);
			helperMap.put(name, helper);
		}
		return helper;
	}

	private ResourceBundle messages;
	private String clzName = null;

	private MessageHelper(Class<? extends Enum> clz) {
		try {
			clzName = clz.getName();
			messages = ResourceBundle.getBundle(clzName);
		} catch (MissingResourceException e) {
			log.warn(clzName + ".properties is missing.");
		}
	}

	public String getMessage(String name) {
		if (messages != null) {
			String value;
			try {
				value = messages.getString(name);
				return value;
			} catch (MissingResourceException e) {
				log.warn("Key:" + name + " is missing in " + clzName + ".properties");
				return name;
			}
		}
		return name;
	}
}
