package pairwisetesting.test.edu;

public abstract class AbstractEducationManager implements IEducationManager {
	
	private double sum;
	private double average;

	public double getSum() {
		return sum;
	}

	public void setSum(double sum) {
		this.sum = sum;
	}

	public double getAverage() {
		return average;
	}

	public void setAverage(double average) {
		this.average = average;
	}
	
	public void doStatistics(Student s) {
		
	}

}
