package ClassRenamer;

import org.objectweb.asm.*;

public class MyClassVisitor extends ClassVisitor {
	public String className;

	public MyClassVisitor(ClassVisitor cv) {
		super(Opcodes.ASM9, cv);
	}

	@Override
	public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
		className = name;
		name = ClassNameMap.getClassName(name);

		if (superName != null) {
			superName = ClassNameMap.getClassName(superName);
		}

		if (interfaces != null) {
			for (int i = 0; i < interfaces.length; i++) {
				interfaces[i] = ClassNameMap.getClassName(interfaces[i]);
			}
		}

		super.visit(version, access, name, signature, superName, interfaces);
	}

	@Override
	public FieldVisitor visitField(int access, String name, String descriptor, String signature, Object value) {
		name = ClassMemberMap.getNewFieldName(className, name, descriptor);
		descriptor = ClassNameMap.getNewDesc(descriptor);

		return super.visitField(access, name, descriptor, signature, value);
	}

	@Override
	public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
		MethodVisitor mv;

		name = ClassMemberMap.getNewMethodName(className, name, descriptor);
		descriptor = ClassNameMap.getNewDesc(descriptor);

		mv = super.visitMethod(access, name, descriptor, signature, exceptions);

		return new MyMethodVisitor(mv);
	}
}