package de.upb.cs.is.jpl.api.evaluation.instanceranking.crossvalidation;


import java.io.File;
import java.util.List;

import de.upb.cs.is.jpl.api.algorithm.learningalgorithm.ILearningAlgorithm;
import de.upb.cs.is.jpl.api.evaluation.ACrossValidationEvaluationTest;
import de.upb.cs.is.jpl.api.evaluation.EvaluationResult;
import de.upb.cs.is.jpl.api.evaluation.EvaluationSetting;
import de.upb.cs.is.jpl.api.evaluation.IEvaluation;
import de.upb.cs.is.jpl.api.evaluation.instanceranking.InstanceRankingEvaluationTestHelper;
import de.upb.cs.is.jpl.api.util.datastructure.Pair;


/**
 * This class contains unit test for validating the implementation of the
 * {@link InstanceRankingCrossValidationEvaluation}.
 * 
 * @author Sebastian Gottschalk
 */
public class InstanceRankingCrossValidationEvaluationTest extends ACrossValidationEvaluationTest {

   private static final String RESOURCE_DIRECTORY_LEVEL = "instanceranking" + File.separator;


   /**
    * Creates a new unit test for the {@link InstanceRankingCrossValidationEvaluation}.
    */
   public InstanceRankingCrossValidationEvaluationTest() {
      super(RESOURCE_DIRECTORY_LEVEL);
   }


   @Override
   public IEvaluation getEvaluation() {
      return new InstanceRankingCrossValidationEvaluation();
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
   public List<Pair<Pair<Integer, List<EvaluationSetting>>, EvaluationResult>> getListEvaluationSettingsSetWithCorrectEvaluationResult() {
      double[][] metricResults = { { 0.8933, 1.4199 }, { 2.1495, 5.8007 } };
      return getCorrectListOfEvaluationSettingsForOneSetForMetricResults(metricResults,
            InstanceRankingEvaluationTestHelper.DATASET_INSTANCE_RANKING);
   }


   @Override
   public List<Pair<Pair<Integer, List<EvaluationSetting>>, EvaluationResult>> getListEvaluationSettingsSetWithWrongEvaluationResult() {
      double[][] metricResults = { { 4.9230, 7.4923 }, { 4.9230, 7.4923 } };
      return getWrongListOfEvaluationSettingsForOneSetForMetricResults(metricResults,
            InstanceRankingEvaluationTestHelper.DATASET_INSTANCE_RANKING);

   }


}
