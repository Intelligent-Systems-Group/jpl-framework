package de.upb.cs.is.jpl.cli.exception.command.addlearningalgorithm;


import de.upb.cs.is.jpl.api.exception.dataset.ParsingFailedException;


/**
 * This exception indicates that the parsing of parameters of an algorithm provided by the user via
 * the command line failed.
 * 
 * @author Alexander Hetzer
 *
 */
public class ParameterParsingFailedException extends ParsingFailedException {

   private static final long serialVersionUID = -3830493488825011782L;


   /**
    * Creates a {@link ParameterParsingFailedException} with the given message.
    * 
    * @param exceptionMessage the message of the exception
    */
   public ParameterParsingFailedException(String exceptionMessage) {
      super(exceptionMessage);
   }


   /**
    * Creates a {@link ParameterParsingFailedException} with the given message as a wrapper for the
    * given exception.
    * 
    * @param exceptionMessage the message of the exception
    * @param exception the exception to wrap.
    */
   public ParameterParsingFailedException(String exceptionMessage, Exception exception) {
      super(exceptionMessage, exception);
   }


}
