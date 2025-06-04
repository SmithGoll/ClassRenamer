package ClassRenamer;

import org.objectweb.asm.*;

public class MyClassNameVisitor extends ClassVisitor {
	public String className;
	private MemberContainer member;

	public MyClassNameVisitor(MemberContainer member) {
		super(Opcodes.ASM9);

		this.member = member;
	}

	@Override
	public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
		className = name;

		String newName = ClassRenamer.generateNewClassName(name);
		if (newName != null) {
			ClassNameMap.putClassName(name, newName);
		}
	}

	@Override
	public FieldVisitor visitField(int access, String name, String descriptor, String signature, Object value) {
		String newName = ClassRenamer.generateNewFieldName(name, descriptor);
		if (newName != null) {
			member.putField(name, newName, descriptor);
		}
		return null;
	}

	@Override
	public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
		String newName = ClassRenamer.generateNewMethodName(name, descriptor);
		if (newName != null) {
			member.putMethod(name, newName, descriptor);
		}
		return null;
	}
}
