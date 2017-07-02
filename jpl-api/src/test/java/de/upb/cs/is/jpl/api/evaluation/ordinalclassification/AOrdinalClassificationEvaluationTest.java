package de.upb.cs.is.jpl.api.evaluation.ordinalclassification;


import java.io.File;
import java.util.List;

import de.upb.cs.is.jpl.api.algorithm.learningalgorithm.ILearningAlgorithm;
import de.upb.cs.is.jpl.api.evaluation.AEvaluationTest;
import de.upb.cs.is.jpl.api.evaluation.EvaluationResult;
import de.upb.cs.is.jpl.api.evaluation.EvaluationSetting;
import de.upb.cs.is.jpl.api.util.datastructure.Pair;


/**
 * This abstract class is used for all tests of ordinal classification evaluations.
 * 
 * @author Tanja Tornede
 */
public abstract class AOrdinalClassificationEvaluationTest extends AEvaluationTest {

   private static final String RESOURCE_DIRECTORY_LEVEL = "ordinalclassification" + File.separator;


   /**
    * Creates a new unit test for ordinal classification evaluations.
    */
   public AOrdinalClassificationEvaluationTest() {
      super(RESOURCE_DIRECTORY_LEVEL);
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
   public List<Pair<EvaluationSetting, EvaluationResult>> getCorrectListOfEvaluationSettings() {
      double[][] lossArraysForAlgorithms = { { 0.98, 2.25 }, { 1.65, 3.69 } };
      return createListOfEvaluationSettingAndCorrectEvaluationResultPairsForLossArrays(lossArraysForAlgorithms,
            OrdinalClassificationEvaluationTestHelper.DATASET_ORDINAL_CLASSIFICATION_TEST);
   }


   @Override
   public List<Pair<EvaluationSetting, EvaluationResult>> getWrongListOfEvaluationSettings() {
      double[][] lossArraysForAlgorithms = { { 0.5, 0.5 }, { 0.5, 0.5 } };
      return createListOfEvaluationSettingAndWrongEvaluationResultPairsForLossArrays(lossArraysForAlgorithms,
            OrdinalClassificationEvaluationTestHelper.DATASET_ORDINAL_CLASSIFICATION_TEST);
   }

}
