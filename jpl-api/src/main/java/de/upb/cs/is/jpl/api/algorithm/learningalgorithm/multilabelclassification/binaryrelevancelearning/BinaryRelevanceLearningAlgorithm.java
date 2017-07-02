package de.upb.cs.is.jpl.api.algorithm.learningalgorithm.multilabelclassification.binaryrelevancelearning;


import java.util.ArrayList;
import java.util.List;

import de.upb.cs.is.jpl.api.algorithm.AAlgorithmConfiguration;
import de.upb.cs.is.jpl.api.algorithm.baselearner.IBaselearnerAlgorithm;
import de.upb.cs.is.jpl.api.algorithm.baselearner.dataset.BaselearnerDataset;
import de.upb.cs.is.jpl.api.algorithm.learningalgorithm.ALearningAlgorithm;
import de.upb.cs.is.jpl.api.algorithm.learningalgorithm.ELearningAlgorithm;
import de.upb.cs.is.jpl.api.algorithm.learningalgorithm.ILearningModel;
import de.upb.cs.is.jpl.api.dataset.IDataset;
import de.upb.cs.is.jpl.api.dataset.IDatasetParser;
import de.upb.cs.is.jpl.api.dataset.multilabelclassification.MultilabelClassificationDataset;
import de.upb.cs.is.jpl.api.dataset.multilabelclassification.MultilabelClassificationDatasetParser;
import de.upb.cs.is.jpl.api.exception.algorithm.TrainModelsFailedException;
import de.upb.cs.is.jpl.api.math.linearalgebra.SparseDoubleVector;


/**
 * The binary relevance learning algorithm is a very simple multilabel classification learning
 * algorithm which aims at decomposing the MLC problem into multiple binary classification problems.
 * The general idea is to train a binary classifier for each label separately. At prediction time an
 * instance is passed to all binary learning models and their predictions are aggregated in order to
 * obtain the overall prediction.
 * 
 * @author Alexander Hetzer
 *
 */
public class BinaryRelevanceLearningAlgorithm extends ALearningAlgorithm<BinaryRelevanceLearningConfiguration> {


   private MultilabelClassificationDataset multilabelClassificationDataset;

   private List<ILearningModel<Double>> baseLearningModels;

   private List<BaselearnerDataset> baseLearnerDatasets;


   /**
    * Creates a new {@link BinaryRelevanceLearningAlgorithm} initialized with the default
    * configuration.
    */
   public BinaryRelevanceLearningAlgorithm() {
      super(ELearningAlgorithm.BINARY_RELEVANCE_LEARNING.getIdentifier());
   }


   /**
    * Creates a new {@link BinaryRelevanceLearningAlgorithm} with the given algorithm as a base
    * learner. Using this constructor will overwrite the default base learner defined in the default
    * configuration.
    * 
    * @param baselearnerAlgorithm the base learner to use for this algorithm
    */
   public BinaryRelevanceLearningAlgorithm(IBaselearnerAlgorithm baselearnerAlgorithm) {
      super(ELearningAlgorithm.BINARY_RELEVANCE_LEARNING.toString());
      this.configuration.setBaseLearnerAlgorithm(baselearnerAlgorithm);
   }


   @Override
   public IDatasetParser getDatasetParser() {
      return new MultilabelClassificationDatasetParser();
   }


   @Override
   protected BinaryRelevanceLearningModel performTraining(IDataset<?, ?, ?> dataset) throws TrainModelsFailedException {
      multilabelClassificationDataset = (MultilabelClassificationDataset) dataset;

      initializeBaseLearnerDatasetsWithNegativeEntries();
      overrideFalseNegativeEntriesInBaseLearnerDatasets();

      for (int i = 0; i < multilabelClassificationDataset.getNumberOfLabels(); i++) {
         ILearningModel<Double> linearClassificationLearningModel = trainBaseLearnerForLabel(i);
         addBaseLearningModelToTrained(linearClassificationLearningModel);
      }

      return new BinaryRelevanceLearningModel(baseLearningModels, multilabelClassificationDataset.getNumberOfFeatures());
   }


   @Override
   public BinaryRelevanceLearningModel train(IDataset<?, ?, ?> dataset) throws TrainModelsFailedException {
      return (BinaryRelevanceLearningModel) super.train(dataset);
   }


