package de.upb.cs.is.jpl.api.algorithm.learningalgorithm.ordinalclassification.simple;


import java.util.ArrayList;
import java.util.List;

import de.upb.cs.is.jpl.api.algorithm.baselearner.dataset.BaselearnerDataset;
import de.upb.cs.is.jpl.api.algorithm.baselearner.dataset.BaselearnerInstance;
import de.upb.cs.is.jpl.api.algorithm.learningalgorithm.ILearningModel;
import de.upb.cs.is.jpl.api.algorithm.learningalgorithm.ordinalclassification.AOrdinalClassificationLearningModel;
import de.upb.cs.is.jpl.api.algorithm.learningalgorithm.ordinalclassification.OrdinalClassificationUtils;
import de.upb.cs.is.jpl.api.dataset.IDataset;
import de.upb.cs.is.jpl.api.dataset.IInstance;
import de.upb.cs.is.jpl.api.dataset.ordinalclassification.OrdinalClassificationDataset;
import de.upb.cs.is.jpl.api.dataset.ordinalclassification.OrdinalClassificationInstance;
import de.upb.cs.is.jpl.api.exception.algorithm.PredictionFailedException;
import de.upb.cs.is.jpl.api.util.CollectionsUtils;


/**
 * This learning model is used for {@link SimpleOrdinalClassification}.
 * 
 * @author Tanja Tornede
 *
 */
public class SimpleOrdinalClassificationLearningModel extends AOrdinalClassificationLearningModel {

   private List<ILearningModel<Double>> baselearnerModels;
   private int numberOfBaselearnerModelsTrained;


   /**
    * Creates a {@link SimpleOrdinalClassificationLearningModel} with the given prediction classes,
    * the given list of baselearner models and the given number of context features the baselearner
    * models are trained on.
    * 
    * @param predictionClasses the classes used for prediction
    * @param baselearnerModels the models from the base learner trained by the
    *           {@link SimpleOrdinalClassification}
    * @param numberOfContextFeaturesTrainedOn the number of trained context features
    */
   public SimpleOrdinalClassificationLearningModel(List<Double> predictionClasses, List<ILearningModel<Double>> baselearnerModels,
         int numberOfContextFeaturesTrainedOn) {
      super(numberOfContextFeaturesTrainedOn);
      initializePredictionClasses(predictionClasses);
      initializeBaselearners(baselearnerModels);
   }


   /**
    * Initializes the baselearners used for prediction.
    * 
    * @param baselearnerModels the list of baselearner models used in this learning model
    */
   private void initializeBaselearners(List<ILearningModel<Double>> baselearnerModels) {
      this.baselearnerModels = CollectionsUtils.getDeepCopyOf(baselearnerModels);
      this.numberOfBaselearnerModelsTrained = this.baselearnerModels.size();
   }


   @Override
   public Double predict(IInstance<?, ?, ?> instance) throws PredictionFailedException {
      OrdinalClassificationInstance ordinalInstance = getOrdinalClassificationInstance(instance);

      Double bestClass = predictionClasses.get(0);
      Double bestProbability = 1 - getBaselearnerPredictionForInstanceAndPredictionClass(ordinalInstance, 0);
      for (int i = 1; i < numberOfBaselearnerModelsTrained - 1; i++) {
         double probability = getProbabilityOfInstanceForPredictionClass(ordinalInstance, i);
         if (probability > bestProbability) {
            bestClass = predictionClasses.get(i);
            bestProbability = probability;
         }
      }

      double probability = getBaselearnerPredictionForInstanceAndPredictionClass(ordinalInstance, numberOfBaselearnerModelsTrained - 1);
      if (probability > bestProbability) {
         bestClass = predictionClasses.get(numberOfBaselearnerModelsTrained);
      }

      return bestClass;
   }


