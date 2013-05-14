package pairwisetesting.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.Map.Entry;

public class LibDependence implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Map<String, byte[]> libMap;

	public LibDependence() {
		libMap = new TreeMap<String, byte[]>();
	}

	public void addJavaLib(String fullPath) {
		String libPath = fullPath.substring(0, fullPath.lastIndexOf("/")+1);
		String libName = fullPath.substring(fullPath.lastIndexOf("/") + 1, fullPath
				.length());
		
		System.out.println("libPath:" + libPath);
		System.out.println("libName:" + libName);
		addJavaLib(libName,libPath);

	}
	
	public boolean isEmpty(){
		return libMap.isEmpty();
	}

	public void addJavaLib(String libName, String libPath) {
		if (!libMap.containsKey(libName)) {

			int bytesum = 0;
			int byteread = 0;
			File file = new File(libPath + libName);
			if (file.exists()) { // 文件存在时
				try {
					InputStream inStream = new FileInputStream(file);
					byte[] buffer = new byte[(int) file.length()];
					while ((byteread = inStream.read(buffer)) != -1) {
						bytesum += byteread; // 字节数 文件大小
						System.out.println(bytesum);
					}
					libMap.put(libName, buffer);
					inStream.close();
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				} finally {

				}
			} else {
				System.out.println("File can not found!!");
			}
		}
	}

	public void writeLib(String libName, String libPath) {
		if (libMap.containsKey(libName)) {
			File file = new File(libPath + libName);
			FileOutputStream outStream = null;
			try {
				outStream = new FileOutputStream(file);
				byte[] buffer = libMap.get(libName);
				outStream.write(buffer);
				outStream.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
	}

	public void writeLibList(String libPath) {
		Set<Entry<String, byte[]>> libSet = libMap.entrySet();
		Iterator<Entry<String, byte[]>> ite = libSet.iterator();
		while (ite.hasNext()) {
			Entry<String, byte[]> entry = ite.next();
			writeLib(entry.getKey(), libPath);
		}
	}
}
