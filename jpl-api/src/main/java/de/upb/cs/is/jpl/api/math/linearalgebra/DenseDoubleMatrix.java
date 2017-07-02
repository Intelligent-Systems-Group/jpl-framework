package de.upb.cs.is.jpl.api.math.linearalgebra;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.upb.cs.is.jpl.api.exception.math.MatrixDecompositionException;
import de.upb.cs.is.jpl.api.util.datastructure.Pair;
import no.uib.cipr.matrix.DenseMatrix;
import no.uib.cipr.matrix.DenseVector;
import no.uib.cipr.matrix.Matrix;
import no.uib.cipr.matrix.MatrixEntry;
import no.uib.cipr.matrix.QR;
import no.uib.cipr.matrix.Vector;
import no.uib.cipr.matrix.sparse.IterativeSolver;
import no.uib.cipr.matrix.sparse.IterativeSolverNotConvergedException;
import no.uib.cipr.matrix.sparse.QMR;


/**
 * This is dense matrix of double values. Apart from many matrix algebra operations implementations,
 * it also solves the set of linear equations represented as a double matrix.
 * 
 * @author Sebastian Osterbrink
 *
 */
public class DenseDoubleMatrix extends AMatrix {

   private static final String SOLVER_DID_NOT_TERMINATE = "Solver did not terminate. Last result returned. The reason is %s.";

   private static final Logger logger = LoggerFactory.getLogger(DenseDoubleMatrix.class);


   /**
    * Creates a new matrix out of a double array.
    *
    * @param matrix values of the matrix
    */
   public DenseDoubleMatrix(double[][] matrix) {
      internalMatrix = new no.uib.cipr.matrix.DenseMatrix(matrix);
   }


   /**
    * Creates a matrix by using the vector n times as a column vector.
    *
    * @param vector the vector which is used
    * @param n times the vector is repeated
    */
   public DenseDoubleMatrix(double[] vector, int n) {
      DenseVector repeatedVector = new DenseVector(vector);
      Vector[] vectorArray = new Vector[n];
      for (int i = 0; i < vectorArray.length; i++) {
         vectorArray[i] = repeatedVector;
      }
      internalMatrix = new no.uib.cipr.matrix.DenseMatrix(vectorArray);
   }


   /**
    * Creates a matrix with zero entries with a specific number of rows and columns.
    *
    * @param numberOfRows the number of rows
    * @param numberOfCols the number of columns
    */
   public DenseDoubleMatrix(int numberOfRows, int numberOfCols) {
      internalMatrix = new no.uib.cipr.matrix.DenseMatrix(numberOfRows, numberOfCols);
   }


   /**
    * Creates a matrix out of a Matrix from MTJ.
    *
    * @param m Matrix from MTJ
    */
   public DenseDoubleMatrix(Matrix m) {
      internalMatrix = m;
   }


   @Override
   public void transponse() {
      if (internalMatrix.numColumns() == internalMatrix.numRows()) {
         internalMatrix = internalMatrix.transpose();
      } else {
         internalMatrix = internalMatrix.transpose(new DenseMatrix(internalMatrix.numColumns(), internalMatrix.numRows()));
      }

   }


   @Override
   public IVector solve(IVector b) {
      DenseVector bInternal = new DenseVector(b.asArray());
      DenseVector result = bInternal.copy().zero();

      IterativeSolver solver = new QMR(result);
      try {
         solver.solve(internalMatrix, bInternal, result);
      } catch (IterativeSolverNotConvergedException exception) {
         logger.info(String.format(SOLVER_DID_NOT_TERMINATE, exception));
      }

      return new DenseDoubleVector(result);
   }


   @Override
   public void multiply(IMatrix multipland) {

      Matrix result = new DenseMatrix(getNumberOfRows(), multipland.getNumberOfColumns());
      internalMatrix = internalMatrix.mult(multipland.asDenseMatrix().internalMatrix, result);

   }


   @Override
   public Pair<IMatrix, IMatrix> getQRDecomposition() throws MatrixDecompositionException {
      Matrix input = internalMatrix.copy();

      // If the Matrix has the wrong dimensions
      if (input.numRows() < input.numColumns()) {
         input = input.transpose(new DenseDoubleMatrix(input.numColumns(), input.numRows()).internalMatrix);
      }

      QR rs = QR.factorize(input);
      return Pair.of(new DenseDoubleMatrix(rs.getQ()), new DenseDoubleMatrix(rs.getR()));
   }


   @Override
   public IMatrix createCopy() {
      return new DenseDoubleMatrix(internalMatrix.copy());
   }


   @Override
   public SparseDoubleMatrix asSparseMatrix() {
      SparseDoubleMatrix result = new SparseDoubleMatrix(internalMatrix.numRows(), internalMatrix.numColumns());
      for (MatrixEntry matrixEntry : internalMatrix) {
         if (Double.compare(matrixEntry.get(), 0.0) != 0)
            result.setValue(matrixEntry.row(), matrixEntry.column(), matrixEntry.get());
      }
      return result;
   }


   @Override
   public DenseDoubleMatrix asDenseMatrix() {
      return this;
   }


   @Override
   public String toString() {
      return internalMatrix.toString();
   }


   @Override
   public IVector getColumnVector(int i) {
      double[] column = new double[internalMatrix.numRows()];
      for (int j = 0; j < column.length; j++) {
         column[j] = internalMatrix.get(j, i);
      }
      return new DenseDoubleVector(column);
   }


   @Override
   public IVector getRowVector(int i) {
      double[] row = new double[internalMatrix.numColumns()];
      for (int j = 0; j < row.length; j++) {
         row[j] = internalMatrix.get(i, j);
      }
      return new DenseDoubleVector(row);
   }


}
