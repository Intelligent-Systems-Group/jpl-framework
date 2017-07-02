package de.upb.cs.is.jpl.api.algorithm.learningalgorithm.labelranking.labelrankingbypairwisecomparison;


import java.util.HashMap;
import java.util.Map;

import de.upb.cs.is.jpl.api.algorithm.AAlgorithmConfiguration;
import de.upb.cs.is.jpl.api.algorithm.baselearner.IBaselearnerAlgorithm;
import de.upb.cs.is.jpl.api.algorithm.baselearner.dataset.BaselearnerDataset;
import de.upb.cs.is.jpl.api.algorithm.learningalgorithm.ALearningAlgorithm;
import de.upb.cs.is.jpl.api.algorithm.learningalgorithm.ELearningAlgorithm;
import de.upb.cs.is.jpl.api.algorithm.learningalgorithm.ILearningModel;
import de.upb.cs.is.jpl.api.dataset.IDataset;
import de.upb.cs.is.jpl.api.dataset.IDatasetParser;
import de.upb.cs.is.jpl.api.dataset.defaultdataset.relative.Ranking;
import de.upb.cs.is.jpl.api.dataset.labelranking.LabelRankingDataset;
import de.upb.cs.is.jpl.api.dataset.labelranking.LabelRankingDatasetParser;
import de.upb.cs.is.jpl.api.exception.algorithm.TrainModelsFailedException;
import de.upb.cs.is.jpl.api.util.CollectionsUtils;
import de.upb.cs.is.jpl.api.util.datastructure.Pair;


/**
 * This and the {@link LabelRankingByPairwiseComparisonLearningModel} class is an implementation of
 * the label ranking by pairwise comparison algorithm (RPC). It is a decomposition of the original
 * label ranking problem into multiple logistic regression models. The ranking for a new instance is
 * predicted according to a score, which is determined for each label with the regression models of
 * label pairs. The labels are ordered according to this score to construct a label ranking.
 * 
 * @author Andreas Kornelsen
 *
 */
public class LabelRankingByPairwiseComparisonLearningAlgorithm extends ALearningAlgorithm<LabelRankingByPairwiseComparisonConfiguration> {

   private static final String ERROR_MESSAGE_NUMBER_OF_FEATRUES = "Not all instances have the same number of features: %d != %d";
   private static final String ERROR_OPERATOR = "There is an unsupported ranking operator in the training dataset.";


   /**
    * Creates a new label ranking by pairwise comparison learning algorithm with the enum
    * identifier.
    */
   public LabelRankingByPairwiseComparisonLearningAlgorithm() {
      super(ELearningAlgorithm.RPC.getIdentifier());
   }


   @Override
   public IDatasetParser getDatasetParser() {
      return new LabelRankingDatasetParser();
   }


   @SuppressWarnings("unchecked")
   @Override
   protected ILearningModel<?> performTraining(IDataset<?, ?, ?> dataset) throws TrainModelsFailedException {
      LabelRankingDataset labelRankingDataset = (LabelRankingDataset) dataset;
      int[] labels = CollectionsUtils.convertIntegerListToArray(labelRankingDataset.getCopyOfLabels());
      int hashMapSize = labels.length * (labels.length - 1) / 2;
      int numberOfFeatues = labelRankingDataset.getFeatureValuesOfAnInstance(0).length;

      Map<Pair<Integer, Integer>, BaselearnerDataset> baselearnerDatasets = getBaselearnerDatasets(labelRankingDataset, labels);
      Map<Pair<Integer, Integer>, ILearningModel<Double>> learningModels = new HashMap<>(hashMapSize);

      IBaselearnerAlgorithm baseLearner = getAlgorithmConfiguration().getBaseLearnerAlgorithm();

      for (Map.Entry<Pair<Integer, Integer>, BaselearnerDataset> entry : baselearnerDatasets.entrySet()) {
         BaselearnerDataset baselearnerDataset = entry.getValue();

         ILearningModel<Double> baseLearnerModelPair = (ILearningModel<Double>) baseLearner.train(baselearnerDataset);
         learningModels.put(entry.getKey(), baseLearnerModelPair);

         validateNumberOfFeatures(numberOfFeatues, baselearnerDataset.getNumberOfFeatures());
      }
      return new LabelRankingByPairwiseComparisonLearningModel(learningModels, labels, numberOfFeatues);
   }


   @Override
   public LabelRankingByPairwiseComparisonLearningModel train(IDataset<?, ?, ?> dataset) throws TrainModelsFailedException {
      return (LabelRankingByPairwiseComparisonLearningModel) super.train(dataset);
   }


