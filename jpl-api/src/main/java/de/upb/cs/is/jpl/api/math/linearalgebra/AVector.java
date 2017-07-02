package de.upb.cs.is.jpl.api.math.linearalgebra;


import de.upb.cs.is.jpl.api.util.StringUtils;


/**
 * An abstract vector class, implementing several common methods for different vector
 * implementations. All vector implementations should subclass this class.
 * 
 * @author Alexander Hetzer
 * 
 */
public abstract class AVector implements IVector {

   @Override
   public void squareRoot() {
      for (int i = 0; i < length(); i++) {
         setValue(i, Math.sqrt(getValue(i)));
      }

   }


   @Override
   public IVector squareRootToCopy() {
      IVector copy = this.duplicate();
      for (int i = 0; i < length(); i++) {
         copy.setValue(i, Math.sqrt(getValue(i)));
      }
      return copy;
   }


   @Override
   public void incrementValueAt(int index, double amount) {
      setValue(index, amount + getValue(index));
   }


   @Override
   public double sum() {
      double sum = 0;
      for (int i = 0; i < length(); i++) {
         sum += getValue(i);
      }
      return sum;
   }


   @Override
   public double mean() {
      return sum() / length();
   }


   @Override
   public double standardDeviation() {
      double mean = mean();
      double std = 0.0;
      for (int i = 0; i < length(); i++) {
         std += Math.pow(mean - getValue(i), 2);
      }
      std = std / length();
      return Math.sqrt(std);
   }


   @Override
   public String toString() {
      StringBuilder sb = new StringBuilder(StringUtils.ROUND_BRACKET_OPEN);
      sb.append(getValue(0));
      for (int i = 1; i < length(); i++) {
         sb.append(StringUtils.COMMA).append(getValue(i));
      }
      sb.append(StringUtils.ROUND_BRACKET_CLOSE);
      return sb.toString();
   }


   @Override
   public void zeroAllDimensions() {
      for (int i = 0; i < length(); i++) {
         setValue(i, 0);
      }
   }


   @Override
   public double euclideanNorm() {
      return Math.sqrt(this.dotProduct(this));
   }


   @Override
   public IVector addVectorToCopy(double[] vectorAsArray) {
      IVector vector = duplicate();
      vector.addVector(vectorAsArray);
      return vector;
   }


   @Override
   public IVector subtractVectorFromCopy(double[] vectorAsArray) {
      IVector vector = duplicate();
      vector.subtractVector(vectorAsArray);
      return vector;
   }


   @Override
   public IVector multiplyByVectorPairwiseToCopy(double[] vectorAsArray) {
      IVector vector = duplicate();
      vector.multiplyByVectorPairwise(vectorAsArray);
      return vector;
   }


   @Override
   public IVector divideByVectorPairwiseToCopy(double[] vectorAsArray) {
      IVector vector = duplicate();
      vector.divideByVectorPairwise(vectorAsArray);
      return vector;
   }


   @Override
   public IVector addConstantToCopy(double constant) {
      IVector vector = duplicate();
      vector.addConstant(constant);
      return vector;
   }


   @Override
   public IVector addVectorToCopy(IVector vector) {
      IVector vectorCopy = duplicate();
      vectorCopy.addVector(vector);
      return vectorCopy;
   }


   @Override
   public IVector subtractConstantFromCopy(double constant) {
      IVector vectorCopy = duplicate();
      vectorCopy.subtractConstant(constant);
      return vectorCopy;
   }


   @Override
   public IVector subtractVectorFromCopy(IVector vector) {
      IVector vectorCopy = duplicate();
      vectorCopy.subtractVector(vector);
      return vectorCopy;
   }


   @Override
   public IVector multiplyByVectorPairwiseToCopy(IVector vector) {
      IVector vectorCopy = duplicate();
      vectorCopy.multiplyByVectorPairwise(vector);
      return vectorCopy;
   }


   @Override
   public IVector multiplyByConstantToCopy(double constant) {
      IVector vectorCopy = duplicate();
      vectorCopy.multiplyByConstant(constant);
      return vectorCopy;
   }


   @Override
   public IVector divideByVectorPairwiseToCopy(IVector vector) {
      IVector vectorCopy = duplicate();
      vectorCopy.divideByVectorPairwise(vector);
      return vectorCopy;
   }


   @Override
   public IVector divideByConstantToCopy(double constant) {
      IVector vectorCopy = duplicate();
      vectorCopy.divideByConstant(constant);
      return vectorCopy;
   }


   @Override
   public Double average() {
      double basisAverage = 0;
      for (int i = 0; i < this.length(); i++) {
         basisAverage += this.getValue(i);
      }
      basisAverage = basisAverage / this.length();
      return basisAverage;
   }


   @Override
   public boolean equals(Object obj) {
      if (obj instanceof AVector) {
         AVector vector = (AVector) obj;
         for (int i = 0; i < vector.length(); i++) {
            if (Double.compare(vector.getValue(i), this.getValue(i)) != 0) {
               return false;
            }
         }

         return true;
      }
      return super.equals(obj);
   }


   @Override
   public int hashCode() {
      int hashCode = 1;
      for (int i = 0; i < length(); i++) {
         hashCode = 31 * hashCode + (new Double(getValue(i)).hashCode());
      }
      return hashCode;
   }


}
