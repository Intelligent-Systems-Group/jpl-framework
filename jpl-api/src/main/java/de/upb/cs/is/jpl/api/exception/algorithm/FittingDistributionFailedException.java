package de.upb.cs.is.jpl.api.exception.algorithm;


/**
 * This exception indicates that fitting a distribution failed. The exact circumstances, including
 * the reason, can be obtained from the exception message.
 * 
 * @author Tanja Tornede
 *
 */
public class FittingDistributionFailedException extends Exception {

   private static final long serialVersionUID = 7730196429313156886L;


   /**
    * Creates a {@link FittingDistributionFailedException} with the given exception message.
    * 
    * @param message the message of the exception
    */
   public FittingDistributionFailedException(String message) {
      super(message);
   }


   /**
    * Creates a {@link FittingDistributionFailedException} with the given exception message as a
    * wrapper for the given throwable.
    * 
    * @param message the message of the exception
    * @param cause the throwable to wrap
    */
   public FittingDistributionFailedException(String message, Throwable cause) {
      super(message, cause);
   }


   /**
    * Creates a {@link FittingDistributionFailedException} as a wrapper for the given throwable. The
    * message will be made up of the exception message of the given throwable.
    * 
    * @param cause the throwable to wrap
    */
   public FittingDistributionFailedException(Throwable cause) {
      super(cause);
   }
}
