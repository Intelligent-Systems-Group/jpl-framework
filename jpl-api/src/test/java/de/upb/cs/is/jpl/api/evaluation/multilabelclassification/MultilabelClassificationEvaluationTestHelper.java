package de.upb.cs.is.jpl.api.evaluation.multilabelclassification;


import java.util.ArrayList;
import java.util.List;

import de.upb.cs.is.jpl.api.algorithm.learningalgorithm.ILearningAlgorithm;
import de.upb.cs.is.jpl.api.algorithm.learningalgorithm.multilabelclassification.binaryrelevancelearning.BinaryRelevanceLearningAlgorithm;
import de.upb.cs.is.jpl.api.algorithm.learningalgorithm.multilabelclassification.classifierchains.ClassifierChainsLearningAlgorithm;
import de.upb.cs.is.jpl.api.metric.EMetric;
import de.upb.cs.is.jpl.api.metric.IMetric;
import de.upb.cs.is.jpl.api.util.StringUtils;


/**
 * This class offers utility methods multilabel classification evaluation tests.
 * 
 * @author Alexander Hetzer
 *
 */
public class MultilabelClassificationEvaluationTestHelper {


   /**
    * Creates a new unit test for multilabel classification evaluations.
    */
   private MultilabelClassificationEvaluationTestHelper() {
      throw new IllegalAccessError(StringUtils.EXCEPTION_MESSAGE_ACCESS_ERROR);
   }


   /**
    * Returns the list of learning algorithms used for multilabel classification evaluation tests.
    * 
    * @return the list of learning algorithms used for multilabel classification evaluation tests
    */
   public static List<ILearningAlgorithm> getLearningAlgorithms() {
      List<ILearningAlgorithm> learningAlgorithms = new ArrayList<>();
      learningAlgorithms.add(new ClassifierChainsLearningAlgorithm());
      learningAlgorithms.add(new BinaryRelevanceLearningAlgorithm());
      return learningAlgorithms;
   }


   /**
    * Returns the list of evaluation metrics used for multilabel classification evaluation tests.
    * 
    * @return the list of evaluation metrics used for multilabel classification evaluation tests
    */
   public static List<IMetric<?, ?>> getEvaluationMetrics() {
      List<IMetric<?, ?>> evaluationMetrics = new ArrayList<>();
      evaluationMetrics.add(EMetric.ACCURACY.createEvaluationMetric());
      evaluationMetrics.add(EMetric.HAMMINGLOSS.createEvaluationMetric());
      return evaluationMetrics;
   }

}
