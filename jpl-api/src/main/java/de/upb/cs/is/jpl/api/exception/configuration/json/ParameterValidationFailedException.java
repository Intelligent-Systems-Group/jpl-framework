package de.upb.cs.is.jpl.api.exception.configuration.json;


import de.upb.cs.is.jpl.api.configuration.json.AJsonConfiguration;
import de.upb.cs.is.jpl.api.exception.JplException;


/**
 * This exception indicates that the validation of the parameters in a {@link AJsonConfiguration}
 * failed. The message should give detailed information on the reason.
 * 
 * @author Alexander Hetzer
 *
 */
public class ParameterValidationFailedException extends JplException {

   private static final long serialVersionUID = -1217695390976298909L;


   /**
    * Creates a new parameter validation failed exception with the given message.
    * 
    * @param exceptionMessage the message of this exception
    */
   public ParameterValidationFailedException(String exceptionMessage) {
      super(exceptionMessage);
   }


   /**
    * Creates a parameter validation failed exception with the given message as a wrapper for the
    * given throwable.
    * 
    * @param message the message of the exception
    * @param cause the throwable to wrap
    */
   public ParameterValidationFailedException(String message, Throwable cause) {
      super(message, cause);
   }
}
