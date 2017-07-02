package de.upb.cs.is.jpl.api.algorithm.learningalgorithm.multilabelclassification.binaryrelevancelearning;


import java.util.ArrayList;
import java.util.List;

import de.upb.cs.is.jpl.api.algorithm.baselearner.ABaseLearningModel;
import de.upb.cs.is.jpl.api.algorithm.baselearner.dataset.BaselearnerInstance;
import de.upb.cs.is.jpl.api.algorithm.learningalgorithm.ILearningModel;
import de.upb.cs.is.jpl.api.algorithm.learningalgorithm.multilabelclassification.AMultilabelClassificationLearningModel;
import de.upb.cs.is.jpl.api.dataset.IDataset;
import de.upb.cs.is.jpl.api.dataset.IInstance;
import de.upb.cs.is.jpl.api.dataset.multilabelclassification.MultilabelClassificationDataset;
import de.upb.cs.is.jpl.api.dataset.multilabelclassification.MultilabelClassificationInstance;
import de.upb.cs.is.jpl.api.exception.algorithm.PredictionFailedException;
import de.upb.cs.is.jpl.api.math.linearalgebra.SparseDoubleVector;


/**
 * This learning model is produced by the {@link BinaryRelevanceLearningAlgorithm}. This learning
 * model is made up of one binary learning model trained for each label of the dataset this model
 * was trained on. It can be used to do predictions on multilabel classification instances.
 * 
 * @author Alexander Hetzer
 *
 */
public class BinaryRelevanceLearningModel extends AMultilabelClassificationLearningModel {


   /**
    * Creates a binary relevance learning model defined by the list of given linear classification
    * learning models.
    * 
    * @param baseLearningModels the linear classification learning models defining this learning
    *           model
    * @param numberOfContextFeaturesTrainedOn the number of context features this model was trained
    *           on
    */
   public BinaryRelevanceLearningModel(List<ILearningModel<Double>> baseLearningModels, int numberOfContextFeaturesTrainedOn) {
      super(baseLearningModels, numberOfContextFeaturesTrainedOn);
   }


   @Override
   public SparseDoubleVector predict(IInstance<?, ?, ?> instance) throws PredictionFailedException {
      assertInstanceIsMultilabelClassificationInstance(instance);
      MultilabelClassificationInstance multilabelClassificationInstance = (MultilabelClassificationInstance) instance;
      return predictLabelsForFeatureVector(multilabelClassificationInstance.getContextFeatureVector());
   }


   @Override
   public List<SparseDoubleVector> predict(IDataset<?, ?, ?> dataset) throws PredictionFailedException {
      assertDatasetIsMultilabelClassificationDataset(dataset);
      MultilabelClassificationDataset multilabelClassificationDataset = (MultilabelClassificationDataset) dataset;
      List<SparseDoubleVector> predictionResults = new ArrayList<>();
      for (int i = 0; i < multilabelClassificationDataset.getNumberOfInstances(); i++) {
         double[] featureVector = multilabelClassificationDataset.getFeatureVectors().get(i);
         SparseDoubleVector labelVector = predictLabelsForFeatureVector(featureVector);
         predictionResults.add(labelVector);
      }
      return predictionResults;
   }


   /**
    * Aggregates the predictions of all base learner models for the given feature vector into a
    * multilabel prediction.
    * 
    * @param featureVector the feature vector to predict
    * @return the multilabel prediction for the given feature vector
    * @throws PredictionFailedException if one of the predictions of the base learners fails
    */
   private SparseDoubleVector predictLabelsForFeatureVector(double[] featureVector) throws PredictionFailedException {
      assertFeatureVectorHasExpectedNumberOfFeatures(featureVector);
      double[] predictedLabelArray = new double[baseLearningModels.size()];
      for (int i = 0; i < baseLearningModels.size(); i++) {
         predictedLabelArray[i] = ABaseLearningModel
               .convertNegativeBinaryPredictionTo01Prediction(baseLearningModels.get(i).predict(new BaselearnerInstance(featureVector)));
      }
      return new SparseDoubleVector(predictedLabelArray);
   }


   @Override
   public boolean equals(Object secondObject) {
      if (super.equals(secondObject) && secondObject instanceof BinaryRelevanceLearningModel) {
         BinaryRelevanceLearningModel secondLearningModel = (BinaryRelevanceLearningModel) secondObject;
         if (this == secondLearningModel) {
            return true;
         }
         if (baseLearningModels.equals(secondLearningModel.baseLearningModels)) {
            return true;
         }
      }
      return false;
   }


   @Override
   public int hashCode() {
      int hashCode = super.hashCode();
      hashCode = 31 * hashCode + baseLearningModels.hashCode();
      return hashCode;
   }


}
