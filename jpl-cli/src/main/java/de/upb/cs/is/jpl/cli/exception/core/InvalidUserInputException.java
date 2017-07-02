package de.upb.cs.is.jpl.cli.exception.core;


import de.upb.cs.is.jpl.api.exception.JplException;


/**
 * This exception indicates some kind of invalid user input. The exact problem should be described
 * in the exception message.
 * 
 * @author Alexander Hetzer
 *
 */
public class InvalidUserInputException extends JplException {

   private static final long serialVersionUID = 5829064172829078953L;


   /**
    * Creates an {@link InvalidUserInputException} with the given message.
    * 
    * @param message the message of the exception
    */
   public InvalidUserInputException(String message) {
      super(message);
   }


   /**
    * Creates an {@link InvalidUserInputException} with the given message as a wrapper for the given
    * throwable.
    * 
    * @param message the message of the exception
    * @param throwable the throwable to wrap
    */
   public InvalidUserInputException(String message, Throwable throwable) {
      super(message, throwable);
   }


}
