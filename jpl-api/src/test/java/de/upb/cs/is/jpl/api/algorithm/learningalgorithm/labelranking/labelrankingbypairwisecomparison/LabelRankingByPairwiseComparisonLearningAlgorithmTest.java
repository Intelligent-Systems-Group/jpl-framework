package de.upb.cs.is.jpl.api.algorithm.learningalgorithm.labelranking.labelrankingbypairwisecomparison;


import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import de.upb.cs.is.jpl.api.algorithm.ITrainableAlgorithm;
import de.upb.cs.is.jpl.api.algorithm.learningalgorithm.labelranking.ALabelRankingTest;
import de.upb.cs.is.jpl.api.dataset.IDataset;
import de.upb.cs.is.jpl.api.dataset.defaultdataset.relative.Ranking;
import de.upb.cs.is.jpl.api.dataset.labelranking.LabelRankingDatasetParser;
import de.upb.cs.is.jpl.api.math.RandomGenerator;
import de.upb.cs.is.jpl.api.util.datastructure.NullType;
import de.upb.cs.is.jpl.api.util.datastructure.Pair;


/**
 * 
 * This class contains the unit tests for the algorithm class
 * {@link LabelRankingByPairwiseComparisonLearningAlgorithm}.
 * 
 * 
 * @author Andreas Kornelsen
 *
 */
public class LabelRankingByPairwiseComparisonLearningAlgorithmTest extends ALabelRankingTest {

   private static final String RESOURCE_DIRECTORY_LEVEL = "labelrankingbypairwisecomparison" + File.separator;
   private static final String CORRECT_DATASET_LABEL_RANKING = "cold-txt.gprf";
   private static final String CORRECT_DATASET_LABEL_RANKING_PREDICT = "cold-txt_predict.gprf";


   /**
    * Creates a new unit test for the {@link LabelRankingByPairwiseComparisonLearningAlgorithm}.
    */
   public LabelRankingByPairwiseComparisonLearningAlgorithmTest() {
      super(RESOURCE_DIRECTORY_LEVEL);
   }


   @Override
   public ITrainableAlgorithm getTrainableAlgorithm() {
      return new LabelRankingByPairwiseComparisonLearningAlgorithm();
   }


   @Override
   public List<IDataset<?, ?, ?>> getWrongDatasetList() {
      List<IDataset<?, ?, ?>> wrongDataset = new ArrayList<IDataset<?, ?, ?>>();
      return wrongDataset;
   }


   @SuppressWarnings("unchecked")
   @Override
   public List<IDataset<double[], NullType, Ranking>> getCorrectDatasetList() {
      IDataset<double[], NullType, Ranking> createDatasetOutOfFile = (IDataset<double[], NullType, Ranking>) this
            .createDatasetOutOfFile(new LabelRankingDatasetParser(), getTestRessourcePathFor(CORRECT_DATASET_LABEL_RANKING));
      return Arrays.asList(createDatasetOutOfFile);
   }


   @Override
   public List<Pair<IDataset<double[], NullType, Ranking>, List<Ranking>>> getPredictionsForDatasetList() {
      RandomGenerator.initializeRNG(1234);

      int[] firstPrediction = new int[] { 3, 1, 0, 2 };
      int[] secondPrediction = new int[] { 3, 2, 1, 0 };
      int[] thirdPrediction = new int[] { 3, 2, 0, 1 };
      int[] fourthPrediction = new int[] { 3, 2, 1, 0 };

      int[] compareOperators = new int[firstPrediction.length - 1];
      Arrays.fill(compareOperators, Ranking.COMPARABLE_ENCODING);
      Ranking firstRanking = new Ranking(firstPrediction, compareOperators);
      Ranking secondRanking = new Ranking(secondPrediction, compareOperators);
      Ranking thirdRanking = new Ranking(thirdPrediction, compareOperators);
      Ranking fourthRanking = new Ranking(fourthPrediction, compareOperators);

      List<Ranking> expectedResult = Arrays.asList(firstRanking, secondRanking, thirdRanking, fourthRanking);
      @SuppressWarnings("unchecked")
      IDataset<double[], NullType, Ranking> correctDatasetLabelRankingPredict = (IDataset<double[], NullType, Ranking>) this
            .createDatasetOutOfFile(new LabelRankingDatasetParser(), getTestRessourcePathFor(CORRECT_DATASET_LABEL_RANKING_PREDICT));

      Pair<IDataset<double[], NullType, Ranking>, List<Ranking>> result = Pair.of(correctDatasetLabelRankingPredict, expectedResult);

      return Arrays.asList(result);
   }
}
