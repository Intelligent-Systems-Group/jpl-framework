package de.upb.cs.is.jpl.api.exception;


/**
 * This exception should only be used as extension point for newly created exceptions for this
 * project.
 * 
 * @author Tanja Tornede
 *
 */
public class JplException extends Exception {

   private static final long serialVersionUID = 6852428534620005979L;


   /**
    * Creates a general JPL exception with a given exception message.
    * 
    * @param message the message of the exception
    */
   public JplException(String message) {
      super(message);
   }


   /**
    * Creates a general JPL exception as a wrapper with a given exception message and the given
    * throwable.
    * 
    * @param message the message of the exception
    * @param throwable the throwable which should be wrapped
    */
   public JplException(String message, Throwable throwable) {
      super(message, throwable);
   }


   /**
    * Creates a general JPL exception as a wrapper the given throwable.
    * 
    * @param throwable the throwable which should be wrapped
    */
   public JplException(Throwable throwable) {
      super(throwable);
   }

}
