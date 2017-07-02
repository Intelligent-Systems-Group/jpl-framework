package de.upb.cs.is.jpl.api.evaluation.labelranking.insample;


import java.util.List;

import de.upb.cs.is.jpl.api.evaluation.EvaluationResult;
import de.upb.cs.is.jpl.api.evaluation.EvaluationSetting;
import de.upb.cs.is.jpl.api.evaluation.IEvaluation;
import de.upb.cs.is.jpl.api.evaluation.labelranking.ALabelRankingEvaluationTest;
import de.upb.cs.is.jpl.api.util.datastructure.Pair;


/**
 * This class contains unit test for validating the implementation of the
 * {@link LabelRankingInSampleEvaluation}.
 * 
 * @author Andreas Kornelsen
 *
 */
public class LabelRankingInSampleEvaluationTest extends ALabelRankingEvaluationTest {

   private static final String DATASET_LABEL_RANKING = "vehicle_dense.gprf";


   /**
    * Creates a new unit test for {@link LabelRankingInSampleEvaluationTest}.
    */
   public LabelRankingInSampleEvaluationTest() {
      super();
   }


   @Override
   public IEvaluation getEvaluation() {
      return new LabelRankingInSampleEvaluation();
   }


   @Override
   public List<Pair<EvaluationSetting, EvaluationResult>> getCorrectListOfEvaluationSettings() {
      double[][] lossArraysForAlgorithms = { { 0.25884712, 0.233167 }, { 0.793538, 0.8382978 } };
      List<Pair<EvaluationSetting, EvaluationResult>> correctEvalutionSettingAndEvaluationResultList = createListOfEvaluationSettingAndCorrectEvaluationResultPairsForLossArrays(
            lossArraysForAlgorithms, DATASET_LABEL_RANKING);
      return correctEvalutionSettingAndEvaluationResultList;
   }


   @Override
   public List<Pair<EvaluationSetting, EvaluationResult>> getWrongListOfEvaluationSettings() {
      double[][] lossArraysForAlgorithms = { { 0.0, 0.0 }, { 0.0, 0.0 } };
      List<Pair<EvaluationSetting, EvaluationResult>> correctEvalutionSettingAndEvaluationResultList = createListOfEvaluationSettingAndWrongEvaluationResultPairsForLossArrays(
            lossArraysForAlgorithms, DATASET_LABEL_RANKING);
      return correctEvalutionSettingAndEvaluationResultList;
   }
}
