package de.upb.cs.is.jpl.api.algorithm.learningalgorithm.multilabelclassification.classifierchains;


import java.util.ArrayList;
import java.util.List;

import de.upb.cs.is.jpl.api.algorithm.baselearner.ABaseLearningModel;
import de.upb.cs.is.jpl.api.algorithm.baselearner.dataset.BaselearnerInstance;
import de.upb.cs.is.jpl.api.algorithm.learningalgorithm.ILearningModel;
import de.upb.cs.is.jpl.api.algorithm.learningalgorithm.multilabelclassification.AMultilabelClassificationLearningModel;
import de.upb.cs.is.jpl.api.algorithm.learningalgorithm.multilabelclassification.binaryrelevancelearning.BinaryRelevanceLearningModel;
import de.upb.cs.is.jpl.api.dataset.IDataset;
import de.upb.cs.is.jpl.api.dataset.IInstance;
import de.upb.cs.is.jpl.api.dataset.multilabelclassification.MultilabelClassificationDataset;
import de.upb.cs.is.jpl.api.dataset.multilabelclassification.MultilabelClassificationInstance;
import de.upb.cs.is.jpl.api.exception.algorithm.PredictionFailedException;
import de.upb.cs.is.jpl.api.math.linearalgebra.SparseDoubleVector;


/**
 * This learning model is produced by the {@link ClassifierChainsLearningAlgorithm}. Similarly to
 * the {@link BinaryRelevanceLearningModel} this learning model is made up of one binary learning
 * model trained for each label of the dataset this model was trained on. The important difference
 * is that the classifier chains require the user to define a permutation on the labels first. Based
 * on this permutation the binary base learning models are used to predict successively on extended
 * examples. The feature vectors of the examples model {@code i} was trained on, are extended by all
 * predictions on the labels coming before label {@code i} in the permutation (based on the learned
 * models). This way label dependencies are taken into account. Obviously the permutation can have
 * an important impact on the prediction performance of this approach. By default the permutation is
 * set to the order the labels are present in the dataset.
 * 
 * @author Alexander Hetzer
 *
 */
public class ClassifierChainsLearningModel extends AMultilabelClassificationLearningModel {

   private List<Integer> permutation;


   /**
    * Creates a new {@link ClassifierChainsLearningModel} with the given base learning models.
    * 
    * @param baseLearningModels the base learning models to use
    * @param numberOfContextFeaturesTrainedOn the number of context features this model was trained
    *           on
    * @param permutation a permutation of the labels to use
    */
   public ClassifierChainsLearningModel(List<ILearningModel<Double>> baseLearningModels, int numberOfContextFeaturesTrainedOn,
         List<Integer> permutation) {
      super(baseLearningModels, numberOfContextFeaturesTrainedOn);
      this.permutation = permutation;
   }


   @Override
   public SparseDoubleVector predict(IInstance<?, ?, ?> instance) throws PredictionFailedException {
      assertInstanceIsMultilabelClassificationInstance(instance);
      MultilabelClassificationInstance multilabelClassificationInstance = (MultilabelClassificationInstance) instance;
      double[] multilabelClassificationFeatureVector = multilabelClassificationInstance.getContextFeatureVector();
      return predictLabelsForFeatureVector(multilabelClassificationFeatureVector);
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
      int numberOfMultilabelClassificationFeatures = featureVector.length;
      for (int i = 0; i < baseLearningModels.size(); i++) {
         int totalAmountOfFeatures = featureVector.length + i;
         double[] artificialFeatureVector = new double[numberOfMultilabelClassificationFeatures + i];
         for (int j = 0; j < numberOfMultilabelClassificationFeatures; j++) {
            artificialFeatureVector[j] = featureVector[j];
         }
         for (int j = numberOfMultilabelClassificationFeatures; j < totalAmountOfFeatures; j++) {
            artificialFeatureVector[j] = convertPositiveBinaryPredictionToNegativePrediction(
                  predictedLabelArray[getPermutationResultFor(j - numberOfMultilabelClassificationFeatures)]);
         }
         BaselearnerInstance baselearnerInstance = new BaselearnerInstance(artificialFeatureVector);
         predictedLabelArray[getPermutationResultFor(i)] = ABaseLearningModel.convertNegativeBinaryPredictionTo01Prediction(
               baseLearningModels.get(getPermutationResultFor(i)).predict(baselearnerInstance));
      }
      return new SparseDoubleVector(predictedLabelArray);
   }


   @Override
   public List<SparseDoubleVector> predict(IDataset<?, ?, ?> dataset) throws PredictionFailedException {
      assertDatasetIsMultilabelClassificationDataset(dataset);
      MultilabelClassificationDataset multilabelClassificationDataset = (MultilabelClassificationDataset) dataset;
      List<SparseDoubleVector> predictions = new ArrayList<>();
      for (int i = 0; i < dataset.getNumberOfInstances(); i++) {
         SparseDoubleVector predictedLabelVector = predictLabelsForFeatureVector(
               multilabelClassificationDataset.getFeatureVectors().get(i));
         predictions.add(predictedLabelVector);
      }
      return predictions;
   }


   @Override
   public int hashCode() {
      final int prime = 31;
      int result = super.hashCode();
      result = prime * result + ((permutation == null) ? 0 : permutation.hashCode());
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
      ClassifierChainsLearningModel other = (ClassifierChainsLearningModel) obj;
      if (permutation == null) {
         if (other.permutation != null)
            return false;
      } else if (!permutation.equals(other.permutation))
         return false;
      return true;
   }


   /**
    * Returns the value of the permutation for the given argument. I.E. if 0 is mapped to 3 in the
    * permutation, calling this method with 0 as an argument will return 3.
    * 
    * @param argument the argument to return the permutation value for
    * @return the value of the permutation for the given argument
    */
   private int getPermutationResultFor(int argument) {
      return permutation.get(argument);
   }

}