   /**
    * Returns the probability that the given instance is of the prediction class with the given
    * index.
    * 
    * @param ordinalInstance the ordinal instance to predict
    * @param indexOfPredictionClass the index of the prediction class in {@link #predictionClasses}
    * 
    * @return the probability that the given instance is of the prediction class with the given
    *         index
    */
   private double getProbabilityOfInstanceForPredictionClass(OrdinalClassificationInstance ordinalInstance, int indexOfPredictionClass)
         throws PredictionFailedException {
      double probabilityOfPreviousPredictionClass = getBaselearnerPredictionForInstanceAndPredictionClass(ordinalInstance,
            indexOfPredictionClass - 1);
      double probabilityOfPredictionClass = getBaselearnerPredictionForInstanceAndPredictionClass(ordinalInstance, indexOfPredictionClass);
      return Math.max(probabilityOfPreviousPredictionClass - probabilityOfPredictionClass, 0);
   }


   /**
    * Returns the prediction of the baselearner for the given ordinal instance and the prediction
    * class with the given index.
    * 
    * @param ordinalInstance the ordinal instance to predict for
    * @param indexOfPredictionClass the index of the prediction class in {@link #predictionClasses}
    * 
    * @return the prediction of the baselearner for given instance and the prediction class with the
    *         given index
    * 
    * @throws PredictionFailedException if prediction of the according model failed
    */
   private double getBaselearnerPredictionForInstanceAndPredictionClass(OrdinalClassificationInstance ordinalInstance,
         int indexOfPredictionClass) throws PredictionFailedException {
      BaselearnerInstance transformedInstance = OrdinalClassificationUtils.transformToBaselearnerInstance(ordinalInstance);
      return baselearnerModels.get(indexOfPredictionClass).predict(transformedInstance);
   }


   @Override
   public List<Double> predict(final IDataset<?, ?, ?> dataset) throws PredictionFailedException {
      OrdinalClassificationDataset ordinalDataset = getOrdinalClassificationDataset(dataset);

      BaselearnerDataset transformedBaselearnerDataset = OrdinalClassificationUtils.transformToBaselearnerDataset(ordinalDataset);

      List<List<Double>> results = new ArrayList<>(numberOfBaselearnerModelsTrained);
      for (int i = 0; i < numberOfBaselearnerModelsTrained; i++) {
         results.add(baselearnerModels.get(i).predict(transformedBaselearnerDataset));
      }

      List<Double> result = new ArrayList<>(dataset.getNumberOfInstances());
      for (int instanceIndex = 0; instanceIndex < ordinalDataset.getNumberOfInstances(); instanceIndex++) {

         double bestPredictionClass = predictionClasses.get(0);
         Double bestProbability = 1 - results.get(0).get(instanceIndex);

         for (int predictionClass = 1; predictionClass < numberOfBaselearnerModelsTrained - 1; predictionClass++) {
            double probability = Math
                  .max(results.get(predictionClass - 1).get(instanceIndex) - results.get(predictionClass).get(instanceIndex), 0);
            if (probability > bestProbability) {
               bestPredictionClass = predictionClasses.get(predictionClass);
               bestProbability = probability;
            }
         }

         double probability = results.get(numberOfBaselearnerModelsTrained - 1).get(instanceIndex);
         if (probability > bestProbability) {
            bestPredictionClass = predictionClasses.get(numberOfBaselearnerModelsTrained - 1);
         }

         result.add(instanceIndex, bestPredictionClass);
      }

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
      SimpleOrdinalClassificationLearningModel other = (SimpleOrdinalClassificationLearningModel) obj;
      if (baselearnerModels == null) {
         if (other.baselearnerModels != null)
            return false;
      } else if (!baselearnerModels.equals(other.baselearnerModels))
         return false;
      if (numberOfBaselearnerModelsTrained != other.numberOfBaselearnerModelsTrained)
         return false;
      return true;
   }


   @Override
   public int hashCode() {
      final int prime = 31;
      int result = super.hashCode();
      result = prime * result + ((baselearnerModels == null) ? 0 : baselearnerModels.hashCode());
      result = prime * result + numberOfBaselearnerModelsTrained;
      return result;
   }

}
