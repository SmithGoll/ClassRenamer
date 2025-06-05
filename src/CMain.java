package com.github.SmithGoll.ClassRenamer;

import java.io.*;
import java.util.*;
import java.util.zip.*;
import java.nio.charset.StandardCharsets;

import org.objectweb.asm.*;

public class CMain {
	private static IClassRenamer renamer;

	public static void main(String[] args) {
		String inputJarFileName;
		String outputJarFileName;
		String customRenamerClassName = null;

		ZipFile inputJar;
		Enumeration<? extends ZipEntry> entries;

		ZipOutputStream outputJar;

		if (args.length != 2 && args.length != 3) {
			System.err.println("Usage: renamer in_jar out_jar");
			System.err.println("   Or: renamer custom_renamer_class in_jar out_jar");
			System.exit(1);
		}

		{
			int idx = 0;
			if (args.length == 3) {
				customRenamerClassName = args[idx++];
			}
			inputJarFileName = args[idx++];
			outputJarFileName = args[idx++];
		}

		if (inputJarFileName.equals(outputJarFileName)) {
			System.err.println("Error: in_jar and out_jar cannot be the same file");
			System.exit(1);
		}

		renamer = loadRenamer(customRenamerClassName);

		try {
			inputJar = new ZipFile(inputJarFileName, StandardCharsets.UTF_8);
			outputJar = new ZipOutputStream(new FileOutputStream(outputJarFileName), StandardCharsets.UTF_8);

			entries = inputJar.entries();
			while (entries.hasMoreElements()) {
				ZipEntry entry = entries.nextElement();
				if (!entry.isDirectory() && entry.getName().endsWith(".class")) {
					initRenameMap(inputJar.getInputStream(entry));
				}
			}

			renamer = null;

			entries = inputJar.entries();
			while (entries.hasMoreElements()) {
				ZipEntry entry = entries.nextElement();
				String fileName = entry.getName();
				InputStream is = entry.isDirectory() ? null : inputJar.getInputStream(entry);

				if (is != null && fileName.endsWith(".class")) {
					processClass(is, outputJar);
				} else {
					outputJar.putNextEntry(new ZipEntry(fileName));
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

	public static IClassRenamer loadRenamer(String className) {
		if (className != null) {
			try {
				Class<?> renamerClass = CMain.class.getClassLoader().loadClass(className);
				return (IClassRenamer)renamerClass.getDeclaredConstructor().newInstance();
			} catch (Exception e) {
				e.printStackTrace();
				System.err.printf("Failed to load renamer \'%s\'%n", className);
			}
		}
		return new MyClassRenamer();
	}

	public static void initRenameMap(InputStream is) {
		MemberContainer member = new MemberContainer();

		try {
			ClassReader cr = new ClassReader(is);
			MyClassNameVisitor cv = new MyClassNameVisitor(member, renamer);

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
