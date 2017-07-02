package de.upb.cs.is.jpl.api.algorithm.baselearner.dataset;


import java.util.Arrays;

import de.upb.cs.is.jpl.api.dataset.ADataset;
import de.upb.cs.is.jpl.api.dataset.IDataset;
import de.upb.cs.is.jpl.api.dataset.IInstance;
import de.upb.cs.is.jpl.api.util.datastructure.NullType;


/**
 * A base learner dataset which can only work with real valued feature vectors. The feature vectors
 * are stored as a two-dimensional double array, whereas the correct results are stored in a simple
 * double array.
 * 
 * This dataset has a fixed size, which is predetermined from the very beginning. It assumes that it
 * is filled completely, before being used.
 * 
 * @author Alexander Hetzer
 *
 */
public class BaselearnerDataset extends ADataset<double[], NullType, Double> {

   private static final String ERROR_DATASET_IS_FULL = "Cannot add another instance to this dataset, as the dataset is full.";

   private int nextFreeFeatureVectorSlot;

   private int numberOfInstances;
   private int numberOfFeatures;

   private double[][] featureVectors;
   private double[] correctResults;
   private double[] instanceWeights;


   /**
    * Creates a new {@link BaselearnerDataset} and initializes the fields correctly.
    * 
    * @param numberOfInstances the maximum number of instances for this dataset
    * @param numberOfFeatures the number of features this dataset supports
    */
   public BaselearnerDataset(int numberOfInstances, int numberOfFeatures) {
      this.numberOfInstances = numberOfInstances;
      this.numberOfFeatures = numberOfFeatures;

      init();
   }


   @Override
   public BaselearnerInstance getInstance(int instanceNumber) {
      assertInstanceIsInBounds(instanceNumber);
      return new BaselearnerInstance(featureVectors[instanceNumber], correctResults[instanceNumber], getWeightForInstance(instanceNumber));
   }


   @Override
   public IDataset<double[], NullType, Double> getPartOfDataset(int from, int to) {
      assertCorrectDatasetPartSelection(from, to);
      return createSubset(from, to);
   }


   /**
    * Creates a subset of this dataset with instances in the interval {@code [from,to[}. The lower
    * bound is inclusive whereas the upper bound is exclusive.
    * 
    * @param from the index of the first instance in the subset
    * @param to the index of the fist instance which is not in the subset
    * 
    * @return the according subset of this dataset
    */
   private BaselearnerDataset createSubset(int from, int to) {
      BaselearnerDataset newDataset = new BaselearnerDataset(to - from, numberOfFeatures);
      for (int i = from; i < to; i++) {
         newDataset.addFeatureVectorWithResult(featureVectors[i], correctResults[i]);
      }
      return newDataset;
   }


   @Override
   public int getNumberOfInstances() {
      return numberOfInstances;
   }


   @Override
   protected void init() {
      featureVectors = new double[numberOfInstances][numberOfFeatures];
      correctResults = new double[numberOfInstances];
      instanceWeights = new double[numberOfInstances];
      for (int i = 0; i < correctResults.length; i++) {
         correctResults[i] = Double.MAX_VALUE;
         instanceWeights[i] = 1.0;
      }

      nextFreeFeatureVectorSlot = 0;
   }


   @Override
   public void addInstance(IInstance<double[], NullType, Double> instance) {
      assertInstanceHasCorrectType(instance, BaselearnerInstance.class);
      BaselearnerInstance baseLearnerInstance = (BaselearnerInstance) instance;
      addFeatureVectorWithResult(baseLearnerInstance.getContextFeatureVector(), baseLearnerInstance.getRating());
   }


   /**
    * Returns the number of features which are supported by this dataset.
    * 
    * @return the number of features supported by this dataset
    */
   public int getNumberOfFeatures() {
      return numberOfFeatures;
   }


   /**
    * Checks if this dataset is full.
    * 
    * @throws IllegalArgumentException if the dataset is full
    */
   private void assertDatasetIsNotFull() {
      if (nextFreeFeatureVectorSlot == numberOfInstances) {
         throw new IllegalArgumentException(ERROR_DATASET_IS_FULL);
      }
   }


