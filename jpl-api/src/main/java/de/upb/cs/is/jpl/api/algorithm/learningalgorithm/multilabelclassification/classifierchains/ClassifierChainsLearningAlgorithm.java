package de.upb.cs.is.jpl.api.algorithm.learningalgorithm.multilabelclassification.classifierchains;


import java.util.ArrayList;
import java.util.List;

import de.upb.cs.is.jpl.api.algorithm.baselearner.IBaselearnerAlgorithm;
import de.upb.cs.is.jpl.api.algorithm.baselearner.dataset.BaselearnerDataset;
import de.upb.cs.is.jpl.api.algorithm.learningalgorithm.ALearningAlgorithm;
import de.upb.cs.is.jpl.api.algorithm.learningalgorithm.ELearningAlgorithm;
import de.upb.cs.is.jpl.api.algorithm.learningalgorithm.ILearningModel;
import de.upb.cs.is.jpl.api.algorithm.learningalgorithm.multilabelclassification.binaryrelevancelearning.BinaryRelevanceLearningAlgorithm;
import de.upb.cs.is.jpl.api.dataset.IDataset;
import de.upb.cs.is.jpl.api.dataset.IDatasetParser;
import de.upb.cs.is.jpl.api.dataset.multilabelclassification.MultilabelClassificationDataset;
import de.upb.cs.is.jpl.api.dataset.multilabelclassification.MultilabelClassificationDatasetParser;
import de.upb.cs.is.jpl.api.exception.algorithm.TrainModelsFailedException;
import de.upb.cs.is.jpl.api.math.linearalgebra.SparseDoubleVector;


/**
 * This class represents the implementation of the classifier chains approach for multilabel
 * classification. The approach was originally introduced by Bernhard Read, Geoff Holmes Pfahringer
 * and Frank Eibe. Similarly to the {@link BinaryRelevanceLearningAlgorithm} a binary classifier is
 * trained for each label separately. The important difference is that the classifier chains require
 * the user to define a permutation on the labels first. Based on this permutation the binary
 * classifiers are learned successively on extended examples. The feature vectors of the examples
 * classifier {@code i} is trained on, are extended by all target values of the labels coming before
 * label {@code i} in the permutation. This way label dependencies are taken into account. Obviously
 * the permutation can have an important impact on the prediction performance of this approach. By
 * default the permutation is set to the order the labels are present in the dataset.
 * 
 * @author Alexander Hetzer
 *
 */
public class ClassifierChainsLearningAlgorithm extends ALearningAlgorithm<ClassifierChainsConfiguration> {

   private static final String ERROR_WRONG_PERMUTATION_SIZE = "The size of the permutation does not coincide with the number of labels of the dataset. Expected: %d - Actual: %d .";

   private MultilabelClassificationDataset multilabelClassificationDataset;

   private List<BaselearnerDataset> baselearnerDatasets;

   private List<ILearningModel<Double>> baseLearningModels;

   private List<Integer> permutation;


   /**
    * Creates a new classifier chains learning algorithm with the enum identifier.
    */
   public ClassifierChainsLearningAlgorithm() {
      super(ELearningAlgorithm.CLASSIFIER_CHAINS.getIdentifier());
   }


   @Override
   public void init() {
      // empty, as we cannot initialize this learner before having the dataset
   }


   /**
    * Initializes the list of baselearner models and datasets {@code null}, which will be filled
    * later on.
    */
   private void initializeBaseLearnerDummies() {
      baseLearningModels = new ArrayList<>(multilabelClassificationDataset.getNumberOfLabels());
      baselearnerDatasets = new ArrayList<>(multilabelClassificationDataset.getNumberOfLabels());
      for (int i = 0; i < multilabelClassificationDataset.getNumberOfLabels(); i++) {
         baseLearningModels.add(null);
         baselearnerDatasets.add(null);
      }
   }


   /**
    * Initializes the permutation from the configuration. If the default permutation is set in the
    * configuration, the default permutation is initialized.
    */
   private void initializePermutation() {
      if (configuration.hasDefaultPermutation()) {
         configuration.initializeDefaultPermutation(multilabelClassificationDataset.getNumberOfLabels());
      }
      this.permutation = configuration.getPermutation();
   }


   /**
    * Asserts that the size of the permutation coincides with the number of labels of the dataset.
    * 
    * @throws TrainModelsFailedException if the size of the permutation does not coincide with the
    *            number of labels of the dataset
    */
   private void assertPermutationHasCorrectSize() throws TrainModelsFailedException {
      // the rest of the possible error cases are all checked in the configuration
      if (permutation.size() != multilabelClassificationDataset.getNumberOfLabels()) {
         throw new TrainModelsFailedException(
               String.format(ERROR_WRONG_PERMUTATION_SIZE, multilabelClassificationDataset.getNumberOfLabels(), permutation.size()));
      }
   }


