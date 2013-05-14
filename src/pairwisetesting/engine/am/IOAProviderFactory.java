package pairwisetesting.engine.am;

import pairwisetesting.coredomain.EngineException;

/**
 * This class acts as the factory to create OA providers.
 * 
 * @see pairwisetesting.engine.am.oaprovider.CascadeOAProviderFactory
 * @see pairwisetesting.engine.am.oaprovider.SelfRuleOAProviderFactory
 * @see OAProvider
 */
public interface IOAProviderFactory {
	/**
	 * Creates OA provider based on the specified number of levels and the
	 * specified number of factors.
	 * 
	 * @param t
	 *            the number of levels
	 * @param m
	 *            the number of factors
	 * 
	 * @return the proper OAProvider based on {@code t} and {@code m}
	 * @throws EngineException
	 *             if the OA factory can not handle the specified {@code t} and
	 *             {@code m}
	 * @throws IllegalArgumentException
	 *             if {@code t} or {@code m} violates the preconditions of the 
	 *             specified OA provider factory implementation
	 */
	OAProvider create(int t, int m) throws EngineException;
}
