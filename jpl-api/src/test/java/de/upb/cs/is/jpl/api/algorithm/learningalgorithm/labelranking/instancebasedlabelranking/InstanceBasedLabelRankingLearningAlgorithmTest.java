package de.upb.cs.is.jpl.api.algorithm.learningalgorithm.labelranking.instancebasedlabelranking;


import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import de.upb.cs.is.jpl.api.algorithm.ITrainableAlgorithm;
import de.upb.cs.is.jpl.api.algorithm.learningalgorithm.labelranking.ALabelRankingTest;
import de.upb.cs.is.jpl.api.algorithm.learningalgorithm.labelranking.labelrankingbypairwisecomparison.LabelRankingByPairwiseComparisonLearningAlgorithm;
import de.upb.cs.is.jpl.api.dataset.IDataset;
import de.upb.cs.is.jpl.api.dataset.defaultdataset.relative.Ranking;
import de.upb.cs.is.jpl.api.dataset.labelranking.LabelRankingDataset;
import de.upb.cs.is.jpl.api.math.RandomGenerator;
import de.upb.cs.is.jpl.api.util.datastructure.NullType;
import de.upb.cs.is.jpl.api.util.datastructure.Pair;


/**
 * 
 * This class contains the unit tests for the algorithm class
 * {@link InstanceBasedLabelRankingLearningAlgorithm}.
 * 
 * 
 * @author Andreas Kornelsen
 *
 */
public class InstanceBasedLabelRankingLearningAlgorithmTest extends ALabelRankingTest {

   private static final String RESOURCE_DIRECTORY_LEVEL = "instancebasedlabelranking" + File.separator;


   /**
    * Creates a new unit test for the {@link LabelRankingByPairwiseComparisonLearningAlgorithm}.
    */
   public InstanceBasedLabelRankingLearningAlgorithmTest() {
      super(RESOURCE_DIRECTORY_LEVEL);
   }


   @Override
   public ITrainableAlgorithm getTrainableAlgorithm() {
      return new InstanceBasedLabelRankingLearningAlgorithm();
   }


   @Override
   public List<IDataset<?, ?, ?>> getWrongDatasetList() {
      return new ArrayList<>();
   }


   @Override
   public List<IDataset<double[], NullType, Ranking>> getCorrectDatasetList() {

      LabelRankingDataset labelRankingDataset = getLabelRankingDataset();

      return Arrays.asList(labelRankingDataset);
   }


   /**
    * Returns a simple rank aggregation dataset.
    *
    * @return the label ranking dataset
    */
   private LabelRankingDataset getLabelRankingDataset() {

      List<Integer> labels = Arrays.asList(0, 1, 2, 3);

      List<Ranking> rankings = new ArrayList<>();
      rankings.add(new Ranking(new int[] { 1, 0, 2, 3 }, Ranking.createCompareOperatorArrayForLabels(new int[] { 3, 3, 3, 3 })));
      rankings.add(new Ranking(new int[] { 0, 1, 2, 3 }, Ranking.createCompareOperatorArrayForLabels(new int[] { 3, 3, 3, 3 })));
      rankings.add(new Ranking(new int[] { 0, 1, 2, 3 }, Ranking.createCompareOperatorArrayForLabels(new int[] { 3, 3, 3, 3 })));
      rankings.add(new Ranking(new int[] { 0, 1, 2, 3 }, Ranking.createCompareOperatorArrayForLabels(new int[] { 3, 3, 3, 3 })));

      List<double[]> features = new ArrayList<>();
      features.add(new double[] { -0.933919, 2.151642, -0.371715, -0.245385, 0.208949, 0.068299, 0.671798 });
      features.add(new double[] { -0.933919, 2.151642, -0.371715, -0.245385, 0.208949, 0.068299, 0.671798 });
      features.add(new double[] { -0.933919, 2.151642, -0.371715, -0.245385, 0.208949, 0.068299, 0.671798 });
      features.add(new double[] { -0.933919, 2.151642, -0.371715, -0.245385, 0.208949, 0.068299, 0.671798 });

      return new LabelRankingDataset(labels, features, rankings);
   }


   @Override
   public List<Pair<IDataset<double[], NullType, Ranking>, List<Ranking>>> getPredictionsForDatasetList() {
      RandomGenerator.initializeRNG(1234);

      int[] firstPrediction = new int[] { 0, 1, 2, 3 };
      int[] secondPrediction = new int[] { 0, 1, 2, 3 };
      int[] thirdPrediction = new int[] { 0, 1, 2, 3 };
      int[] fourthPrediction = new int[] { 0, 1, 2, 3 };


      int[] compareOperators = new int[firstPrediction.length - 1];
      Arrays.fill(compareOperators, Ranking.COMPARABLE_ENCODING);
      Ranking firstRanking = new Ranking(firstPrediction, compareOperators);
      Ranking secondRanking = new Ranking(secondPrediction, compareOperators);
      Ranking thirdRanking = new Ranking(thirdPrediction, compareOperators);
      Ranking fourthRanking = new Ranking(fourthPrediction, compareOperators);

      List<Ranking> expectedResult = Arrays.asList(firstRanking, secondRanking, thirdRanking, fourthRanking);

      Pair<IDataset<double[], NullType, Ranking>, List<Ranking>> result = Pair.of(getLabelRankingDataset(), expectedResult);

      return Arrays.asList(result);
   }

}
