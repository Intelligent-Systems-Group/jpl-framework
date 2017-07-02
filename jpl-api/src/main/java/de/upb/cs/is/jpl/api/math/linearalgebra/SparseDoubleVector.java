package de.upb.cs.is.jpl.api.math.linearalgebra;


import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import de.upb.cs.is.jpl.api.math.RandomGenerator;
import de.upb.cs.is.jpl.api.util.CollectionsUtils;
import no.uib.cipr.matrix.DenseVector;
import no.uib.cipr.matrix.Vector.Norm;
import no.uib.cipr.matrix.sparse.SparseVector;


/**
 * Sparse vector implementation wrapping the MTJ implementation of a sparse vector.
 * 
 * @author Sebastian Osterbrink
 *
 */
public class SparseDoubleVector extends AVector {

   protected SparseVector internalVector;

   private boolean isChanged = true;


   /**
    * Creates a new SparseDoubleVector which contains the given values.
    * 
    * @param indexes an array which includes all indices for which there exists a value
    * @param values an array which contains all values
    * @param dimension the total dimension of the vector
    */
   public SparseDoubleVector(int[] indexes, double[] values, int dimension) {
      internalVector = new SparseVector(dimension, indexes, values);
      setIsChanged();
   }


   /**
    * Creates a new SparseDoubleVector which contains only zero values.
    * 
    * @param dimension the dimension of the vector
    */
   public SparseDoubleVector(int dimension) {
      internalVector = new SparseVector(dimension);
   }


   /**
    * Creates a new SparseDoubleVector which contains the given values.
    * 
    * @param asArray the vector in the form on a double array
    */
   public SparseDoubleVector(double[] asArray) {
      List<Integer> indicesWithNonZeroEntry = new ArrayList<>();
      List<Double> nonZeroEntries = new ArrayList<>();
      for (int i = 0; i < asArray.length; i++) {
         if (Double.compare(asArray[i], 0.0) != 0) {
            indicesWithNonZeroEntry.add(i);
            nonZeroEntries.add(asArray[i]);
         }
      }

      internalVector = new SparseVector(asArray.length, CollectionsUtils.convertIntegerListToArray(indicesWithNonZeroEntry),
            CollectionsUtils.convertDoubleListToArray(nonZeroEntries));
      setIsChanged();
   }


   /**
    * Creates a new SparseDoubleVector from a MTJ SparseVector class object.
    * 
    * @param mtjVector an MTJ Vector class object
    */
   public SparseDoubleVector(SparseVector mtjVector) {
      internalVector = mtjVector;
      setIsChanged();
   }


   @Override
   public void addVector(double[] vectorAsArray) {
      setIsChanged();
      addVector(new SparseDoubleVector(vectorAsArray));
   }


   @Override
   public void subtractVector(double[] vectorAsArray) {
      setIsChanged();
      internalVector = (SparseVector) internalVector.add(-1, new SparseDoubleVector(vectorAsArray).internalVector);
   }


   @Override
   public void multiplyByVectorPairwise(double[] vectorAsArray) {
      setIsChanged();
      SparseVector vector = internalVector;
      int[] indexes = vector.getIndex();
      for (int i = 0; i < indexes.length; i++) {
         vector.set(indexes[i], vector.get(indexes[i]) * vectorAsArray[indexes[i]]);
      }
      internalVector = vector;
   }


   /**
    * Changes the status of this vector to changed.
    */
   private void setIsChanged() {
      isChanged = true;
   }


   @Override
   public void divideByVectorPairwise(double[] vectorAsArray) {
      setIsChanged();
      SparseVector vector = internalVector;
      int[] indexes = vector.getIndex();
      for (int i = 0; i < indexes.length; i++) {
         vector.set(indexes[i], vector.get(indexes[i]) / vectorAsArray[indexes[i]]);
      }
      internalVector = vector;
   }


   @Override
   public double dotProduct(double[] vectorAsArray) {
      return internalVector.dot(new DenseVector(vectorAsArray));
   }


   @Override
   public int length() {
      return internalVector.size();
   }


   @Override
   public double getValue(int index) {
      return internalVector.get(index);
   }


   @Override
   public void setValue(int index, double value) {
      setIsChanged();
      internalVector.set(index, value);
   }


   @Override
   public void addVector(IVector vector) {
      setIsChanged();
      internalVector = (SparseVector) internalVector.add(vector.toSparseVector().internalVector);
   }


   @Override
   public void subtractVector(IVector vector) {
      setIsChanged();
      internalVector = (SparseVector) internalVector.add(-1, vector.toSparseVector().internalVector);
   }


