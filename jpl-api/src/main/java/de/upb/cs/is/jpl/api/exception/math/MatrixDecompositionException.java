package de.upb.cs.is.jpl.api.exception.math;


/**
 * This exception is thrown if the decomposition of the matrix was not successfully executed.
 * 
 * @author Sebastian Gottschalk
 *
 */
public class MatrixDecompositionException extends Exception {

   private static final long serialVersionUID = 5829064172543078953L;


   /**
    * Creates a {@link MatrixDecompositionException} with the given message.
    * 
    * @param message the message of the exception
    */
   public MatrixDecompositionException(String message) {
      super(message);
   }


   /**
    * Creates a {@link MatrixDecompositionException} with the given exception message as a wrapper
    * for the given throwable.
    * 
    * @param message the message of the exception
    * @param cause the throwable to wrap
    */
   public MatrixDecompositionException(String message, Throwable cause) {
      super(message, cause);
   }


   /**
    * Creates a {@link MatrixDecompositionException} as a wrapper for the given throwable. The
    * message will be made up of the exception message of the given throwable.
    * 
    * @param cause the throwable to wrap
    */
   public MatrixDecompositionException(Throwable cause) {
      super(cause);
   }
}
