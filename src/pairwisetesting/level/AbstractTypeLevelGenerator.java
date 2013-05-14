package pairwisetesting.level;

import java.io.File;
import java.util.ArrayList;

import com.google.common.base.Preconditions;

import pairwisetesting.util.ClassUtil;
import pairwisetesting.util.Directory;

/**
 * The generator generates levels for factor with abstract type, whose levels
 * are the concrete implementations for the abstract type.
 */
public class AbstractTypeLevelGenerator implements ILevelGenerator {

	private String className;
	private String binaryDir;

	/**
	 * Constructs a generator with the specified full class name and the
	 * directory contains the binary files.
	 * 
	 * @param className
	 *            the full class name
	 * @param binaryDir
	 *            the directory contains the binary files
	 * @throws NullPointerException
	 *             if {@code className} or {@code binaryDir} is null
	 */
	public AbstractTypeLevelGenerator(String className, String binaryDir) {
		Preconditions.checkNotNull(className, "class name");
		Preconditions.checkNotNull(binaryDir, "binary directory");
		this.className = className;
		this.binaryDir = binaryDir;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see pairwisetesting.level.ILevelGenerator#generateLevels()
	 */
	public String[] generateLevels() {
		ArrayList<String> dir = new ArrayList<String>();

		Class<?> targetClass = ClassUtil.getClass(className);
		Directory.TreeInfo treeInfo = Directory.walk(binaryDir, ".*[.]class$");
		for (File javaFile : treeInfo) {
			String str = javaFile.getPath();
			String currentClassName = str.replace(File.separator, ".").replace(
					binaryDir + ".", "").replace(".class", "");
			Class<?> clazz = ClassUtil.getClass(currentClassName);

			// System.out.println("=>" + clazz);
			// System.out.println(Arrays.asList(clazz.getInterfaces()));
			if (!ClassUtil.isAbstractClass(clazz)
					&& (ClassUtil.containsInterface(clazz, targetClass) 
						|| ClassUtil.containsSuperClass(clazz, targetClass))) {
				dir.add(currentClassName);
			}
		}
		return dir.toArray(new String[0]);
	}
}
