package de.upb.cs.is.jpl.api.exception.configuration.json;


import de.upb.cs.is.jpl.api.configuration.json.AJsonConfiguration;
import de.upb.cs.is.jpl.api.exception.JplRuntimeException;


/**
 * This class is the wrong configuration type exception, which should be used in case the and class
 * json configuration class extending {@link AJsonConfiguration} is not of same instance as that it
 * should be while setting it.
 * 
 * @author Pritha Gupta
 *
 */
public class WrongConfigurationTypeException extends JplRuntimeException {


   private static final long serialVersionUID = 1L;


   /**
    * Creates a Wrong Configuration type exception with the given message.
    * 
    * @param message the message of the exception
    */
   public WrongConfigurationTypeException(String message) {
      super(message);
   }


   /**
    * Creates a Wrong Configuration type Exception runtime exception with the given message wrapping
    * the given throwable.
    * 
    * @param message the message of the exception
    * @param cause the throwable to wrap
    */
   public WrongConfigurationTypeException(String message, Throwable cause) {
      super(message, cause);
   }

}
