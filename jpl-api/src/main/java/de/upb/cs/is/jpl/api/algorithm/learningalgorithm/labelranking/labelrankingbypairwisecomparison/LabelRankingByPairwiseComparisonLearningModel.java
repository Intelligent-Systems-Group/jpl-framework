package de.upb.cs.is.jpl.api.algorithm.learningalgorithm.labelranking.labelrankingbypairwisecomparison;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.upb.cs.is.jpl.api.algorithm.baselearner.dataset.BaselearnerInstance;
import de.upb.cs.is.jpl.api.algorithm.learningalgorithm.ILearningModel;
import de.upb.cs.is.jpl.api.algorithm.learningalgorithm.labelranking.ALabelRankingLearningModel;
import de.upb.cs.is.jpl.api.dataset.IDataset;
import de.upb.cs.is.jpl.api.dataset.IInstance;
import de.upb.cs.is.jpl.api.dataset.defaultdataset.relative.Ranking;
import de.upb.cs.is.jpl.api.dataset.labelranking.LabelRankingDataset;
import de.upb.cs.is.jpl.api.dataset.labelranking.LabelRankingInstance;
import de.upb.cs.is.jpl.api.exception.algorithm.PredictionFailedException;
import de.upb.cs.is.jpl.api.exception.algorithm.TrainModelsFailedException;
import de.upb.cs.is.jpl.api.util.CollectionsUtils;
import de.upb.cs.is.jpl.api.util.datastructure.Pair;


/**
 * The learning model is created for {@link LabelRankingByPairwiseComparisonLearningAlgorithm} after
 * training it on the {@link LabelRankingDataset}.
 * 
 * @author Andreas Kornelsen
 *
 */
public class LabelRankingByPairwiseComparisonLearningModel extends ALabelRankingLearningModel {

   private static final String ERROR_MESSAGE_NUMBER_OF_FEATRUES = "The instances doesn't have all the same number of features: %d != %d";
   private Map<Pair<Integer, Integer>, ILearningModel<Double>> learningModels;
   private int[] labels;
   private int hashMapSize;


   /**
    * Creates a new model for label ranking by pairwise comparison algorithm with list of learning
    * models for each label pair and the array of labels.
    *
    * @param learningModels the learning models for the label ranking prediction
    * @param labels the labels for ranking
    * @param numberOfFeaturesDuringTheTrain the number of features during the prediction
    */
   public LabelRankingByPairwiseComparisonLearningModel(Map<Pair<Integer, Integer>, ILearningModel<Double>> learningModels, int[] labels,
         int numberOfFeaturesDuringTheTrain) {
      super(numberOfFeaturesDuringTheTrain);
      this.learningModels = learningModels;
      this.labels = labels;
      this.hashMapSize = labels.length * (labels.length - 1) / 2;
   }


   @Override
   public List<Ranking> predict(IDataset<?, ?, ?> dataset) throws PredictionFailedException {
      assertDatasetHasCorrectType(dataset, LabelRankingDataset.class);

      LabelRankingDataset labelRankingDataset = (LabelRankingDataset) dataset;
      List<Ranking> listOfRankings = new ArrayList<>(labelRankingDataset.getNumberOfInstances());

      for (int instanceIndex = 0; instanceIndex < labelRankingDataset.getNumberOfInstances(); instanceIndex++) {
         double[] contextVector = labelRankingDataset.getFeatureValuesOfAnInstance(instanceIndex);
         validateNumberOfFeatures(numberOfContextFeaturesTrainedOn, contextVector.length);
         BaselearnerInstance baselearnerInstance = new BaselearnerInstance(contextVector);
         Ranking rankingForBaseLearnerInstance = predictRankingForBaseLearnerInstance(baselearnerInstance);
         listOfRankings.add(rankingForBaseLearnerInstance);
      }

      return listOfRankings;
   }


   @Override
   public Ranking predict(IInstance<?, ?, ?> instance) throws PredictionFailedException {
      assertInstanceHasCorrectType(instance, LabelRankingInstance.class);

      double[] contextFeatureVector = (double[]) instance.getContextFeatureVector();
      validateNumberOfFeatures(numberOfContextFeaturesTrainedOn, contextFeatureVector.length);
      BaselearnerInstance baselearnerInstance = new BaselearnerInstance(contextFeatureVector);
      return predictRankingForBaseLearnerInstance(baselearnerInstance);
   }