   /**
    * Adds the given feature vector and result to this dataset.
    * 
    * @param featureVector the feature vector to be added
    * @param result the result to be added
    * @param weight the weight associated with the instance
    */
   public void addFeatureVectorWithResultAndWeight(double[] featureVector, double result, double weight) {
      assertDatasetIsNotFull();
      double[] featureVectorCopy = Arrays.copyOf(featureVector, featureVector.length);
      featureVectors[nextFreeFeatureVectorSlot] = featureVectorCopy;
      correctResults[nextFreeFeatureVectorSlot] = result;
      instanceWeights[nextFreeFeatureVectorSlot] = weight;
      nextFreeFeatureVectorSlot++;
   }


   /**
    * Adds the given feature vector and result to this dataset.
    * 
    * @param featureVector the feature vector to be added
    * @param result the result to be added
    */
   public void addFeatureVectorWithResult(double[] featureVector, double result) {
      assertDatasetIsNotFull();
      double[] featureVectorCopy = Arrays.copyOf(featureVector, featureVector.length);
      featureVectors[nextFreeFeatureVectorSlot] = featureVectorCopy;
      correctResults[nextFreeFeatureVectorSlot] = result;
      nextFreeFeatureVectorSlot++;
   }


   /**
    * Adds the given feature vector without any result to this dataset.
    * 
    * @param featureVector the feature vector to be added
    */
   public void addFeatureVectorWithoutResult(double[] featureVector) {
      assertDatasetIsNotFull();
      double[] featureVectorCopy = Arrays.copyOf(featureVector, featureVector.length);
      featureVectors[nextFreeFeatureVectorSlot] = featureVectorCopy;
      nextFreeFeatureVectorSlot++;
   }


   /**
    * Adds the given feature vector without any result to this dataset.
    * 
    * @param featureVector the feature vector to be added
    * @param weight the weight associated with the instance
    */
   public void addFeatureVectorWithWeightWithoutResult(double[] featureVector, double weight) {
      assertDatasetIsNotFull();
      double[] featureVectorCopy = Arrays.copyOf(featureVector, featureVector.length);
      featureVectors[nextFreeFeatureVectorSlot] = featureVectorCopy;
      instanceWeights[nextFreeFeatureVectorSlot] = weight;
      nextFreeFeatureVectorSlot++;
   }


   /**
    * Returns the two dimensional feature vector array.
    * 
    * @return the feature vectors of this dataset
    */
   public double[][] getFeatureVectors() {
      return featureVectors;
   }


   /**
    * Returns the correct results for the feature vectors in this dataset.
    * 
    * @return the correct results of this dataset
    */
   public double[] getCorrectResults() {
      return correctResults;
   }


   /**
    * Returns the instance weights of this dataset. Note that these might not be complete or even
    * {@code null}.
    * 
    * @return the instance weights of this dataset
    */
   public double[] getInstanceWeights() {
      return instanceWeights;
   }


   /**
    * Returns the weight associated with the instance with the given index.
    * 
    * @param indexOfInstance the index of the instance to get the weight for
    * @return the weight associated with the instance with the given index
    */
   public double getWeightForInstance(int indexOfInstance) {
      if (instanceWeights != null && instanceWeights.length == getNumberOfInstances()) {
         return instanceWeights[indexOfInstance];
      }
      return 1.0;
   }


   @Override
   public int hashCode() {
      final int prime = 31;
      int result = super.hashCode();
      result = prime * result + Arrays.hashCode(correctResults);
      result = prime * result + Arrays.deepHashCode(featureVectors);
      result = prime * result + Arrays.hashCode(instanceWeights);
      result = prime * result + nextFreeFeatureVectorSlot;
      result = prime * result + numberOfFeatures;
      result = prime * result + numberOfInstances;
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
      BaselearnerDataset other = (BaselearnerDataset) obj;
      if (!Arrays.equals(correctResults, other.correctResults))
         return false;
      if (!Arrays.deepEquals(featureVectors, other.featureVectors))
         return false;
      if (!Arrays.equals(instanceWeights, other.instanceWeights))
         return false;
      if (nextFreeFeatureVectorSlot != other.nextFreeFeatureVectorSlot)
         return false;
      if (numberOfFeatures != other.numberOfFeatures)
         return false;
      if (numberOfInstances != other.numberOfInstances)
         return false;
      return true;
   }

}
