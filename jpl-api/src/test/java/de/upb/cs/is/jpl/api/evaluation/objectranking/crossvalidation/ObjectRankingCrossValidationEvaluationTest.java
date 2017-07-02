package de.upb.cs.is.jpl.api.evaluation.objectranking.crossvalidation;


import java.util.List;

import de.upb.cs.is.jpl.api.algorithm.learningalgorithm.ILearningAlgorithm;
import de.upb.cs.is.jpl.api.evaluation.ACrossValidationEvaluationTest;
import de.upb.cs.is.jpl.api.evaluation.EvaluationResult;
import de.upb.cs.is.jpl.api.evaluation.EvaluationSetting;
import de.upb.cs.is.jpl.api.evaluation.IEvaluation;
import de.upb.cs.is.jpl.api.evaluation.objectranking.ObjectRankingEvaluationTestHelper;
import de.upb.cs.is.jpl.api.util.CollectionsUtils;
import de.upb.cs.is.jpl.api.util.datastructure.Pair;


/**
 * This class contains unit test for validating the implementation of the
 * {@link ObjectRankingCrossValidationEvaluation}.
 * 
 * @author Pritha Gupta
 *
 */
public class ObjectRankingCrossValidationEvaluationTest extends ACrossValidationEvaluationTest {


   /**
    * Creates a new unit test for the {@link ObjectRankingCrossValidationEvaluationTest}.
    */
   public ObjectRankingCrossValidationEvaluationTest() {
      super(ObjectRankingEvaluationTestHelper.RESOURCE_DIRECTORY_LEVEL);
   }


   @Override
   public IEvaluation getEvaluation() {
      return new ObjectRankingCrossValidationEvaluation();
   }


   @Override
   public List<Pair<Pair<Integer, List<EvaluationSetting>>, EvaluationResult>> getListEvaluationSettingsSetWithCorrectEvaluationResult() {
      double[][] metricResults = { { 0.1355, 0.1839 }, { 0.1407, 0.1934 } };
      return getCorrectListOfEvaluationSettingsForOneSetForMetricResults(metricResults,
            ObjectRankingEvaluationTestHelper.OBJECT_RANKING_TEST_DATASET);
   }


   @Override
   public List<Pair<Pair<Integer, List<EvaluationSetting>>, EvaluationResult>> getListEvaluationSettingsSetWithWrongEvaluationResult() {
      double[][] metricResults = { { 0.36, 0.36 }, { 0.25, 0.25 } };
      return getWrongListOfEvaluationSettingsForOneSetForMetricResults(metricResults,
            ObjectRankingEvaluationTestHelper.OBJECT_RANKING_TEST_DATASET);
   }


   @Override
   public List<ILearningAlgorithm> getLearningAlgorithms() {
      return ObjectRankingEvaluationTestHelper.createListOfLearningAlagorithms();
   }


   @Override
   public void setEvaluationMetrics() {
      evaluationMetrics = CollectionsUtils.getDeepCopyOf(ObjectRankingEvaluationTestHelper.createEvaluationMetrics());

   }

}
