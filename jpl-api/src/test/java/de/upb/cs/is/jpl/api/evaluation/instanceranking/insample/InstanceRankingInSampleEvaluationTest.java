package de.upb.cs.is.jpl.api.evaluation.instanceranking.insample;


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
 * {@link InstanceRankingInSampleEvaluation}.
 * 
 * @author Sebastian Gottschalk
 */
public class InstanceRankingInSampleEvaluationTest extends AEvaluationTest {
   private static final String RESOURCE_DIRECTORY_LEVEL = "instanceranking" + File.separator;


   /**
    * Creates a new unit test for the {@link InstanceRankingInSampleEvaluation}.
    */
   public InstanceRankingInSampleEvaluationTest() {
      super(RESOURCE_DIRECTORY_LEVEL);
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
      double[][] lossArraysForAlgorithms = { { 1.0648, 1.9351 }, { 2.1290, 5.6912 } };
      return createListOfEvaluationSettingAndCorrectEvaluationResultPairsForLossArrays(lossArraysForAlgorithms,
            InstanceRankingEvaluationTestHelper.DATASET_INSTANCE_RANKING);
   }


   @Override
   public List<Pair<EvaluationSetting, EvaluationResult>> getWrongListOfEvaluationSettings() {
      double[][] lossArraysForAlgorithms = { { 2.0648, 4.9351 }, { 2.0648, 4.9351 } };
      return createListOfEvaluationSettingAndWrongEvaluationResultPairsForLossArrays(lossArraysForAlgorithms,
            InstanceRankingEvaluationTestHelper.DATASET_INSTANCE_RANKING);

   }


   @Override
   public IEvaluation getEvaluation() {
      return new InstanceRankingInSampleEvaluation();
   }
}
