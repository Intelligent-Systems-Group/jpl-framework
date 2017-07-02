package de.upb.cs.is.jpl.api.math.linearalgebra;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.upb.cs.is.jpl.api.util.datastructure.Pair;
import no.uib.cipr.matrix.Matrix;
import no.uib.cipr.matrix.MatrixEntry;
import no.uib.cipr.matrix.QR;
import no.uib.cipr.matrix.Vector;
import no.uib.cipr.matrix.sparse.LinkedSparseMatrix;
import no.uib.cipr.matrix.sparse.SparseVector;


/**
 * The SparseDoubleMatrix encapsulates a MTJ {@link LinkedSparseMatrix}
 * 
 * @author Sebastian Osterbrink
 *
 */
public class SparseDoubleMatrix extends AMatrix {

   @SuppressWarnings("unused")
   private static final Logger logger = LoggerFactory.getLogger(SparseDoubleMatrix.class);


   /**
    * Creates an empty matrix with the given dimensions.
    * 
    * @param rowNumbers the number of rows
    * @param columnNumbers the number of columns
    */
   public SparseDoubleMatrix(int rowNumbers, int columnNumbers) {
      internalMatrix = new LinkedSparseMatrix(rowNumbers, columnNumbers);
   }


   /**
    * Creates a new {@link SparseDoubleMatrix} by repeating a single vector.
    * 
    * @param vector the vector which is repeated
    * @param numberOfRepeats the number of repeats
    * @param useVectorAsRowVector a boolean value which indicates whether the vector should be used
    *           as a row or as a column vector
    */
   public SparseDoubleMatrix(double[] vector, int numberOfRepeats, boolean useVectorAsRowVector) {
      int numberOfColumns = useVectorAsRowVector ? vector.length : numberOfRepeats;
      int numberOfRows = !useVectorAsRowVector ? vector.length : numberOfRepeats;
      internalMatrix = new LinkedSparseMatrix(numberOfRows, numberOfColumns);
      for (int i = 0; i < numberOfColumns; i++) {
         for (int j = 0; j < numberOfRows; j++) {
            double value = useVectorAsRowVector ? vector[i] : vector[j];
            internalMatrix.set(j, i, value);
         }
      }
   }


   /**
    * Creates a new {@link SparseDoubleMatrix} from a 2-dimensional double array.
    * 
    * @param rawCopy2D the 2-D array which contains all the values
    */
   public SparseDoubleMatrix(double[][] rawCopy2D) {
      internalMatrix = new LinkedSparseMatrix(rawCopy2D.length, rawCopy2D[0].length);
      for (int i = 0; i < rawCopy2D.length; i++) {
         for (int j = 0; j < rawCopy2D[i].length; j++) {
            internalMatrix.set(i, j, rawCopy2D[i][j]);
         }
      }
   }


   /**
    * Creates a new {@link SparseDoubleMatrix} from a MTJ {@link Matrix} object.
    * 
    * @param matrix the MTJ {@link Matrix}
    */
   public SparseDoubleMatrix(Matrix matrix) {
      internalMatrix = matrix;
   }


   @Override
   public void transponse() {
      internalMatrix = internalMatrix.transpose();
   }


   @Override
   public IVector solve(IVector b) {
      Vector bInternal = b.toSparseVector().internalVector;
      SparseVector result = (SparseVector) bInternal.copy();
      internalMatrix.solve(bInternal, result);
      return new SparseDoubleVector(result);
   }


   @Override
   public void multiply(IMatrix multiplier) {
      Matrix result = new LinkedSparseMatrix(internalMatrix);
      internalMatrix = internalMatrix.mult(multiplier.asSparseMatrix().internalMatrix, result);
   }


   @Override
   public Pair<IMatrix, IMatrix> getQRDecomposition() {
      Matrix input = internalMatrix.copy();

      // If the Matrix has the wrong dimensions
      if (input.numRows() < input.numColumns()) {
         input = input.transpose(new LinkedSparseMatrix(input.numColumns(), input.numRows()));
      }

      QR rs = QR.factorize(input);
      return Pair.of(new SparseDoubleMatrix(rs.getQ()), new SparseDoubleMatrix(rs.getR()));
   }


   @Override
   public IMatrix createCopy() {
      return new SparseDoubleMatrix(internalMatrix.copy());
   }


   @Override
   public SparseDoubleMatrix asSparseMatrix() {
      return this;
   }


   @Override
   public DenseDoubleMatrix asDenseMatrix() {
      DenseDoubleMatrix result = new DenseDoubleMatrix(internalMatrix.numRows(), internalMatrix.numColumns());
      for (MatrixEntry matrixEntry : internalMatrix) {
         result.setValue(matrixEntry.row(), matrixEntry.column(), matrixEntry.get());
      }
      return result;
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
      return new SparseDoubleVector(column);
   }


   @Override
   public IVector getRowVector(int i) {
      double[] row = new double[internalMatrix.numColumns()];
      for (int j = 0; j < row.length; j++) {
         row[j] = internalMatrix.get(i, j);
      }
      return new SparseDoubleVector(row);
   }

}
