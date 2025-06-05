package com.github.SmithGoll.ClassRenamer;

import org.objectweb.asm.*;

public class MyClassNameVisitor extends ClassVisitor {
	public String className;
	private IClassRenamer renamer;
	private MemberContainer member;

	public MyClassNameVisitor(MemberContainer member, IClassRenamer renamer) {
		super(Opcodes.ASM9);

		this.member = member;
		this.renamer = renamer;
	}

	@Override
	public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
		className = name;

		String newName = renamer.generateNewClassName(name);
		if (newName != null) {
			ClassNameMap.putClassName(name, newName);
		}
	}

	@Override
	public FieldVisitor visitField(int access, String name, String descriptor, String signature, Object value) {
		String newName = renamer.generateNewFieldName(className, name, descriptor);
		if (newName != null) {
			member.putField(name, newName, descriptor);
		}
		return null;
	}

	@Override
	public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
		String newName = renamer.generateNewMethodName(className, name, descriptor);
		if (newName != null) {
			member.putMethod(name, newName, descriptor);
		}
		return null;
	}
}
