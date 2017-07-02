package de.upb.cs.is.jpl.cli.exception.command.trainmodels;


import de.upb.cs.is.jpl.api.exception.JplException;


/**
 * The WrongParameterTypeException is thrown if the ParameterManager can not return a specific type.
 *
 * @author Sebastian Gottschalk
 */
public class WrongParameterTypeException extends JplException {
   private static final long serialVersionUID = 3859253852948371590L;


   /**
    * Creates a {@link WrongParameterTypeException} with the given message.
    * 
    * @param message the message of the exception
    */
   public WrongParameterTypeException(String message) {
      super(message);
   }

}
