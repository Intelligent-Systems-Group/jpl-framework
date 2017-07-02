package de.upb.cs.is.jpl.cli.exception.command;


import de.upb.cs.is.jpl.api.exception.JplException;


/**
 * This exception can be seen as a wrapper for a {@code null} value. If one needs to store an
 * exception and does not want to initialize the variable with {@code null}, this class can be used.
 * 
 * @author Alexander Hetzer
 *
 */
public class NullException extends JplException {

   private static final long serialVersionUID = 146505761771485722L;


   /**
    * Creates a {@link NullException} with the given message.
    * 
    * @param message the message of the exception
    */
   public NullException(String message) {
      super(message);
   }


}
