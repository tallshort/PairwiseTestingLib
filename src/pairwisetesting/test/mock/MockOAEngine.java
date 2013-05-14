package pairwisetesting.test.mock;

import pairwisetesting.coredomain.Engine;
import pairwisetesting.coredomain.EngineException;
import pairwisetesting.coredomain.MetaParameter;


public class MockOAEngine extends Engine {
	
	public MockOAEngine() {
		this.transformer = new MockTestDataTransformer();
	}

	@Override
	public String[][] generateRawTestData(MetaParameter mp) throws EngineException {
		final String[][] L9_3_4 = new String[][] {
			{"1", "1", "1", "1"}, 
			{"1", "2", "2", "2"}, 
			{"1", "3", "3", "3"}, 
			{"2", "1", "2", "3"}, 
			{"2", "2", "3", "1"}, 
			{"2", "3", "1", "2"}, 
			{"3", "1", "3", "2"}, 
			{"3", "2", "1", "3"}, 
			{"3", "3", "2", "1"}, 
		};

		return L9_3_4;
	}

}