   /**
    * Initializes the learning algorithm.
    * 
    * @throws TrainModelsFailedException if the size of the permutation does not coincide with the
    *            number of labels of the dataset
    */
   private void initializeLearningAlgorithm() throws TrainModelsFailedException {
      initializeBaseLearnerDummies();
      initializePermutation();
      assertPermutationHasCorrectSize();
      initializeBaselearnerDatasets();
   }


   @Override
   protected ILearningModel<?> performTraining(IDataset<?, ?, ?> dataset) throws TrainModelsFailedException {
      multilabelClassificationDataset = (MultilabelClassificationDataset) dataset;
      initializeLearningAlgorithm();
      trainBaselearners();
      return new ClassifierChainsLearningModel(baseLearningModels, multilabelClassificationDataset.getNumberOfFeatures(), permutation);
   }


   @Override
   public ClassifierChainsLearningModel train(IDataset<?, ?, ?> dataset) throws TrainModelsFailedException {
      return (ClassifierChainsLearningModel) super.train(dataset);
   }


   /**
    * Trains the base learners based on the permutation of the labels.
    * 
    * @throws TrainModelsFailedException if training one of the base learners fails
    */
   private void trainBaselearners() throws TrainModelsFailedException {
      for (int i = 0; i < baselearnerDatasets.size(); i++) {
         IBaselearnerAlgorithm baseLearner = this.configuration.getBaseLearnerAlgorithm();
         @SuppressWarnings("unchecked")
         ILearningModel<Double> learningModel = (ILearningModel<Double>) baseLearner
               .train(baselearnerDatasets.get(getPermutationResultFor(i)));
         baseLearningModels.set(getPermutationResultFor(i), learningModel);
      }
   }


   /**
    * Initializes the base learner datasets.
    */
   private void initializeBaselearnerDatasets() {
      List<double[]> featureVectors = multilabelClassificationDataset.getFeatureVectors();
      List<SparseDoubleVector> labelVectors = multilabelClassificationDataset.getCorrectResults();
      int numberOfFeatures = multilabelClassificationDataset.getNumberOfFeatures();
      for (int i = 0; i < multilabelClassificationDataset.getNumberOfLabels(); i++) {
         int numberOfFeaturesForBaseLearnerDataset = numberOfFeatures + i;
         BaselearnerDataset baselearnerDataset = new BaselearnerDataset(multilabelClassificationDataset.getNumberOfInstances(),
               numberOfFeaturesForBaseLearnerDataset);

         for (int j = 0; j < multilabelClassificationDataset.getNumberOfInstances(); j++) {
            double[] featureVector = new double[numberOfFeaturesForBaseLearnerDataset];
            for (int h = 0; h < numberOfFeatures; h++) {
               featureVector[h] = featureVectors.get(j)[h];
            }
            for (int h = numberOfFeatures; h < numberOfFeaturesForBaseLearnerDataset; h++) {
               featureVector[h] = convertPositiveBinaryPredictionToNegativePrediction(
                     labelVectors.get(j).getValue(getPermutationResultFor(h - numberOfFeatures)));
            }
            baselearnerDataset.addFeatureVectorWithResult(featureVector, convertPositiveBinaryPredictionToNegativePrediction(
                  labelVectors.get(j).getValue(getPermutationResultFor(numberOfFeaturesForBaseLearnerDataset - numberOfFeatures))));
         }
         baselearnerDatasets.set(getPermutationResultFor(i), baselearnerDataset);
      }
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


   @Override
   protected ClassifierChainsConfiguration createDefaultAlgorithmConfiguration() {
      return new ClassifierChainsConfiguration();
   }


   @Override
   public IDatasetParser getDatasetParser() {
      return new MultilabelClassificationDatasetParser();
   }


   @Override
   public int hashCode() {
      final int prime = 31;
      int result = super.hashCode();
      result = prime * result + ((baseLearningModels == null) ? 0 : baseLearningModels.hashCode());
      result = prime * result + ((baselearnerDatasets == null) ? 0 : baselearnerDatasets.hashCode());
      result = prime * result + ((multilabelClassificationDataset == null) ? 0 : multilabelClassificationDataset.hashCode());
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
      ClassifierChainsLearningAlgorithm other = (ClassifierChainsLearningAlgorithm) obj;
      if (baseLearningModels == null) {
         if (other.baseLearningModels != null)
            return false;
      } else if (!baseLearningModels.equals(other.baseLearningModels))
         return false;
      if (baselearnerDatasets == null) {
         if (other.baselearnerDatasets != null)
            return false;
      } else if (!baselearnerDatasets.equals(other.baselearnerDatasets))
         return false;
      if (multilabelClassificationDataset == null) {
         if (other.multilabelClassificationDataset != null)
            return false;
      } else if (!multilabelClassificationDataset.equals(other.multilabelClassificationDataset))
         return false;
      if (permutation == null) {
         if (other.permutation != null)
            return false;
      } else if (!permutation.equals(other.permutation))
         return false;
      return true;
   }


}
