package de.upb.cs.is.jpl.api.evaluation.ordinalclassification;


import java.util.ArrayList;
import java.util.List;

import de.upb.cs.is.jpl.api.algorithm.learningalgorithm.ILearningAlgorithm;
import de.upb.cs.is.jpl.api.algorithm.learningalgorithm.ordinalclassification.reductionframework.OrdinalClassificationReductionFramework;
import de.upb.cs.is.jpl.api.algorithm.learningalgorithm.ordinalclassification.simple.SimpleOrdinalClassification;
import de.upb.cs.is.jpl.api.metric.EMetric;
import de.upb.cs.is.jpl.api.metric.IMetric;
import de.upb.cs.is.jpl.api.util.StringUtils;


/**
 * This class offers utility methods ordinal classification evaluation tests.
 * 
 * @author Tanja Tornede
 *
 */
public class OrdinalClassificationEvaluationTestHelper {

   /** An ordinal dataset which can be used for testing. */
   public static final String DATASET_ORDINAL_CLASSIFICATION_TEST = "test_automobile-0-arff.gprf";

   /** An ordinal training dataset which can be used for testing. */
   public static final String DATASET_ORDINAL_CLASSIFICATION_TRAIN = "train_automobile-0-arff.gprf";


   /**
    * Hides the public constructor.
    */
   private OrdinalClassificationEvaluationTestHelper() {
      throw new IllegalAccessError(StringUtils.EXCEPTION_MESSAGE_ACCESS_ERROR);
   }


   /**
    * Returns the learning algorihtms of ordinal classification.
    * 
    * @return all learning algorithms of ordinal classification
    */
   public static List<ILearningAlgorithm> getLearningAlgorithms() {
      List<ILearningAlgorithm> learningAlgorithms = new ArrayList<>();
      learningAlgorithms.add(new SimpleOrdinalClassification());
      learningAlgorithms.add(new OrdinalClassificationReductionFramework());
      return learningAlgorithms;
   }


   /**
    * Returns the valid evaluation metrics for ordinal classification.
    * 
    * @return the evaluation metrics for ordinal classification
    */
   public static List<IMetric<?, ?>> getEvaluationMetrics() {
      List<IMetric<?, ?>> evaluationMetrics = new ArrayList<>();
      evaluationMetrics.add(EMetric.MEAN_ABSOLUTE_ERROR.createEvaluationMetric());
      evaluationMetrics.add(EMetric.MEAN_SQUARED_ERROR.createEvaluationMetric());
      return evaluationMetrics;
   }

}
