package de.upb.cs.is.jpl.api.evaluation.labelranking.suppliedtestset;


import java.util.List;

import de.upb.cs.is.jpl.api.evaluation.EvaluationResult;
import de.upb.cs.is.jpl.api.evaluation.EvaluationSetting;
import de.upb.cs.is.jpl.api.evaluation.IEvaluation;
import de.upb.cs.is.jpl.api.evaluation.labelranking.ALabelRankingEvaluationTest;
import de.upb.cs.is.jpl.api.util.datastructure.Pair;


/**
 * This class contains unit test for validating the implementation of the
 * {@link LabelRankingSuppliedTestsetEvaluation}.
 * 
 * @author Andreas Kornelsen
 *
 */
public class LabelRankingSuppliedTestsetEvaluationTest extends ALabelRankingEvaluationTest {

   private static final String DATASET_LABEL_RANKING_TRAIN = "bodyfat_dense_train.gprf";
   private static final String DATASET_LABEL_RANKING_TEST = "bodyfat_dense_test.gprf";


   /**
    * Instantiates a new label ranking supplied testset evaluation unit test.
    */
   public LabelRankingSuppliedTestsetEvaluationTest() {
      super();
   }


   @Override
   public IEvaluation getEvaluation() {
      return new LabelRankingSuppliedTestsetEvaluation();
   }


   @Override
   public List<Pair<EvaluationSetting, EvaluationResult>> getCorrectListOfEvaluationSettings() {
      double[][] metricResults = { { 0.107769, 0.128289 }, { 0.26654, 0.316729 } };
      List<Pair<EvaluationSetting, EvaluationResult>> correctEvalutionSettingAndEvaluationResultList = createListOfEvaluationSettingAndCorrectEvaluationResultPairsForLossArrays(
            metricResults, DATASET_LABEL_RANKING_TRAIN, DATASET_LABEL_RANKING_TEST);
      return correctEvalutionSettingAndEvaluationResultList;
   }


   @Override
   public List<Pair<EvaluationSetting, EvaluationResult>> getWrongListOfEvaluationSettings() {
      double[][] metricResults = { { 0.0, 0.0 }, { 0.0, 0.0 } };
      List<Pair<EvaluationSetting, EvaluationResult>> wrongEvalutionSettingAndEvaluationResultList = createListOfEvaluationSettingAndWrongEvaluationResultPairsForLossArrays(
            metricResults, DATASET_LABEL_RANKING_TRAIN, DATASET_LABEL_RANKING_TEST);
      return wrongEvalutionSettingAndEvaluationResultList;
   }

}
