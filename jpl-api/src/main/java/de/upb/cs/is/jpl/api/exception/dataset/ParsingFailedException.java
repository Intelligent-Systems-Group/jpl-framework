package de.upb.cs.is.jpl.api.exception.dataset;


import de.upb.cs.is.jpl.api.exception.JplException;


/**
 * 
 * This exception indicates that a parsing process failed due to some reason. The exact reason
 * should be given in the exception message.
 * 
 * @author Alexander Hetzer
 *
 */
public class ParsingFailedException extends JplException {
   private static final long serialVersionUID = 7536682317682562392L;


   /**
    * Creates a {@link ParsingFailedException} with the given message.
    * 
    * @param message the message of the exception
    */
   public ParsingFailedException(String message) {
      super(message);
   }


   /**
    * Creates a {@link ParsingFailedException} with the given message as a wrapper for the given
    * exception.
    * 
    * @param message the message of the exception
    * @param exception the exception to wrap
    */
   public ParsingFailedException(String message, Exception exception) {
      super(message, exception);
   }


   /**
    * Creates a {@link ParsingFailedException} as a wrapper for the given exception.
    * 
    * @param exception the exception to wrap
    */
   public ParsingFailedException(Exception exception) {
      super(exception);
   }

}
