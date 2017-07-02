package de.upb.cs.is.jpl.api.algorithm.learningalgorithm.instanceranking.perceptronrank;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import de.upb.cs.is.jpl.api.algorithm.learningalgorithm.ALearningModel;
import de.upb.cs.is.jpl.api.dataset.IDataset;
import de.upb.cs.is.jpl.api.dataset.IInstance;
import de.upb.cs.is.jpl.api.dataset.instanceranking.InstanceRankingDataset;
import de.upb.cs.is.jpl.api.dataset.instanceranking.InstanceRankingInstance;
import de.upb.cs.is.jpl.api.exception.algorithm.PredictionFailedException;


/**
 * The learning model the produced by the {@link PerceptronRankLearningAlgorithm}. It initialized
 * with help of the {@code weightings} and {@code thresholds} which are created in the training
 * phase of the algorithm.
 *
 * @author Sebastian Gottschalk
 */
public class PerceptronRankLearningModel extends ALearningModel<Integer> {
   private final double[] weightings;
   private final double[] thresholds;

   private static final String ERROR_WRONG_NUMBER_OF_CONTEXT_FEATURES = "The instance has a wrong number of context features to interact with the model.";
   private static final String ERROR_PREDITION_FAILED = "The prediction of the current instance failed because of an integer overflow.";


   /**
    * Create a new PRankLearningModel.
    * 
    * @param weightings weightings of the algorithm
    * @param thresholds thresholds of the algorithm
    */
   PerceptronRankLearningModel(double[] weightings, double[] thresholds) {
      this.weightings = weightings;
      this.thresholds = thresholds;
   }


   @Override
   public Integer predict(IInstance<?, ?, ?> instance) throws PredictionFailedException {
      checkInstanceCompatible(instance);
      return predictCompatibleInstance(instance);
   }


   @Override
   public List<Integer> predict(final IDataset<?, ?, ?> dataset) throws PredictionFailedException {
      checkDatasetCompatible(dataset);

      List<Integer> result = new ArrayList<>(dataset.getNumberOfInstances());
      for (int i = 0; i < dataset.getNumberOfInstances(); i++) {
         result.add(predictCompatibleInstance(dataset.getInstance(i)));
      }
      return result;
   }


   @Override
   public boolean equals(Object secondObject) {
      if (super.equals(secondObject) && secondObject instanceof PerceptronRankLearningModel) {
         PerceptronRankLearningModel prankLearningModel = (PerceptronRankLearningModel) secondObject;
         if (Arrays.equals(weightings, prankLearningModel.weightings) && Arrays.equals(thresholds, prankLearningModel.thresholds)) {
            return true;
         }
      }
      return false;
   }


   @Override
   public int hashCode() {
      int hashCode = super.hashCode();
      hashCode += 31 * Arrays.hashCode(weightings);
      hashCode += 31 * Arrays.hashCode(thresholds);
      return hashCode;
   }


   @Override
   public boolean isDatasetCompatible(IDataset<?, ?, ?> dataset) {
      if (!(dataset instanceof InstanceRankingDataset)) {
         return false;
      }
      InstanceRankingDataset castedDataset = (InstanceRankingDataset) dataset;

      return castedDataset.getNumberOfFeatures() == weightings.length;
   }


   @Override
   public boolean isInstanceCompatible(IInstance<?, ?, ?> instance) {
      if (!(instance instanceof InstanceRankingInstance)) {
         return false;
      }
      InstanceRankingInstance castedInstance = (InstanceRankingInstance) instance;

      return castedInstance.getContextFeatureVector().length == weightings.length;
   }


   /**
    * Checks if the instance is not compatible.
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
   private Integer predictCompatibleInstance(IInstance<?, ?, ?> instance) throws PredictionFailedException {
      InstanceRankingInstance currentInstance = (InstanceRankingInstance) instance;

      for (int j = 0; j < thresholds.length; j++) {
         if ((PerceptronRankLearningAlgorithm.buildScalarProduct(weightings, currentInstance.getContextFeatureVector())
               - thresholds[j]) < 0) {
            return j + 1;
         }
      }
      throw new PredictionFailedException(ERROR_PREDITION_FAILED);

   }


}
