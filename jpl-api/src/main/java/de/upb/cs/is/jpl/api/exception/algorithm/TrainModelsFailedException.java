package de.upb.cs.is.jpl.api.exception.algorithm;


import de.upb.cs.is.jpl.api.exception.JplException;


/**
 * This exception indicates that the training of a model failed. The exact circumstances, including
 * the reason, can be obtained from the exception message.
 * 
 * @author Sebastian Gottschalk
 * 
 */
public class TrainModelsFailedException extends JplException {

   private static final long serialVersionUID = 7730196429313156886L;


   /**
    * Creates a {@link TrainModelsFailedException} with the given exception message.
    * 
    * @param message the message of the exception
    */
   public TrainModelsFailedException(String message) {
      super(message);
   }


   /**
    * Creates a {@link TrainModelsFailedException} with the given exception message as a wrapper for
    * the given throwable.
    * 
    * @param message the message of the exception
    * @param cause the throwable to wrap
    */
   public TrainModelsFailedException(String message, Throwable cause) {
      super(message, cause);
   }


   /**
    * Creates a {@link TrainModelsFailedException} as a wrapper for the given throwable. The message
    * will be made up of the exception message of the given throwable.
    * 
    * @param cause the throwable to wrap
    */
   public TrainModelsFailedException(Throwable cause) {
      super(cause);
   }
}
