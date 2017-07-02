package de.upb.cs.is.jpl.api.evaluation.labelranking;


import java.util.ArrayList;
import java.util.List;

import de.upb.cs.is.jpl.api.algorithm.learningalgorithm.ILearningAlgorithm;
import de.upb.cs.is.jpl.api.algorithm.learningalgorithm.labelranking.instancebasedlabelranking.InstanceBasedLabelRankingLearningAlgorithm;
import de.upb.cs.is.jpl.api.algorithm.learningalgorithm.labelranking.labelrankingbypairwisecomparison.LabelRankingByPairwiseComparisonLearningAlgorithm;
import de.upb.cs.is.jpl.api.metric.EMetric;
import de.upb.cs.is.jpl.api.metric.IMetric;
import de.upb.cs.is.jpl.api.util.StringUtils;


/**
 * This class contains static helper methods for the evaluation tests.
 * 
 * @author Andreas Kornelsen
 *
 */
public class LabelRankingEvaluationTestHelper {


   /**
    * Creates a new unit test for label ranking evaluations.
    */
   private LabelRankingEvaluationTestHelper() {
      throw new IllegalAccessError(StringUtils.EXCEPTION_MESSAGE_ACCESS_ERROR);
   }


   /**
    * Returns the list of learning algorithms used for label ranking evaluation tests.
    * 
    * @return the list of learning algorithms used for label ranking evaluation tests
    */
   public static List<ILearningAlgorithm> getLearningAlgorithms() {
      List<ILearningAlgorithm> learningAlgorithms = new ArrayList<>();
      learningAlgorithms.add(new InstanceBasedLabelRankingLearningAlgorithm());
      learningAlgorithms.add(new LabelRankingByPairwiseComparisonLearningAlgorithm());
      return learningAlgorithms;
   }


   /**
    * Returns the list of evaluation metrics used for label ranking evaluation tests.
    * 
    * @return the list of evaluation metrics used for label ranking evaluation tests
    */
   public static List<IMetric<?, ?>> getEvaluationMetrics() {
      List<IMetric<?, ?>> evaluationMetrics = new ArrayList<>();
      evaluationMetrics.add(EMetric.KENDALLS_TAU.createEvaluationMetric());
      evaluationMetrics.add(EMetric.SPEARMANS_RANK_CORRELATION.createEvaluationMetric());
      return evaluationMetrics;
   }


}
