package de.upb.cs.is.jpl.api.evaluation.labelranking.percentagesplit;


import java.io.File;
import java.util.List;

import de.upb.cs.is.jpl.api.algorithm.learningalgorithm.ILearningAlgorithm;
import de.upb.cs.is.jpl.api.evaluation.APercentageSplitEvaluationTest;
import de.upb.cs.is.jpl.api.evaluation.EvaluationResult;
import de.upb.cs.is.jpl.api.evaluation.EvaluationSetting;
import de.upb.cs.is.jpl.api.evaluation.IEvaluation;
import de.upb.cs.is.jpl.api.evaluation.labelranking.LabelRankingEvaluationTestHelper;
import de.upb.cs.is.jpl.api.evaluation.labelranking.insample.LabelRankingInSampleEvaluation;
import de.upb.cs.is.jpl.api.util.datastructure.Pair;


/**
 * Test the {@link LabelRankingInSampleEvaluation}.
 * 
 * @author Andreas Kornelsen
 *
 */
public class LabelRankingPercentageSplitEvaluationTest extends APercentageSplitEvaluationTest {

   private static final String RESOURCE_DIRECTORY_LEVEL = "labelranking" + File.separator;
   private static final String DATASET_LABEL_RANKING = "vehicle_dense.gprf";


   /**
    * Creates a new {@link LabelRankingPercentageSplitEvaluationTest}.
    */
   public LabelRankingPercentageSplitEvaluationTest() {
      super(RESOURCE_DIRECTORY_LEVEL);
   }


   @Override
   public List<Pair<Pair<Integer, List<EvaluationSetting>>, EvaluationResult>> getListEvaluationSettingsSetWithCorrectEvaluationResult() {
      double[][] metricResults = { { 0.242125, 0.20826 }, { 0.813320, 0.849803 } };
      return getCorrectListOfEvaluationSettingsForOneSetForMetricResults(metricResults, DATASET_LABEL_RANKING);
   }


   @Override
   public List<Pair<Pair<Integer, List<EvaluationSetting>>, EvaluationResult>> getListEvaluationSettingsSetWithWrongEvaluationResult() {
      double[][] metricResults = { { 0.0, 0.0 }, { 0.0, 0.0 } };
      return getWrongListOfEvaluationSettingsForOneSetForMetricResults(metricResults, DATASET_LABEL_RANKING);
   }


   @Override
   public IEvaluation getEvaluation() {
      return new LabelRankingPercentageSplitEvaluation();
   }


   @Override
   public List<ILearningAlgorithm> getLearningAlgorithms() {
      return LabelRankingEvaluationTestHelper.getLearningAlgorithms();
   }


   @Override
   public void setEvaluationMetrics() {
      this.evaluationMetrics = LabelRankingEvaluationTestHelper.getEvaluationMetrics();
   }


}