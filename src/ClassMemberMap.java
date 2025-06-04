package ClassRenamer;

import java.util.*;

public class ClassMemberMap {
	public static Map<String, MemberContainer> classMap = new HashMap<String, MemberContainer>();

	public static void putMember(String className, MemberContainer member) {
		classMap.put(className, member);
	}

	public static String getNewFieldName(String className, String oldName, String desc) {
		MemberContainer member = classMap.get(className);
		if (member == null) {
			return oldName;
		}

		String newFieldName = member.getField(oldName, desc);
		if (newFieldName == null) {
			return oldName;
		}

		newFieldName = newFieldName.split(":")[0];
		return newFieldName;
	}

	public static String getNewMethodName(String className, String oldName, String desc) {
		MemberContainer member = classMap.get(className);
		if (member == null) {
			return oldName;
		}

		String newMethodName = member.getMethod(oldName, desc);
		if (newMethodName == null) {
			return oldName;
		}

		newMethodName = newMethodName.split(":")[0];
		return newMethodName;
	}
}