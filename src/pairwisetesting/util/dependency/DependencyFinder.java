package pairwisetesting.util.dependency;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

import pairwisetesting.util.ClassUtil;
import pairwisetesting.util.Directory;

public class DependencyFinder {

	private String className;

	// Short binary path, "bin"
	private String binPath;

	// Short source path, "src"
	private String srcPath;

	// All source file under srcPath
	HashSet<String> directory;

	// All dependent classes
	HashSet<String> classSet;
	HashSet<String> srcList;
	HashSet<String> libList;
	HashSet<String> impList;

	HashSet<String> stdLibSet;
	HashSet<String> siteSet;

	/**
	 * 
	 */
	public DependencyFinder(String fullClassName, String sourcePath,
			String binaryPath) {
		className = fullClassName;

		srcPath = sourcePath;
		binPath = binaryPath;

		classSet = new HashSet<String>();
		srcList = new HashSet<String>();
		libList = new HashSet<String>();
		impList = new HashSet<String>();

		directory = walkDirectory(srcPath);

		stdLibSet = new HashSet<String>();
		stdLibSet.add("java");
		stdLibSet.add("javax");

		siteSet = new HashSet<String>();
		siteSet.add("net");
		siteSet.add("com");
		siteSet.add("org");
		siteSet.add("nu");
	}

	public DependencyFinder(String fullClassName, String sourcePath,
			String binaryPath, String endPath) {

		if (!endPath.endsWith("/")) {
			endPath += "/";
		}

		className = fullClassName;

		srcPath = endPath + sourcePath;
		binPath = endPath + binaryPath;

		classSet = new HashSet<String>();
		srcList = new HashSet<String>();
		libList = new HashSet<String>();
		impList = new HashSet<String>();

		directory = walkDirectory(srcPath);

		stdLibSet = new HashSet<String>();
		stdLibSet.add("java");
		stdLibSet.add("javax");

		siteSet = new HashSet<String>();
		siteSet.add("net");
		siteSet.add("com");
		siteSet.add("org");
		siteSet.add("nu");

	}

	public DependencyResult findDependency() {
		findDependency(className);

		DependencyResult result = new DependencyResult();
		result.libList = new ArrayList<String>(libList);
		result.mockList = new ArrayList<String>(generateMockList());
		result.srcList = new ArrayList<String>(srcList);
		result.impList = new ArrayList<String>(impList);

		return result;
	}

	private void findDependency(String fullClassName) {
		String xmlPath = extract(fullClassName);
		HashSet<String> rawResult = c2c(fullClassName, xmlPath);
		HashSet<String> classList = splitRawC2CResult(rawResult);
		resolveKeyLibName(rawResult);

		Iterator<String> iter = classList.iterator();
		// System.out.println(classList);
		
		while (iter.hasNext()) {
			String className = iter.next();
			String classPath = buildSourcePath(className);
			if (className.equals(this.className) || srcList.contains(classPath))
				continue;
			classSet.add(className);
			srcList.add(classPath);
			findDependency(className);
		}
	}

