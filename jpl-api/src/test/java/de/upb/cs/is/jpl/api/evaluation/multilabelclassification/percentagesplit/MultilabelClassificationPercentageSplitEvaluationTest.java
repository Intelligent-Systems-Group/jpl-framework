package de.upb.cs.is.jpl.api.evaluation.multilabelclassification.percentagesplit;


import java.util.List;

import de.upb.cs.is.jpl.api.algorithm.learningalgorithm.ILearningAlgorithm;
import de.upb.cs.is.jpl.api.evaluation.APercentageSplitEvaluationTest;
import de.upb.cs.is.jpl.api.evaluation.EvaluationResult;
import de.upb.cs.is.jpl.api.evaluation.EvaluationSetting;
import de.upb.cs.is.jpl.api.evaluation.IEvaluation;
import de.upb.cs.is.jpl.api.evaluation.multilabelclassification.AMultilabelClassificationEvaluationTest;
import de.upb.cs.is.jpl.api.evaluation.multilabelclassification.MultilabelClassificationEvaluationTestHelper;
import de.upb.cs.is.jpl.api.util.datastructure.Pair;


/**
 * This class tests the {@link MultilabelClassificationPercentageSplitEvaluation}.
 * 
 * @author Alexander Hetzer
 *
 */
public class MultilabelClassificationPercentageSplitEvaluationTest extends APercentageSplitEvaluationTest {


   /**
    * Creates a new {@link MultilabelClassificationPercentageSplitEvaluationTest}.
    */
   public MultilabelClassificationPercentageSplitEvaluationTest() {
      super(AMultilabelClassificationEvaluationTest.RESOURCE_DIRECTORY_LEVEL);
   }


   @Override
   public List<Pair<Pair<Integer, List<EvaluationSetting>>, EvaluationResult>> getListEvaluationSettingsSetWithCorrectEvaluationResult() {
      double[][] metricResults = { { 0.397, 0.26 }, { 0.27, 0.26 } };
      return getCorrectListOfEvaluationSettingsForOneSetForMetricResults(metricResults,
            AMultilabelClassificationEvaluationTest.DATASET_MULTILABEL_CLASSIFICATION);
   }


   @Override
   public List<Pair<Pair<Integer, List<EvaluationSetting>>, EvaluationResult>> getListEvaluationSettingsSetWithWrongEvaluationResult() {
      double[][] metricResults = { { 0.36, 0.36 }, { 0.25, 0.25 } };
      return getWrongListOfEvaluationSettingsForOneSetForMetricResults(metricResults,
            AMultilabelClassificationEvaluationTest.DATASET_MULTILABEL_CLASSIFICATION);
   }


   @Override
   public IEvaluation getEvaluation() {
      return new MultilabelClassificationPercentageSplitEvaluation();
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
