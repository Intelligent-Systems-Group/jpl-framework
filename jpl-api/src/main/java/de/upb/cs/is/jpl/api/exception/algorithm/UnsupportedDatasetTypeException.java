package de.upb.cs.is.jpl.api.exception.algorithm;


import de.upb.cs.is.jpl.api.exception.JplRuntimeException;


/**
 * This exception indicates that an unsupported dataset type was detected. Usually this will be the
 * case in algorithms or learning models assuming the use of a specific dataset.
 * 
 * @author Alexander Hetzer
 *
 */
public class UnsupportedDatasetTypeException extends JplRuntimeException {

   private static final long serialVersionUID = 5431644027506183710L;


   /**
    * Creates an {@link UnsupportedOperationException} with the given exception message.
    * 
    * @param message the message of the exception
    */
   public UnsupportedDatasetTypeException(String message) {
      super(message);
   }


   /**
    * Creates an {@link UnsupportedOperationException} with the given exception message as a wrapper
    * for the given throwable.
    * 
    * @param message the message of the exception
    * @param cause the throwable to wrap
    */
   public UnsupportedDatasetTypeException(String message, Throwable cause) {
      super(message, cause);
   }

}
