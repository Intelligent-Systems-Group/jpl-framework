package de.upb.cs.is.jpl.api.evaluation.ordinalclassification.suppliedtestset;


import java.util.List;

import de.upb.cs.is.jpl.api.evaluation.EvaluationResult;
import de.upb.cs.is.jpl.api.evaluation.EvaluationSetting;
import de.upb.cs.is.jpl.api.evaluation.IEvaluation;
import de.upb.cs.is.jpl.api.evaluation.ordinalclassification.AOrdinalClassificationEvaluationTest;
import de.upb.cs.is.jpl.api.evaluation.ordinalclassification.OrdinalClassificationEvaluationTestHelper;
import de.upb.cs.is.jpl.api.evaluation.ordinalclassification.crossvalidation.OrdinalClassificationCrossValidationEvaluation;
import de.upb.cs.is.jpl.api.util.datastructure.Pair;


/**
 * Test for the {@link OrdinalClassificationCrossValidationEvaluation}.
 * 
 * @author Tanja Tornede
 *
 */
public class OrdinalClassificationSuppliedTestSetEvaluationTest extends AOrdinalClassificationEvaluationTest {


   /**
    * Creates a new unit test for ordinal classification evaluations.
    */
   public OrdinalClassificationSuppliedTestSetEvaluationTest() {
      super();
   }


   @Override
   public IEvaluation getEvaluation() {
      return new OrdinalClassificationSuppliedTestSetEvaluation();
   }


   @Override
   public List<Pair<EvaluationSetting, EvaluationResult>> getCorrectListOfEvaluationSettings() {
      double[][] lossArraysForAlgorithms = { { 1.07, 2.69 }, { 1.21, 2.13 } };
      return createListOfEvaluationSettingAndCorrectEvaluationResultPairsForLossArrays(lossArraysForAlgorithms,
            OrdinalClassificationEvaluationTestHelper.DATASET_ORDINAL_CLASSIFICATION_TRAIN,
            OrdinalClassificationEvaluationTestHelper.DATASET_ORDINAL_CLASSIFICATION_TEST);
   }


   @Override
   public List<Pair<EvaluationSetting, EvaluationResult>> getWrongListOfEvaluationSettings() {
      double[][] lossArraysForAlgorithms = { { 0.5, 0.5 }, { 0.5, 0.5 } };
      return createListOfEvaluationSettingAndWrongEvaluationResultPairsForLossArrays(lossArraysForAlgorithms,
            OrdinalClassificationEvaluationTestHelper.DATASET_ORDINAL_CLASSIFICATION_TRAIN,
            OrdinalClassificationEvaluationTestHelper.DATASET_ORDINAL_CLASSIFICATION_TEST);
   }

}
