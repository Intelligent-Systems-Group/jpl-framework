package de.upb.cs.is.jpl.cli.exception.command.evaluatealgorithmscommand;

import de.upb.cs.is.jpl.api.exception.evaluation.EvaluationFailedException;

/**
 * This exception indicates that either the datasets are not set or the learning problem are not set
 * for the evaluation. The exact circumstances, including the reason, can be obtained from the
 * exception message.
 * 
 * @author Pritha Gupta
 *
 */
public class DatasetsOrLearningAlgorithmsNotSetForEvaluationException extends EvaluationFailedException {

   private static final long serialVersionUID = 123457896354L;


   /**
    * Creates a general dataset or learning algorithms not set exception with a given exception message.
    * 
    * @param message the message of the exception
    */
   public DatasetsOrLearningAlgorithmsNotSetForEvaluationException(String message) {
      super(message);
   }

}
