package de.upb.cs.is.jpl.api.math.linearalgrbra;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import de.upb.cs.is.jpl.api.exception.math.MatrixDecompositionException;
import de.upb.cs.is.jpl.api.math.linearalgebra.IMatrix;
import de.upb.cs.is.jpl.api.math.linearalgebra.SparseDoubleMatrix;
import de.upb.cs.is.jpl.api.util.TestUtils;
import de.upb.cs.is.jpl.api.util.datastructure.Pair;


/**
 * This class tests every method which can be called on the {@link SparseDoubleMatrix}.
 *
 * @author Sebastian Osterbrink
 */
public class SparseDoubleMatrixTest {

   private static final String ERROR_EXPECTED_EXCEPTION_WAS_THROWN_AFTER_GET_VALUE_WITH_AN_INDEX_OUTSIDE_THE_MATRIX = "Expected exception was thrown, after getValue() with an index outside the matrix";
   private static final String ERROR_NO_ECEPTION_THROWN_AFTER_GET_VALUE_WITH_AN_INDEX_OUTSIDE_THE_MATRIX = "No eception thrown, after getValue() with an index outside the matrix";


   /**
    * Check if a new matrix can be created from an array.
    */
   @Test
   public void testCreateMatrixFromArray() {
      double[][] data = { { 1.0, 2.0 }, { 3.0, 4.0 } };
      IMatrix matrix = new SparseDoubleMatrix(data);
      assertEquals(1.0, matrix.getValue(0, 0), TestUtils.DOUBLE_DELTA);
      assertEquals(2.0, matrix.getValue(0, 1), TestUtils.DOUBLE_DELTA);
      assertEquals(3.0, matrix.getValue(1, 0), TestUtils.DOUBLE_DELTA);
      assertEquals(4.0, matrix.getValue(1, 1), TestUtils.DOUBLE_DELTA);

      IMatrix copiedMatrix = matrix.createCopy();
      assertEquals(1.0, copiedMatrix.getValue(0, 0), TestUtils.DOUBLE_DELTA);
      assertEquals(2.0, copiedMatrix.getValue(0, 1), TestUtils.DOUBLE_DELTA);
      assertEquals(3.0, copiedMatrix.getValue(1, 0), TestUtils.DOUBLE_DELTA);
      assertEquals(4.0, copiedMatrix.getValue(1, 1), TestUtils.DOUBLE_DELTA);

      double[] vector = { 1.0, 2.0 };
      IMatrix vectorMatrix = new SparseDoubleMatrix(vector, 3, true);
      assertEquals(1.0, vectorMatrix.getValue(2, 0), TestUtils.DOUBLE_DELTA);
      assertEquals(2.0, vectorMatrix.getValue(2, 1), TestUtils.DOUBLE_DELTA);
      // assertResultVectorIs4(vector);
   }


   /**
    * Test if the methods to get the number of rows/columns and rank work correctly.
    */
   @Test
   public void testSetterMethod() {
      double[][] data = { { 1.0, 2.0 }, { 3.0, 4.0 } };
      IMatrix matrix = new SparseDoubleMatrix(data);
      matrix.setValue(0, 0, 42.0);
      assertEquals(42.0, matrix.getValue(0, 0), TestUtils.DOUBLE_DELTA);
   }


   /**
    * Test if the methods to get the number of rows/columns and rank work correctly.
    */
   @Test
   public void testColumnsRowsAndRankMethod() {
      double[][] data = { { 1.0, 1.0 }, { 1.0, 1.0 }, { 1.0, 1.0 } };
      IMatrix matrix = new SparseDoubleMatrix(data);
      assertEquals(3L, matrix.getNumberOfRows());
      assertEquals(2L, matrix.getNumberOfColumns());
      assertEquals(1L, matrix.getRank());
   }


   /**
    * Ensure that the correct exception is thrown if a wrong index is requested.
    */
   @Test
   public void testGetWrongIndexMethod() {
      double[][] data = { { 1.0, 1.0 }, { 1.0, 1.0 }, { 1.0, 1.0 } };
      IMatrix matrix = new SparseDoubleMatrix(data);
      try {
         matrix.getValue(4, 5);
         assertTrue(ERROR_NO_ECEPTION_THROWN_AFTER_GET_VALUE_WITH_AN_INDEX_OUTSIDE_THE_MATRIX, false);
      } catch (ArrayIndexOutOfBoundsException e) {
         assertTrue(ERROR_EXPECTED_EXCEPTION_WAS_THROWN_AFTER_GET_VALUE_WITH_AN_INDEX_OUTSIDE_THE_MATRIX, true);
      }
   }


   /**
    * Test if the function for QRDecomposition works correctly.
    * 
    * @throws MatrixDecompositionException if matrix is not decomposable
    */
   @Test
   public void testQRDecompositionMethod() throws MatrixDecompositionException {
      double[][] data = { { 3.0, -6.0 }, { 4.0, -8.0 }, { 0.0, 1.0 } };

      IMatrix matrix = new SparseDoubleMatrix(data);

      Pair<IMatrix, IMatrix> qrMatrix = matrix.getQRDecomposition();

      IMatrix qMatrix = qrMatrix.getFirst().createCopy();
      IMatrix rMatrix = qrMatrix.getSecond().createCopy();

      // Q
      assertEquals(-0.6, qMatrix.getValue(0, 0), TestUtils.DOUBLE_DELTA);
      assertEquals(0.0, qMatrix.getValue(0, 1), TestUtils.DOUBLE_DELTA);
      assertEquals(-0.8, qMatrix.getValue(1, 0), TestUtils.DOUBLE_DELTA);
      assertEquals(0.0, qMatrix.getValue(1, 1), TestUtils.DOUBLE_DELTA);
      assertEquals(0.0, qMatrix.getValue(2, 0), TestUtils.DOUBLE_DELTA);
      assertEquals(-1.0, qMatrix.getValue(2, 1), TestUtils.DOUBLE_DELTA);

      // R
      assertEquals(-5.0, rMatrix.getValue(0, 0), TestUtils.DOUBLE_DELTA);
      assertEquals(10.0, rMatrix.getValue(0, 1), TestUtils.DOUBLE_DELTA);
      assertEquals(0.0, rMatrix.getValue(1, 0), TestUtils.DOUBLE_DELTA);
      assertEquals(-1.0, rMatrix.getValue(1, 1), TestUtils.DOUBLE_DELTA);

      // dimensions
      assertEquals(2.0, qMatrix.getNumberOfColumns(), TestUtils.DOUBLE_DELTA);
      assertEquals(3.0, qMatrix.getNumberOfRows(), TestUtils.DOUBLE_DELTA);

      IMatrix multiplyMatrix = qMatrix.createCopy();
      multiplyMatrix.multiply(rMatrix);
      assertEquals(3.0, multiplyMatrix.getValue(0, 0), TestUtils.DOUBLE_DELTA);
      assertEquals(-6.0, multiplyMatrix.getValue(0, 1), TestUtils.DOUBLE_DELTA);
      assertEquals(4.0, multiplyMatrix.getValue(1, 0), TestUtils.DOUBLE_DELTA);
      assertEquals(-8.0, multiplyMatrix.getValue(1, 1), TestUtils.DOUBLE_DELTA);
      assertEquals(0.0, multiplyMatrix.getValue(2, 0), TestUtils.DOUBLE_DELTA);
      assertEquals(1.0, multiplyMatrix.getValue(2, 1), TestUtils.DOUBLE_DELTA);
   }
}
