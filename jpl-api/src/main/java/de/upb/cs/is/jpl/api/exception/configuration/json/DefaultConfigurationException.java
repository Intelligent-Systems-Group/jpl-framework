package de.upb.cs.is.jpl.api.exception.configuration.json;


import de.upb.cs.is.jpl.api.exception.JplRuntimeException;


/**
 * This exception indicates that a problem with a default configuration of either an algorithm or an
 * evaluation (metric) occurred. More details should be given in the exception message.
 * 
 * @author Alexander Hetzer
 *
 */
public class DefaultConfigurationException extends JplRuntimeException {

   private static final long serialVersionUID = 8081279400984803584L;


   /**
    * Creates a {@link DefaultConfigurationException} with the given message.
    * 
    * @param message the message of the exception
    */
   public DefaultConfigurationException(String message) {
      super(message);
   }


   /**
    * Creates a {@link DefaultConfigurationException} with the given message as a wrapper for the
    * given throwable.
    * 
    * @param message the message of the exception
    * @param cause the throwable to be wrapped
    */
   public DefaultConfigurationException(String message, Throwable cause) {
      super(message, cause);
   }

}