   /**
    * Validate number of features of the actual base learner and the dataset.
    *
    * @param datasetNumberOfFeatures the number of features
    * @param actualNumberOfFeatures the actual number of features
    * @throws TrainModelsFailedException if the feature number is not the same
    */
   private void validateNumberOfFeatures(int datasetNumberOfFeatures, int actualNumberOfFeatures) throws TrainModelsFailedException {
      if (datasetNumberOfFeatures != actualNumberOfFeatures) {
         throw new TrainModelsFailedException(
               String.format(ERROR_MESSAGE_NUMBER_OF_FEATRUES, datasetNumberOfFeatures, actualNumberOfFeatures));
      }
   }


   /**
    * Provides l(l-1)/2 {@link BaselearnerDataset}s for the preference learning models.
    *
    * @param labelRankingDataset the label ranking dataset
    * @param labels the labels of the rankings
    * @return the datasets for the rankings
    * @throws TrainModelsFailedException if the validate comparative operators failed
    */
   private Map<Pair<Integer, Integer>, BaselearnerDataset> getBaselearnerDatasets(LabelRankingDataset labelRankingDataset, int[] labels)
         throws TrainModelsFailedException {
      int hashMapSize = labels.length * (labels.length - 1) / 2;
      Map<Pair<Integer, Integer>, BaselearnerDataset> baselearnerDatasets = new HashMap<>(hashMapSize);

      for (int firstLabelIndex = 0; firstLabelIndex < labels.length; firstLabelIndex++) {

         int secondLabelIndex = firstLabelIndex + 1;
         while (secondLabelIndex < labels.length) {

            Pair<Integer, Integer> itemPair = Pair.of(labels[firstLabelIndex], labels[secondLabelIndex]);
            BaselearnerDataset baselearnerDataset = new BaselearnerDataset(labelRankingDataset.getNumberOfInstances(),
                  labelRankingDataset.getFeatureValuesOfAnInstance(0).length);

            for (int i = 0; i < labelRankingDataset.getNumberOfInstances(); i++) {
               double[] contextVector = labelRankingDataset.getFeatureValuesOfAnInstance(i);
               Ranking ranking = labelRankingDataset.getRankingOfInstance(i);
               validateComparativeOperators(ranking.getCompareOperators());
               int[] item = ranking.getOrderingForRanking();

               int firstLabelPreferedToSecondLabel = isItemPrefered(item, itemPair.getFirst(), itemPair.getSecond());
               baselearnerDataset.addFeatureVectorWithResult(contextVector, firstLabelPreferedToSecondLabel);
            }
            baselearnerDatasets.put(itemPair, baselearnerDataset);
            secondLabelIndex++;
         }
      }

      return baselearnerDatasets;

   }


   /**
    * Validates whether the operators are set correctly or not. The Kemeny-Young Algorithm doesn't
    * work for all operators. It works for ordered relation.
    *
    * @param comparativeOperators the comparative operators
    * @throws TrainModelsFailedException if the order is not total
    */
   private void validateComparativeOperators(int[] comparativeOperators) throws TrainModelsFailedException {
      for (Integer comparator : comparativeOperators) {
         if (Ranking.COMPARABLE_ENCODING != comparator) {
            throw new TrainModelsFailedException(ERROR_OPERATOR);
         }
      }
   }


   /**
    * Compares the preference between two provided ranking items.
    * 
    * @param items the label ranking items
    * @param rankItem the first rank item
    * @param compareRankItem the second rank item
    * @return the result for the {@link BaselearnerDataset} of the two rankItems
    */
   private int isItemPrefered(int[] items, int rankItem, int compareRankItem) {
      int indexRankItem = Integer.MAX_VALUE;
      int indexCompareRankItem = Integer.MAX_VALUE;
      for (int i = 0; i < items.length; i++) {
         int item = items[i];
         if (item == rankItem) {
            indexRankItem = i;
         }
         if (item == compareRankItem) {
            indexCompareRankItem = i;
         }
      }

      return indexRankItem < indexCompareRankItem ? 1 : -1;
   }


   @Override
   protected AAlgorithmConfiguration createDefaultAlgorithmConfiguration() {
      return new LabelRankingByPairwiseComparisonConfiguration();
   }


   @Override
   public void init() {
      // There is no configuration required during the init phase.
   }


   @Override
   public boolean equals(Object object) {
      if (this == object)
         return true;
      if (!super.equals(object))
         return false;
      if (object instanceof LabelRankingByPairwiseComparisonLearningAlgorithm) {
         LabelRankingByPairwiseComparisonLearningAlgorithm comparedObject = (LabelRankingByPairwiseComparisonLearningAlgorithm) object;
         return this.configuration.equals(comparedObject.getAlgorithmConfiguration());
      }
      return false;
   }


   @Override
   public int hashCode() {
      return super.hashCode() + 31 * configuration.hashCode();
   }

}
