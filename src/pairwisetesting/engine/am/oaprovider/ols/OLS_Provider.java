package pairwisetesting.engine.am.oaprovider.ols;

import java.util.List;

/**
 * This interface acts as the Orthogonal Latin Square provider.
 * 
 * @see Rp_OLS_Provider
 * @see Poly_OLS_Provider
 */
public interface OLS_Provider {
	/**
	 *  Returns the generated OLS based on Finite Field.
	 * 
	 * @param t
	 *            the order of the OLS
	 * @param n
	 *            the number of OLS
	 *            
	 * @return the generated OLS based on Finite Field
	 * @throws IllegalArgumentException
	 *             if {@code t} or {@code n} violates the preconditions of the 
	 *             specified OLS provider implementation
	 */
	List<int[][]> generate_OLS(int t, int n);
}