   /**
    * Adds the given base learning model to the list of trained base learning models.
    * 
    * @param linearClassificationLearningModel the learning model to add to the list of trained base
    *           learning models
    */
   private void addBaseLearningModelToTrained(ILearningModel<Double> linearClassificationLearningModel) {
      baseLearningModels.add(linearClassificationLearningModel);
   }


   /**
    * Trains the given base learner for the given label id and returns the learning model produced.
    * 
    * @param labelId the id of the label to train the base learner on
    * @return the learning model obtained from training the given base learner for the given label
    * @throws TrainModelsFailedException if the training of the base learner failed
    */
   @SuppressWarnings("unchecked")
   private ILearningModel<Double> trainBaseLearnerForLabel(int labelId) throws TrainModelsFailedException {
      return (ILearningModel<Double>) configuration.getBaseLearnerAlgorithm().train(baseLearnerDatasets.get(labelId));
   }


   /**
    * Overrides all negative entries in all base learner sets, which should not be negative with
    * positive values. As the label vectors of the multilabel classification dataset are assumed to
    * be sparse, this method will not have to override too many values. Note that this method should
    * only be called after {@link #initializeBaseLearnerDatasetsWithNegativeEntries()}.
    */
   private void overrideFalseNegativeEntriesInBaseLearnerDatasets() {
      List<SparseDoubleVector> labelVectors = multilabelClassificationDataset.getCorrectResults();
      for (int i = 0; i < multilabelClassificationDataset.getNumberOfInstances(); i++) {
         SparseDoubleVector labelVector = labelVectors.get(i);
         int[] nonZeroIndices = labelVector.getNonZeroIndices();
         for (int j = 0; j < nonZeroIndices.length; j++) {
            BaselearnerDataset baseLearnerDatasetForLabelJ = baseLearnerDatasets.get(nonZeroIndices[j]);
            baseLearnerDatasetForLabelJ.getCorrectResults()[i] = 1;
         }
      }
   }


   /**
    * Initializes one base learner dataset per label with the feature vectors defined in the overall
    * multilabel classification dataset. All results are initialized with -1. Note that this is
    * efficient if the label vectors of the multilabel classification are in general sparse.
    */
   private void initializeBaseLearnerDatasetsWithNegativeEntries() {
      for (int i = 0; i < multilabelClassificationDataset.getNumberOfLabels(); i++) {
         BaselearnerDataset baselearnerDataset = new BaselearnerDataset(multilabelClassificationDataset.getNumberOfInstances(),
               multilabelClassificationDataset.getNumberOfFeatures());

         List<double[]> featureVectors = multilabelClassificationDataset.getFeatureVectors();
         for (int j = 0; j < multilabelClassificationDataset.getNumberOfInstances(); j++) {
            baselearnerDataset.addFeatureVectorWithResult(featureVectors.get(j), -1);
         }
         baseLearnerDatasets.add(baselearnerDataset);
      }
   }


   @Override
   protected AAlgorithmConfiguration createDefaultAlgorithmConfiguration() {
      BinaryRelevanceLearningConfiguration binaryRelevanceLearningConfiguration = new BinaryRelevanceLearningConfiguration();
      binaryRelevanceLearningConfiguration.initializeDefaultConfiguration();
      return binaryRelevanceLearningConfiguration;
   }


   @Override
   public void init() {
      baseLearningModels = new ArrayList<>();
      baseLearnerDatasets = new ArrayList<>();
   }


   @Override
   public int hashCode() {
      final int prime = 31;
      int result = super.hashCode();
      result = prime * result + ((baseLearnerDatasets == null) ? 0 : baseLearnerDatasets.hashCode());
      result = prime * result + ((baseLearningModels == null) ? 0 : baseLearningModels.hashCode());
      result = prime * result + ((multilabelClassificationDataset == null) ? 0 : multilabelClassificationDataset.hashCode());
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
      BinaryRelevanceLearningAlgorithm other = (BinaryRelevanceLearningAlgorithm) obj;
      if (baseLearnerDatasets == null) {
         if (other.baseLearnerDatasets != null)
            return false;
      } else if (!baseLearnerDatasets.equals(other.baseLearnerDatasets))
         return false;
      if (baseLearningModels == null) {
         if (other.baseLearningModels != null)
            return false;
      } else if (!baseLearningModels.equals(other.baseLearningModels))
         return false;
      if (multilabelClassificationDataset == null) {
         if (other.multilabelClassificationDataset != null)
            return false;
      } else if (!multilabelClassificationDataset.equals(other.multilabelClassificationDataset))
         return false;
      return true;
   }


}
