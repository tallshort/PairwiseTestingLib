package pairwisetesting.test.mock;

import pairwisetesting.engine.am.OAProvider;

public class MockOAProvider extends OAProvider {

	@Override
	public int[][] get(int m) {
		final int[][] L9_3_4 = new int[][] { 
				{ 1, 1, 1, 1 }, { 1, 2, 2, 2 },
				{ 1, 3, 3, 3 }, { 2, 1, 2, 3 }, 
				{ 2, 2, 3, 1 }, { 2, 3, 1, 2 },
				{ 3, 1, 3, 2 }, { 3, 2, 1, 3 }, 
				{ 3, 3, 2, 1 }
				};

		return L9_3_4;
	}

}
