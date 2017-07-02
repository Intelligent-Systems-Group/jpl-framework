package de.upb.cs.is.jpl.api.evaluation.objectranking;


import java.util.List;

import de.upb.cs.is.jpl.api.algorithm.learningalgorithm.ILearningAlgorithm;
import de.upb.cs.is.jpl.api.evaluation.AEvaluationTest;
import de.upb.cs.is.jpl.api.util.CollectionsUtils;


/**
 * This abstract class is used for all tests for evaluations for object ranking problem.
 * 
 * @author Pritha Gupta
 * 
 */
public abstract class AObjectRankingEvaluationTest extends AEvaluationTest {


   /**
    * Creates a new unit test for rank aggregation evaluations with the additional path to the
    * resources given.
    * 
    * @param additionalResourcePath the additional path to the resources
    */
   public AObjectRankingEvaluationTest(String additionalResourcePath) {
      super(ObjectRankingEvaluationTestHelper.RESOURCE_DIRECTORY_LEVEL + additionalResourcePath);
   }


   /**
    * Creates a new unit test for rank aggregation evaluations without any additional path to the
    * resources.
    */
   public AObjectRankingEvaluationTest() {
      super(ObjectRankingEvaluationTestHelper.RESOURCE_DIRECTORY_LEVEL);
   }


   @Override
   public List<ILearningAlgorithm> getLearningAlgorithms() {
      return ObjectRankingEvaluationTestHelper.createListOfLearningAlagorithms();
   }


   @Override
   public void setEvaluationMetrics() {
      evaluationMetrics = CollectionsUtils.getDeepCopyOf(ObjectRankingEvaluationTestHelper.createEvaluationMetrics());

   }


}
