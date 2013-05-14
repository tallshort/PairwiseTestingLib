package pairwisetesting.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.Map.Entry;

public class TestingMetaParameter implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Map<String, String> javaSource;
	private String testClassName;
	private String endPath = "";
	private Map<String, String> libList;

	public TestingMetaParameter() {
		this.javaSource = new TreeMap<String, String>();
		this.testClassName = null;
		libList = new TreeMap<String, String>();
		libList.put("testng-5.8-jdk15.jar", "testng-5.8-jdk15.jar");
		libList.put("xom-1.1.jar", "xom-1.1.jar");
	}

	public void addLib(String libName) {
		libList.put(libName, libName);
	}

	public String getLibString() {
		String result = "";
		Set<Entry<String, String>> set = libList.entrySet();
		Iterator<Entry<String, String>> ite = set.iterator();

		while (ite.hasNext()) {
			result += endPath + "lib/" + ite.next().getKey() + ";";
		}

		return result.substring(0, result.length() - 1);
	}

	public void writeFiles() {
		Set<Entry<String, String>> tempSet = javaSource.entrySet();
		Iterator<Entry<String, String>> ite = tempSet.iterator();

		while (ite.hasNext()) {
			Entry<String, String> file = ite.next();
			TextFile.write(endPath + file.getKey(), file.getValue());
		}

	}

	public void addSourceFile(String fileName) {
		if (!this.javaSource.containsKey(fileName)) {
			String fileContent = TextFile.read(endPath + fileName);
			javaSource.put(fileName, fileContent);
		}
	}

	public void addSourceFileFromList(ArrayList<String> fileList) {
		// System.out.println("fileList :" + fileList.size());
		// javaSource.clear();
		Iterator<String> ite = fileList.iterator();

		while (ite.hasNext()) {
			addSourceFile(ite.next());
		}

	}

	public void addTestCase(String fileName) {
		if (javaSource.containsKey(fileName)) {
			javaSource.remove(fileName);
		}
		
		System.out.println("read TestCase file");
		String fileContent = TextFile.read(endPath + fileName);
		javaSource.put(fileName, fileContent);

	}

	public String[] getFileArray() {

		Set<Entry<String, String>> tempSet = javaSource.entrySet();

		String[] temp = new String[tempSet.size()];

		Iterator<Entry<String, String>> ite = tempSet.iterator();
		int i = 0;
		while (ite.hasNext()) {
			temp[i++] = endPath + ite.next().getKey();
		}
		return temp;

	}

	public String getEndPath() {
		return this.endPath;
	}

	public void setEndPath(String path) {
		this.endPath = path;
	}

	public void setTestCase(String fileName) {
		this.testClassName = fileName.substring(fileName.indexOf("/") + 1,
				fileName.lastIndexOf(".")).replace("/", ".");
		// this.testCaseContent = TextFile.read(fileName);
	}

	public String gettestCaseClassName() {
		return this.testClassName;
	}

}
