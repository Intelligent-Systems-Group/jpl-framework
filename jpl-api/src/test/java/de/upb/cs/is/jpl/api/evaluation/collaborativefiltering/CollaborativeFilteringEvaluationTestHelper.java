package de.upb.cs.is.jpl.api.evaluation.collaborativefiltering;


import java.util.ArrayList;
import java.util.List;

import de.upb.cs.is.jpl.api.algorithm.learningalgorithm.ILearningAlgorithm;
import de.upb.cs.is.jpl.api.algorithm.learningalgorithm.collaborativefiltering.matrixfactorization.MatrixFactorizationLearningAlgorithm;
import de.upb.cs.is.jpl.api.metric.EMetric;
import de.upb.cs.is.jpl.api.metric.IMetric;
import de.upb.cs.is.jpl.api.util.StringUtils;


/**
 * Testhelper for collaborative filtering.
 * 
 * @author Sebastian Osterbrink
 *
 */
public class CollaborativeFilteringEvaluationTestHelper {

   /**
    * The MovieLens 100k dataset.
    */
   public static final String MOVIELENS_GPRF = "movielens.gprf";


   /**
    * Hides the public constructor.
    */
   private CollaborativeFilteringEvaluationTestHelper() {
      throw new IllegalAccessError(StringUtils.EXCEPTION_MESSAGE_ACCESS_ERROR);
   }


   /**
    * Returns the learning algorihtms of ordinal classification.
    * 
    * @return all learning algorithms of ordinal classification
    */
   public static List<ILearningAlgorithm> getLearningAlgorithms() {
      List<ILearningAlgorithm> learningAlgorithms = new ArrayList<>();
      learningAlgorithms.add(new MatrixFactorizationLearningAlgorithm());
      return learningAlgorithms;
   }


   /**
    * Returns the valid evaluation metrics for ordinal classification.
    * 
    * @return the evaluation metrics for ordinal classification
    */
   public static List<IMetric<?, ?>> getEvaluationMetrics() {
      List<IMetric<?, ?>> evaluationMetrics = new ArrayList<>();
      evaluationMetrics.add(EMetric.MEAN_SQUARED_ERROR.createEvaluationMetric());
      return evaluationMetrics;
   }

}
