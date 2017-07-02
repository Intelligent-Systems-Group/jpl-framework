package de.upb.cs.is.jpl.api.math.linearalgebra;


import no.uib.cipr.matrix.Matrix;
import no.uib.cipr.matrix.MatrixEntry;
import no.uib.cipr.matrix.QRP;


/**
 * Abstract matrix class, implementing several common methods for different matrix implementations.
 * All matrix implementations should subclass this class.
 *
 * @author Sebastian Gottschalk
 *
 */
public abstract class AMatrix implements IMatrix {

   protected Matrix internalMatrix;


   @Override
   public double getValue(int row, int col) {
      return internalMatrix.get(row, col);
   }


   @Override
   public void setValue(int row, int col, double value) {
      internalMatrix.set(row, col, value);
   }


   @Override
   public int getNumberOfColumns() {
      return internalMatrix.numColumns();
   }


   @Override
   public int getNumberOfRows() {
      return internalMatrix.numRows();
   }


   @Override
   public int getRank() {
      QRP result = QRP.factorize(internalMatrix);
      return result.getRank();
   }


   @Override
   public double getFrobeniusNorm() {
      double norm = 0;
      for (MatrixEntry matrixEntry : internalMatrix) {
         norm += matrixEntry.get() * matrixEntry.get();
      }
      return Math.sqrt(norm);
   }


   @Override
   public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result + ((internalMatrix == null) ? 0 : internalMatrix.hashCode());
      return result;
   }


   @Override
   public boolean equals(Object obj) {
      if (this == obj)
         return true;
      if (obj == null)
         return false;
      if (!(obj instanceof AMatrix))
         return false;
      AMatrix other = (AMatrix) obj;
      if (internalMatrix == null) {
         if (other.internalMatrix != null)
            return false;
      } else if (!internalMatrix.equals(other.internalMatrix))
         return false;
      return true;
   }

}
