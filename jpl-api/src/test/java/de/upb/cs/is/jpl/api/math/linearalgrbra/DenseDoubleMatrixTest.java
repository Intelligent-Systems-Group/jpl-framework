package de.upb.cs.is.jpl.api.math.linearalgrbra;


import static org.junit.Assert.assertEquals;

import org.junit.BeforeClass;
import org.junit.Test;

import de.upb.cs.is.jpl.api.configuration.logging.LoggingConfiguration;
import de.upb.cs.is.jpl.api.exception.math.MatrixDecompositionException;
import de.upb.cs.is.jpl.api.math.linearalgebra.DenseDoubleMatrix;
import de.upb.cs.is.jpl.api.math.linearalgebra.IMatrix;
import de.upb.cs.is.jpl.api.math.linearalgebra.IVector;
import de.upb.cs.is.jpl.api.util.TestUtils;
import de.upb.cs.is.jpl.api.util.datastructure.Pair;


/**
 * This class tests every method which can be called on the {@link DenseDoubleMatrix}.
 *
 * @author Sebastian Gottschalk
 */
public class DenseDoubleMatrixTest {

   /**
    * Initializes the logging framework with the default logging configuration before running this
    * suite.
    */
   @BeforeClass
   public static void setUp() {
      LoggingConfiguration.setupLoggingConfiguration();
   }


   /**
    * Check if a new matrix can be created from an array.
    */
   @Test
   public void testCreateMatrixFromArray() {

      double[][] data = { { 1.0, 2.0 }, { 3.0, 4.0 } };
      IMatrix matrix = new DenseDoubleMatrix(data);
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
      IMatrix vectorMatrix = new DenseDoubleMatrix(vector, 3);
      assertEquals(1.0, vectorMatrix.getValue(0, 0), TestUtils.DOUBLE_DELTA);
      assertEquals(2.0, vectorMatrix.getValue(1, 1), TestUtils.DOUBLE_DELTA);
   }


   /**
    * Checks if the getter and setter methods are working correctly.
    */
   @Test
   public void testSetterAndGetterMethod() {
      double[][] data = { { 1.0, 2.0 }, { 3.0, 4.0 } };
      IMatrix matrix = new DenseDoubleMatrix(data);
      matrix.setValue(0, 0, 42.0);
      assertEquals(42.0, matrix.getValue(0, 0), TestUtils.DOUBLE_DELTA);
      // assertResultVectorIs4(vector);
   }


   /**
    * Test if the methods to get the number of rows/columns and rank work correctly.
    */
   @Test
   public void testColumnsRowsAndRankMethod() {
      double[][] data = { { 1.0, 1.0 }, { 1.0, 1.0 }, { 1.0, 1.0 } };
      IMatrix matrix = new DenseDoubleMatrix(data);
      assertEquals(3L, matrix.getNumberOfRows());
      assertEquals(2L, matrix.getNumberOfColumns());
      assertEquals(1L, matrix.getRank());
   }


   /**
    * Test if a wrong index results in the expected exception.
    */
   @Test(expected = IndexOutOfBoundsException.class)
   public void testGetWrongIndexMethod() {
      double[][] data = { { 1.0, 1.0 }, { 1.0, 1.0 }, { 1.0, 1.0 } };
      IMatrix matrix = new DenseDoubleMatrix(data);

      matrix.getValue(4, 5);

   }


   /**
    * Test if the function for QRDecomposition works correctly.
    * 
    * @throws MatrixDecompositionException if matrix is not decomposable
    */
   @Test
   public void testQRDecompositionMethod() throws MatrixDecompositionException {
      double[][] data = { { 3.0, -6.0 }, { 4.0, -8.0 }, { 0.0, 1.0 } };

      IMatrix matrix = new DenseDoubleMatrix(data);

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
      assertEquals(2.0, qMatrix.getNumberOfColumns(), TestUtils.DOUBLE_DELTA);
      assertEquals(3.0, qMatrix.getNumberOfRows(), TestUtils.DOUBLE_DELTA);


      // R
      assertEquals(-5.0, rMatrix.getValue(0, 0), TestUtils.DOUBLE_DELTA);
      assertEquals(10.0, rMatrix.getValue(0, 1), TestUtils.DOUBLE_DELTA);
      assertEquals(0.0, rMatrix.getValue(1, 0), TestUtils.DOUBLE_DELTA);
      assertEquals(-1.0, rMatrix.getValue(1, 1), TestUtils.DOUBLE_DELTA);


      // Calculate Q^T * b
      qMatrix.transponse();
      double[][] multiplicant = { { -1.0 }, { 7.0 }, { 2.0 } };
      qMatrix.multiply(new DenseDoubleMatrix(multiplicant));

      // Solve
      IVector solutionMatrix = rMatrix.solve(qMatrix.getColumnVector(0));
      assertEquals(5.0, solutionMatrix.getValue(0), TestUtils.DOUBLE_DELTA);
      assertEquals(2.0, solutionMatrix.getValue(1), TestUtils.DOUBLE_DELTA);

   }
}
