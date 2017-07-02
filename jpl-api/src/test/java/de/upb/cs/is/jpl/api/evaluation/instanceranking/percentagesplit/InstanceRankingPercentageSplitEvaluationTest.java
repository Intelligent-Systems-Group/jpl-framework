package de.upb.cs.is.jpl.api.evaluation.instanceranking.percentagesplit;


import java.io.File;
import java.util.List;

import de.upb.cs.is.jpl.api.algorithm.learningalgorithm.ILearningAlgorithm;
import de.upb.cs.is.jpl.api.evaluation.APercentageSplitEvaluationTest;
import de.upb.cs.is.jpl.api.evaluation.EvaluationResult;
import de.upb.cs.is.jpl.api.evaluation.EvaluationSetting;
import de.upb.cs.is.jpl.api.evaluation.IEvaluation;
import de.upb.cs.is.jpl.api.evaluation.instanceranking.InstanceRankingEvaluationTestHelper;
import de.upb.cs.is.jpl.api.util.datastructure.Pair;


/**
 * This class contains unit test for validating the implementation of the
 * {@link InstanceRankingPercentageSplitEvaluation}.
 * 
 * @author Sebastian Gottschalk
 */
public class InstanceRankingPercentageSplitEvaluationTest extends APercentageSplitEvaluationTest {

   private static final String RESOURCE_DIRECTORY_LEVEL = "instanceranking" + File.separator;


   /**
    * Creates a new unit test for the {@link InstanceRankingPercentageSplitEvaluation}.
    */
   public InstanceRankingPercentageSplitEvaluationTest() {
      super(RESOURCE_DIRECTORY_LEVEL);
   }


   @Override
   public IEvaluation getEvaluation() {
      return new InstanceRankingPercentageSplitEvaluation();
   }


   @Override
   public List<Pair<Pair<Integer, List<EvaluationSetting>>, EvaluationResult>> getListEvaluationSettingsSetWithCorrectEvaluationResult() {
      double[][] metricResults = { { 0.8829, 1.3829 }, { 2.1620, 5.8850 } };
      return getCorrectListOfEvaluationSettingsForOneSetForMetricResults(metricResults,
            InstanceRankingEvaluationTestHelper.DATASET_INSTANCE_RANKING);
   }


   @Override
   public List<ILearningAlgorithm> getLearningAlgorithms() {
      return InstanceRankingEvaluationTestHelper.getLearningAlgorithms();
   }


   @Override
   public void setEvaluationMetrics() {
      evaluationMetrics = InstanceRankingEvaluationTestHelper.getEvaluationMetrics();

   }


   @Override
   public List<Pair<Pair<Integer, List<EvaluationSetting>>, EvaluationResult>> getListEvaluationSettingsSetWithWrongEvaluationResult() {
      double[][] metricResults = { { 2.7848, -2.1139 }, { 2.7848, -2.1139 } };
      return getWrongListOfEvaluationSettingsForOneSetForMetricResults(metricResults,
            InstanceRankingEvaluationTestHelper.DATASET_INSTANCE_RANKING);

   }

}
