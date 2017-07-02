package de.upb.cs.is.jpl.api.exception.math;


/**
 * This exception is thrown if the the matrix could not be inverted.
 * 
 * @author Sebastian Gottschalk
 *
 */
public class MatrixInversionException extends Exception {

   private static final long serialVersionUID = 5848664172543078953L;


   /**
    * Creates a {@link MatrixInversionException} with the given message.
    * 
    * @param exceptionMessage the message of the exception
    */
   public MatrixInversionException(String exceptionMessage) {
      super(exceptionMessage);
   }


   /**
    * Creates a {@link MatrixInversionException} with the given exception message as a wrapper for
    * the given throwable.
    * 
    * @param message the message of the exception
    * @param cause the throwable to wrap
    */
   public MatrixInversionException(String message, Throwable cause) {
      super(message, cause);
   }


   /**
    * Creates a {@link MatrixInversionException} as a wrapper for the given throwable. The message
    * will be made up of the exception message of the given throwable.
    * 
    * @param cause the throwable to wrap
    */
   public MatrixInversionException(Throwable cause) {
      super(cause);
   }
}
