package de.upb.cs.is.jpl.api.algorithm.learningalgorithm.rankaggregation.kemenyyoung;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.upb.cs.is.jpl.api.algorithm.learningalgorithm.rankaggregation.ARankAggregationLearningModel;
import de.upb.cs.is.jpl.api.dataset.IDataset;
import de.upb.cs.is.jpl.api.dataset.IInstance;
import de.upb.cs.is.jpl.api.dataset.defaultdataset.relative.Ranking;
import de.upb.cs.is.jpl.api.dataset.rankaggregation.RankAggregationDataset;
import de.upb.cs.is.jpl.api.dataset.rankaggregation.RankAggregationInstance;
import de.upb.cs.is.jpl.api.exception.algorithm.PredictionFailedException;
import de.upb.cs.is.jpl.api.math.util.TwoDMatrixUtils;
import de.upb.cs.is.jpl.api.util.CollectionsUtils;
import de.upb.cs.is.jpl.api.util.PermuatorCombinator;


/**
 * The model class is created by Kemeny-Young algorithm after the training on the dataset.
 * 
 * @author Pritha Gupta
 *
 */
public class KemenyYoungLearningModel extends ARankAggregationLearningModel {


   private static final Logger logger = LoggerFactory.getLogger(KemenyYoungLearningModel.class);
   private static final String PRINT_KEMENY_SCORE_MATRIX = "Optimized Score Matrix: %s";
   private static final String PRINT_PREDECTION_TIME = "Time taken to build the model: %s";
   private static final String PRINT_FINAL_AGGREGATED_RANK = "Final Aggregated Rank: %s";
   private static final String PRINT_MAXIMUM_SCORE = "Maximum Score is: %d";
   private TwoDMatrixUtils kemenyYoungPairwiseScoreMatrix;


   /**
    * Creates new {@link KemenyYoungLearningModel} with the pairwise 2-D matrix of scores for each
    * pair of ranking and list of the labels to be ranked.
    * 
    * @param kemenyYoungPairwiseScoreMatrix the pairwise score matrix
    * @param listOfLabels the list of labels
    */
   public KemenyYoungLearningModel(TwoDMatrixUtils kemenyYoungPairwiseScoreMatrix, List<Integer> listOfLabels) {
      super(listOfLabels);
      this.kemenyYoungPairwiseScoreMatrix = kemenyYoungPairwiseScoreMatrix;
   }


   @Override
   public List<Ranking> predict(IDataset<?, ?, ?> dataset) throws PredictionFailedException {
      assertDatasetHasCorrectType(dataset, RankAggregationDataset.class);
      assetDatasetCompatibility(dataset);
      List<Ranking> result = new ArrayList<>();
      result.add(predictRanking());
      return result;
   }


   @Override
   public Ranking predict(IInstance<?, ?, ?> instance) throws PredictionFailedException {
      assertInstanceHasCorrectType(instance, RankAggregationInstance.class);
      assertInstanceCompatibility(instance);
      return predictRanking();
   }


   /**
    * The rank aggregation of labels. The ranking is constructed according to the pair wise score
    * matrix of the labels.
    *
    * @return the aggregated ranking based on the score of the labels
    * @throws PredictionFailedException if the aggregated ranking cannot be predicted
    */
   private Ranking predictRanking() throws PredictionFailedException {
      long time = System.currentTimeMillis();
      int numOflabels = listOfLabels.size();
      buildGraph();
      int[] aggregatedRank;
      aggregatedRank = getExhaustiveSearchResult(numOflabels);
      int[] comparators = Ranking.createCompareOperatorArrayForLabels(CollectionsUtils.convertIntegerListToArray(listOfLabels));
      Ranking finalRanking  = new Ranking(aggregatedRank, comparators);

      logger.debug(String.format(PRINT_KEMENY_SCORE_MATRIX, kemenyYoungPairwiseScoreMatrix.toString()));
      logger.debug(String.format(PRINT_PREDECTION_TIME, String.valueOf(System.currentTimeMillis() - time)));
      logger.debug(String.format(PRINT_FINAL_AGGREGATED_RANK, finalRanking.toString()));
      return finalRanking;

   }


