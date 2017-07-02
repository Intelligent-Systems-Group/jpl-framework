package de.upb.cs.is.jpl.api.evaluation.labelranking.crossvalidation;


import java.io.File;
import java.util.List;

import de.upb.cs.is.jpl.api.algorithm.learningalgorithm.ILearningAlgorithm;
import de.upb.cs.is.jpl.api.evaluation.ACrossValidationEvaluationTest;
import de.upb.cs.is.jpl.api.evaluation.EvaluationResult;
import de.upb.cs.is.jpl.api.evaluation.EvaluationSetting;
import de.upb.cs.is.jpl.api.evaluation.IEvaluation;
import de.upb.cs.is.jpl.api.evaluation.labelranking.LabelRankingEvaluationTestHelper;
import de.upb.cs.is.jpl.api.util.datastructure.Pair;


/**
 * Tests for the {@link LabelRankingCrossValidationEvaluation}.
 * 
 * @author Andreas Kornelsen
 * 
 */
public class LabelRankingCrossValidationEvaluationTest extends ACrossValidationEvaluationTest {

   private static final String RESOURCE_DIRECTORY_LEVEL = "labelranking" + File.separator;

   private static final String DATASET_LABEL_RANKING = "vehicle_dense.gprf";


   /**
    * Instantiates a new label ranking cross validation evaluation test.
    */
   public LabelRankingCrossValidationEvaluationTest() {
      super(RESOURCE_DIRECTORY_LEVEL);
   }


   @Override
   public IEvaluation getEvaluation() {
      return new LabelRankingCrossValidationEvaluation();
   }


   @Override
   public List<ILearningAlgorithm> getLearningAlgorithms() {
      return LabelRankingEvaluationTestHelper.getLearningAlgorithms();
   }


   @Override
   public void setEvaluationMetrics() {
      this.evaluationMetrics = LabelRankingEvaluationTestHelper.getEvaluationMetrics();
   }


   @Override
   public List<Pair<Pair<Integer, List<EvaluationSetting>>, EvaluationResult>> getListEvaluationSettingsSetWithCorrectEvaluationResult() {
      double[][] metricResults = { { 0.27436604, 0.24388781345 }, { 0.8136027, 0.8519991 } };
      return getCorrectListOfEvaluationSettingsForOneSetForMetricResults(metricResults, DATASET_LABEL_RANKING);
   }


   @Override
   public List<Pair<Pair<Integer, List<EvaluationSetting>>, EvaluationResult>> getListEvaluationSettingsSetWithWrongEvaluationResult() {
      double[][] metricResults = { { 0, 0 }, { 0, 0 } };
      return getWrongListOfEvaluationSettingsForOneSetForMetricResults(metricResults, DATASET_LABEL_RANKING);
   }
}