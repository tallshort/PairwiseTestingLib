package pairwisetesting.util;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.google.common.base.Preconditions;

/**
 * The utility class converts the input string to proper type.
 */
public class Converter {
	
	// Suppress default constructor for noninstantiability
	private Converter() {
		throw new AssertionError();
	}

	/**
	 * Converts the specified input string to int type.
	 * 
	 * @param input
	 *            the specified input string
	 * @param intType
	 *            the integer class object marker
	 * @return the int value of {@code input}
	 * @throws NullPointerException
	 *             if {@code input} or {@code intType} is null
	 */
	public static int convertTo(String input, Class<Integer> intType) {
		Preconditions.checkNotNull(input, "input");
		Preconditions.checkNotNull(intType, "integer type");
		return Integer.parseInt(input);
	}
	
	/**
	 * Converts the specified input string to short type.
	 * 
	 * @param input
	 *            the specified input string
	 * @param shortType
	 *            the short integer class object marker
	 * @return the short value of {@code input}
	 * @throws NullPointerException
	 *             if {@code input} or {@code shortType} is null
	 */
	public static short convertTo(String input, Class<Short> shortType) {
		Preconditions.checkNotNull(input, "input");
		Preconditions.checkNotNull(shortType, "short integer type");
		return Short.parseShort(input);
	}
	
	/**
	 * Converts the specified input string to long type.
	 * 
	 * @param input
	 *            the specified input string
	 * @param longType
	 *            the long integer class object marker
	 * @return the long value of {@code input}
	 * @throws NullPointerException
	 *             if {@code input} or {@code longType} is null
	 */
	public static long convertTo(String input, Class<Long> longType) {
		Preconditions.checkNotNull(input, "input");
		Preconditions.checkNotNull(longType, "long integer type");
		return Long.parseLong(input);
	}
	
	/**
	 * Converts the specified input string to float type.
	 * 
	 * @param input
	 *            the specified input string
	 * @param floatType
	 *            the float class object marker
	 * @return the float value of {@code input}
	 * @throws NullPointerException
	 *             if {@code input} or {@code floatType} is null
	 */
	public static float convertTo(String input, Class<Float> floatType) {
		Preconditions.checkNotNull(input, "input");
		Preconditions.checkNotNull(floatType, "float type");
		return Float.parseFloat(input);
	}
	
	/**
	 * Converts the specified input string to double type.
	 * 
	 * @param input
	 *            the specified input string
	 * @param doubleType
	 *            the double class object marker
	 * @return the double value of {@code input}
	 * @throws NullPointerException
	 *             if {@code input} or {@code doubleType} is null
	 */
	public static double convertTo(String input, Class<Double> doubleType) {
		Preconditions.checkNotNull(input, "input");
		Preconditions.checkNotNull(doubleType, "double type");
		return Double.parseDouble(input);
	}
	
	/**
	 * Converts the specified input string to char type.
	 * 
	 * @param input
	 *            the specified input string
	 * @param charType
	 *            the character class object marker
	 * @return the char value of {@code input}
	 * @throws NullPointerException
	 *             if {@code input} or {@code charType} is null
	 */
	public static char convertTo(String input, Class<Character> charType) {
		Preconditions.checkNotNull(input, "input");
		Preconditions.checkNotNull(charType, "character type");
		if (input.equals("")) {
			return '\0';
		}
		return input.charAt(0);
	}
	
	/**
	 * Converts the specified input string to byte type.
	 * 
	 * @param input
	 *            the specified input string
	 * @param byteType
	 *            the byte class object marker
	 * @return the byte value of {@code input}
	 * @throws NullPointerException
	 *             if {@code input} or {@code byteType} is null
	 */
	public static byte convertTo(String input, Class<Byte> byteType) {
		Preconditions.checkNotNull(input, "input");
		Preconditions.checkNotNull(byteType, "byte type");
		if (input.equals("")) {
			return '\0';
		}
		return (byte)input.charAt(0);
	}
	
	/**
	 * Converts the specified input string to boolean type.
	 * 
	 * @param input
	 *            the specified input string
	 * @param booleanType
	 *            the boolean class object marker
	 * @return the boolean value of {@code input}
	 * @throws NullPointerException
	 *             if {@code input} or {@code booleanType} is null
	 */
	public static boolean convertTo(String input, Class<Boolean> booleanType) {
		Preconditions.checkNotNull(input, "input");
		Preconditions.checkNotNull(booleanType, "boolean type");
		return Boolean.parseBoolean(input);
	}
	
	/**
	 * Converts the specified input string to string type.
	 * 
	 * @param input
	 *            the specified input string
	 * @param stringType
	 *            the string class object marker
	 * @return the string value of {@code input}
	 * @throws NullPointerException
	 *             if {@code input} or {@code stringType} is null
	 */
	public static String convertTo(String input, Class<String> stringType) {
		Preconditions.checkNotNull(input, "input");
		Preconditions.checkNotNull(stringType, "string type");
		return input;
	}
	
	/**
	 * Converts the specified input string to enum type.
	 * 
	 * @param input
	 *            the specified input string
	 * @param enumType
	 *            the enum class object marker
	 * @return the enum value of {@code input}
	 * @throws NullPointerException
	 *             if {@code input} or {@code enumType} is null
	 */
	public static <T extends Enum<T>> T convertTo(String input, 
			Class<T> enumType) {
		Preconditions.checkNotNull(input, "input");
		Preconditions.checkNotNull(enumType, "enum type");
		return Enum.valueOf(enumType, input);
	}
	
	/**
	 * Converts the specified input string to date type.
	 * 
	 * @param input
	 *            the specified input string
	 * @param dateType
	 *            the date class object marker
	 * @return the date value of {@code input}
	 * @throws NullPointerException
	 *             if {@code input} or {@code dateType} is null
	 * @throws RuntimeException
	 *             if anything wrong during the processing
	 */
	public static Date convertTo(String input, Class<Date> dateType) {
		Preconditions.checkNotNull(input, "input");
		Preconditions.checkNotNull(dateType, "date type");
		Date date = null;
		try {
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			date = dateFormat.parse(input);
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
		return date;
	}

}

