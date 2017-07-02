package de.upb.cs.is.jpl.api.evaluation.collaborativefiltering.crossvalidation;


import java.io.File;
import java.util.List;

import de.upb.cs.is.jpl.api.algorithm.learningalgorithm.ILearningAlgorithm;
import de.upb.cs.is.jpl.api.evaluation.ACrossValidationEvaluationTest;
import de.upb.cs.is.jpl.api.evaluation.EvaluationResult;
import de.upb.cs.is.jpl.api.evaluation.EvaluationSetting;
import de.upb.cs.is.jpl.api.evaluation.IEvaluation;
import de.upb.cs.is.jpl.api.evaluation.collaborativefiltering.CollaborativeFilteringEvaluationTestHelper;
import de.upb.cs.is.jpl.api.evaluation.collaborativefiltering.percentagesplit.CollaborativeFilteringPercentageSplitEvaluation;
import de.upb.cs.is.jpl.api.util.datastructure.Pair;


/**
 * Tests for the {@link CollaborativeFilteringPercentageSplitEvaluation}.
 * 
 * @author Sebastian Osterbrink
 *
 */
public class CollaborativeFilteringCrossValidationTest extends ACrossValidationEvaluationTest {

   private static final String COLLABORATIVEFITERING = "collaborativefiltering" + File.separator;


   /**
    * Creates a new {@link CollaborativeFilteringCrossValidationTest}.
    */
   public CollaborativeFilteringCrossValidationTest() {
      super(COLLABORATIVEFITERING);
   }


   @Override
   public IEvaluation getEvaluation() {
      return new CollaborativeFilteringCrossValidationEvaluation();
   }


   @Override
   public List<ILearningAlgorithm> getLearningAlgorithms() {
      return CollaborativeFilteringEvaluationTestHelper.getLearningAlgorithms();
   }


   @Override
   public void setEvaluationMetrics() {
      evaluationMetrics = CollaborativeFilteringEvaluationTestHelper.getEvaluationMetrics();
   }


   @Override
   public List<Pair<Pair<Integer, List<EvaluationSetting>>, EvaluationResult>> getListEvaluationSettingsSetWithCorrectEvaluationResult() {
      double[][] expectedResults = { { 0.941, 0.817 } };
      return getCorrectListOfEvaluationSettingsForOneSetForMetricResults(expectedResults,
            CollaborativeFilteringEvaluationTestHelper.MOVIELENS_GPRF);
   }


   @Override
   public List<Pair<Pair<Integer, List<EvaluationSetting>>, EvaluationResult>> getListEvaluationSettingsSetWithWrongEvaluationResult() {
      double[][] metricResults = { { 1.00, 0.00 } };
      return getWrongListOfEvaluationSettingsForOneSetForMetricResults(metricResults,
            CollaborativeFilteringEvaluationTestHelper.MOVIELENS_GPRF);
   }


}
