package com.github.SmithGoll.ClassRenamer;

public interface IClassRenamer {
	public String generateNewClassName(String name);

	public String generateNewFieldName(String className, String name, String desc);

	public String generateNewMethodName(String className, String name, String desc);
}