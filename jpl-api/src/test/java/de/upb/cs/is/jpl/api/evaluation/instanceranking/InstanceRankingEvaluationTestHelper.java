package de.upb.cs.is.jpl.api.evaluation.instanceranking;


import java.util.ArrayList;
import java.util.List;

import de.upb.cs.is.jpl.api.algorithm.learningalgorithm.ILearningAlgorithm;
import de.upb.cs.is.jpl.api.algorithm.learningalgorithm.instanceranking.combinedrankingandregression.CombinedRankingAndRegressionLearningAlgorithm;
import de.upb.cs.is.jpl.api.algorithm.learningalgorithm.instanceranking.perceptronrank.PerceptronRankLearningAlgorithm;
import de.upb.cs.is.jpl.api.metric.EMetric;
import de.upb.cs.is.jpl.api.metric.IMetric;


/**
 * This class holds helping methods for the instance ranking evaluation tests
 * 
 * @author Sebastian Gottschalk
 *
 */
public class InstanceRankingEvaluationTestHelper {
   /**
    * The dataset file which is used for the instance ranking evaluation tests.
    */
   public static final String DATASET_INSTANCE_RANKING = "instancemovielens.gprf";


   private InstanceRankingEvaluationTestHelper() {
      // Nothing to do here
   }


   /**
    * Returns the learning algorithms for the instance ranking tests.
    * 
    * @return the learning algorithms
    */
   public static List<ILearningAlgorithm> getLearningAlgorithms() {
      List<ILearningAlgorithm> learningAlgorithms = new ArrayList<>();
      learningAlgorithms.add(new PerceptronRankLearningAlgorithm());
      learningAlgorithms.add(new CombinedRankingAndRegressionLearningAlgorithm());
      return learningAlgorithms;
   }


   /**
    * Returns the evaluation metrics for the instance ranking tests.
    * 
    * @return the evaluation metrics
    */
   public static List<IMetric<?, ?>> getEvaluationMetrics() {
      List<IMetric<?, ?>> evaluationMetrics = new ArrayList<>();
      EMetric eEvaluationMetric = EMetric.MEAN_ABSOLUTE_ERROR;
      evaluationMetrics.add(eEvaluationMetric.createEvaluationMetric());
      eEvaluationMetric = EMetric.MEAN_SQUARED_ERROR;
      evaluationMetrics.add(eEvaluationMetric.createEvaluationMetric());

      return evaluationMetrics;
   }


}
