package pairwisetesting.test;

import junit.framework.TestCase;
import pairwisetesting.coredomain.Engine;
import pairwisetesting.coredomain.EngineException;
import pairwisetesting.coredomain.Factor;
import pairwisetesting.coredomain.MetaParameter;
import pairwisetesting.engine.am.AMEngine;
import pairwisetesting.engine.am.oaprovider.SelfRuleOAProviderFactory;
import pairwisetesting.engine.jenny.JennyEngine;
import pairwisetesting.engine.pict.PICTEngine;


public class BenchmarkTest extends TestCase {
	public void benchmarkEngine(Engine engine, MetaParameter mp) throws EngineException {
		long before = System.nanoTime();
		String[][] testData = engine.generateTestData(mp);
		long after = System.nanoTime();
		System.out.print((after - before) / 1000000);
//		System.out.print("\t");
		System.out.print(testData.length);
		System.out.print("\t");
//		System.out.println("Level: " + mp.getMaxNumOfLevels());
//		System.out.println("Factor: " + mp.getNumOfFactors());
//		System.out.println("Run: " + testData.length);
//		for (String[] row : testData) {
//			System.out.println(Arrays.toString(row));
//		}
	}
	
	public MetaParameter getMetaParameter(int numberOfLevels, int numberOfFactors) {
		MetaParameter mp = new MetaParameter(2);
		for (int i = 0; i < numberOfFactors; i++) {
			Factor factor = new Factor("Factor_" + (i+1));
			for (int j = 0; j < numberOfLevels; j++) {
				factor.addLevel("" + (j+1));
			}
			mp.addFactor(factor);
		}
		return mp;
	}
	
	public void testBenchmark() throws EngineException {
		Engine[] engines = new Engine[] {
				new AMEngine(new SelfRuleOAProviderFactory()),
				new JennyEngine(), 
				new PICTEngine(),
		};
		
		for (Engine engine : engines) {
			System.out.print(engine.getClass().getSimpleName() + "\t");	
		}
		System.out.println("");
		
		for (int i = 3; i < 12; i++) {
			MetaParameter mp = getMetaParameter(i, 11);
			for (Engine engine : engines) {
				benchmarkEngine(engine, mp);	
			}
			System.out.println("");			
		}

	}
}
