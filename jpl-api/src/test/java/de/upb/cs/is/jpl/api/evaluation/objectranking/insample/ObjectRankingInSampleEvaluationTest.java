package de.upb.cs.is.jpl.api.evaluation.objectranking.insample;


import java.util.List;

import de.upb.cs.is.jpl.api.evaluation.EvaluationResult;
import de.upb.cs.is.jpl.api.evaluation.EvaluationSetting;
import de.upb.cs.is.jpl.api.evaluation.IEvaluation;
import de.upb.cs.is.jpl.api.evaluation.objectranking.AObjectRankingEvaluationTest;
import de.upb.cs.is.jpl.api.evaluation.objectranking.ObjectRankingEvaluationTestHelper;
import de.upb.cs.is.jpl.api.util.datastructure.Pair;


/**
 * This class contains unit test for validating the implementation of the
 * {@link ObjectRankingInSampleEvaluation}.
 * 
 * @author Pritha Gupta
 *
 */
public class ObjectRankingInSampleEvaluationTest extends AObjectRankingEvaluationTest {

   /**
    * Creates a new unit test for {@link ObjectRankingInSampleEvaluationTest}.
    */
   public ObjectRankingInSampleEvaluationTest() {
      super();
   }


   @Override
   public IEvaluation getEvaluation() {
      return new ObjectRankingInSampleEvaluation();
   }


   @Override
   public List<Pair<EvaluationSetting, EvaluationResult>> getCorrectListOfEvaluationSettings() {
      double[][] lossArraysForAlgorithms = { { 0.1336, 0.1778 }, { 0.14008, 0.189 } };
      List<Pair<EvaluationSetting, EvaluationResult>> correctEvalutionSettingAndEvaluationResultList = createListOfEvaluationSettingAndCorrectEvaluationResultPairsForLossArrays(
            lossArraysForAlgorithms, ObjectRankingEvaluationTestHelper.OBJECT_RANKING_TRAIN_DATASET);
      return correctEvalutionSettingAndEvaluationResultList;
   }


   @Override
   public List<Pair<EvaluationSetting, EvaluationResult>> getWrongListOfEvaluationSettings() {
      double[][] lossArraysForAlgorithms = { { 0.0, 0.0 }, { 0.0, 0.0 } };
      List<Pair<EvaluationSetting, EvaluationResult>> correctEvalutionSettingAndEvaluationResultList = createListOfEvaluationSettingAndWrongEvaluationResultPairsForLossArrays(
            lossArraysForAlgorithms, ObjectRankingEvaluationTestHelper.OBJECT_RANKING_TRAIN_DATASET);
      return correctEvalutionSettingAndEvaluationResultList;
   }

}
