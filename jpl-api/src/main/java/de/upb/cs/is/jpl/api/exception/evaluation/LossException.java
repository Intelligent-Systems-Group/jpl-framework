package de.upb.cs.is.jpl.api.exception.evaluation;


import de.upb.cs.is.jpl.api.metric.IMetric;


/**
 * This exception will be thrown by the {@link IMetric} if its is not able to calculate the loss for
 * the provided prediction and true value.
 * 
 * @author Pritha Gupta
 *
 */
public class LossException extends EvaluationFailedException {

   private static final long serialVersionUID = -8508169362999145572L;


   /**
    * Creates a {@link LossException} with the given message.
    * 
    * @param message the message of the exception
    */
   public LossException(String message) {
      super(message);
   }


   /**
    * Creates a general {@link LossException} as a wrapper with a given exception message and the
    * given throwable.
    * 
    * @param message the message of the exception
    * @param throwable the throwable which should be wrapped
    */
   public LossException(String message, Throwable throwable) {
      super(message, throwable);
   }

}
