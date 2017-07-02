package de.upb.cs.is.jpl.api.exception.algorithm;


import de.upb.cs.is.jpl.api.exception.JplRuntimeException;


/**
 * This exception indicates that an unsupported instance type was detected. Usually this will be the
 * case in algorithms or datasets assuming the use of a specific instance.
 * 
 * @author Alexander Hetzer
 *
 */
public class UnsupportedInstanceTypeException extends JplRuntimeException {

   private static final long serialVersionUID = 8583917655124952994L;


   /**
    * Creates an unsupported instance type exception with the given message.
    * 
    * @param message the message of this exception
    */
   public UnsupportedInstanceTypeException(String message) {
      super(message);
   }


   /**
    * Creates an unsupported instance type exception with the given message, wrapping the given
    * throwable.
    * 
    * @param message the message of the exception
    * @param cause the throwable to wrap
    */
   public UnsupportedInstanceTypeException(String message, Throwable cause) {
      super(message, cause);
   }

}
