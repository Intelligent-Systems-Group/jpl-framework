package de.upb.cs.is.jpl.api.evaluation.rankagrregation.insampleevaluation;


import java.util.List;

import de.upb.cs.is.jpl.api.evaluation.EvaluationResult;
import de.upb.cs.is.jpl.api.evaluation.EvaluationSetting;
import de.upb.cs.is.jpl.api.evaluation.IEvaluation;
import de.upb.cs.is.jpl.api.evaluation.rankaggregation.insample.RankAggregationInSampleEvaluation;
import de.upb.cs.is.jpl.api.evaluation.rankagrregation.ARankAggregationEvaluationTest;
import de.upb.cs.is.jpl.api.util.datastructure.Pair;


/**
 * This class contains test for validating the implementation of the
 * {@link RankAggregationInSampleEvaluation}.
 * 
 * @author Pritha Gupta
 *
 */
public class RankAggregationInSampleEvaluationTest extends ARankAggregationEvaluationTest {


   /**
    * Creates a new test for the {@link RankAggregationInSampleEvaluationTest}.
    */
   public RankAggregationInSampleEvaluationTest() {
      super();
   }


   @Override
   public IEvaluation getEvaluation() {
      return new RankAggregationInSampleEvaluation();
   }


   @Override
   public List<Pair<EvaluationSetting, EvaluationResult>> getCorrectListOfEvaluationSettings() {
      double[][] lossArraysForAlgorithms = { { 0.92, 0.96 }, { 0.92, 0.97 } };
      List<Pair<EvaluationSetting, EvaluationResult>> correctEvalutionSettingAndEvaluationResultList = createListOfEvaluationSettingAndCorrectEvaluationResultPairsForLossArrays(
            lossArraysForAlgorithms, RANK_AGGREGATION_TRAIN_DATASET);
      return correctEvalutionSettingAndEvaluationResultList;
   }


   @Override
   public List<Pair<EvaluationSetting, EvaluationResult>> getWrongListOfEvaluationSettings() {
      double[][] lossArraysForAlgorithms = { { 0.0, 0.0 }, { 0.0, 0.0 } };
      List<Pair<EvaluationSetting, EvaluationResult>> correctEvalutionSettingAndEvaluationResultList = createListOfEvaluationSettingAndWrongEvaluationResultPairsForLossArrays(
            lossArraysForAlgorithms, RANK_AGGREGATION_TRAIN_DATASET);
      return correctEvalutionSettingAndEvaluationResultList;
   }
}
