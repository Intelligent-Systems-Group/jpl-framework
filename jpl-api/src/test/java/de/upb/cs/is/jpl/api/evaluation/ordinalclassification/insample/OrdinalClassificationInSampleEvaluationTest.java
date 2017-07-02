package de.upb.cs.is.jpl.api.evaluation.ordinalclassification.insample;


import de.upb.cs.is.jpl.api.evaluation.IEvaluation;
import de.upb.cs.is.jpl.api.evaluation.ordinalclassification.AOrdinalClassificationEvaluationTest;


/**
 * Test for the {@link OrdinalClassificationInSampleEvaluation}.
 * 
 * @author Tanja Tornede
 *
 */
public class OrdinalClassificationInSampleEvaluationTest extends AOrdinalClassificationEvaluationTest {


   /**
    * Creates a new unit test for ordinal classification.
    */
   public OrdinalClassificationInSampleEvaluationTest() {
      super();
   }


   @Override
   public IEvaluation getEvaluation() {
      return new OrdinalClassificationInSampleEvaluation();
   }

}
