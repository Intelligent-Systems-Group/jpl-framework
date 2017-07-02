package de.upb.cs.is.jpl.api.exception;


/**
 * This class is the JPL specific runtime exception, which should be used as a root for all custom
 * runtime exceptions implemented for the tool.
 * 
 * @author Alexander Hetzer
 *
 */
public class JplRuntimeException extends RuntimeException {

   private static final long serialVersionUID = -2825952716273786841L;


   /**
    * Creates a JPL runtime exception with the given message.
    * 
    * @param message the message of the exception
    */
   public JplRuntimeException(String message) {
      super(message);
   }


   /**
    * Creates a JPL runtime exception with the given message wrapping the given throwable.
    * 
    * @param message the message of the exception
    * @param cause the throwable to wrap
    */
   public JplRuntimeException(String message, Throwable cause) {
      super(message, cause);
   }


   /**
    * Creates a {@link JplRuntimeException} as a wrapper for the given throwable. The message will
    * be made up of the exception message of the given throwable.
    * 
    * @param cause the throwable to wrap
    */
   public JplRuntimeException(Throwable cause) {
      super(cause);
   }

}
