package de.upb.cs.is.jpl.api.algorithm.baselearner.regression.logistic;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import de.upb.cs.is.jpl.api.algorithm.baselearner.ABaseLearningModel;
import de.upb.cs.is.jpl.api.algorithm.baselearner.dataset.BaselearnerDataset;
import de.upb.cs.is.jpl.api.algorithm.baselearner.dataset.BaselearnerInstance;
import de.upb.cs.is.jpl.api.dataset.IDataset;
import de.upb.cs.is.jpl.api.dataset.IInstance;
import de.upb.cs.is.jpl.api.exception.algorithm.PredictionFailedException;
import de.upb.cs.is.jpl.api.math.linearalgebra.DenseDoubleVector;
import de.upb.cs.is.jpl.api.math.linearalgebra.IVector;
import de.upb.cs.is.jpl.api.math.util.FunctionUtils;


/**
 * This class represents the final learning model produced by {@link LogisticRegression}, storing a
 * weight vector and a bias. It can be used to make predictions on a single instance or on a
 * complete dataset.
 * 
 * @author Alexander Hetzer
 *
 */
public class LogisticRegressionLearningModel extends ABaseLearningModel<Double> {

   private IVector weightVector;
   private double bias;


   /**
    * Creates an instance of a logistic regression learning model initialized from the given weight
    * vector. Note that it assumes that the first (0th) coordinate of the vector contains the bias
    * whereas the rest of the vector is the actual weight vector.
    * 
    * @param weightVector the weight vector to initialize this learning model from
    */
   public LogisticRegressionLearningModel(IVector weightVector) {
      super(weightVector.length());
      initializeModelFromWeightVector(weightVector);
      this.numberOfFeaturesTrainedOn = this.weightVector.length();
   }


   /**
    * Initializes the bias and the local weight vector from the given weight vector. The 0th
    * coordinate of the given weight vector is interpreted as the bias, whereas the rest of it is
    * interpreted as the real weight vector.
    * 
    * @param weightVector the optimized weight vector produced by logistic regression with the 0th
    *           component being the bias
    */
   private void initializeModelFromWeightVector(IVector weightVector) {
      this.weightVector = new DenseDoubleVector(weightVector.length() - 1);
      for (int i = 1; i < weightVector.length(); i++) {
         this.weightVector.setValue(i - 1, weightVector.getValue(i));
      }
      bias = weightVector.getValue(0);
   }


   @Override
   public List<Double> predict(IDataset<?, ?, ?> dataset) throws PredictionFailedException {
      checkDatasetForCompatibility(dataset);
      BaselearnerDataset baseLearnerDataset = (BaselearnerDataset) dataset;
      List<Double> results = new ArrayList<>();
      for (double[] feature : baseLearnerDataset.getFeatureVectors()) {
         results.add(predictOnFeatureArray(feature));
      }
      return results;
   }


   @Override
   public Double predict(IInstance<?, ?, ?> instance) throws PredictionFailedException {
      checkInstanceForCompatibility(instance);
      BaselearnerInstance baseLearnerInstance = (BaselearnerInstance) instance;
      double[] features = baseLearnerInstance.getContextFeatureVector();
      return predictOnFeatureArray(features);
   }


   /**
    * Returns the prediction of this model on the given feature array.
    * 
    * @param features the features as array
    * @return the prediction result of this model on the given feature array
    */
   private double predictOnFeatureArray(double[] features) {
      IVector featureVector = new DenseDoubleVector(features);
      return FunctionUtils.logisticFunction(weightVector.dotProduct(featureVector) + bias);
   }


   @Override
   public IVector getWeightVector() {
      return weightVector;
   }


   @Override
   public double getBias() {
      return bias;
   }


   @Override
   public boolean isDatasetCompatible(IDataset<?, ?, ?> dataset) {
      if (dataset instanceof BaselearnerDataset) {
         BaselearnerDataset baselearnerDataset = (BaselearnerDataset) dataset;
         return areFeatureVectorsSizedCorrectly(Arrays.asList(baselearnerDataset.getFeatureVectors()));
      }
      return false;
   }


   @Override
   public boolean isInstanceCompatible(IInstance<?, ?, ?> instance) {
      if (instance instanceof BaselearnerInstance) {
         BaselearnerInstance baselearnerInstance = (BaselearnerInstance) instance;
         return isFeatureVectorSizedCorrectly(baselearnerInstance.getContextFeatureVector());
      }
      return false;
   }


   /**
    * Returns {@code true} if the given features vector has as many entries as this model has
    * training features.
    * 
    * @param featureVector the feature vector to check
    * @return {@code true} if the given features vector has as many entries as this model has
    *         training features, otherwise {@code false}
    */
   private boolean isFeatureVectorSizedCorrectly(double[] featureVector) {
      return featureVector.length == numberOfFeaturesTrainedOn;
   }


   /**
    * Checks if all of the features in the given list have the size required by this learning model.
    * 
    * @param featureVectors the list of feature vectors to check
    * @return {@code true} if the given features vectors have as many entries as this model has
    *         training features, otherwise {@code false}
    */
   private boolean areFeatureVectorsSizedCorrectly(List<double[]> featureVectors) {
      if (featureVectors != null) {
         for (double[] featureVector : featureVectors) {
            if (!isFeatureVectorSizedCorrectly(featureVector)) {
               return false;
            }
         }
         return true;
      }
      return false;
   }


   @Override
   public int hashCode() {
      final int prime = 31;
      int result = super.hashCode();
      long temp;
      temp = Double.doubleToLongBits(bias);
      result = prime * result + (int) (temp ^ (temp >>> 32));
      result = prime * result + ((weightVector == null) ? 0 : weightVector.hashCode());
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
      LogisticRegressionLearningModel other = (LogisticRegressionLearningModel) obj;
      if (Double.doubleToLongBits(bias) != Double.doubleToLongBits(other.bias))
         return false;
      if (weightVector == null) {
         if (other.weightVector != null)
            return false;
      } else if (!weightVector.equals(other.weightVector))
         return false;
      return true;
   }


}
