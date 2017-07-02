package de.upb.cs.is.jpl.api.evaluation.ordinalclassification.percentagesplit;


import java.io.File;
import java.util.List;

import de.upb.cs.is.jpl.api.algorithm.learningalgorithm.ILearningAlgorithm;
import de.upb.cs.is.jpl.api.evaluation.APercentageSplitEvaluationTest;
import de.upb.cs.is.jpl.api.evaluation.EvaluationResult;
import de.upb.cs.is.jpl.api.evaluation.EvaluationSetting;
import de.upb.cs.is.jpl.api.evaluation.IEvaluation;
import de.upb.cs.is.jpl.api.evaluation.ordinalclassification.OrdinalClassificationEvaluationTestHelper;
import de.upb.cs.is.jpl.api.evaluation.ordinalclassification.crossvalidation.OrdinalClassificationCrossValidationEvaluation;
import de.upb.cs.is.jpl.api.util.datastructure.Pair;


/**
 * Test for the {@link OrdinalClassificationCrossValidationEvaluation}.
 * 
 * @author Tanja Tornede
 *
 */
public class OrdinalClassificationPercentageSplitEvaluationTest extends APercentageSplitEvaluationTest {

   private static final String ADDTIONAL_RESOURCE_PATH = "ordinalclassification" + File.separator;


   /**
    * Creates a new unit test for rank aggregation evaluations without any additional path to the
    * resources.
    */
   public OrdinalClassificationPercentageSplitEvaluationTest() {
      super(ADDTIONAL_RESOURCE_PATH);
   }


   @Override
   public IEvaluation getEvaluation() {
      return new OrdinalClassificationPercentageSplitEvaluation();
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
      double[][] metricResults = { { 1.46, 3.25 }, { 1.71, 4.59 } };
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
