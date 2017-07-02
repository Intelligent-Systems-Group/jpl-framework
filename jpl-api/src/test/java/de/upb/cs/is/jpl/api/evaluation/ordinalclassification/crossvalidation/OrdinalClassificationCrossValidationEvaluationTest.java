package de.upb.cs.is.jpl.api.evaluation.ordinalclassification.crossvalidation;


import java.io.File;
import java.util.List;

import de.upb.cs.is.jpl.api.algorithm.learningalgorithm.ILearningAlgorithm;
import de.upb.cs.is.jpl.api.evaluation.ACrossValidationEvaluationTest;
import de.upb.cs.is.jpl.api.evaluation.EvaluationResult;
import de.upb.cs.is.jpl.api.evaluation.EvaluationSetting;
import de.upb.cs.is.jpl.api.evaluation.IEvaluation;
import de.upb.cs.is.jpl.api.evaluation.ordinalclassification.OrdinalClassificationEvaluationTestHelper;
import de.upb.cs.is.jpl.api.util.datastructure.Pair;


/**
 * Test for the {@link OrdinalClassificationCrossValidationEvaluation}.
 * 
 * @author Tanja Tornede
 *
 */
public class OrdinalClassificationCrossValidationEvaluationTest extends ACrossValidationEvaluationTest {

   private static final String RESOURCE_DIRECTORY_LEVEL = "ordinalclassification" + File.separator;


   /**
    * Creates a new unit test for rank aggregation evaluations without any additional path to the
    * resources.
    */
   public OrdinalClassificationCrossValidationEvaluationTest() {
      super(RESOURCE_DIRECTORY_LEVEL);
   }


   @Override
   public IEvaluation getEvaluation() {
      return new OrdinalClassificationCrossValidationEvaluation();
   }


   @Override
   public List<ILearningAlgorithm> getLearningAlgorithms() {
      return OrdinalClassificationEvaluationTestHelper.getLearningAlgorithms();
   }


   @Override
   public void setEvaluationMetrics() {
      evaluationMetrics = OrdinalClassificationEvaluationTestHelper.getEvaluationMetrics();
   }


   @Override
   public List<Pair<Pair<Integer, List<EvaluationSetting>>, EvaluationResult>> getListEvaluationSettingsSetWithCorrectEvaluationResult() {
      double[][] metricResults = { { 1.61, 3.73 }, { 2.03, 5.84 } };
      return getCorrectListOfEvaluationSettingsForOneSetForMetricResults(metricResults,
            OrdinalClassificationEvaluationTestHelper.DATASET_ORDINAL_CLASSIFICATION_TEST);
   }


   @Override
   public List<Pair<Pair<Integer, List<EvaluationSetting>>, EvaluationResult>> getListEvaluationSettingsSetWithWrongEvaluationResult() {
      double[][] metricResults = { { 0, 0 }, { 0, 0 } };
      return getWrongListOfEvaluationSettingsForOneSetForMetricResults(metricResults,
            OrdinalClassificationEvaluationTestHelper.DATASET_ORDINAL_CLASSIFICATION_TEST);
   }

}
