package pairwisetesting.engine.am;

import com.google.common.base.Preconditions;

import pairwisetesting.engine.am.oaprovider.hadamard.H_2s_OAProvider;
import pairwisetesting.engine.am.oaprovider.ols.OLS_t2_OAProvider;
import pairwisetesting.engine.am.oaprovider.ols.OLS_tu_OAProvider;

/**
 * This abstract class acts as the OA provider to provide OA.
 * 
 * @see H_2s_OAProvider
 * @see OLS_t2_OAProvider
 * @see OLS_tu_OAProvider
 */
public abstract class OAProvider {

	/**
	 * The number of levels.
	 */
	protected int t;

	/**
	 * Constructs an OA provider with the specified number of levels.
	 * 
	 * @param t
	 *            the number of levels and it should be at least 1
	 * @throws IllegalArgumentException
	 *             if {@code t} < 1
	 */
	public OAProvider(int t) {
		Preconditions.checkArgument(t >= 1, 
				"The number of levels should >= 1.");
		this.t = t;
	}

	/**
	 * Constructs an OA provider with 2-level
	 * 
	 * @param t
	 *            the number of levels
	 */
	public OAProvider() {
		this(2);
	}

	/**
	 * Returns the OA based on the specified number of factors.
	 * 
	 * @param m
	 *            the number of factors
	 * @return the OA based on {@code m}
	 * @throws IllegalArgumentException
	 *             if {@code m} violates the preconditions of the specified OA
	 *             provider implementation
	 */
	public abstract int[][] get(int m);

}
