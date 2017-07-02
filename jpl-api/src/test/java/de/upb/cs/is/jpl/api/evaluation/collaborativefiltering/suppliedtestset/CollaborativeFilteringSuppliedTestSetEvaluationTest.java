package de.upb.cs.is.jpl.api.evaluation.collaborativefiltering.suppliedtestset;


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
 * Test for {@link CollaborativeFilteringSuppliedTestSetEvaluation}.
 * 
 * @author Sebastian Osterbrink
 *
 */
public class CollaborativeFilteringSuppliedTestSetEvaluationTest extends AEvaluationTest {
   private static final String DATASET_TRAIN = "ml-100k_training.gprf";
   private static final String DATASET_TEST = "ml-100k_testing.gprf";
   private static final String COLLABORATIVEFITERING = "collaborativefiltering" + File.separator;


   /**
    * Create the test.
    */
   public CollaborativeFilteringSuppliedTestSetEvaluationTest() {
      super(COLLABORATIVEFITERING);
   }


   @Override
   public IEvaluation getEvaluation() {
      return new CollaborativeFilteringSuppliedTestSetEvaluation();
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
      double[][] lossArraysForAlgorithms = { { 0.940 } };
      List<Pair<EvaluationSetting, EvaluationResult>> evalutionSettingAndEvaluationResultList = createListOfEvaluationSettingAndCorrectEvaluationResultPairsForLossArrays(
            lossArraysForAlgorithms, DATASET_TRAIN, DATASET_TEST);

      return evalutionSettingAndEvaluationResultList;
   }


   @Override
   public List<Pair<EvaluationSetting, EvaluationResult>> getWrongListOfEvaluationSettings() {
      double[][] lossArraysForAlgorithms = { { 0.0 } };
      List<Pair<EvaluationSetting, EvaluationResult>> evalutionSettingAndEvaluationResultList = createListOfEvaluationSettingAndWrongEvaluationResultPairsForLossArrays(
            lossArraysForAlgorithms, DATASET_TRAIN, DATASET_TEST);

      return evalutionSettingAndEvaluationResultList;

   }

}
