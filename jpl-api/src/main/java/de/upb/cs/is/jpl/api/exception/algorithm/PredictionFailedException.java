package de.upb.cs.is.jpl.api.exception.algorithm;


import de.upb.cs.is.jpl.api.exception.JplException;


/**
 * This exception indicates that the prediction of a model failed. The exact circumstances,
 * including the reason, can be obtained from the exception message.
 * 
 * @author Tanja Tornede
 *
 */
public class PredictionFailedException extends JplException {

   private static final long serialVersionUID = 4054278012203008262L;


   /**
    * Creates a {@link PredictionFailedException} with the given exception message.
    * 
    * @param message the message of the exception
    */
   public PredictionFailedException(String message) {
      super(message);
   }


   /**
    * Creates a {@link PredictionFailedException} with the given exception message as a wrapper for
    * the given throwable.
    * 
    * @param message the message of the exception
    * @param cause the throwable to wrap
    */
   public PredictionFailedException(String message, Throwable cause) {
      super(message, cause);
   }


   /**
    * Creates a {@link PredictionFailedException} as a wrapper for the given throwable. The message
    * will be made up of the exception message of the given throwable.
    * 
    * @param cause the throwable to wrap
    */
   public PredictionFailedException(Throwable cause) {
      super(cause);
   }


}
