package de.upb.cs.is.jpl.api.evaluation.instanceranking.suppliedtestset;


import java.io.File;
import java.util.List;

import de.upb.cs.is.jpl.api.algorithm.learningalgorithm.ILearningAlgorithm;
import de.upb.cs.is.jpl.api.evaluation.AEvaluationTest;
import de.upb.cs.is.jpl.api.evaluation.EvaluationResult;
import de.upb.cs.is.jpl.api.evaluation.EvaluationSetting;
import de.upb.cs.is.jpl.api.evaluation.IEvaluation;
import de.upb.cs.is.jpl.api.evaluation.instanceranking.InstanceRankingEvaluationTestHelper;
import de.upb.cs.is.jpl.api.util.datastructure.Pair;


/**
 * This class contains unit test for validating the implementation of the
 * {@link InstanceRankingSuppliedTestsetEvaluation}.
 * 
 * @author Sebastian Gottschalk
 */
public class InstanceRankingSuppliedTestSetEvaluationTest extends AEvaluationTest {
   private static final String RESOURCE_DIRECTORY_LEVEL = "instanceranking" + File.separator;
   private static final String SUPPLIED_TEST_DATASET = "suppliedinstancemovielens.gprf";


   /**
    * Creates a new unit test for the {@link InstanceRankingSuppliedTestsetEvaluation}.
    */
   public InstanceRankingSuppliedTestSetEvaluationTest() {
      super(RESOURCE_DIRECTORY_LEVEL);
   }


   @Override
   public IEvaluation getEvaluation() {
      return new InstanceRankingSuppliedTestsetEvaluation();
   }


   @Override
   public List<ILearningAlgorithm> getLearningAlgorithms() {
      return InstanceRankingEvaluationTestHelper.getLearningAlgorithms();
   }


   @Override
   public void setEvaluationMetrics() {
      evaluationMetrics = InstanceRankingEvaluationTestHelper.getEvaluationMetrics();

   }


   @Override
   public List<Pair<EvaluationSetting, EvaluationResult>> getCorrectListOfEvaluationSettings() {
      double[][] lossArraysForAlgorithms = { { 0.8801, 1.4516 }, { 1.8315, 4.5213 } };
      return createListOfEvaluationSettingAndCorrectEvaluationResultPairsForLossArrays(lossArraysForAlgorithms,
            InstanceRankingEvaluationTestHelper.DATASET_INSTANCE_RANKING, SUPPLIED_TEST_DATASET);
   }


   @Override
   public List<Pair<EvaluationSetting, EvaluationResult>> getWrongListOfEvaluationSettings() {
      double[][] lossArraysForAlgorithms = { { 2.0648, 4.9351 }, { 2.0648, 4.9351 } };
      return createListOfEvaluationSettingAndWrongEvaluationResultPairsForLossArrays(lossArraysForAlgorithms,
            InstanceRankingEvaluationTestHelper.DATASET_INSTANCE_RANKING, SUPPLIED_TEST_DATASET);

   }
}
