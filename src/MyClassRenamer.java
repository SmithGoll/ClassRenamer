package com.github.SmithGoll.ClassRenamer;

public class MyClassRenamer implements IClassRenamer {
	private int classCounter = 0;
	private int fieldCounter = 0;
	private int methodCounter = 0;

	private static final String classFormat = "class_%04d";
	private static final String fieldFormat = "field_%04d";
	private static final String methodFormat = "method_%04d";

	public MyClassRenamer() {
	}

	@Override
	public String generateNewClassName(String name) {
		String filePath = "";
		int lastPathSeparatorIndex = name.lastIndexOf('/');

		if (lastPathSeparatorIndex != -1) {
			filePath = name.substring(0, lastPathSeparatorIndex + 1);
			name = name.substring(lastPathSeparatorIndex + 1);
		}

		if (name.length() < 3) {
			return filePath + String.format(classFormat, classCounter++);
		}
		return null;
	}

	@Override
	public String generateNewFieldName(String className, String name, String desc) {
		if (name.length() < 3) {
			return String.format(fieldFormat, fieldCounter++);
		}
		return null;
	}

	@Override
	public String generateNewMethodName(String className, String name, String desc) {
		if (name.length() < 3) {
			return String.format(methodFormat, methodCounter++);
		}
		return null;
	}
}