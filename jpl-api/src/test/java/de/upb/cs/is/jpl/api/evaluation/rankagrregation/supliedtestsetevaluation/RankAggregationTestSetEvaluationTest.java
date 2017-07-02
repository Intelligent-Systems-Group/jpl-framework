package de.upb.cs.is.jpl.api.evaluation.rankagrregation.supliedtestsetevaluation;


import java.util.List;

import de.upb.cs.is.jpl.api.evaluation.EvaluationResult;
import de.upb.cs.is.jpl.api.evaluation.EvaluationSetting;
import de.upb.cs.is.jpl.api.evaluation.IEvaluation;
import de.upb.cs.is.jpl.api.evaluation.rankaggregation.suppliedtestset.RankAggregationSuppliedTestSetEvaluation;
import de.upb.cs.is.jpl.api.evaluation.rankagrregation.ARankAggregationEvaluationTest;
import de.upb.cs.is.jpl.api.util.datastructure.Pair;


/**
 * This class contains test for validating the implementation of the
 * {@link RankAggregationSuppliedTestSetEvaluation}.
 * 
 * @author Pritha Gupta
 *
 */
public class RankAggregationTestSetEvaluationTest extends ARankAggregationEvaluationTest {


   /**
    * Creates a new test for the {@link RankAggregationTestSetEvaluationTest}.
    */
   public RankAggregationTestSetEvaluationTest() {
      super();
   }


   @Override
   public IEvaluation getEvaluation() {
      return new RankAggregationSuppliedTestSetEvaluation();
   }


   @Override
   public List<Pair<EvaluationSetting, EvaluationResult>> getCorrectListOfEvaluationSettings() {
      double[][] lossArraysForAlgorithms = { { 0.99, 0.9706 }, { 0.99, 0.9706 } };
      List<Pair<EvaluationSetting, EvaluationResult>> correctEvalutionSettingAndEvaluationResultList = createListOfEvaluationSettingAndCorrectEvaluationResultPairsForLossArrays(
            lossArraysForAlgorithms, RANK_AGGREGATION_TRAIN_DATASET, RANK_AGGREGATION_TEST_DATASET);
      return correctEvalutionSettingAndEvaluationResultList;
   }


   @Override
   public List<Pair<EvaluationSetting, EvaluationResult>> getWrongListOfEvaluationSettings() {
      double[][] lossArraysForAlgorithms = { { 0.0, 0.0 }, { 0.0, 0.0 } };
      List<Pair<EvaluationSetting, EvaluationResult>> correctEvalutionSettingAndEvaluationResultList = createListOfEvaluationSettingAndWrongEvaluationResultPairsForLossArrays(
            lossArraysForAlgorithms, RANK_AGGREGATION_TRAIN_DATASET, RANK_AGGREGATION_TEST_DATASET);
      return correctEvalutionSettingAndEvaluationResultList;
   }


}
