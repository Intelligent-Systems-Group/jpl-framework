package de.upb.cs.is.jpl.cli.exception.command.evaluatealgorithmscommand;


import de.upb.cs.is.jpl.api.exception.evaluation.EvaluationFailedException;


/**
 * This exception indicates that either the evaluation could not be setup properly. The exact
 * circumstances, including the reason, can be obtained from the exception message.
 * 
 * @author Pritha Gupta
 *
 */
public class CouldNotSetupEvaluationException extends EvaluationFailedException {


   private static final long serialVersionUID = 123457896355L;


   /**
    * Creates a general could not setup an evaluation exception with a given exception message.
    * 
    * @param message the message of the exception
    */
   public CouldNotSetupEvaluationException(String message) {
      super(message);
   }

}
