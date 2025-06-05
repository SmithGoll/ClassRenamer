package com.github.SmithGoll.ClassRenamer;

import org.objectweb.asm.*;

public class MyMethodVisitor extends MethodVisitor {
	public MyMethodVisitor(MethodVisitor mv) {
		super(Opcodes.ASM9, mv);
	}

	@Override
	public void visitTypeInsn(int opcode, String type) {
		if (opcode == Opcodes.NEW) {
			type = ClassNameMap.getClassName(type);
		} else if (opcode == Opcodes.ANEWARRAY) {
			if (type.contains("[L")) { // Class multidimensional array
				type = ClassNameMap.getNewDesc(type);
			} else {
				type = ClassNameMap.getClassName(type);
			}
		}

		super.visitTypeInsn(opcode, type);
	}

	@Override
	public void visitFieldInsn(int opcode, String owner, String name, String descriptor) {
		name = ClassMemberMap.getNewFieldName(owner, name, descriptor);
		owner = ClassNameMap.getClassName(owner);
		descriptor = ClassNameMap.getNewDesc(descriptor);

		super.visitFieldInsn(opcode, owner, name, descriptor);
	}

	@Override
	public void visitMethodInsn(int opcode, String owner, String name, String descriptor, boolean isInterface) {
		name = ClassMemberMap.getNewMethodName(owner, name, descriptor);
		owner = ClassNameMap.getClassName(owner);
		descriptor = ClassNameMap.getNewDesc(descriptor);

		super.visitMethodInsn(opcode, owner, name, descriptor, isInterface);
	}
}