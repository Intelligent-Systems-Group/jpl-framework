package de.upb.cs.is.jpl.api.algorithm.learningalgorithm.multilabelclassification;


import java.util.Arrays;
import java.util.List;

import de.upb.cs.is.jpl.api.algorithm.learningalgorithm.ALearningModel;
import de.upb.cs.is.jpl.api.algorithm.learningalgorithm.ILearningModel;
import de.upb.cs.is.jpl.api.dataset.IDataset;
import de.upb.cs.is.jpl.api.dataset.IInstance;
import de.upb.cs.is.jpl.api.dataset.multilabelclassification.MultilabelClassificationDataset;
import de.upb.cs.is.jpl.api.dataset.multilabelclassification.MultilabelClassificationInstance;
import de.upb.cs.is.jpl.api.exception.algorithm.PredictionFailedException;
import de.upb.cs.is.jpl.api.math.linearalgebra.SparseDoubleVector;


/**
 * This abstract class is designed to be the base class for all learning models used for multilabel
 * classification learning algorithms, which predict a {@link SparseDoubleVector}. It offers
 * convenience methods such as checking if an instance has the correct number of features.
 * 
 * @author Alexander Hetzer
 *
 */
public abstract class AMultilabelClassificationLearningModel extends ALearningModel<SparseDoubleVector> {

   private static final String ERROR_WRONG_NUMBER_OF_FEATURES = "Instance %s has wrong amount of features. Expected '%d', but found '%d'.";

   protected List<ILearningModel<Double>> baseLearningModels;
   protected int numberOfContextFeaturesTrainedOn;


   /**
    * Creates a new {@link AMultilabelClassificationLearningModel} from the given list of base
    * learning models and the expected number of context features.
    * 
    * @param baseLearningModels the base learners to use in this model
    * @param numberOfContextFeaturesTrainedOn the number of context features this model was trained
    *           on
    */
   public AMultilabelClassificationLearningModel(List<ILearningModel<Double>> baseLearningModels, int numberOfContextFeaturesTrainedOn) {
      this.baseLearningModels = baseLearningModels;
      this.numberOfContextFeaturesTrainedOn = numberOfContextFeaturesTrainedOn;
   }


   /**
    * Checks if the given feature vector has the given amount of features. If this is not the case a
    * {@link PredictionFailedException} is thrown. Assumes that the argument is not {@code null}.
    * 
    * @param featureVector the feature vector to check
    * @throws PredictionFailedException if the given feature vector does not have the given amount
    *            of features
    */
   protected void assertFeatureVectorHasExpectedNumberOfFeatures(double[] featureVector) throws PredictionFailedException {
      if (!isFeatureVectorSizedCorrectly(featureVector)) {
         throw new PredictionFailedException(String.format(ERROR_WRONG_NUMBER_OF_FEATURES, Arrays.toString(featureVector),
               numberOfContextFeaturesTrainedOn, featureVector.length));
      }
   }


   /**
    * Asserts that the given instance is a {@link MultilabelClassificationInstance}. If this is not
    * the case a {@link PredictionFailedException} is thrown.
    * 
    * @param instance the instance to check
    * @throws PredictionFailedException if the given instance is not a
    *            {@link MultilabelClassificationInstance}
    */
   protected void assertInstanceIsMultilabelClassificationInstance(IInstance<?, ?, ?> instance) throws PredictionFailedException {
      assertInstanceHasCorrectType(instance, MultilabelClassificationInstance.class);
   }


   /**
    * Asserts that the given dataset is a {@link MultilabelClassificationDataset}. If this is not
    * the case a {@link PredictionFailedException} is thrown.
    * 
    * @param dataset the dataset to check
    * @throws PredictionFailedException if the given dataset is not a
    *            {@link MultilabelClassificationDataset}
    */
   protected void assertDatasetIsMultilabelClassificationDataset(IDataset<?, ?, ?> dataset) throws PredictionFailedException {
      assertDatasetHasCorrectType(dataset, MultilabelClassificationDataset.class);
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
      return featureVector.length == numberOfContextFeaturesTrainedOn;
   }


   @Override
   public boolean isDatasetCompatible(IDataset<?, ?, ?> dataset) {
      if (dataset != null && dataset instanceof MultilabelClassificationDataset) {
         MultilabelClassificationDataset multilabelClassificationDataset = (MultilabelClassificationDataset) dataset;
         return multilabelClassificationDataset.getNumberOfFeatures() == numberOfContextFeaturesTrainedOn;
      }
      return false;
   }


   @Override
   public boolean isInstanceCompatible(IInstance<?, ?, ?> instance) {
      if (instance != null && instance instanceof MultilabelClassificationInstance) {
         MultilabelClassificationInstance multilabelClassificationInstance = (MultilabelClassificationInstance) instance;
         return isFeatureVectorSizedCorrectly(multilabelClassificationInstance.getContextFeatureVector());
      }
      return false;
   }


   @Override
   public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result + ((baseLearningModels == null) ? 0 : baseLearningModels.hashCode());
      result = prime * result + numberOfContextFeaturesTrainedOn;
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
      AMultilabelClassificationLearningModel other = (AMultilabelClassificationLearningModel) obj;
      if (baseLearningModels == null) {
         if (other.baseLearningModels != null)
            return false;
      } else if (!baseLearningModels.equals(other.baseLearningModels))
         return false;
      if (numberOfContextFeaturesTrainedOn != other.numberOfContextFeaturesTrainedOn)
         return false;
      return true;
   }


}
