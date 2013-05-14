package pairwisetesting.engine.am.oaprovider;

import com.google.common.base.Preconditions;

import pairwisetesting.coredomain.EngineException;
import pairwisetesting.engine.am.IOAProviderFactory;
import pairwisetesting.engine.am.OAProvider;
import pairwisetesting.engine.am.oaprovider.hadamard.H_2s_OAProvider;
import pairwisetesting.engine.am.oaprovider.ols.OLS_t2_OAProvider;
import pairwisetesting.engine.am.oaprovider.ols.OLS_tu_OAProvider;
import pairwisetesting.engine.am.oaprovider.util.MathUtil;

/**
 * This OA provider factory uses several OA provider implementations to create
 * the proper OA provider based on the number of levels and the number of 
 * factors.
 * 
 * @see OAProvider
 * @see H_2s_OAProvider
 * @see OLS_t2_OAProvider
 * @see OLS_tu_OAProvider
 */
public class CascadeOAProviderFactory implements IOAProviderFactory {
	
	/**
	 * Creates OA provider based on the specified number of levels and the
	 * specified number of factor using several cascaded OA provider
	 * implementations.
	 * 
	 * @see pairwisetesting.engine.am.IOAProviderFactory#create(int, int)
	 * @throws IllegalArgumentException
	 *             if {@code t} < 1 or {@code m} < 2
	 */
	public OAProvider create(int t, int m) throws EngineException {
		Preconditions.checkArgument(t >= 1, 
				"The number of levels should >= 1.");
		Preconditions.checkArgument(m >= 2, 
				"The number of factors should >= 2.");
		if (t == 2 && MathUtil.is_2sMinusOne(m)) {
			return new H_2s_OAProvider();
		} else if (MathUtil.partOfPrimePower(t) != null) {
			if (m <= t + 1) {
				return new OLS_t2_OAProvider(t);
			} else {
				return new OLS_tu_OAProvider(t);
			}
		} else {
			throw new EngineException("No OA provider available");
		}
	}
	
}
