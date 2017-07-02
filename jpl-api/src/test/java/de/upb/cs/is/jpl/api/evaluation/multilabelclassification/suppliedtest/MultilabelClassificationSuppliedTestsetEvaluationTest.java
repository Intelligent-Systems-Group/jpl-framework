package de.upb.cs.is.jpl.api.evaluation.multilabelclassification.suppliedtest;


import java.util.List;

import de.upb.cs.is.jpl.api.evaluation.EvaluationResult;
import de.upb.cs.is.jpl.api.evaluation.EvaluationSetting;
import de.upb.cs.is.jpl.api.evaluation.IEvaluation;
import de.upb.cs.is.jpl.api.evaluation.multilabelclassification.AMultilabelClassificationEvaluationTest;
import de.upb.cs.is.jpl.api.evaluation.multilabelclassification.suppliedtestset.MultilabelClassificationSuppliedTestsetEvaluation;
import de.upb.cs.is.jpl.api.util.datastructure.Pair;


/**
 * This class tests the {@link MultilabelClassificationSuppliedTestsetEvaluation}.
 * 
 * @author Alexander Hetzer
 *
 */
public class MultilabelClassificationSuppliedTestsetEvaluationTest extends AMultilabelClassificationEvaluationTest {

   @Override
   public IEvaluation getEvaluation() {
      return new MultilabelClassificationSuppliedTestsetEvaluation();
   }


   @Override
   public List<Pair<EvaluationSetting, EvaluationResult>> getCorrectListOfEvaluationSettings() {
      double[][] lossArraysForAlgorithms = { { 0.37, 0.25 }, { 0.16, 0.30 } };
      List<Pair<EvaluationSetting, EvaluationResult>> correctEvalutionSettingAndEvaluationResultList = createListOfEvaluationSettingAndCorrectEvaluationResultPairsForLossArrays(
            lossArraysForAlgorithms, DATASET_MULTILABEL_CLASSIFICATION_TRAIN, DATASET_MULTILABEL_CLASSIFICATION_TEST);
      return correctEvalutionSettingAndEvaluationResultList;
   }


   @Override
   public List<Pair<EvaluationSetting, EvaluationResult>> getWrongListOfEvaluationSettings() {
      double[][] lossArraysForAlgorithms = { { 0.0, 0.0 }, { 0.0, 0.0 } };
      List<Pair<EvaluationSetting, EvaluationResult>> correctEvalutionSettingAndEvaluationResultList = createListOfEvaluationSettingAndWrongEvaluationResultPairsForLossArrays(
            lossArraysForAlgorithms, DATASET_MULTILABEL_CLASSIFICATION_TRAIN, DATASET_MULTILABEL_CLASSIFICATION_TEST);
      return correctEvalutionSettingAndEvaluationResultList;
   }


}
