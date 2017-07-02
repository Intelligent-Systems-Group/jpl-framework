package de.upb.cs.is.jpl.api.evaluation.labelranking;


import java.io.File;
import java.util.List;

import de.upb.cs.is.jpl.api.algorithm.learningalgorithm.ILearningAlgorithm;
import de.upb.cs.is.jpl.api.evaluation.AEvaluationTest;


/**
 * This abstract class is used for all label ranking evaluation tests.
 * 
 * @author Andreas Kornelsen
 *
 */
public abstract class ALabelRankingEvaluationTest extends AEvaluationTest {

   /** The Constant RESOURCE_DIRECTORY_LEVEL. */
   private static final String RESOURCE_DIRECTORY_LEVEL = "labelranking" + File.separator;


   /**
    * Instantiates a new a label ranking evaluation test.
    */
   public ALabelRankingEvaluationTest() {
      super(RESOURCE_DIRECTORY_LEVEL);
   }


   @Override
   public List<ILearningAlgorithm> getLearningAlgorithms() {
      return LabelRankingEvaluationTestHelper.getLearningAlgorithms();
   }


   @Override
   public void setEvaluationMetrics() {
      evaluationMetrics = LabelRankingEvaluationTestHelper.getEvaluationMetrics();
   }
}
