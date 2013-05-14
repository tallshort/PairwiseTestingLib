package pairwisetesting.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class ObjectSerializ implements Serializable {
	/**
	 * 此例是一个序列化/反序列化的方法 ：
	 * 
	 * 主要思想是把一个对象写入对象输出流，然后把这个流里面的数据导入字节数组输出流
	 * 再把字节数组输出流导入到字节数组。这时，由于byte[]数组是从ObjectOutputStream之后得来的， 那么是不可以new
	 * String(buff)的，因为其中包含了不可见字符，根本不是一个字符串。
	 * 只能用sun.misc.BASE64Encoder把它翻译成字符串形式。 至此，这就完成了序列化成字符串行式的过程
	 * 
	 * 再通过sun.misc.BASE64Decoder().decodeBuffer把字符串转换成一个byte[]， 然后通过其反思路完成反序列化过程
	 */
	private static final long serialVersionUID = 1L;

	public static String Object2String(Object input) {
		ObjectOutputStream OOS = null;
		// 序列化后数据流给ByteArrayOutputStream 来保存。
		// ByteArrayOutputStream 可转成字符串或字节数组
		ByteArrayOutputStream BAOS = new ByteArrayOutputStream();

		String Sstr = "";
		try {
			OOS = new ObjectOutputStream(BAOS);
			OOS.writeObject(input);
			byte[] buff = BAOS.toByteArray();

			/*
			 * 由于byte[]数组是从ObjectOutputStream之后得来的， 那么是不可以new
			 * String(buff)的，因为其中包含了不可见字符，根本不是一个字符串
			 */
			// 转换成字符串
			sun.misc.BASE64Encoder encoder = new sun.misc.BASE64Encoder();
			Sstr = encoder.encode(buff);

			OOS.close();

		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception ex) {
			System.out.println("序列化时产生错误");
		}

		return Sstr;
	}

	public static Object String2Object(String Sstr) {
		// ByteArrayInputStream 可接收一个字节数组"byte[]"。供反序列化做参数
		ByteArrayInputStream BAIS = null;
		// 反序列化使用的输入流
		ObjectInputStream OIS = null;
		// 把字符串转成一个byte[]

		Object result = null;

		try {
			byte[] DSbuff = new sun.misc.BASE64Decoder().decodeBuffer(Sstr);
			// 实现反序列化
			BAIS = new ByteArrayInputStream(DSbuff);
			OIS = new ObjectInputStream(BAIS);
			result = (Object) OIS.readObject();
			OIS.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception ex) {
			System.out.println("序列化时产生错误");
		}
		return result;
	}
}