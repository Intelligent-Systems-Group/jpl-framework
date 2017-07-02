package de.upb.cs.is.jpl.cli.exception.command.evaluatealgorithmscommand;

import de.upb.cs.is.jpl.api.exception.evaluation.EvaluationFailedException;

/**
 * This exception indicates that the evaluation identifier is correct, but it is not valid or is not
 * implemented for given learning Problem. The exact circumstances, including the reason, can be
 * obtained from the exception message.
 * 
 * @author Pritha Gupta
 *
 */
public class EvaluationDoesnotExistForLearningProblemException extends EvaluationFailedException {


   private static final long serialVersionUID = 123457896352L;


   /**
    * Creates a general evaluation does not exist for a learning problem exception with a given
    * exception message.
    * 
    * @param message the message of the exception
    */
   public EvaluationDoesnotExistForLearningProblemException(String message) {
      super(message);
   }

}
