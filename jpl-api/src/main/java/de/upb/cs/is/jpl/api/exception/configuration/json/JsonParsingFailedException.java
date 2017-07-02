package de.upb.cs.is.jpl.api.exception.configuration.json;


import de.upb.cs.is.jpl.api.exception.dataset.ParsingFailedException;


/**
 * 
 * This exception indicates that a json parsing process failed due to some reason. The exact reason
 * should be given in the exception message.
 * 
 * @author Alexander Hetzer
 *
 */
public class JsonParsingFailedException extends ParsingFailedException {

   private static final long serialVersionUID = 7811931679284280812L;


   /**
    * Creates a {@link JsonParsingFailedException} with the given message.
    * 
    * @param exceptionMessage the exception message
    */
   public JsonParsingFailedException(String exceptionMessage) {
      super(exceptionMessage);
   }


   /**
    * Creates a {@link JsonParsingFailedException} with the given message as a wrapper for the given
    * exception.
    * 
    * @param exceptionMessage the exception to pass to super class
    * @param exception the exception which caused this {@link JsonParsingFailedException} to be
    *           thrown
    */
   public JsonParsingFailedException(String exceptionMessage, Exception exception) {
      super(exceptionMessage, exception);
   }


   /**
    * Constructs a {@link JsonParsingFailedException} as a wrapper for the given exception.
    * 
    * @param exception the exception which caused this {@link JsonParsingFailedException} to be
    *           thrown
    */
   public JsonParsingFailedException(Exception exception) {
      super(exception);
   }

}
