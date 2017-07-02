package de.upb.cs.is.jpl.api.evaluation.objectranking.suppliedtestset;


import java.util.List;

import de.upb.cs.is.jpl.api.evaluation.EvaluationResult;
import de.upb.cs.is.jpl.api.evaluation.EvaluationSetting;
import de.upb.cs.is.jpl.api.evaluation.IEvaluation;
import de.upb.cs.is.jpl.api.evaluation.objectranking.AObjectRankingEvaluationTest;
import de.upb.cs.is.jpl.api.evaluation.objectranking.ObjectRankingEvaluationTestHelper;
import de.upb.cs.is.jpl.api.evaluation.objectranking.suppliedtestset.ObjectRankingSuppliedTestSetEvaluation;
import de.upb.cs.is.jpl.api.util.datastructure.Pair;


/**
 * This class contains unit test for validating the implementation of the
 * {@link ObjectRankingSuppliedTestSetEvaluation}.
 * 
 * @author Pritha Gupta
 *
 */
public class ObjectRankingSuppliedTestsetEvaluationTest extends AObjectRankingEvaluationTest {

   /**
    * Creates a new unit test for {@link ObjectRankingSuppliedTestsetEvaluationTest}.
    */
   public ObjectRankingSuppliedTestsetEvaluationTest() {
      super();
   }


   @Override
   public IEvaluation getEvaluation() {
      return new ObjectRankingSuppliedTestSetEvaluation();
   }


   @Override
   public List<Pair<EvaluationSetting, EvaluationResult>> getCorrectListOfEvaluationSettings() {
      double[][] lossArraysForAlgorithms = { { 0.1407, 0.192606 }, { 0.1237, 0.1685 } };
      List<Pair<EvaluationSetting, EvaluationResult>> correctEvalutionSettingAndEvaluationResultList = createListOfEvaluationSettingAndCorrectEvaluationResultPairsForLossArrays(
            lossArraysForAlgorithms, ObjectRankingEvaluationTestHelper.OBJECT_RANKING_TRAIN_DATASET,
            ObjectRankingEvaluationTestHelper.OBJECT_RANKING_TEST_DATASET);
      return correctEvalutionSettingAndEvaluationResultList;
   }


   @Override
   public List<Pair<EvaluationSetting, EvaluationResult>> getWrongListOfEvaluationSettings() {
      double[][] lossArraysForAlgorithms = { { 0.0, 0.0 }, { 0.0, 0.0 } };
      List<Pair<EvaluationSetting, EvaluationResult>> correctEvalutionSettingAndEvaluationResultList = createListOfEvaluationSettingAndWrongEvaluationResultPairsForLossArrays(
            lossArraysForAlgorithms, ObjectRankingEvaluationTestHelper.OBJECT_RANKING_TRAIN_DATASET,
            ObjectRankingEvaluationTestHelper.OBJECT_RANKING_TEST_DATASET);
      return correctEvalutionSettingAndEvaluationResultList;
   }

}
