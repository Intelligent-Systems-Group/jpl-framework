package de.upb.cs.is.jpl.api.evaluation.objectranking;


import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonObject;

import de.upb.cs.is.jpl.api.algorithm.BaseLearnerJsonObjectCreator;
import de.upb.cs.is.jpl.api.algorithm.baselearner.EBaseLearner;
import de.upb.cs.is.jpl.api.algorithm.learningalgorithm.ILearningAlgorithm;
import de.upb.cs.is.jpl.api.algorithm.learningalgorithm.objectranking.expectedrankregression.ExpectedRankRegression;
import de.upb.cs.is.jpl.api.algorithm.learningalgorithm.objectranking.pairwiserankingalgorithm.PairwiseRankingLearningAlgorithm;
import de.upb.cs.is.jpl.api.exception.configuration.json.ParameterValidationFailedException;
import de.upb.cs.is.jpl.api.metric.EMetric;
import de.upb.cs.is.jpl.api.metric.IMetric;
import de.upb.cs.is.jpl.api.util.JsonUtils;


/**
 * Utility class for Object ranking evaluation tests.
 * 
 * @author Pritha Gupta
 *
 */
public class ObjectRankingEvaluationTestHelper {

   /**
    * Resource directory for object ranking evaluations.
    */
   public static final String RESOURCE_DIRECTORY_LEVEL = "objectranking" + File.separator;

   /**
    * Object Ranking dataset
    */
   public static final String OBJECT_RANKING_TRAIN_DATASET = "sushi-dataset-zip-b-first-500-instances.gprf";

   /**
    * Object Ranking Test dataset
    */
   public static final String OBJECT_RANKING_TEST_DATASET = "sushi-dataset-zip-b-second-500-instances.gprf";


   /**
    * Returns the list of evaluation metrics used for object ranking evaluation tests.
    * 
    * @return the list of evaluation metrics used for object ranking evaluation tests
    */
   public static List<IMetric<?, ?>> createEvaluationMetrics() {
      List<IMetric<?, ?>> evaluationMetrics = new ArrayList<>();
      EMetric eEvaluationMetric = EMetric.KENDALLS_TAU;
      evaluationMetrics.add(eEvaluationMetric.createEvaluationMetric());
      eEvaluationMetric = EMetric.SPEARMANS_RANK_CORRELATION;
      evaluationMetrics.add(eEvaluationMetric.createEvaluationMetric());
      return evaluationMetrics;
   }


   /**
    * Returns the list of learning algorithms used for object ranking evaluation tests.
    * 
    * @return the list of learning algorithms used for object ranking evaluation tests
    */
   public static List<ILearningAlgorithm> createListOfLearningAlagorithms() {
      List<ILearningAlgorithm> learningAlgorithms = new ArrayList<>();
      learningAlgorithms.add(new ExpectedRankRegression());
      PairwiseRankingLearningAlgorithm algorithm = new PairwiseRankingLearningAlgorithm();
      JsonObject object = BaseLearnerJsonObjectCreator.getBaseLearnerJsonObject(
            new BaseLearnerJsonObjectCreator(EBaseLearner.LOGISTIC_CLASSIFICATION.getBaseLearnerIdentifier(), new JsonObject()));
      JsonObject methodType = JsonUtils.createJsonObjectFromKeyAndValue("method_type", "svor");
      try {
         algorithm.setParameters(object);
         algorithm.setParameters(methodType);
      } catch (ParameterValidationFailedException e) {
         e.printStackTrace();
      }
      learningAlgorithms.add(algorithm);
      return learningAlgorithms;
   }
}
