package de.upb.cs.is.jpl.api.algorithm.learningalgorithm.instanceranking.combinedrankingandregression;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import de.upb.cs.is.jpl.api.algorithm.learningalgorithm.ALearningModel;
import de.upb.cs.is.jpl.api.dataset.IDataset;
import de.upb.cs.is.jpl.api.dataset.IInstance;
import de.upb.cs.is.jpl.api.dataset.defaultdataset.DefaultInstance;
import de.upb.cs.is.jpl.api.dataset.defaultdataset.absolute.DefaultAbsoluteDataset;
import de.upb.cs.is.jpl.api.exception.algorithm.PredictionFailedException;
import de.upb.cs.is.jpl.api.math.util.DoubleVectorUtils;


/**
 * The learning model produced by the {@link CombinedRankingAndRegressionLearningAlgorithm}. It
 * initialized with help of the {@code weightingVector} and a specific {@code lossfunction} which
 * are created in the training phase of the algorithm.
 *
 * @author Sebastian Gottschalk
 */
public class CombinedRankingAndRegressionLearningModel extends ALearningModel<Double> {
   private static final String ERROR_WRONG_LOSSFUNCTION = "The used loss function should be 'logistic' or 'squared'.";
   private static final String LOGISTIC_PARAMETER = "logistic";
   private static final String SQUARED_PARAMETER = "squared";

   private static final String ERROR_WRONG_NUMBER_OF_CONTEXT_FEATURES = "The instance has a wrong number of context features to interact with the model.";


   private double[] weightingVector;
   private String lossfunction;


   /**
    * Create a new CRRLearningModel.
    * 
    * @param weightingVector weightings of the algorithm
    * @param lossfunction 'squared' or 'logistic' loss function of the algorithm
    */
   public CombinedRankingAndRegressionLearningModel(double[] weightingVector, String lossfunction) {
      this.weightingVector = weightingVector;
      this.lossfunction = lossfunction;
   }


   @Override
   public Double predict(IInstance<?, ?, ?> instance) throws PredictionFailedException {
      checkInstanceCompatible(instance);
      return predictCompatibleInstance(instance);
   }


   @Override
   public List<Double> predict(final IDataset<?, ?, ?> dataset) throws PredictionFailedException {
      checkDatasetCompatible(dataset);

      List<Double> result = new ArrayList<>(dataset.getNumberOfInstances());
      for (int i = 0; i < dataset.getNumberOfInstances(); i++) {
         result.add(predictCompatibleInstance(dataset.getInstance(i)));
      }
      return result;
   }


   @Override
   public boolean equals(Object secondObject) {
      if (super.equals(secondObject) && secondObject instanceof CombinedRankingAndRegressionLearningModel) {
         CombinedRankingAndRegressionLearningModel crrLearningModel = (CombinedRankingAndRegressionLearningModel) secondObject;
         if (Arrays.equals(weightingVector, crrLearningModel.weightingVector) && lossfunction.equals(crrLearningModel.lossfunction)) {
            return true;
         }
      }
      return false;
   }


   @Override
   public int hashCode() {
      int hashCode = super.hashCode();
      hashCode += 31 * Arrays.hashCode(weightingVector);
      hashCode += 31 * lossfunction.hashCode();
      return hashCode;
   }


   @Override
   public boolean isDatasetCompatible(IDataset<?, ?, ?> dataset) {
      if (!(dataset instanceof DefaultAbsoluteDataset)) {
         return false;
      }
      DefaultAbsoluteDataset castedDataset = (DefaultAbsoluteDataset) dataset;

      return castedDataset.getNumberOfInstances() != 0 && !castedDataset.getContexVectors().isEmpty()
            && castedDataset.getContextVector(0).length == weightingVector.length;
   }


   @Override
   public boolean isInstanceCompatible(IInstance<?, ?, ?> instance) {
      if (!(instance instanceof DefaultInstance)) {
         return false;
      }
      @SuppressWarnings("unchecked")
      DefaultInstance<Double> castedInstance = (DefaultInstance<Double>) instance;

      return weightingVector.length == castedInstance.getContextFeatureVector().length;
   }


   /**
    * Checks if the instance is not compatible
    * 
    * @param instance the instance which should be checked
    * @throws PredictionFailedException if the instance is not compatible
    */
   private void checkInstanceCompatible(IInstance<?, ?, ?> instance) throws PredictionFailedException {
      if (!isInstanceCompatible(instance)) {
         throw new PredictionFailedException(ERROR_WRONG_NUMBER_OF_CONTEXT_FEATURES);
      }
   }


   /**
    * Checks if the dataset is not compatible.
    * 
    * @param dataset the dataset which should be checked
    * @throws PredictionFailedException if the dataset is not compatible
    */
   private void checkDatasetCompatible(IDataset<?, ?, ?> dataset) throws PredictionFailedException {
      if (!isDatasetCompatible(dataset)) {
         throw new PredictionFailedException(ERROR_WRONG_NUMBER_OF_CONTEXT_FEATURES);
      }
   }


   /**
    * Predict the rank of a single instance.
    * 
    * @param instance the instance which should be predicted
    * @return the prediction of the instance
    * @throws PredictionFailedException if the prediction failed
    */
   private Double predictCompatibleInstance(IInstance<?, ?, ?> instance) throws PredictionFailedException {
      @SuppressWarnings("unchecked")
      DefaultInstance<Double> instanceForPrediction = (DefaultInstance<Double>) instance;

      if (LOGISTIC_PARAMETER.equals(lossfunction)) {
         return 1 / (1.0
               + Math.pow(Math.E, -DoubleVectorUtils.multiplyVectors(weightingVector, instanceForPrediction.getContextFeatureVector())));
      } else if (SQUARED_PARAMETER.equals(lossfunction)) {
         return DoubleVectorUtils.multiplyVectors(weightingVector, instanceForPrediction.getContextFeatureVector());
      }

      throw new PredictionFailedException(ERROR_WRONG_LOSSFUNCTION);

   }

}
