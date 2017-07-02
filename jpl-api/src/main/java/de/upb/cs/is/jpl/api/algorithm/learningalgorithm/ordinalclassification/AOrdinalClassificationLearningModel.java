package de.upb.cs.is.jpl.api.algorithm.learningalgorithm.ordinalclassification;


import java.util.List;

import de.upb.cs.is.jpl.api.algorithm.learningalgorithm.ALearningModel;
import de.upb.cs.is.jpl.api.dataset.IDataset;
import de.upb.cs.is.jpl.api.dataset.IInstance;
import de.upb.cs.is.jpl.api.dataset.ordinalclassification.OrdinalClassificationDataset;
import de.upb.cs.is.jpl.api.dataset.ordinalclassification.OrdinalClassificationInstance;
import de.upb.cs.is.jpl.api.exception.algorithm.PredictionFailedException;
import de.upb.cs.is.jpl.api.util.CollectionsUtils;


/**
 * This abstract learning model is used for learning models of ordinal classification.
 * 
 * @author Tanja Tornede
 *
 */
public abstract class AOrdinalClassificationLearningModel extends ALearningModel<Double> {

   protected static final String ERROR_GIVEN_INSTANCE_NOT_APPLICABLE_TO_THIS_MODEL = "The given instance is not applicable to this model.";
   protected static final String ERROR_GIVEN_DATASET_NOT_APPLICABLE_TO_THIS_MODEL = "The given dataset is not applicable to this model.";

   protected int numberOfFeaturesTrainedOn;
   protected List<Double> predictionClasses;


   /**
    * Creates a new {@link AOrdinalClassificationLearningModel} from the given expected number of
    * context features.
    * 
    * @param numberOfContextFeaturesTrainedOn the number of context features this model was trained
    *           on
    */
   public AOrdinalClassificationLearningModel(int numberOfContextFeaturesTrainedOn) {
      this.numberOfFeaturesTrainedOn = numberOfContextFeaturesTrainedOn;
   }


   /**
    * Initializes the prediction classes.
    * 
    * @param predictionClasses the prediction classes used for prediction
    */
   protected void initializePredictionClasses(List<Double> predictionClasses) {
      this.predictionClasses = CollectionsUtils.getDeepCopyOf(predictionClasses);
   }


   /**
    * Checks weather the given {@link OrdinalClassificationInstance} has a rating that is one of the
    * previously defined ones.
    * 
    * @param ordinalInstance the instance to check
    * 
    * @throws PredictionFailedException if the rating of the given instance is not one of the
    *            previously defined ones
    */
   protected void assertInstanceHasValidRating(OrdinalClassificationInstance ordinalInstance) throws PredictionFailedException {
      if (!predictionClasses.isEmpty() && !predictionClasses.contains(ordinalInstance.getRating())) {
         throw new PredictionFailedException(ERROR_GIVEN_INSTANCE_NOT_APPLICABLE_TO_THIS_MODEL);
      }
   }


   /**
    * Checks weather the given {@link OrdinalClassificationDataset} has ratings which were
    * previously defined.
    * 
    * @param ordinalDataset the dataset to check
    * 
    * @throws PredictionFailedException if the ratings of the given dataset are not of the
    *            previously defined ones
    */
   protected void assertDatasetHasValidRatings(OrdinalClassificationDataset ordinalDataset) throws PredictionFailedException {
      if (!predictionClasses.containsAll(ordinalDataset.getRatings())) {
         throw new PredictionFailedException(ERROR_GIVEN_DATASET_NOT_APPLICABLE_TO_THIS_MODEL);
      }
   }


   /**
    * Converts the given dataset to an {@link OrdinalClassificationDataset}, if it is compatible,
    * and returns it.
    * 
    * @param dataset the dataset to convert
    * 
    * @return the given dataset as {@link OrdinalClassificationDataset}
    * 
    * @throws PredictionFailedException if the dataset is not an
    *            {@link OrdinalClassificationDataset}, or if it is not applicable for this algorithm
    */
   protected OrdinalClassificationDataset getOrdinalClassificationDataset(final IDataset<?, ?, ?> dataset)
         throws PredictionFailedException {
      if (!isDatasetCompatible(dataset)) {
         throw new PredictionFailedException(ERROR_GIVEN_DATASET_NOT_APPLICABLE_TO_THIS_MODEL);
      }
      OrdinalClassificationDataset ordinalDataset = (OrdinalClassificationDataset) dataset;
      assertDatasetHasValidRatings(ordinalDataset);
      return ordinalDataset;
   }


   @Override
   public boolean isDatasetCompatible(IDataset<?, ?, ?> dataset) {
      if (dataset instanceof OrdinalClassificationDataset) {
         OrdinalClassificationDataset baselearnerDataset = (OrdinalClassificationDataset) dataset;
         return isFeatureVectorSizedCorrectly(baselearnerDataset.getNumberOfFeatures());
      }
      return false;
   }


   /**
    * Converts the given instance to an {@link OrdinalClassificationInstance}, if it is compatible,
    * and returns it.
    * 
    * @param instance the instance to convert
    * 
    * @return the given instance as {@link OrdinalClassificationInstance}
    * 
    * @throws PredictionFailedException if the instance is not an
    *            {@link OrdinalClassificationInstance}, or if it is not applicable for this
    *            algorithm
    */
   protected OrdinalClassificationInstance getOrdinalClassificationInstance(final IInstance<?, ?, ?> instance)
         throws PredictionFailedException {
      if (!isInstanceCompatible(instance)) {
         throw new PredictionFailedException(ERROR_GIVEN_INSTANCE_NOT_APPLICABLE_TO_THIS_MODEL);
      }
      OrdinalClassificationInstance ordinalInstance = (OrdinalClassificationInstance) instance;
      assertInstanceHasValidRating(ordinalInstance);
      return ordinalInstance;
   }


   @Override
   public boolean isInstanceCompatible(IInstance<?, ?, ?> instance) {
      if (instance instanceof OrdinalClassificationInstance) {
         OrdinalClassificationInstance ordinalInstance = (OrdinalClassificationInstance) instance;
         return isFeatureVectorSizedCorrectly(ordinalInstance.getContextFeatureVector().length);
      }
      return false;
   }


   /**
    * Returns {@code true} if the given features vector size has the same size as this model has
    * training features.
    * 
    * @param featureVectorSize the size of the feature vector to check
    * 
    * @return {@code true} if the given features vector has as many entries as this model has
    *         training features, otherwise {@code false}
    */
   private boolean isFeatureVectorSizedCorrectly(int featureVectorSize) {
      return featureVectorSize == numberOfFeaturesTrainedOn;
   }


   @Override
   public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result + numberOfFeaturesTrainedOn;
      result = prime * result + ((predictionClasses == null) ? 0 : predictionClasses.hashCode());
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
      AOrdinalClassificationLearningModel other = (AOrdinalClassificationLearningModel) obj;
      if (numberOfFeaturesTrainedOn != other.numberOfFeaturesTrainedOn)
         return false;
      if (predictionClasses == null) {
         if (other.predictionClasses != null)
            return false;
      } else if (!predictionClasses.equals(other.predictionClasses))
         return false;
      return true;
   }

}
