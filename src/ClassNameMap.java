package com.github.SmithGoll.ClassRenamer;

import java.util.Map;
import java.util.HashMap;

public class ClassNameMap {
	private static final Map<String, String> nameMap = new HashMap<>();

	public static void putClassName(String oldName, String newName) {
		nameMap.put(oldName, newName);
	}

	public static String getClassName(String key) {
		String newClassName = nameMap.get(key);
		if (newClassName == null) {
			return key;
		}
		return newClassName;
	}
/*
	public static String getNewDesc(String desc) {
		for (Map.Entry<String, String> entry : nameMap.entrySet()) {
			String methodDesc = String.format("L%s;", entry.getKey());
			if (desc.indexOf(methodDesc) > -1) {
				String newMethodDesc = String.format("L%s;", entry.getValue());
				desc = desc.replace(methodDesc, newMethodDesc);
			}
		}
		return desc;
	}
*/
	public static String getNewDesc(String desc) {
		int strLen = desc.length();
		StringBuilder sb = new StringBuilder();

		int i = 0;
		while (i < strLen) {
			int i2 = desc.indexOf('L', i);
			if (i2 < 0) {
				sb.append(desc.substring(i));
				break;
			}

			sb.append(desc.substring(i, i2 + 1));

			i = desc.indexOf(';', i2) + 1;
			sb.append(getClassName(desc.substring(i2 + 1, i - 1)));
			sb.append(';');
		}
		return sb.toString();
	}
}