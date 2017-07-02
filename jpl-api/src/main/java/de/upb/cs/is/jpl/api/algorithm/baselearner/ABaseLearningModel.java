package de.upb.cs.is.jpl.api.algorithm.baselearner;


import de.upb.cs.is.jpl.api.algorithm.baselearner.dataset.BaselearnerDataset;
import de.upb.cs.is.jpl.api.algorithm.baselearner.dataset.BaselearnerInstance;
import de.upb.cs.is.jpl.api.algorithm.learningalgorithm.ALearningModel;
import de.upb.cs.is.jpl.api.dataset.IDataset;
import de.upb.cs.is.jpl.api.dataset.IInstance;
import de.upb.cs.is.jpl.api.exception.UnsupportedOperationException;
import de.upb.cs.is.jpl.api.exception.algorithm.PredictionFailedException;
import de.upb.cs.is.jpl.api.math.linearalgebra.IVector;


/**
 * This class is an abstract base learning model implementation, which assumes to work with the
 * {@link BaselearnerDataset} and {@link BaselearnerInstance}. It offers convenience method for
 * checking the type of a dataset and an instance to be compatible with the
 * {@link BaselearnerDataset} or the {@link BaselearnerInstance}. Every learning model produced by a
 * {@link IBaselearnerAlgorithm} should subclass this class.
 * 
 * @author Alexander Hetzer
 *
 * @param <RATING> the type of the prediction on a single instance done by this learning model
 */
public abstract class ABaseLearningModel<RATING> extends ALearningModel<RATING> {

   private static final String ERROR_GIVEN_DATASET_NOT_COMPATIBLE_FOR_THIS_LEARNING_MODEL = "The given dataset is not compatible for this learning model.";
   private static final String ERROR_GIVEN_INSTANCE_NOT_COMPATIBLE_FOR_THIS_LEARNING_MODEL = "The given instance is not compatible for this learning model.";

   protected int numberOfFeaturesTrainedOn;


   /**
    * Creates a new {@link ABaseLearningModel} with the given given
    * {@code numberOfFeaturesTrainedOn}.
    * 
    * @param numberOfFeaturesTrainedOn the number of trained features of this model
    */
   public ABaseLearningModel(int numberOfFeaturesTrainedOn) {
      this.numberOfFeaturesTrainedOn = numberOfFeaturesTrainedOn;
   }


   /**
    * Checks if the given dataset is compatible with this learning model. If this is not the case a
    * {@link PredictionFailedException} is thrown.
    * 
    * @param dataset the dataset to check for compatibility
    * 
    * @throws PredictionFailedException if the dataset is not compatible
    */
   protected void checkDatasetForCompatibility(IDataset<?, ?, ?> dataset) throws PredictionFailedException {
      assertDatasetHasCorrectType(dataset, BaselearnerDataset.class);
      if (!isDatasetCompatible(dataset)) {
         throw new PredictionFailedException(ERROR_GIVEN_DATASET_NOT_COMPATIBLE_FOR_THIS_LEARNING_MODEL);
      }
   }


   /**
    * Checks if the given instance is compatible with this learning model. If this is not the case a
    * {@link PredictionFailedException} is thrown.
    * 
    * @param instance the instance to check for compatibility
    * 
    * @throws PredictionFailedException if the instance is not compatible
    */
   protected void checkInstanceForCompatibility(IInstance<?, ?, ?> instance) throws PredictionFailedException {
      assertInstanceHasCorrectType(instance, BaselearnerInstance.class);
      if (!isInstanceCompatible(instance)) {
         throw new PredictionFailedException(ERROR_GIVEN_INSTANCE_NOT_COMPATIBLE_FOR_THIS_LEARNING_MODEL);
      }
   }


   /**
    * The final bias of this model.
    * 
    * @return the bias of the this model
    * @throws UnsupportedOperationException if this operation is unsupported
    */
   public abstract double getBias() throws UnsupportedOperationException;


   /**
    * The final weight vector of this model.
    * 
    * @return the weight vector of this model
    * @throws UnsupportedOperationException if this operation is unsupported
    */
   public abstract IVector getWeightVector() throws UnsupportedOperationException;


   /**
    * Converts the given prediction ({@code -1} or {@code 1}) to a {@code 0} / {@code 1} prediction.
    * If the given prediction is {@code 1}, it will stay {@code 1}. If it is {@code -1} it will
    * become {@code 0}.
    * 
    * @param prediction the prediction to convert ({@code -1} or {@code 1})
    * @return {@code 0}, if the given prediction is {@code -1}, {@code 1}, otherwise
    */
   public static double convertNegativeBinaryPredictionTo01Prediction(double prediction) {
      if (Double.compare(prediction, -1.0) == 0) {
         return 0;
      }
      return 1;
   }


   @Override
   public boolean isDatasetCompatible(IDataset<?, ?, ?> dataset) {
      if (dataset instanceof BaselearnerDataset) {
         BaselearnerDataset baselearnerDataset = (BaselearnerDataset) dataset;
         return isFeatureVectorSizedCorrectly(baselearnerDataset.getNumberOfFeatures());
      }
      return false;
   }


   @Override
   public boolean isInstanceCompatible(IInstance<?, ?, ?> instance) {
      if (instance instanceof BaselearnerInstance) {
         BaselearnerInstance baselearnerInstance = (BaselearnerInstance) instance;
         return isFeatureVectorSizedCorrectly(baselearnerInstance.getFeatureVector().length);
      }
      return false;
   }


   /**
    * Returns {@code true} if the given features vector size has the same size as this model has
    * training features.
    * 
    * @param featureVectorSize the size of the feature vector to check
    * @return {@code true} if the given features vector has as many entries as this model has
    *         training features, otherwise {@code false}
    */
   private boolean isFeatureVectorSizedCorrectly(int featureVectorSize) {
      return featureVectorSize == numberOfFeaturesTrainedOn;
   }


   @Override
   public boolean equals(Object secondObject) {
      if (super.equals(secondObject) && secondObject instanceof ABaseLearningModel) {
         return true;
      }
      return false;
   }

}
