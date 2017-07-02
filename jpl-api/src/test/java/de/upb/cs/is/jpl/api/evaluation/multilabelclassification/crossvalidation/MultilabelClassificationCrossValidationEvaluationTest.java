package de.upb.cs.is.jpl.api.evaluation.multilabelclassification.crossvalidation;


import java.util.List;

import org.junit.Before;

import de.upb.cs.is.jpl.api.algorithm.learningalgorithm.ILearningAlgorithm;
import de.upb.cs.is.jpl.api.evaluation.ACrossValidationEvaluationTest;
import de.upb.cs.is.jpl.api.evaluation.EvaluationResult;
import de.upb.cs.is.jpl.api.evaluation.EvaluationSetting;
import de.upb.cs.is.jpl.api.evaluation.IEvaluation;
import de.upb.cs.is.jpl.api.evaluation.multilabelclassification.AMultilabelClassificationEvaluationTest;
import de.upb.cs.is.jpl.api.evaluation.multilabelclassification.MultilabelClassificationEvaluationTestHelper;
import de.upb.cs.is.jpl.api.math.RandomGenerator;
import de.upb.cs.is.jpl.api.util.datastructure.Pair;


/**
 * This class tests the {@link MultilabelClassificationCrossValidationEvaluation}.
 * 
 * @author Alexander Hetzer
 *
 */
public class MultilabelClassificationCrossValidationEvaluationTest extends ACrossValidationEvaluationTest {


   /**
    * Creates a new {@link MultilabelClassificationCrossValidationEvaluationTest}.
    */
   public MultilabelClassificationCrossValidationEvaluationTest() {
      super(AMultilabelClassificationEvaluationTest.RESOURCE_DIRECTORY_LEVEL);
   }


   @Override
   @Before
   public void setupTest() {
      super.setupTest();
      RandomGenerator.initializeRNG(1234);
   }


   @Override
   public List<Pair<Pair<Integer, List<EvaluationSetting>>, EvaluationResult>> getListEvaluationSettingsSetWithCorrectEvaluationResult() {
      double[][] metricResults = { { 0.33, 0.286 }, { 0.32, 0.2450 } };
      return getCorrectListOfEvaluationSettingsForOneSetForMetricResults(metricResults,
            AMultilabelClassificationEvaluationTest.DATASET_MULTILABEL_CLASSIFICATION);
   }


   @Override
   public List<Pair<Pair<Integer, List<EvaluationSetting>>, EvaluationResult>> getListEvaluationSettingsSetWithWrongEvaluationResult() {
      double[][] metricResults = { { 0, 0 }, { 0, 0 } };
      return getWrongListOfEvaluationSettingsForOneSetForMetricResults(metricResults,
            AMultilabelClassificationEvaluationTest.DATASET_MULTILABEL_CLASSIFICATION);
   }


   @Override
   public IEvaluation getEvaluation() {
      return new MultilabelClassificationCrossValidationEvaluation();
   }


   @Override
   public List<ILearningAlgorithm> getLearningAlgorithms() {
      return MultilabelClassificationEvaluationTestHelper.getLearningAlgorithms();
   }


   @Override
   public void setEvaluationMetrics() {
      this.evaluationMetrics = MultilabelClassificationEvaluationTestHelper.getEvaluationMetrics();
   }

}
