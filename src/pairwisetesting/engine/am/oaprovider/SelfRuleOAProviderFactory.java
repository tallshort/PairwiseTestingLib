package pairwisetesting.engine.am.oaprovider;

import com.google.common.base.Preconditions;

import pairwisetesting.engine.am.IOAProviderFactory;
import pairwisetesting.engine.am.OAProvider;
import pairwisetesting.engine.am.oaprovider.ols.OLS_t2_OAProvider;
import pairwisetesting.engine.am.oaprovider.util.MathUtil;

/**
 * This self-rule OA provider factory uses {@link OLS_t2_OAProvider} OA provider 
 * implementation to create the OA provider to handle all the valid number of
 * levels and factors.
 * 
 * @see OAProvider
 * @see OLS_t2_OAProvider
 */
public class SelfRuleOAProviderFactory implements IOAProviderFactory {
	
	/**
	 * Creates OA provider using {@link OLS_t2_OAProvider} OA provider
	 * implementation.
	 * 
	 * @see pairwisetesting.engine.am.IOAProviderFactory#create(int, int)
	 * @throws IllegalArgumentException
	 *             if {@code t} < 1 or {@code m} < 2
	 */
	public OAProvider create(int t, int m) {
		Preconditions.checkArgument(t >= 1, 
				"The number of levels should >= 1.");
		Preconditions.checkArgument(m >= 2, 
				"The number of factors should >= 2.");
		if (MathUtil.partOfPrimePower(t) != null && m <= t + 1) {
			return new OLS_t2_OAProvider(t);
		} else {
			// Find the bigger OA that can handle the request
			int nextPrimePower = 0;
			if (t > m - 2) {
				nextPrimePower = MathUtil.nextPrimePower(t);
			} else {
				nextPrimePower = MathUtil.nextPrimePower(m - 2);
			}
			while (true) {
				if (m <= nextPrimePower + 1) {
					return new OLS_t2_OAProvider(nextPrimePower);
				} else {
					nextPrimePower = MathUtil.nextPrimePower(nextPrimePower);
				}
			}
		}
	}

}