   @Override
   public void multiplyByVectorPairwise(IVector secondVector) {
      setIsChanged();
      SparseVector sparseVector = internalVector;
      int[] indexes = sparseVector.getIndex();
      for (int i = 0; i < indexes.length; i++) {
         sparseVector.set(indexes[i], sparseVector.get(indexes[i]) * secondVector.getValue(indexes[i]));
      }
      internalVector = sparseVector;
   }


   @Override
   public void multiplyByConstant(double constant) {
      setIsChanged();
      internalVector = internalVector.scale(constant);
   }


   @Override
   public void divideByVectorPairwise(IVector secondVector) {
      setIsChanged();
      SparseVector sparseVector = internalVector;
      int[] indexes = sparseVector.getIndex();
      for (int i = 0; i < indexes.length; i++) {
         sparseVector.set(indexes[i], sparseVector.get(indexes[i]) / secondVector.getValue(indexes[i]));
      }
      internalVector = sparseVector;
   }


   @Override
   public void divideByConstant(double constant) {
      setIsChanged();
      internalVector = internalVector.scale(1 / constant);
   }


   @Override
   public double dotProduct(IVector vector) {
      return internalVector.dot(vector.toSparseVector().internalVector);
   }


   @Override
   public boolean isSparse() {
      return true;
   }


   @Override
   public double[] asArray() {
      double[] result = new double[internalVector.size()];
      for (int i = 0; i < result.length; i++) {
         result[i] = internalVector.get(i);
      }
      return result;
   }


   @Override
   public DenseDoubleVector toDenseVector() {
      return new DenseDoubleVector(asArray());
   }


   @Override
   public SparseDoubleVector toSparseVector() {
      return this;
   }


   @Override
   public IVector duplicate() {
      return new SparseDoubleVector(asArray());
   }


   @Override
   public void normalize() {
      setIsChanged();
      internalVector = internalVector.scale(internalVector.norm(Norm.Two));
   }


   @Override
   public void addConstant(double constant) {
      setIsChanged();
      double[] contantAsVector = new double[internalVector.size()];
      for (int i = 0; i < contantAsVector.length; i++) {
         contantAsVector[i] = constant;
      }
      addVector(contantAsVector);
   }


   @Override
   public void subtractConstant(double constant) {
      setIsChanged();
      addConstant(-1 * constant);
   }


   @Override
   public void fillRandomly() {
      setIsChanged();
      Random random = RandomGenerator.getRNG();
      int numberToAdd = random.nextInt(internalVector.size());
      List<Integer> unfilledIndexes = new ArrayList<>();
      for (int i = 0; i < internalVector.size(); i++) {
         unfilledIndexes.add(i);
      }
      for (int numberOfAddedValues = 0; numberOfAddedValues < numberToAdd; numberOfAddedValues++) {
         int randomIndex = random.nextInt(unfilledIndexes.size());
         int toBeFilledIndex = unfilledIndexes.get(randomIndex);
         double fillValue = random.nextDouble();
         internalVector.set(toBeFilledIndex, fillValue);
         unfilledIndexes.remove(0);
      }
   }


   /**
    * Returns an array containing the non-zero indices of this sparse vector.
    * 
    * @return an integer array containing the non-zero indices of this sparse vector
    */
   public int[] getNonZeroIndices() {
      if (isChanged) {
         List<Integer> indicesWithNonZeroEntry = new ArrayList<>(length());
         List<Double> nonZeroEntries = new ArrayList<>(length());
         for (int i = 0; i < length(); i++) {
            double value = internalVector.get(i);
            if (Double.compare(value, 0.0) != 0) {
               indicesWithNonZeroEntry.add(i);
               nonZeroEntries.add(value);
            }
         }
         // do we need to recopy?
         if (indicesWithNonZeroEntry.size() != internalVector.getIndex().length) {
            this.internalVector = new SparseVector(indicesWithNonZeroEntry.size(),
                  CollectionsUtils.convertIntegerListToArray(indicesWithNonZeroEntry),
                  CollectionsUtils.convertDoubleListToArray(nonZeroEntries));
         }
         setUnchanged();
      }
      return internalVector.getIndex();
   }


   /**
    * Changes the status of this vector to unchanged.
    */
   private void setUnchanged() {
      isChanged = false;
   }


   @Override
   public int hashCode() {
      final int prime = 31;
      int result = super.hashCode();
      result = prime * result + ((internalVector == null) ? 0 : internalVector.hashCode());
      result = prime * result + (isChanged ? 1231 : 1237);
      return result;
   }


   @Override
   public boolean equals(Object obj) {
      if (this == obj)
         return true;
      if (!super.equals(obj))
         return false;
      if (getClass() != obj.getClass())
         return false;
      SparseDoubleVector other = (SparseDoubleVector) obj;
      // we cannot compare the internal vector
      if (isChanged != other.isChanged)
         return false;
      return true;
   }


}
