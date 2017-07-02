package de.upb.cs.is.jpl.api.exception.dataset;


import de.upb.cs.is.jpl.api.dataset.IInstance;
import de.upb.cs.is.jpl.api.exception.JplException;


/**
 * Exception which can be thrown, if the {@link IInstance} object which is added to the dataset is
 * not of the expected type or if it contains different item vectors than the dataset.
 * 
 * @author Sebastian Osterbrink
 *
 */
public class InvalidInstanceException extends JplException {

   private static final long serialVersionUID = 7971739211293609359L;


   /**
    * Creates a new {@link InvalidInstanceException}with the given message as a wrapper for the
    * given exception.
    * 
    * @param message the message of the exception
    */
   public InvalidInstanceException(String message) {
      super(message);
   }


}
