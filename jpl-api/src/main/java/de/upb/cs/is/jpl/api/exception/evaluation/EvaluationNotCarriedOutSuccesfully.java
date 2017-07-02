package de.upb.cs.is.jpl.api.exception.evaluation;


import de.upb.cs.is.jpl.api.evaluation.EvaluationResult;


/**
 * This exception will be thrown by the evaluation if its is not able to calculate any
 * {@link EvaluationResult}.
 * 
 * @author Pritha Gupta
 *
 */
public class EvaluationNotCarriedOutSuccesfully extends EvaluationFailedException {

   private static final long serialVersionUID = -8542932644324390543L;


   /**
    * Creates a {@link EvaluationNotCarriedOutSuccesfully} with the given message.
    * 
    * @param message the message of the exception
    */
   public EvaluationNotCarriedOutSuccesfully(String message) {
      super(message);
   }
}
