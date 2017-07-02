package de.upb.cs.is.jpl.api.math.linearalgebra;


import de.upb.cs.is.jpl.api.exception.math.MatrixDecompositionException;
import de.upb.cs.is.jpl.api.util.datastructure.Pair;


/**
 * This interface defines all methods, which are required by different matrix implementations.
 *
 * @author Sebastian Gottschalk
 * @author Sebastian Osterbrink
 */
public interface IMatrix {

   /**
    * Returns a single entry out of the matrix.
    *
    * @param row the row number of the entry
    * @param col the column number of the entry
    * @return the value of the entry at row and column
    */
   double getValue(int row, int col);


   /**
    * Replaces the value of a single entry with the provided value.
    *
    * @param row the row number of the entry
    * @param col the column number of the entry
    * @param value the entry value which should be placed on row and column
    */
   void setValue(int row, int col, double value);


   /**
    * Returns the number of columns of the matrix.
    *
    * @return the number of columns
    */
   int getNumberOfColumns();


   /**
    * Returns the number of rows of the matrix.
    *
    * @return the number of rows
    */
   int getNumberOfRows();


   /**
    * Returns the rank of the matrix.
    *
    * @return the rank of the matrix
    */
   int getRank();


   /**
    * Transpose the matrix.
    */
   void transponse();


   /**
    * Solves a equation in form of A * x = b.
    * 
    * @param b the vector which should be the result of the equation
    * @return the vector which solves the equation
    */
   IVector solve(IVector b);


   /**
    * Multiplies two matrixes A * B.
    * 
    * @param matrixB the current matrix A is multiplied with this parameter
    */
   void multiply(IMatrix matrixB);


   /**
    * Decompose this matrix to Q and R.
    * 
    * @return the pair of matrices Q and R
    * @throws MatrixDecompositionException throws exception if matrix is not decomposable
    */
   Pair<IMatrix, IMatrix> getQRDecomposition() throws MatrixDecompositionException;


   /**
    * Creates a copy of the current matrix.
    *
    * @return the copy of matrix
    */
   IMatrix createCopy();


   /**
    * Returns the current matrix as a {@link SparseDoubleMatrix}.
    * 
    * @return the current matrix as a {@link SparseDoubleMatrix}
    */
   SparseDoubleMatrix asSparseMatrix();


   /**
    * Returns the current matrix as a {@link DenseDoubleMatrix}.
    * 
    * @return the current matrix as a {@link DenseDoubleMatrix}
    */
   DenseDoubleMatrix asDenseMatrix();


   /**
    * Returns the i-th column vector of this matrix.
    * 
    * @param i the number of the column
    * @return the vector in this column
    */
   IVector getColumnVector(int i);


   /**
    * Returns the i-th row vector of this matrix.
    * 
    * @param i the number of the row
    * @return the vector in this row
    */
   IVector getRowVector(int i);


   /**
    * Calculate the Frobenius Norm for this matrix.
    * 
    * @return the calculated norm
    */
   double getFrobeniusNorm();


}
