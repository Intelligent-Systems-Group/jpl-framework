package de.upb.cs.is.jpl.api.exception;


/**
 * 
 * This exception indicates that mainly in getter functions that we are getting a null argument. The
 * exact reason should be given in the exception message.
 * 
 * @author Pritha Gupta
 *
 */
public class NullArgumentException extends JplException {


   private static final long serialVersionUID = 7811931679284282321L;


   /**
    * Creates a {@link NullArgumentException} exception with the given message.
    * 
    * @param message the message of the exception
    */
   public NullArgumentException(String message) {
      super(message);
   }


}