   /**
    * This method checks if the integer array is empty or newly initialized.
    * 
    * @param array the integer array to be checked
    * @return {@code true} if array contains just zero values, else {@code false}
    */
   public static boolean isArrayEmpty(int[] array) {
      for (int item : array) {
         if (item != 0)
            return false;
      }
      return true;
   }


   /**
    * This function generates the aggregated ranking using the exhaustive search technique.
    * 
    * @return the aggregated ranking
    */
   public int[] getExhaustiveSearchResult(int numOflabels) {
      int[] aggregatedRank = new int[listOfLabels.size()];
      if (numOflabels < 6) {
         List<Integer[]> permutations = PermuatorCombinator.getPermutationsnPr(listOfLabels, 0);
         int maximumScore = Integer.MIN_VALUE;
         int score;
         for (Integer[] order : permutations) {
            score = 0;
            for (int i = 0; i < order.length; i++) {
               for (int j = i; j < order.length; j++) {
                  score += kemenyYoungPairwiseScoreMatrix.get(order[i], order[j]);
               }
            }
            if (score > maximumScore) {
               aggregatedRank = CollectionsUtils.convertIntegerListToArray(Arrays.asList(order));
               maximumScore = score;
            }
         }
         logger.debug(String.format(PRINT_MAXIMUM_SCORE, maximumScore));
      } else {
         Map<Integer, Double> scores = new HashMap<>();
         double score;
         for (int i = 0; i < numOflabels; i++) {
            score = 0.0;
            scores.put(i, score);
            for (int j = 0; j < numOflabels; j++) {
               score += kemenyYoungPairwiseScoreMatrix.get(i, j);
            }
            scores.put(i, score);
         }
         aggregatedRank = CollectionsUtils.getSortedKeyValuesInDecreasingValueOrder(scores);
      }
      return aggregatedRank;

   }


   /**
    * The function takes the Kemeny-Young score matrix and optimizes it.
    */
   public void buildGraph() {
      List<Integer[]> pairs = PermuatorCombinator.getCombinationsnCr(listOfLabels, 2);
      for (Integer[] pair : pairs) {
         int i = pair[0];
         int j = pair[1];
         int rowiColj = kemenyYoungPairwiseScoreMatrix.get(i, j);
         int rowjColi = kemenyYoungPairwiseScoreMatrix.get(j, i);
         if (rowiColj > rowjColi) {
            kemenyYoungPairwiseScoreMatrix.set(i, j, rowiColj - rowjColi);
            kemenyYoungPairwiseScoreMatrix.set(j, i, 0);
         } else {
            kemenyYoungPairwiseScoreMatrix.set(j, i, rowjColi - rowiColj);
            kemenyYoungPairwiseScoreMatrix.set(i, j, 0);
         }
      }
   }


   /**
    * Returns the pairwise score matrix of the {@link KemenyYoungLearningAlgorithm} learning model.
    * 
    * @return the pairwise score matrix for learning model
    */
   public TwoDMatrixUtils getKemenyYoungPairwiseScoreMatrix() {
      return kemenyYoungPairwiseScoreMatrix;
   }


   @Override
   public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result + ((kemenyYoungPairwiseScoreMatrix == null) ? 0 : kemenyYoungPairwiseScoreMatrix.hashCode());
      result = prime * result + ((listOfLabels == null) ? 0 : listOfLabels.hashCode());
      return result;
   }


   @Override
   public boolean equals(Object obj) {
      if (this == obj)
         return true;
      if (!super.equals(obj))
         return false;
      if (!(obj instanceof KemenyYoungLearningModel))
         return false;
      KemenyYoungLearningModel other = (KemenyYoungLearningModel) obj;
      if (kemenyYoungPairwiseScoreMatrix == null) {
         if (other.kemenyYoungPairwiseScoreMatrix != null)
            return false;
      } else if (!kemenyYoungPairwiseScoreMatrix.equals(other.kemenyYoungPairwiseScoreMatrix))
         return false;
      if (listOfLabels == null) {
         if (other.listOfLabels != null)
            return false;
      } else if (!listOfLabels.equals(other.listOfLabels))
         return false;
      return true;
   }


}
