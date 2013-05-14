package pairwisetesting.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class ObjectSerializ implements Serializable {
	/**
	 * ������һ�����л�/�����л��ķ��� ��
	 * 
	 * ��Ҫ˼���ǰ�һ������д������������Ȼ����������������ݵ����ֽ����������
	 * �ٰ��ֽ�������������뵽�ֽ����顣��ʱ������byte[]�����Ǵ�ObjectOutputStream֮������ģ� ��ô�ǲ�����new
	 * String(buff)�ģ���Ϊ���а����˲��ɼ��ַ�����������һ���ַ�����
	 * ֻ����sun.misc.BASE64Encoder����������ַ�����ʽ�� ���ˣ������������л����ַ�����ʽ�Ĺ���
	 * 
	 * ��ͨ��sun.misc.BASE64Decoder().decodeBuffer���ַ���ת����һ��byte[]�� Ȼ��ͨ���䷴˼·��ɷ����л�����
	 */
	private static final long serialVersionUID = 1L;

	public static String Object2String(Object input) {
		ObjectOutputStream OOS = null;
		// ���л�����������ByteArrayOutputStream �����档
		// ByteArrayOutputStream ��ת���ַ������ֽ�����
		ByteArrayOutputStream BAOS = new ByteArrayOutputStream();

		String Sstr = "";
		try {
			OOS = new ObjectOutputStream(BAOS);
			OOS.writeObject(input);
			byte[] buff = BAOS.toByteArray();

			/*
			 * ����byte[]�����Ǵ�ObjectOutputStream֮������ģ� ��ô�ǲ�����new
			 * String(buff)�ģ���Ϊ���а����˲��ɼ��ַ�����������һ���ַ���
			 */
			// ת�����ַ���
			sun.misc.BASE64Encoder encoder = new sun.misc.BASE64Encoder();
			Sstr = encoder.encode(buff);

			OOS.close();

		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception ex) {
			System.out.println("���л�ʱ��������");
		}

		return Sstr;
	}

	public static Object String2Object(String Sstr) {
		// ByteArrayInputStream �ɽ���һ���ֽ�����"byte[]"���������л�������
		ByteArrayInputStream BAIS = null;
		// �����л�ʹ�õ�������
		ObjectInputStream OIS = null;
		// ���ַ���ת��һ��byte[]

		Object result = null;

		try {
			byte[] DSbuff = new sun.misc.BASE64Decoder().decodeBuffer(Sstr);
			// ʵ�ַ����л�
			BAIS = new ByteArrayInputStream(DSbuff);
			OIS = new ObjectInputStream(BAIS);
			result = (Object) OIS.readObject();
			OIS.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception ex) {
			System.out.println("���л�ʱ��������");
		}
		return result;
	}
}