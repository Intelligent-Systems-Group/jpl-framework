package de.upb.cs.is.jpl.api.exception.dataset;


import de.upb.cs.is.jpl.api.exception.JplException;


/**
 * This exception is thrown if an value for is detected that was not allowed.
 * 
 * @author Tanja Tornede
 *
 */
public class NotAllowedValueException extends JplException {

   private static final long serialVersionUID = 5431644027506183710L;


   /**
    * Creates an {@link NotAllowedValueException} with the given exception message.
    * 
    * @param message the message of the exception
    */
   public NotAllowedValueException(String message) {
      super(message);
   }

}
