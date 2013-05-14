package pairwisetesting.test.edu;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;

public interface ISorter {
	public double[] sort(double[] numbers);

	public double[] sort1(HashSet<Double> numbers);
	public double[] sort2(ArrayList<Double> numbers);
	public double[] sort3(LinkedList<Double> numbers);
	public double[] sort4(HashMap<String, Double> numbers);
}
