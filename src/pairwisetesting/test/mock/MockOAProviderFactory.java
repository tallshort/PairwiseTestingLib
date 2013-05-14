package pairwisetesting.test.mock;

import pairwisetesting.engine.am.IOAProviderFactory;
import pairwisetesting.engine.am.OAProvider;

public class MockOAProviderFactory implements IOAProviderFactory {

	public OAProvider create(int t, int m) {
		return new MockOAProvider();
	}

}
