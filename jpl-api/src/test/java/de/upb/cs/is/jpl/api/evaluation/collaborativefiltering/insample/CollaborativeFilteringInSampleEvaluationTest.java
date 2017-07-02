package de.upb.cs.is.jpl.api.evaluation.collaborativefiltering.insample;


import java.io.File;
import java.util.List;

import de.upb.cs.is.jpl.api.algorithm.learningalgorithm.ILearningAlgorithm;
import de.upb.cs.is.jpl.api.evaluation.AEvaluationTest;
import de.upb.cs.is.jpl.api.evaluation.EvaluationResult;
import de.upb.cs.is.jpl.api.evaluation.EvaluationSetting;
import de.upb.cs.is.jpl.api.evaluation.IEvaluation;
import de.upb.cs.is.jpl.api.evaluation.collaborativefiltering.CollaborativeFilteringEvaluationTestHelper;
import de.upb.cs.is.jpl.api.util.datastructure.Pair;


/**
 * Test the {@link CollaborativeFilteringInSampleEvaluation}.
 * 
 * @author Sebastian Osterbrink
 *
 */
public class CollaborativeFilteringInSampleEvaluationTest extends AEvaluationTest {

   private static final String COLLABORATIVEFITERING = "collaborativefiltering" + File.separator;


   /**
    * Create the test.
    */
   public CollaborativeFilteringInSampleEvaluationTest() {
      super(COLLABORATIVEFITERING);
   }


   @Override
   public IEvaluation getEvaluation() {
      return new CollaborativeFilteringInSampleEvaluation();
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
   public List<Pair<EvaluationSetting, EvaluationResult>> getCorrectListOfEvaluationSettings() {
      double[][] lossArraysForAlgorithms = { { 0.719 } };
      return createListOfEvaluationSettingAndCorrectEvaluationResultPairsForLossArrays(lossArraysForAlgorithms,
            CollaborativeFilteringEvaluationTestHelper.MOVIELENS_GPRF);
   }


   @Override
   public List<Pair<EvaluationSetting, EvaluationResult>> getWrongListOfEvaluationSettings() {
      double[][] lossArraysForAlgorithms = { { 0.0 } };
      return createListOfEvaluationSettingAndWrongEvaluationResultPairsForLossArrays(lossArraysForAlgorithms,
            CollaborativeFilteringEvaluationTestHelper.MOVIELENS_GPRF);

   }


}