	/**
	 * Call DependencyExtractor.bat to generate xml file Return the path of the
	 * generated xml file
	 */
	private String extract(String className) {
		String fullBinaryPath = buildBinaryPath(className);
		String xmlPath = buildXMLPath(className);

		StringBuilder extCommand = new StringBuilder();
		extCommand.append("DependencyExtractor.bat -xml -out ");
		extCommand.append(xmlPath).append(" ");
		extCommand.append(fullBinaryPath);

		try {
			String command = extCommand.toString();
			//System.out.println(command);

			Process p = Runtime.getRuntime().exec(command);

			// Wait for the process p to end
			// Ensure the XML file is generated
			// Or, we can't call c2c, c2p, ...
			p.waitFor();

		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		return xmlPath;
	}

	private HashSet<String> c2c(String className, String xmlPath) {
		String name = shortClassName(className);

		// Command line for c2c
		StringBuilder c2cCommand = new StringBuilder();
		c2cCommand.append("c2c.bat ").append("-scope-includes ");
		c2cCommand.append("/").append(name).append("/ ");
		c2cCommand.append(xmlPath);

		HashSet<String> resultList = new HashSet<String>();

		try {
			String command = c2cCommand.toString();
			//System.out.println(command);

			Process p = Runtime.getRuntime().exec(command);
			BufferedReader in = new BufferedReader(new InputStreamReader(p
					.getInputStream()));

			String str = null;
			while (null != (str = in.readLine())) {
				resultList.add(str);
				//System.out.println(str);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Here return formated results
		return formatC2COutput(resultList);
	}

	private HashSet<String> formatC2COutput(HashSet<String> c2cOutput) {
		HashSet<String> results = new HashSet<String>();
		Iterator<String> iter = c2cOutput.iterator();

		while (iter.hasNext()) {
			String line = iter.next();
			String str = line.trim();

			if (!str.startsWith("--> "))
				continue;

			String dep = str.substring(4);
			String string = dep.endsWith(" *") ? dep.substring(0,
					dep.length() - 2) : dep;

			// System.out.println(string);

			string = string.replaceAll("(\\[\\])+$", "");

			// We only care about individual source file
			String[] name = string.split("[$]");
			// System.out.println(name[0]);
			results.add(name[0]);
		}
		return results;
	}

	// Remove all class under source dir from rawResult
	private HashSet<String> splitRawC2CResult(HashSet<String> rawResult) {
		HashSet<String> classList = new HashSet<String>();
		Iterator<String> iter = rawResult.iterator();
		while (iter.hasNext()) {
			String str = iter.next();
			String path = buildSourcePath(str);
			if (directory.contains(path)) {
				classList.add(str);
				iter.remove();
			}
		}
		return classList;
	}

	private String buildSourcePath(String className) {
		String classPath = className.replace('.', '/');

		StringBuilder builder = new StringBuilder();
		builder.append(srcPath).append("/");
		builder.append(classPath).append(".java");

		return builder.toString();
	}

	private String buildBinaryPath(String className) {
		String classPath = className.replace('.', '/');

		StringBuilder builder = new StringBuilder();
		builder.append(binPath).append("/");
		builder.append(classPath).append(".class");

		return builder.toString();
	}

	private String buildXMLPath(String className) {
		String classPath = className.replace('.', '/');

		StringBuilder builder = new StringBuilder();
		builder.append(binPath).append("/");
		builder.append(classPath).append(".xml");

		return builder.toString();
	}

	private String shortClassName(String className) {
		String[] array = className.split("\\.");

		// The last element in array is the narrowed name
		return array[array.length - 1];
	}

	private HashSet<String> walkDirectory(String srcPath) {
		HashSet<String> dir = new HashSet<String>();

		Directory.TreeInfo treeInfo = Directory.walk(srcPath, ".*[.]java$");
		for (File javaFile : treeInfo) {
			String str = javaFile.getPath();
			String path = str.replace('\\', '/');
			dir.add(path);
		}
		return dir;
	}

	private void resolveKeyLibName(HashSet<String> libSet) {
		Iterator<String> iter = libSet.iterator();
		while (iter.hasNext()) {
			String libName = iter.next();
			String[] array = libName.split("[.]");
			if (stdLibSet.contains(array[0]))
				continue;
			if (siteSet.contains(array[0]))
				libList.add(array[1]);
			else
				libList.add(array[0]);
			
			impList.add(libName);
		}
	}

	private HashSet<String> generateMockList() {
		HashSet<String> mockSet = new HashSet<String>();

		HashSet<Class<?>> concreteSet = new HashSet<Class<?>>();
		HashSet<Class<?>> interfaceSet = new HashSet<Class<?>>();
		HashSet<Class<?>> absClassSet = new HashSet<Class<?>>();

		for (String fullClassName : classSet) {

			// Collect interfaces & abstract & concrete classes
			try {
				Class<?> clazz = ClassUtil.getClass(fullClassName);
				if (clazz.isInterface()) {
					interfaceSet.add(clazz);
				} else if (Modifier.isAbstract(clazz.getModifiers())) {
					absClassSet.add(clazz);
				} else {
					concreteSet.add(clazz);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		HashSet<String> allConcreteClassNames = getAllConcreteClassNames();
		//System.out.println(interfaceSet);
		
		// Remove implemented interfaces & extended abstract classes
		// If some class implements an interface or extends an abstract class, 
		// then add the class into the srcList
		for (String s : allConcreteClassNames) {
			
			
			Class<?> c = ClassUtil.getClass(s);
			Iterator<Class<?>> iter = interfaceSet.iterator();
			while (iter.hasNext()) {
				if (ClassUtil.containsInterface(c, iter.next())) {
					srcList.add(buildSourcePath(s));
					iter.remove();
				}
			}
			iter = absClassSet.iterator();
			while (iter.hasNext()) {
				if (ClassUtil.containsSuperClass(c, iter.next())) {
					srcList.add(buildSourcePath(s));
					iter.remove();
				}
			}
		}

		// Left interfaces & abstract classes need mock
		for (Class<?> c : interfaceSet) {
			mockSet.add(c.getName());
		}
		for (Class<?> c : absClassSet) {
			mockSet.add(c.getName());
		}
		return mockSet;
	}
	
	private HashSet<String> getAllConcreteClassNames(){
		HashSet<String> set = new HashSet<String>();
		
		Directory.TreeInfo treeInfo = Directory.walk(binPath, ".*[.]class$");
		for (File javaFile : treeInfo) {
			String str = javaFile.getPath();
			String currentClassName
				= str.replace(File.separator, ".").replace(binPath + ".", "").replace(".class", "");
			Class<?> clazz = ClassUtil.getClass(currentClassName);
			if (!ClassUtil.isAbstractClass(clazz)) {
				set.add(currentClassName);
			}
		}
		return set;
	}
	

}
