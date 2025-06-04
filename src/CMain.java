package ClassRenamer;

import java.io.*;
import java.util.*;
import java.util.zip.*;
import java.nio.charset.StandardCharsets;

import org.objectweb.asm.*;

public class CMain {
	public static void main(String[] args) {
		ZipFile inputJar;
		Enumeration<? extends ZipEntry> entries;

		ZipOutputStream outputJar;

		if (args.length != 2) {
			System.err.println("Usage: renamer in_jar out_jar");
			System.exit(1);
		}

		if (args[0].equals(args[1])) {
			System.err.println("Err: in_jar and out_jar cannot be the same file");
			System.exit(1);
		}

		try {
			inputJar = new ZipFile(args[0], StandardCharsets.UTF_8);
			outputJar = new ZipOutputStream(new FileOutputStream(args[1]), StandardCharsets.UTF_8);

			entries = inputJar.entries();
			while (entries.hasMoreElements()) {
				ZipEntry entry = entries.nextElement();
				if (!entry.isDirectory() && entry.getName().endsWith(".class")) {
					initRenameMap(inputJar.getInputStream(entry));
				}
			}

			entries = inputJar.entries();
			while (entries.hasMoreElements()) {
				ZipEntry entry = entries.nextElement();
				InputStream is = entry.isDirectory() ? null : inputJar.getInputStream(entry);

				if (is != null && entry.getName().endsWith(".class")) {
					processClass(is, outputJar);
				} else {
					outputJar.putNextEntry(entry);
					if (is != null) {
						Utils.streamCopy(is, outputJar);
						is.close();
					}
					outputJar.closeEntry();
				}
			}

			inputJar.close();
			outputJar.close();
		} catch (Exception e) {
			System.err.println("Error processing class:");
			e.printStackTrace();
			System.exit(1);
		}

		System.out.println("Done");
	}

	public static void initRenameMap(InputStream is) {
		MemberContainer member = new MemberContainer();

		try {
			ClassReader cr = new ClassReader(is);
			MyClassNameVisitor cv = new MyClassNameVisitor(member);

			cr.accept(cv, 0);

			ClassMemberMap.putMember(cv.className, member);

			cr = null;
			cv = null;
			is.close();
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	public static void processClass(InputStream is, ZipOutputStream outputJar) {
		try {
			ClassReader cr = new ClassReader(is);
			ClassWriter cw = new ClassWriter(/*cr, */0);

			MyClassVisitor cv = new MyClassVisitor(cw);

			cr.accept(cv, 0);

			ZipEntry entry = new ZipEntry(ClassNameMap.getClassName(cv.className) + ".class");

			outputJar.putNextEntry(entry);
			outputJar.write(cw.toByteArray());
			outputJar.closeEntry();

			cr = null;
			cw = null;
			cv = null;
			is.close();
		} catch (Exception e) {
			System.err.println("An error occurred when processing class file");
			e.printStackTrace();
			System.exit(1);
		}
	}
}
