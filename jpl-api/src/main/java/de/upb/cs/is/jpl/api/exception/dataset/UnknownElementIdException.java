package de.upb.cs.is.jpl.api.exception.dataset;


/**
 * Exception which is thrown if the user attempts to get data for and id which is not part of the
 * Dataset
 * 
 * @author Sebastian Osterbrink
 */
public class UnknownElementIdException extends Exception {

   private static final long serialVersionUID = 3225652100200340982L;


   /**
    * Creates {@link UnknownElementIdException} which is thrown if the user attempts to get data for
    * and id which is not part of the dataset.
    * 
    * @param message the message of the exception
    */
   public UnknownElementIdException(String message) {
      super(message);
   }


}
