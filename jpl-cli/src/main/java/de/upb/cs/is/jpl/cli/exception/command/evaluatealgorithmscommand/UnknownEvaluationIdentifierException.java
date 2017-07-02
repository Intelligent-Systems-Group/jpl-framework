package de.upb.cs.is.jpl.cli.exception.command.evaluatealgorithmscommand;

import de.upb.cs.is.jpl.api.exception.evaluation.EvaluationFailedException;

/**
 * This exception indicates that the evaluation identifier is not correct for the evaluation
 * command.The exact circumstances, including the reason, can be obtained from the exception
 * message.
 * 
 * @author Pritha Gupta
 *
 */
public class UnknownEvaluationIdentifierException extends EvaluationFailedException {

   private static final long serialVersionUID = 123457896351L;


   /**
    * Creates a general unknown evaluation identifier exception with a given exception message.
    * 
    * @param message the message of the exception
    */
   public UnknownEvaluationIdentifierException(String message) {
      super(message);
   }


}
