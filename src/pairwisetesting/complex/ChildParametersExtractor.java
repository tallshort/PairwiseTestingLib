package pairwisetesting.complex;

import java.lang.reflect.Field;
import java.util.ArrayList;

import com.google.common.base.Preconditions;

import pairwisetesting.util.ClassUtil;

/**
 * This class acts as the extractor to extract the child parameters for some 
 * complex parameter.
 */
public class ChildParametersExtractor {
	
	/**
	 * Extracts and returns the child parameters for the complex parameter with
	 * the specified class name.
	 * 
	 * @param className
	 *            the specified complex parameter's class name
	 * @return the child parameters for the complex parameter with the specified
	 *         class name
	 * @throws NullPointerException
	 *             if {@code className} is null
	 */
	public Parameter[] getParameters(String className) {
		Preconditions.checkNotNull(className, "complex parameter's class name");
		ArrayList<Parameter> list = new ArrayList<Parameter>(); 
		
		try {
			Field[] fields = ClassUtil.getClass(className).getDeclaredFields();
			for (Field f : fields) {
				if (ClassUtil.isSimpleType(f.getType())) {
					Parameter p
						= new SimpleParameter(f.getType().getName(), 
								f.getName());
					list.add(p);
					
				} else {
					// Need extract its child parameters
					ComplexParameter cp
						= new ComplexParameter(f.getType().getName(), 
								f.getName());
					Parameter[] parameters
						= getParameters(f.getType().getName());
					
					for (Parameter child : parameters)
						cp.add(child);
					list.add(cp);
				}			
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list.toArray(new Parameter[0]);
	}
	
}