   /**
    * Validate number of features of the actual base learner instance and the number of features
    * during the training.
    *
    * @param datasetNumberOfFeatures the number of features
    * @param actualNumberOfFeatures the actual number of features
    * @throws TrainModelsFailedException if the feature number is not the same
    */
   private void validateNumberOfFeatures(int datasetNumberOfFeatures, int actualNumberOfFeatures) throws PredictionFailedException {
      if (datasetNumberOfFeatures != actualNumberOfFeatures) {
         throw new PredictionFailedException(ERROR_MESSAGE_NUMBER_OF_FEATRUES);
      }
   }


   /**
    * Predict ranking for a base learner instance by determining the score for each label and order
    * the labels according to this score.
    *
    * @param baselearnerInstance the baselearner instance
    * @return the prediction for on instance
    * @throws PredictionFailedException if the prediction of the base learner fails
    */
   private Ranking predictRankingForBaseLearnerInstance(BaselearnerInstance baselearnerInstance) throws PredictionFailedException {

      Map<Pair<Integer, Integer>, Double> rating = new HashMap<>(hashMapSize);

      for (Map.Entry<Pair<Integer, Integer>, ILearningModel<Double>> entry : learningModels.entrySet()) {
         double predict = entry.getValue().predict(baselearnerInstance);
         rating.put(entry.getKey(), predict);
      }

      Map<Integer, Double> labelScores = getLabelScores(rating);

      return getRanking(labelScores);
   }


   /**
    * Returns the ranking according to the score of the labels.
    *
    * @param labelScores the scores of the labels
    * @return the ranking
    */
   private Ranking getRanking(Map<Integer, Double> labelScores) {

      int[] permutationOfRanking = CollectionsUtils.getSortedKeyValuesInDecreasingValueOrder(labelScores);
      // from order to rank
      int[] labelRanking = new int[permutationOfRanking.length];
      for (int i = 0; i < permutationOfRanking.length; i++) {
         labelRanking[permutationOfRanking[i]] = i;
      }

      return new Ranking(labelRanking, Ranking.createCompareOperatorArrayForLabels(labelRanking));
   }


   /**
    * Calculate the score for each label. The score is calculated by the sum of all pairs where the
    * label occurs.
    * 
    * @param rating the score for each label pair, number of label pairs = m(m - 1) / 2
    * @return the score for each label
    */
   private Map<Integer, Double> getLabelScores(Map<Pair<Integer, Integer>, Double> rating) {

      double[] scoreList = new double[labels.length];

      for (int i = 0; i < labels.length; i++) {
         int firstLabel = labels[i];

         for (int j = 0; j < labels.length; j++) {
            int secondLabel = labels[j];

            if (i < j) {
               Pair<Integer, Integer> key = Pair.of(firstLabel, secondLabel);
               scoreList[i] += rating.get(key);
            } else if (i > j) {
               Pair<Integer, Integer> key = Pair.of(secondLabel, firstLabel);
               scoreList[i] += 1 - rating.get(key);
            }
         }
      }

      Map<Integer, Double> scores = new HashMap<>(scoreList.length);
      for (int i = 0; i < scoreList.length; i++) {
         scores.put(i, scoreList[i]);
      }

      return scores;
   }


   @Override
   public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result + Arrays.hashCode(labels);
      result = prime * result + ((learningModels == null) ? 0 : learningModels.hashCode());
      result = prime * result + numberOfContextFeaturesTrainedOn;
      return result;
   }


   @Override
   public boolean equals(Object obj) {
      if (this == obj)
         return true;
      if (!super.equals(obj))
         return false;
      if (!(obj instanceof LabelRankingByPairwiseComparisonLearningModel))
         return false;
      LabelRankingByPairwiseComparisonLearningModel other = (LabelRankingByPairwiseComparisonLearningModel) obj;
      if (!Arrays.equals(labels, other.labels))
         return false;
      if (learningModels == null) {
         if (other.learningModels != null)
            return false;
      } else if (!learningModels.equals(other.learningModels))
         return false;
      if (numberOfContextFeaturesTrainedOn != other.numberOfContextFeaturesTrainedOn)
         return false;
      return true;
   }

}
