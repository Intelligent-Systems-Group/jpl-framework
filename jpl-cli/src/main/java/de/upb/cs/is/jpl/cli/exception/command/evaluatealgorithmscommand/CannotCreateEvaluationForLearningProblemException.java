package de.upb.cs.is.jpl.cli.exception.command.evaluatealgorithmscommand;


import de.upb.cs.is.jpl.api.evaluation.EEvaluation;
import de.upb.cs.is.jpl.api.evaluation.IEvaluation;
import de.upb.cs.is.jpl.api.exception.evaluation.EvaluationFailedException;


/**
 * This exception indicates that either the evaluation {@link IEvaluation} could not be created for
 * the given {@link EEvaluation}. The exact circumstances, including the reason, can be obtained
 * from the exception message.
 * 
 * @author Pritha Gupta
 *
 */
public class CannotCreateEvaluationForLearningProblemException extends EvaluationFailedException {


   private static final long serialVersionUID = 123457896353L;


   /**
    * Creates a general could not create evaluation for a learning problem exception with a given
    * exception message.
    * 
    * @param message the message of the exception
    */
   public CannotCreateEvaluationForLearningProblemException(String message) {
      super(message);
   }

}
