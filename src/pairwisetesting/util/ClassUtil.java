package pairwisetesting.util;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.common.base.Preconditions;

/**
 * This utility class provides useful methods to handle Class related issues.
 */
public class ClassUtil {

	private static HashMap<String, Class<?>> primitiveClassMap 
									= new HashMap<String, Class<?>>();
	static {
		primitiveClassMap.put("short", short.class);
		primitiveClassMap.put("int", int.class);
		primitiveClassMap.put("long", long.class);
		primitiveClassMap.put("float", float.class);
		primitiveClassMap.put("double", double.class);
		primitiveClassMap.put("char", char.class);
		primitiveClassMap.put("boolean", boolean.class);
	}

	// Suppress default constructor for noninstantiability
	private ClassUtil() {
		throw new AssertionError();
	}

	/**
	 * Returns the class object associated with the specified class name.
	 * 
	 * @param className
	 *            the specified class name
	 * @return the Class object associated with {@code className}
	 * @throws NullPointerException
	 *             if {@code className} is null
	 */
	public static Class<?> getClass(String className) {
		Preconditions.checkNotNull(className, "class name");
		try {
			if (primitiveClassMap.containsKey(className)) {
				return primitiveClassMap.get(className);
			} else {
				ClassLoader cl = Thread.currentThread().getContextClassLoader();
				return cl.loadClass(className);
				// return Class.forName(className, true, cl);
			}
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Returns <tt>true</tt> if the specified class object is an abstract class.
	 * 
	 * @param clazz
	 *            the specified class object
	 * @return <tt>true</tt> if {@code clazz} is an abstract class.
	 * @throws NullPointerException
	 *             if {@code clazz} is null
	 */
	public static boolean isAbstractClass(Class<?> clazz) {
		Preconditions.checkNotNull(clazz, "class object");
		return Modifier.isAbstract(clazz.getModifiers());
	}

	/**
	 * Returns <tt>true</tt> if the specified class object is an interface.
	 * 
	 * @param clazz
	 *            the specified class object
	 * @return <tt>true</tt> if {@code clazz} is an interface
	 * @throws NullPointerException
	 *             if {@code clazz} is null
	 */
	public static boolean isInterface(Class<?> clazz) {
		Preconditions.checkNotNull(clazz, "class object");
		return clazz.isInterface();
	}

	/**
	 * Returns the return type name of method with the specified method
	 * signature. If parameterTypeNames is empty, then return the first method's
	 * return type name with the same method name.
	 * 
	 * @param className
	 *            the specified class name
	 * @param methodName
	 *            the specified method's name
	 * @param parameterTypeNames
	 *            the specified method's parameter type names
	 * @return the return type name of the specified method signature
	 * @throws NullPointerException
	 *             if {@code className} or {@code methodName} is null
	 * @throws RuntimeException if something wrong during processing
	 */
	public static String getReturnTypeName(String className, String methodName,
			String... parameterTypeNames) {
		return getReturnTypeName(getClass(className), methodName,
				parameterTypeNames);
	}

	/**
	 * Returns the return type name of method with the specified method
	 * signature. If parameterTypeNames is empty, then return the first method's
	 * return type name with the same method name.
	 * 
	 * @param clazz
	 *            the specified class object
	 * @param methodName
	 *            the specified method's name
	 * @param parameterTypeNames
	 *            the specified method's parameter type names
	 * @return the return type name of the specified method signature
	 * @throws NullPointerException
	 *             if {@code className} or {@code methodName} is null
	 * @throws RuntimeException
	 *             if something wrong during processing
	 */
	public static String getReturnTypeName(Class<?> clazz, String methodName,
			String... parameterTypeNames) {
		Preconditions.checkNotNull(clazz, "class object");
		Preconditions.checkNotNull(methodName, "method name");
		ArrayList<Class<?>> parameterTypeList = new ArrayList<Class<?>>();
		for (String parameterTypeName : parameterTypeNames) {
			parameterTypeList.add(getClass(parameterTypeName));
		}
		Method method = null;
		try {
			if (parameterTypeList.size() == 0) {
				Method[] allMethods = clazz.getDeclaredMethods();
				for (Method m : allMethods) {
					if (m.getName().equals(methodName)) {
						method = m;
						break;
					}
				}
			} else {
				method = clazz.getDeclaredMethod(methodName, 
						parameterTypeList.toArray(new Class<?>[0]));
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return method.getReturnType().getName();
	}

	/**
	 * Returns the simple class name of the specified class name.
	 * 
	 * @param className
	 *            the specified class name
	 * @return the simple class name of {@code className}
	 * @throws NullPointerException
	 *             if {@code className} is null
	 */
	public static String getSimpleClassName(String className) {
		Preconditions.checkNotNull(className, "class name");
		return className.replaceFirst("(.*[.])", "");
	}

	/**
	 * Returns <tt>true</tt> if the specified class object is a simple type. For
	 * example, String or Enum.
	 * <p>Currently array type is considered as simple type.
	 * <p>Currently container type is considered as simple type.
	 * 
	 * @param clazz
	 *            the specified class object
	 * @return <tt>true</tt> if {@code clazz} is a simple type
	 * @throws NullPointerException
	 *             if {@code clazz} is null
	 */
	public static boolean isSimpleType(Class<?> clazz) {
		Preconditions.checkNotNull(clazz, "class object");
		return (clazz.isPrimitive() 
				|| clazz == String.class 
				|| clazz.isEnum()
				|| clazz.isArray()
				|| containsInterface(clazz, List.class)
				|| containsInterface(clazz, Map.class) || containsInterface(
				clazz, Set.class));
	}

	/**
	 * @see ClassUtil#isSimpleType(Class)
	 * @param className the specified class name
	 * @throws NullPointerException
	 *             if {@code className} is null
	 */
	public static boolean isSimpleType(String className) {
		Preconditions.checkNotNull(className, "class name");
		if (className.equals("void")) {
			return true;
		}
		// Currently only support one dimension array type
		return isSimpleType(getClass(className.replace("[]", "")));
	}

	/**
	 * Returns the first method object that matches the specified return type
	 * name and method name.
	 * 
	 * @param clazz
	 *            the specified class object
	 * @param returnTypeName
	 *            the specified method's return type name
	 * @param methodName
	 *            the specified method's name
	 * @return the first method object that matches {@code returnTypeName} and
	 *         {@code methodName}
	 * @throws NullPointerException
	 *             if {@code clazz} or {@code returnTypeName} or 
	 *             {@code methodName} is null
	 */
	public static Method getFirstMethod(Class<?> clazz, String returnTypeName,
			String methodName) {
		Preconditions.checkNotNull(clazz, "class object");
		Preconditions.checkNotNull(returnTypeName, "return type name");
		Preconditions.checkNotNull(methodName, " method name");
		Method[] allMethods = clazz.getDeclaredMethods();
		for (Method m : allMethods) {
			if (m.getReturnType().getCanonicalName().equals(returnTypeName)
					&& m.getName().equals(methodName)) {
				return m;
			}
		}
		return null;
	}

	/**
	 * Returns <tt>true</tt> if the specified class contains the specified 
	 * interface.
	 * 
	 * @param dstClass
	 *            the class object to check
	 * @param srcClass
	 *            the interface object to check
	 * @return <tt>true</tt> if {@code dstClass} contains interface {@code
	 *         srcClass}
	 * @throws NullPointerException
	 *             if {@code srcClass} is null 
	 */
	public static boolean containsInterface(Class<?> dstClass, 
			Class<?> srcClass) {
		Preconditions.checkNotNull(srcClass, "the interface object to check");
		if (dstClass == null)
			return false;
		return (Arrays.asList(dstClass.getInterfaces()).contains(srcClass) 
				|| containsInterface(dstClass.getSuperclass(), srcClass));
	}

	/**
	 * Returns <tt>true</tt> if the specified class contains the specified super
	 * class.
	 * 
	 * @param dstClass
	 *            the class object to check
	 * @param srcClass
	 *            the super class object to check
	 * @return <tt>true</tt> if {@code dstClass} contains super class {@code
	 *         srcClass}
	 * @throws NullPointerException
	 *             if {@code dstClass} or {@code srcClass} is null 
	 */
	public static boolean containsSuperClass(Class<?> dstClass, 
			Class<?> srcClass) {
		Preconditions.checkNotNull(dstClass, "the class object to check");
		Preconditions.checkNotNull(srcClass, "the super class object to check");
		Class<?> superClass = dstClass.getSuperclass();
		if (superClass == null)
			return false;
		return (superClass == srcClass 
				|| containsSuperClass(superClass, srcClass));
	}
}
