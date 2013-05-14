package pairwisetesting.engine.am.oaprovider.hadamard;

import java.util.Arrays;

import com.google.common.base.Preconditions;

/**
 * This class encapsulates the Mathematics concept Matrix.
 * <p>
 * <strong>Note that this implementation is not synchronized.</strong>
 */
public class Matrix {

	/**
	 * The int 2D array to store the elements of the matrix.
	 */
	private int[][] elements;

	/**
	 * Constructs a matrix with the specified number of rows and columns.
	 * 
	 * @param numOfRows
	 *            the specified number of rows
	 * @param numOfColumns
	 *            the specified number of columns
	 * @throws IllegalArgumentException
	 *             if {@code numOfRows} or {@code numOfColumns} < 1
	 */
	public Matrix(int numOfRows, int numOfColumns) {
		Preconditions.checkArgument(numOfRows >= 1,
				"The number of rows should >= 1.");
		Preconditions.checkArgument(numOfColumns >= 1,
				"The number of columns should >= 1.");
		this.elements = new int[numOfRows][numOfColumns];
	}

	/**
	 * Sets value to the specified element.
	 * 
	 * @param row
	 *            the row number
	 * @param column
	 *            the column number
	 * @param value
	 *            the value assigned to the element
	 * @throws IndexOutOfBoundsException
	 *             if {@code row} or {@code column} is out of the bounds of the
	 *             matrix
	 */
	public void setElement(int row, int column, int value) {
		this.elements[row - 1][column - 1] = value;
	}

	/**
	 * Returns the value of the specified element.
	 * 
	 * @param row
	 *            the row number
	 * @param column
	 *            the column number
	 * @return the value of the specified element
	 * @throws IndexOutOfBoundsException
	 *             if {@code row} or {@code column} is out of the bounds of the
	 *             matrix
	 */
	public int getElement(int row, int column) {
		return this.elements[row - 1][column - 1];
	}

	/**
	 * Returns the number of rows.
	 * 
	 * @return the number of rows
	 */
	public int getNumOfRows() {
		return this.elements.length;
	}

	/**
	 * Returns the number of columns.
	 * 
	 * @return the number of columns
	 */
	public int getNumOfColumns() {
		return this.elements[0].length;
	}

	/**
	 * Returns a new matrix as the result of direct product with another matrix.
	 * 
	 * @param other another matrix
	 * @return a new matrix as the result of direct product with another matrix
	 * @throws NullPointerException
	 *             if {@code other} is null
	 */
	public Matrix directProduct(Matrix other) {
		Preconditions.checkNotNull(other, "another matrix");
		Matrix res = new Matrix(this.getNumOfRows() * other.getNumOfRows(),
				this.getNumOfColumns() * other.getNumOfColumns());
		for (int rowA = 1; rowA <= this.getNumOfRows(); rowA++) {
			for (int colA = 1; colA <= this.getNumOfColumns(); colA++) {
				for (int rowB = 1; rowB <= other.getNumOfRows(); rowB++) {
					for (int colB = 1; colB <= other.getNumOfColumns(); colB++) {
						res.setElement(
								(rowA - 1) * other.getNumOfRows() + rowB,
								(colA - 1) * other.getNumOfColumns() + colB,
								this.getElement(rowA, colA)
										* other.getElement(rowB, colB));
					}
				}
			}
		}
		return res;
	}

	/**
	 * Generates a int 2D array starting from the specified start column.
	 * 
	 * @param startColumn
	 *            the start column
	 * @return a int 2D array starting from the specified start column
	 * @throws IndexOutOfBoundsException
	 *             if {@code startColumn} is out of the bounds of the matrix
	 */
	public int[][] to2DArray(int startColumn) {
		int[][] res = new int[this.getNumOfRows()][this.getNumOfColumns()
				- startColumn + 1];
		for (int column = startColumn; 
			column <= this.getNumOfColumns(); column++) {
			for (int row = 1; row <= this.getNumOfRows(); row++) {
				res[row - 1][column - startColumn] = this.getElement(row,
						column);
			}
		}
		return res;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(elements);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Matrix other = (Matrix) obj;
		if (!Arrays.deepEquals(elements, other.elements))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return Arrays.deepToString(this.elements);
	}

}
