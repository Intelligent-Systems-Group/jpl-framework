package de.upb.cs.is.jpl.api.exception;


/**
 * 
 * This exception indicates that the called method is unsupported in that special implementation.
 * 
 * @author Tanja Tornede
 *
 */
public class UnsupportedOperationException extends JplException {
   private static final long serialVersionUID = 7536682317682562392L;


   /**
    * Creates a {@link UnsupportedOperationException} exception with the given message.
    * 
    * @param message the message of the exception
    */
   public UnsupportedOperationException(String message) {
      super(message);
   }

}
