package de.upb.cs.is.jpl.api.evaluation.multilabelclassification;


import java.io.File;
import java.util.List;

import de.upb.cs.is.jpl.api.algorithm.learningalgorithm.ILearningAlgorithm;
import de.upb.cs.is.jpl.api.evaluation.AEvaluationTest;
import de.upb.cs.is.jpl.api.evaluation.EvaluationResult;
import de.upb.cs.is.jpl.api.evaluation.EvaluationSetting;
import de.upb.cs.is.jpl.api.util.datastructure.Pair;


/**
 * This class is an abstract test class for the evaluations of multilabel classification.
 * 
 * @author Alexander Hetzer
 *
 */
public abstract class AMultilabelClassificationEvaluationTest extends AEvaluationTest {

   /**
    * The resource directory level used in evaluation tests.
    */
   public static final String RESOURCE_DIRECTORY_LEVEL = "multilabelclassification" + File.separator;
   /**
    * The default dataset used in evaluation tests.
    */
   public static final String DATASET_MULTILABEL_CLASSIFICATION = "emotions-arff.gprf";

   /**
    * The default training dataset used in evaluation tests.
    */
   public static final String DATASET_MULTILABEL_CLASSIFICATION_TRAIN = "emotions-arff-train.gprf";

   /**
    * The default test dataset used in evaluation tests.
    */
   public static final String DATASET_MULTILABEL_CLASSIFICATION_TEST = "emotions-arff-test.gprf";


   /**
    * Creates a new {@link AMultilabelClassificationEvaluationTest}.
    */
   public AMultilabelClassificationEvaluationTest() {
      super(RESOURCE_DIRECTORY_LEVEL);
   }


   @Override
   public List<ILearningAlgorithm> getLearningAlgorithms() {
      return MultilabelClassificationEvaluationTestHelper.getLearningAlgorithms();
   }


   @Override
   public void setEvaluationMetrics() {
      evaluationMetrics = MultilabelClassificationEvaluationTestHelper.getEvaluationMetrics();
   }


   @Override
   public List<Pair<EvaluationSetting, EvaluationResult>> getCorrectListOfEvaluationSettings() {
      double[][] lossArraysForAlgorithms = { { 0.43, 0.23 }, { 0.43, 0.21 } };
      List<Pair<EvaluationSetting, EvaluationResult>> correctEvalutionSettingAndEvaluationResultList = createListOfEvaluationSettingAndCorrectEvaluationResultPairsForLossArrays(
            lossArraysForAlgorithms, DATASET_MULTILABEL_CLASSIFICATION);
      return correctEvalutionSettingAndEvaluationResultList;
   }


   @Override
   public List<Pair<EvaluationSetting, EvaluationResult>> getWrongListOfEvaluationSettings() {
      double[][] lossArraysForAlgorithms = { { 0.0, 0.0 }, { 0.0, 0.0 } };
      List<Pair<EvaluationSetting, EvaluationResult>> correctEvalutionSettingAndEvaluationResultList = createListOfEvaluationSettingAndWrongEvaluationResultPairsForLossArrays(
            lossArraysForAlgorithms, DATASET_MULTILABEL_CLASSIFICATION);
      return correctEvalutionSettingAndEvaluationResultList;
   }

}
