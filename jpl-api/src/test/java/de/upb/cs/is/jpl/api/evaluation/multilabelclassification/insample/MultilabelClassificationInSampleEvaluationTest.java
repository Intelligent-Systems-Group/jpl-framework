package de.upb.cs.is.jpl.api.evaluation.multilabelclassification.insample;


import de.upb.cs.is.jpl.api.evaluation.IEvaluation;
import de.upb.cs.is.jpl.api.evaluation.multilabelclassification.AMultilabelClassificationEvaluationTest;


/**
 * This class tests the {@link MultilabelClassificationInSampleEvaluation}.
 * 
 * @author Alexander Hetzer
 *
 */
public class MultilabelClassificationInSampleEvaluationTest extends AMultilabelClassificationEvaluationTest {


   @Override
   public IEvaluation getEvaluation() {
      return new MultilabelClassificationInSampleEvaluation();
   }

}
