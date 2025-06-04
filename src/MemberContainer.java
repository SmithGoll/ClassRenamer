package ClassRenamer;

import java.util.*;

public class MemberContainer {
	private Map<String, String> fieldMap;
	private Map<String, String> methodMap;

	public MemberContainer() {
		fieldMap = new HashMap<String, String>();
		methodMap = new HashMap<String, String>();
	}

	public void putField(String name, String newName, String desc) {
		fieldMap.put(String.format("%s:%s", name, desc), String.format("%s:%s", newName, ClassNameMap.getNewDesc(desc)));
	}

	public void putMethod(String name, String newName, String desc) {
		methodMap.put(String.format("%s:%s", name, desc), String.format("%s:%s", newName, ClassNameMap.getNewDesc(desc)));
	}

	public String getField(String name, String desc) {
		return fieldMap.get(String.format("%s:%s", name, desc));
	}

	public String getMethod(String name, String desc) {
		return methodMap.get(String.format("%s:%s", name, desc));
	}
}