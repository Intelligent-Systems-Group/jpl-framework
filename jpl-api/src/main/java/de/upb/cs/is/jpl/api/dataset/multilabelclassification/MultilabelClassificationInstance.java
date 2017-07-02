package de.upb.cs.is.jpl.api.dataset.multilabelclassification;


import java.util.Arrays;

import de.upb.cs.is.jpl.api.algorithm.baselearner.dataset.BaselearnerInstance;
import de.upb.cs.is.jpl.api.dataset.AInstance;
import de.upb.cs.is.jpl.api.math.linearalgebra.SparseDoubleVector;
import de.upb.cs.is.jpl.api.util.StringUtils;
import de.upb.cs.is.jpl.api.util.datastructure.NullType;


/**
 * This class is a simple multilabel classification dataset instance, which is used in combination
 * with the {@link MultilabelClassificationDataset}. It offers a feature vector containing double
 * values and a field for setting the number of features of the instance.
 * 
 * @author Alexander Hetzer
 *
 */
public class MultilabelClassificationInstance extends AInstance<double[], NullType, SparseDoubleVector> {

   private static final String IDENTIFIER_CORRECT_RESULT = "correct_result";
   private static final String IDENTIFIER_FEATURE_VECTOR = "feature_vector";
   private static final String IDENTIFIER_MULTILABEL_CLASSIFICATION_INSTANCE = "multilabel_classification_instance";

   private double[] featureVector;

   private int numberOfFeatures;


   /**
    * Creates an empty {@link MultilabelClassificationInstance} with the given number of features.
    * 
    * @param numberOfFeatures the number of features this instance has
    */
   public MultilabelClassificationInstance(int numberOfFeatures) {
      this.numberOfFeatures = numberOfFeatures;
      this.featureVector = new double[numberOfFeatures];
   }


   /**
    * Creates a {@link MultilabelClassificationInstance} with the given feature vector and the given
    * correct result.
    * 
    * @param featureVector the feature vector of the instance to be created
    * @param correctLabelVector the correct label vector of the instance to be created
    */
   public MultilabelClassificationInstance(double[] featureVector, double[] correctLabelVector) {
      this.featureVector = featureVector;
      this.numberOfFeatures = featureVector.length;
      this.rating = new SparseDoubleVector(correctLabelVector);
   }


   /**
    * Creates a {@link BaselearnerInstance} with the same features as this instance but an empty
    * result.
    * 
    * @return a {@link BaselearnerInstance} with the same features as this instance but an empty
    *         result
    */
   public BaselearnerInstance toBaselearnerInstance() {
      return new BaselearnerInstance(featureVector);
   }


   /**
    * Returns the feature vector of this instance.
    * 
    * @return the feature vector of this instance
    */
   public double[] getFeatureVector() {
      return featureVector;
   }


   /**
    * Returns the number of features of this instance.
    * 
    * @return the number of features of this instance
    */
   public int getNumberOfFeatures() {
      return numberOfFeatures;
   }


   @Override
   public double[] getContextFeatureVector() {
      return featureVector;
   }


   @Override
   public void setContextFeatureVector(double[] contextFeatureVector) {
      featureVector = contextFeatureVector;
   }


   @Override
   public NullType getItemFeatureVectors() {
      throw new UnsupportedOperationException();
   }


   @Override
   public void setItemFeatureVector(NullType itemFeatureVectors) {
      throw new UnsupportedOperationException();
   }


   @Override
   public int hashCode() {
      final int prime = 31;
      int result = super.hashCode();
      result = prime * result + Arrays.hashCode(featureVector);
      result = prime * result + numberOfFeatures;
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
      MultilabelClassificationInstance other = (MultilabelClassificationInstance) obj;
      if (!Arrays.equals(featureVector, other.featureVector))
         return false;
      if (numberOfFeatures != other.numberOfFeatures)
         return false;
      return true;
   }


   @Override
   public String toString() {
      return IDENTIFIER_MULTILABEL_CLASSIFICATION_INSTANCE + StringUtils.ROUND_BRACKET_OPEN + IDENTIFIER_FEATURE_VECTOR + StringUtils.COLON
            + Arrays.toString(featureVector) + StringUtils.COMMA_WITH_SINGLE_WHITESPACE_BEHIND + IDENTIFIER_CORRECT_RESULT
            + StringUtils.COLON + rating.toString() + StringUtils.ROUND_BRACKET_CLOSE;
   }


}
