package pairwisetesting.engine;

import java.util.ArrayList;

public interface PTSInterface {

    public boolean startAlgorithm();

    public ArrayList<String> getOutputList();
    
    public void initEngine(ArrayList<String> iNames,
			ArrayList<ArrayList<String>> iValues,int nWay);

}
