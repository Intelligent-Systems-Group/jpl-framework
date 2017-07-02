package de.upb.cs.is.jpl.api.evaluation.objectranking.percentagesplit;


import java.util.List;

import de.upb.cs.is.jpl.api.algorithm.learningalgorithm.ILearningAlgorithm;
import de.upb.cs.is.jpl.api.evaluation.APercentageSplitEvaluationTest;
import de.upb.cs.is.jpl.api.evaluation.EvaluationResult;
import de.upb.cs.is.jpl.api.evaluation.EvaluationSetting;
import de.upb.cs.is.jpl.api.evaluation.IEvaluation;
import de.upb.cs.is.jpl.api.evaluation.objectranking.ObjectRankingEvaluationTestHelper;
import de.upb.cs.is.jpl.api.util.CollectionsUtils;
import de.upb.cs.is.jpl.api.util.datastructure.Pair;


/**
 * This class contains unit test for validating the implementation of the
 * {@link ObjectRankingPercentageSplitEvaluation}.
 * 
 * @author Pritha Gupta
 *
 */
public class ObjectRankingPercentageSplitEvaluationTest extends APercentageSplitEvaluationTest {

   /**
    * 
    */
   public ObjectRankingPercentageSplitEvaluationTest() {
      super(ObjectRankingEvaluationTestHelper.RESOURCE_DIRECTORY_LEVEL);
   }


   @Override
   public IEvaluation getEvaluation() {
      return new ObjectRankingPercentageSplitEvaluation();
   }


   @Override
   public List<Pair<Pair<Integer, List<EvaluationSetting>>, EvaluationResult>> getListEvaluationSettingsSetWithCorrectEvaluationResult() {
      double[][] metricResults = { { 0.145, 0.192 }, { 0.138, 0.1856 } };
      return getCorrectListOfEvaluationSettingsForOneSetForMetricResults(metricResults,
            ObjectRankingEvaluationTestHelper.OBJECT_RANKING_TRAIN_DATASET);
   }


   @Override
   public List<Pair<Pair<Integer, List<EvaluationSetting>>, EvaluationResult>> getListEvaluationSettingsSetWithWrongEvaluationResult() {
      double[][] metricResults = { { 0.36, 0.36 }, { 0.25, 0.25 } };
      return getWrongListOfEvaluationSettingsForOneSetForMetricResults(metricResults,
            ObjectRankingEvaluationTestHelper.OBJECT_RANKING_TRAIN_DATASET);
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
