package de.upb.cs.is.jpl.api.exception.evaluation;


import de.upb.cs.is.jpl.api.exception.JplException;


/**
 * This exception indicates that a evaluation command could not be run properly from the json or
 * command line definition of the evaluation.
 * 
 * @author Pritha Gupta
 *
 */
public class EvaluationFailedException extends JplException {


   private static final long serialVersionUID = -8542644324390543L;


   /**
    * Creates a general Evaluate Command exception with a given exception message.
    * 
    * @param message the message of the exception
    */
   public EvaluationFailedException(String message) {
      super(message);
   }


   /**
    * Creates a general Evaluate Command exception as a wrapper with a given exception message and
    * the given throwable.
    * 
    * @param message the message of the exception
    * @param throwable the throwable which should be wrapped
    */
   public EvaluationFailedException(String message, Throwable throwable) {
      super(message, throwable);
   }


   /**
    * Creates a general Evaluate Command exception as a wrapper the given throwable.
    * 
    * @param throwable the throwable which should be wrapped
    */
   public EvaluationFailedException(Throwable throwable) {
      super(throwable);
   }


}
